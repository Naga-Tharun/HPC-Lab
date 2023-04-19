#!/bin/bash

clear

javac TTASLock.java
javac TTASverify.java

for i in 1 2 4 6 8 10 12 14 16;
do
    java TTASverify $i 10000 >> output.dat
done

rm -rf TTASLock.class TTASverify.class