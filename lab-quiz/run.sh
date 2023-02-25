#!/bin/bash

clear

gcc chain_multiplication.c -o chain_multiplication -fopenmp
./chain_multiplication > out.dat

rm -rf chain_multiplication