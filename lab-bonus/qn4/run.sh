#!/bin/bash

clear

javac CohortLock.java
javac CohortLockverify.java

for i in 1 2 4 6 8 10 12 14 16;
do
    java CohortLockverify $i 10000 >> output.dat
done

rm -rf *.class 