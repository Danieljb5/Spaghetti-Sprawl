#!/bin/sh

mkdir jar
cp build/artifacts/Spaghetti-Sprawl.jar jar
cp assets jar -r
cp buildRunnable.sh jar
cd jar
cat buildRunnable.sh Spaghetti-Sprawl.jar > Spaghetti-Sprawl
rm buildRunnable.sh
chmod +x Spaghetti-Sprawl
