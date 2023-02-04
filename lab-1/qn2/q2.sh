#!/bin/bash

clear

gcc q2.c -o q2 -fopenmp
./q2 512
# ./q2 512 > dump.dat
# ./q2 1024 >> dump.dat
# ./q2 2048 >> dump.dat

rm -rf q2