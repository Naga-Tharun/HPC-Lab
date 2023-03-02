#!/bin/bash

clear

# gcc-12 matrix_chain_mult.c -o matrix_chain_mult -fopenmp
gcc matrix_chain_mult.c -o matrix_chain_mult -fopenmp
./matrix_chain_mult >> output.dat

rm -rf matrix_chain_mult
