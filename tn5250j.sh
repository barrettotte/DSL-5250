#!/bin/bash
# note: this could probably be a gradle step...

version=0.7.6

mkdir -p lib
wget https://github.com/tn5250j/tn5250j/releases/download/$version/tn5250j-$version-full-bin.zip 
unzip tn5250j-$version-full-bin.zip -d lib/
rm tn5250j-$version-full-bin.zip