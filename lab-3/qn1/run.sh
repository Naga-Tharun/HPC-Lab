#!/bin/bash

clear

mkdir input_data
mkdir output_data

for i in 1024 2048 4096 8192;
do
    for j in 1024 2048 4096 8192;
    do 
        # g++-12 m_matrix_generator.cpp -o m_matrix_generator && ./m_matrix_generator $i $j "input_data/m_"$i"_"$j".txt"
        g++ m_matrix_generator.cpp -o m_matrix_generator && ./m_matrix_generator $i $j "input_data/m_"$i"_"$j".txt"

        g++ threshold.cpp -o threshold -fopenmp
        # g++-12 threshold.cpp -o threshold -fopenmp
        ./threshold $i $j "input_data/m_"$i"_"$j".txt" 10 "output_data/b_"$i"_"$j".txt" >> runtime_data.dat
    done
done

rm -rf m_matrix_generator threshold
