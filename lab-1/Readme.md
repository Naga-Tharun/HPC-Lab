# HPC LAB 1

**How to run:**

Open the directory corresponding to the respective question labeled as qn1, qn2, qn3, qn4.

For example, qn1.

In qn1 directory, run q1.sh using bash script by

```
bash q1.sh
```

This creates dump.dat file by running q1.c.

The bash script varies the matrix size and runs q1.c.

The dump.dat file contains the data about the runtimes for different matrix sizes for all possible powers using different sequence of threads.

run the segregate.py script using,

```
python3 segregate.py
```

This creates plot_data directory which contains RMT and RTM.

RMT subdirectory which contains data for all the required threads for all the powers of the matrix computed.

RTM subdirectory contains data for the different matrices for all the powers of the matrix computed.

We plot the graphs using plot_rmt.sh and plot_rtm.sh script files.

```
gnuplot plot_rmt.sh
gnuplot plot_rtm.sh
```

These files create the RMT.pdf and RTM.pdf which contain the required graphs,

Runtime vs Matrix Sizes by fixing number of threads.
Runtime vs Threads by fixing the Matrix Size.

Compare the runtime for all the 4 methods (OMM, OMM transpose, BMM, BMM transpose) using plot_compare.sh script

```
gnuplot plot_compare.sh
```

The plots are available in the file plot_compare.pdf


**Question 1:**

Using openmp and varying the no. of threads we calculate the nth power of a square matrix. We use the current time of the day to get the time of execution to calculate the nth power of a square matrix. The no. of threads varies as 1, 2, 4, 6, 8, 10, 12, 14, 16. The power, n varies from 2-16.

First we created a function to multiply 2 matrices (using 3 for loops), we used openmp to multiply parallelly , before calling the function we are setting the no of threads. Next we created a function to find power of a matrix using the multiply function we defined. Finally using the gettimeoftheday we calculated time taken. we used couple other for loops for different values of power, threads , matrix size.

**Question 2:**

First we created a matrix multiply function in which we used 6 loops 3 for incrementing pointers (ii,jj,kk)  with the given block size , inner for loops are for actual matrix multiplication . Remaining power of matrix function, loops for different values powers, threads , matrix size remains same.

**Question 3:**

Same as Q1 but instead of multiplying row to col we are taking transpose of matrix and multiplying row to row of a matrix to it's transpose.

**Question 4:**

Same as Q2 but instead of multiplying row to col we are taking transpose of matrix and multiplying row to row of a matrix to it's transpose as memory access time for rows is smaller than for columns. The access type is row major which leads to greater access time for columns.
