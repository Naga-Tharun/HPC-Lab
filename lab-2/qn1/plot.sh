#!/bin/bash

set autoscale
set term pdf size 7,3
set output "plots.pdf"
set xlabel "Number of threads" font "Bold,9" offset 0,1
set ylabel "Runtime (milli seconds)" font "Bold,9" offset 3,0
set xtics font "Verdana,7"
set ytics font "Bold,7"
set key font "Bold,5" sample 0.3
set boxwidth 1
set style data histogram
set style histogram cluster gap 1
set style fill solid border -1

set multiplot layout 2,2 title "Runtime vs threads for different data types"

set title "Runtime vs threads for data type: int" font "Bold,9" offset 1.0,-0.5
plot "outI.dat" using 2:xtic(1) title "int"

set title "Runtime vs threads for data type: float" font "Bold,9" offset 1.0,-0.5
plot "outF.dat" using 2:xtic(1) title "float"

set title "Runtime vs threads for data type: double" font "Bold,9" offset 1.0,-0.5
plot "outD.dat" using 2:xtic(1) title "double"

set title "Runtime vs threads for various data types" font "Bold,9" offset 1.0,-0.5
plot "outI.dat" using 2:xtic(1) title "int",\
    "outF.dat" using 2:xtic(1) title "float",\
    "outD.dat" using 2:xtic(1) title "double"

unset multiplot