#!/bin/bash

clear

javac arrayBasedQueueLock.java
javac arrayBasedQueueverify.java

for i in 1 2 4 6 8 10 12 14 16;
do
    java arrayBasedQueueverify $i 10000 >> output.dat
done

rm -rf arrayBasedQueueLock.class arrayBasedQueueverify.class