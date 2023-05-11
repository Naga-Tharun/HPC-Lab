#!/bin/bash

clear

javac HierarchicalBackoffLock.java
javac HierarchicalBackoffverify.java

for i in 1 2 4 6 8 10 12 14 16;
do
    java HierarchicalBackoffverify $i 10000 >> output.dat
done

rm -rf *.class 