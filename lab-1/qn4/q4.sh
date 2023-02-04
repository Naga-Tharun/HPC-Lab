#!/bin/bash

clear

gcc q4.c -o q4 -fopenmp
# ./q4 512
./q4 512 > dump.dat
./q4 1024 >> dump.dat
./q4 2048 >> dump.dat

rm -rf q4