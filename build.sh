#!/bin/sh

set -e

echo "Creating Build Directory"
rm -rf build
mkdir build

echo "Compiling"
javac -classpath lib/lwjgl.jar:lib/lwjgl_util.jar:lib/slick-util.jar src/minecraftmini/*.java -d build

cd build
echo "Creating Jar"
jar -cfm ../MineCraft.jar ../src/minecraftmini/manifest.txt minecraftmini/*.class

echo "Done"
