#!/bin/bash

clear

javac TTASBackoffLock.java
javac TTASBackoffverify.java

for i in 1 2 4 6 8 10 12 14 16;
do
    java TTASBackoffverify $i 10000 >> output.dat
done

rm -rf TTASBackoffLock.class TTASBackoffverify.class