#!/bin/bash

clear

javac LazyList.java
javac List_Test.java

mkdir runtime_data

for j in 200000 2000000 20000000;
do
    mkdir "runtime_data/data_"$j
    mkdir "runtime_data/data_"$j"/c100_i0_d0"
    mkdir "runtime_data/data_"$j"/c70_i20_d10"
    mkdir "runtime_data/data_"$j"/c50_i25_d25"
    mkdir "runtime_data/data_"$j"/c30_i35_d35"
    mkdir "runtime_data/data_"$j"/c0_i50_d50"

    for i in 1 2 4 6 8 10 12 14 16;
    do
        java List_Test $i $j 100000 50 100 0 >> "runtime_data/data_"$j"/c100_i0_d0/th_"$i".dat"
        java List_Test $i $j 100000 50 70 20 >> "runtime_data/data_"$j"/c70_i20_d10/th_"$i".dat"
        java List_Test $i $j 100000 50 50 25 >> "runtime_data/data_"$j"/c50_i25_d25/th_"$i".dat"
        java List_Test $i $j 100000 50 30 35 >> "runtime_data/data_"$j"/c30_i35_d35/th_"$i".dat"
        java List_Test $i $j 100000 50 0 50 >> "runtime_data/data_"$j"/c0_i50_d50/th_"$i".dat"
    done
done