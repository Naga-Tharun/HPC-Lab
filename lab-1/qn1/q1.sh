#!/bin/bash

clear

gcc q1.c -o q1 -fopenmp
# ./q1 512
./q1 512 > dump.dat
./q1 1024 >> dump.dat
./q1 2048 >> dump.dat

rm -rf q1