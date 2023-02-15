#!/bin/bash

clear

gcc mergeSort_int.c -o mergeSort_int -fopenmp
./mergeSort_int > outI.dat

gcc mergeSort_float.c -o mergeSort_float -fopenmp
./mergeSort_float > outF.dat

gcc mergeSort_double.c -o mergeSort_double -fopenmp
./mergeSort_double > outD.dat

rm -rf mergeSort_int mergeSort_float mergeSort_double