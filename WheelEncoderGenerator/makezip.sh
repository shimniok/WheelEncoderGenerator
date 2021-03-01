#!/bin/bash

## Create ZIP images for each of the operating system images

BUILDDIR=target
OS="linux windows mac"
DIR=weg
ZIPDIR=$BUILDDIR/zip

[ -d $ZIPDIR ] || mkdir $ZIPDIR

for o in $OS; do
    if [ -d $BUILDDIR/$o/$DIR ]; then
        (cd $BUILDDIR/$o ; zip -r $DIR.zip $DIR) && \
        mv $BUILDDIR/$o/$DIR.zip $ZIPDIR/$DIR-$o.zip
    else
        echo "ERROR: missing OS $o"
        exit 2
    fi
done
