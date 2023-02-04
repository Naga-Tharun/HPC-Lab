#!/bin/bash

clear

gcc q3.c -o q3 -fopenmp
# ./q3 512
./q3 512 > dump.dat
./q3 1024 >> dump.dat
./q3 2048 >> dump.dat

rm -rf q3