#!/bin/bash

clear

javac TASLock.java
javac TASverify.java

for i in 1 2 4 6 8 10 12 14 16;
do
    java TASverify $i 10000 >> output.dat
done

rm -rf TASLock.class TASverify.class