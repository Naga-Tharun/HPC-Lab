#!/bin/bash
set autoscale
set term pdf size 8,10
set output "RMT.pdf"
set xlabel "Matrix size" font "Bold,9" offset 0,1
set ylabel "Runtime (seconds)" font "Bold,9" offset 3,0
set xtics font "Verdana,7"
set ytics font "Bold,7"
set key font "Bold,5" sample 0.3
set boxwidth 1
set style data histogram 
set style histogram cluster gap 1
set style fill solid border -1
do for [k in "4 8 16 32 64"] {
    set multiplot layout 5,2 title "Runtime vs Matrix sizes by fixing number of threads for block size ".k
    set title "Runtime vs Matrix size using 1 thread" font "Bold,9" offset 1.0,-0.5
    plot for[i=2:16] "plot_data/RMT/block_".k."/thread_1/RMT_".i.".dat" using 5:xtic(1) title "power ".i
    do for [i=2:16:2] {
        set title "Runtime vs Matrix size using ".i." thread" font "Bold,9" offset 1.0,-0.5
        plot for[j=2:16] "plot_data/RMT/block_".k."/thread_".i."/RMT_".j.".dat" using 5:xtic(1) title "power ".j
    }
}
unset multiplot