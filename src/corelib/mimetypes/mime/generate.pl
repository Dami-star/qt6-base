#!/usr/bin/perl
# Copyright (C) 2019 Intel Corporation.
# SPDX-License-Identifier: LicenseRef-Qt-Commercial OR GPL-3.0-only WITH Qt-GPL-exception-1.0
use strict;
use warnings;
use Config;
local $/;               # Enable "slurp" mode

sub checkCommand($) {
    use File::Spec::Functions;
    my $cmd = $_[0] . $Config{_exe};
    for my $path (path()) {
        return 1 if -x catfile($path, $cmd);
    }
    return 0;
}

my $data;
my $compress;
my $macro;
my $zlib = eval 'use Compress::Zlib; use IO::Compress::Gzip; return 1;';
my $fname = shift @ARGV;

if ($zlib) {
    # Prefer internal zlib support (useful on Windows where gzip.exe isn't
    # always presnet)
    $macro = "MIME_DATABASE_IS_GZIP";
} elsif (checkCommand("gzip")) {
    # No builtin support for compression (old Perl?)
    $compress = "gzip -c9";
    $macro = "MIME_DATABASE_IS_GZIP";
}

# Check if Qt is being built with zstd support
if ($fname eq "--zstd") {
    $fname = shift @ARGV;
    if (checkCommand("zstd")) {
        $compress = "zstd -cq19 -T1";
        $macro = "MIME_DATABASE_IS_ZSTD";
    }
}

# Check if xml (from xmlstarlet) is in $PATH
my $cmd;
if (checkCommand("xmlstarlet")) {
    # Minify the data before compressing
    $cmd = "xmlstarlet sel -D -B -t -c / $fname";
    $cmd .= "| $compress" if $compress;
} elsif (checkCommand("xml")) {
    # Minify the data before compressing
    $cmd = "xml sel -D -B -t -c / $fname";
    $cmd .= "| $compress" if $compress;
} elsif ($compress) {
    $cmd = "$compress < $fname"
}
if ($cmd) {
    # Run the command and read everything
    open CMD, "$cmd |";
    binmode CMD;
    $data = <CMD>;
    close CMD;
    die("Failed to run $cmd") if ($? >> 8);
} else {
    # No command, just read the file
    open F, "<$fname";
    $data = <F>;
    close F;
}

# Do we need to compress with zlib?
if (!$compress && $zlib) {
    $data = eval q{
        use Compress::Zlib;
        use IO::Compress::Gzip qw(gzip);
        my $compressed;
        gzip \$data => \$compressed,
            Minimal => 1,
            Level => Z_BEST_COMPRESSION;
        return $compressed;
    };
}

# Now print as hex
printf "#define %s\n", $macro if $macro;
printf "static const unsigned char mimetype_database[] = {";
my $i = 0;
map {
    printf "\n  " if $i++ % 12 == 0;
    printf "0x%02x, ", ord $_
} split //, $data;
printf "\n};\n";
printf "static constexpr size_t MimeTypeDatabaseOriginalSize = %d;\n",
    (stat $fname)[7];
