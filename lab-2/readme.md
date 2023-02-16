# HPC LAB 2

**How to run:**

Open the directory corresponding to the respective question labeled as qn1, qn2,.

For example, qn1.

In qn1 directory, run q1.sh using bash script by

```
bash run.sh
```

This creates data dump files by running all the three mergesort programs for int, float and double.

The outX.dat file contains the data about the runtimes for different merge sort runs on arrays with random numbers. X here is I for interger, F for float and D for double.

We plot the graphs using plot.sh.

```
gnuplot plot.sh
```

The plots are available in the file plots.pdf

**Question 1:**

Merge sort on a large array using bottom up approach for different data types; int, float and double.

Programs:

- mergeSort_int.c
- mergeSort_float.c
- mergeSort_double.c

Outputs:

- outI.dat
- outF.dat
- outD.dat

Plot in plots.pdf

**Question 2:**

N Queens problem on a NxN chessboard and recording all possible solutions.

Programs:

- nqueens.cpp

plot_data folder contains filtered data as data_X.dat files for generating the plot.pdf.
