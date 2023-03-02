#!/bin/bash

clear

# g++-12 seed_generator.cpp -o seed_generator && ./seed_generator 10000 10000 "initial_seed.txt"
g++ seed_generator.cpp -o seed_generator && ./seed_generator 10000 10000 "initial_seed.txt"

# g++-12 game_of_life.cpp -o game_of_life -fopenmp
g++ game_of_life.c -o game_of_life -fopenmp
./game_of_life "initial_seed.txt" 10000 10000 10 "final_seed.txt" >> output.dat

rm -rf game_of_life seed_generator
