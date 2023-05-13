#!/bin/bash

clear

javac cbst.java
javac cbst_test.java

mkdir runtime_data

for j in 10000;
do
    for i in 1 2 4 6 8 10 12 14 16;
    do
        java cbst_test $i $j 5000 50 100 0 >> "runtime_data/c100_i0_d0.dat"
        java cbst_test $i $j 5000 50 70 20 >> "runtime_data/c70_i20_d10.dat"
        java cbst_test $i $j 5000 50 50 25 >> "runtime_data/c50_i25_d25.dat"
        # java cbst_test $i $j 500 50 30 35 >> "runtime_data/c30_i35_d35.dat"
        # java cbst_test $i $j 500 50 0 50 >> "runtime_data/c0_i50_d50.dat"
    done
done

rm -rf *.class