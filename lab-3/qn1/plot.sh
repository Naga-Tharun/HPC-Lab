#!/bin/bash
set autoscale
set term pdf size 3,2
set output "plots.pdf"
set xlabel "Number of threads" font "Bold,9" offset 0,1
set ylabel "Runtime (seconds)" font "Bold,9" offset 3,0
set xtics font "Verdana,7"
set ytics font "Bold,7"
set key font "Bold,5" sample 0.3
set boxwidth 1
set style data histogram 
set style histogram cluster gap 1
set style fill solid border -1
set multiplot layout 1,1 title "Speedup of threshold on varying the number of threads"
set title "Speedup with p = 10%" font "Bold,9" offset 1.0,-0.5
plot "time_8192_8192.dat" using 5:xtic(4) title ""
unset multiplot