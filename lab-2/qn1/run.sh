#!/bin/bash

clear

gcc mergeSort_int.c -o mergeSort_int -fopenmp
./mergeSort_int > data_int.dat

gcc mergeSort_float.c -o mergeSort_float -fopenmp
./mergeSort_float > data_float.dat

gcc mergeSort_double.c -o mergeSort_double -fopenmp
./mergeSort_double > data_double.dat

rm -rf mergeSort_int mergeSort_float mergeSort_double