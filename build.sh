#!/bin/sh

set -e

echo "Creating Build Directory"
rm -rf build
mkdir build

echo "Compiling"
javac -classpath lib/lwjgl.jar:lib/lwjgl_util.jar:lib/slick-util.jar source/*.java -d build

cd build
echo "Creating Jar"
jar -cfm ../MineCraft.jar ../source/manifest.txt *.class

echo "Done"