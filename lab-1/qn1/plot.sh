set autoscale
set term pdf size 8,2
set output "64.pdf" 
set xlabel "Matrix size" font "Bold,9" offset 0,1
set ylabel "Runtime (seconds)" font "Bold,9" offset 3,0
set xtics font "Verdana,7"
#set xtics {1,2,4,8,16,32,64,"128" 128} font "Bold,4" offset 0, graph 0.05
set ytics font "Bold,7"
# set xtics "" rotate by 0 font "Bold,7" offset 0, graph 0.05
#set xtic rotate by -90 scale 0
#set ytics font ",3"
set key at screen 0.73,0.99 font "Bold,5" horizontal sample 0.3 spacing 1 width 0.2 height 3.5 maxrows 2
set boxwidth 0.75
set style data histogram 
set style histogram cluster gap 1
set style fill solid border -1
set yrange [0:650]
set multiplot layout 1,2
set title "Runtime vs Matrix size using 1 thread" font "Bold,9" offset 1.0,-0.5
set size 0.45,0.9
plot "plot_data/RMT/1_thread/RMT_2.dat" using 4:xtic(1) title "Power 2",\
"plot_data/RMT/1_thread/RMT_3.dat" using 4:xtic(1) title "Power 3",\
"plot_data/RMT/1_thread/RMT_4.dat" using 4:xtic(1) title "Power 4",\
"plot_data/RMT/1_thread/RMT_5.dat" using 4:xtic(1) title "Power 5",\
"plot_data/RMT/1_thread/RMT_6.dat" using 4:xtic(1) title "Power 6",\
"plot_data/RMT/1_thread/RMT_7.dat" using 4:xtic(1) title "Power 7",\
"plot_data/RMT/1_thread/RMT_8.dat" using 4:xtic(1) title "Power 8",\
"plot_data/RMT/1_thread/RMT_9.dat" using 4:xtic(1) title "Power 9",\
"plot_data/RMT/1_thread/RMT_10.dat" using 4:xtic(1) title "Power 10",\
"plot_data/RMT/1_thread/RMT_11.dat" using 4:xtic(1) title "Power 11",\
"plot_data/RMT/1_thread/RMT_12.dat" using 4:xtic(1) title "Power 12",\
"plot_data/RMT/1_thread/RMT_13.dat" using 4:xtic(1) title "Power 13",\
"plot_data/RMT/1_thread/RMT_14.dat" using 4:xtic(1) title "Power 14",\
"plot_data/RMT/1_thread/RMT_15.dat" using 4:xtic(1) title "Power 15",\
"plot_data/RMT/1_thread/RMT_16.dat" using 4:xtic(1) title "Power 16"
# set yrange [0:10000]
# set title "Sorted Input" font "Bold,7" offset -1.0,-1.0
# set size 0.45,0.9
# plot "64.dat"  using 4:xtic(1) notitle "without removeSkewness()", "64.dat" using 5:xtic(1) notitle "with removeSkewness()"
set yrange [0:400]
# set multiplot layout 1,2
set title "Runtime vs Matrix size using 2 thread" font "Bold,9" offset 1.0,-0.5
set size 0.45,0.9
plot "plot_data/RMT/2_thread/RMT_2.dat" using 4:xtic(1) title "Power 2",\
"plot_data/RMT/2_thread/RMT_3.dat" using 4:xtic(1) title "Power 3",\
"plot_data/RMT/2_thread/RMT_4.dat" using 4:xtic(1) title "Power 4",\
"plot_data/RMT/2_thread/RMT_5.dat" using 4:xtic(1) title "Power 5",\
"plot_data/RMT/2_thread/RMT_6.dat" using 4:xtic(1) title "Power 6",\
"plot_data/RMT/2_thread/RMT_7.dat" using 4:xtic(1) title "Power 7",\
"plot_data/RMT/2_thread/RMT_8.dat" using 4:xtic(1) title "Power 8",\
"plot_data/RMT/2_thread/RMT_9.dat" using 4:xtic(1) title "Power 9",\
"plot_data/RMT/2_thread/RMT_10.dat" using 4:xtic(1) title "Power 10",\
"plot_data/RMT/2_thread/RMT_11.dat" using 4:xtic(1) title "Power 11",\
"plot_data/RMT/2_thread/RMT_12.dat" using 4:xtic(1) title "Power 12",\
"plot_data/RMT/2_thread/RMT_13.dat" using 4:xtic(1) title "Power 13",\
"plot_data/RMT/2_thread/RMT_14.dat" using 4:xtic(1) title "Power 14",\
"plot_data/RMT/2_thread/RMT_15.dat" using 4:xtic(1) title "Power 15",\
"plot_data/RMT/2_thread/RMT_16.dat" using 4:xtic(1) title "Power 16"
unset multiplot
