import os

os.system("mkdir plot_data")

os.system("mkdir plot_data/RMT")
# Enable only for first time, then disable
os.system("mkdir plot_data/RMT/thread_1")
for i in range(2, 17, 2):
  cmd = "mkdir plot_data/RMT/thread_" + str(i)
  os.system(cmd)

with open("dump.dat") as f:
  for line in f:
    tmp = line.split("\n")[0]
    tp = tmp.split("\t")

    cmd = "echo \"" + tmp + "\" >> plot_data/RMT/thread_" + tp[2] + "/RMT_" + tp[1] + ".dat"
    os.system(cmd)


os.system("mkdir plot_data/RTM")
# Enable only for first time, then disable
os.system("mkdir plot_data/RTM/matrix_512")
os.system("mkdir plot_data/RTM/matrix_1024")
os.system("mkdir plot_data/RTM/matrix_2048")

with open("dump.dat") as f:
  for line in f:
    tmp = line.split("\n")[0]
    tp = tmp.split("\t")

    cmd = "echo \"" + tmp + "\" >> plot_data/RTM/matrix_" + tp[0] + "/RTM_" + tp[1] + ".dat"
    os.system(cmd)