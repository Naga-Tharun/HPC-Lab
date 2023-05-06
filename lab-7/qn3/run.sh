#!/bin/bash

clear

javac MCSQueueLock.java
javac MCSQueueverify.java

for i in 1 2 4 6 8 10 12 14 16;
do
    java MCSQueueverify $i 10000 >> output.dat
done

rm -rf *.class