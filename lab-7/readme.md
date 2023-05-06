# ArrayBasedQueueLock, CLHQueueLock, MCSQueueLock

This repository contains implementations of three different lock algorithms in Java: arrayBasedQueueLock, CLHQueueLock, and MCSQueueLock. Additionally, verification programs: arrayBasedQueueverify, CLHQueueverify, MCSQueueverify, are provided to test and compare the performance of these lock implementations.

## Lock Implementations

### arrayBasedQueueLock

arrayBasedQueueLock is a simple queue-based lock implementation using a circular array as its underlying data structure. Each thread acquires the lock by atomically incrementing the rear index and waits for its turn by spinning on the front index.

### CLHQueueLock

The CLH Queue Lock is a queue-based lock implementation where threads form a virtual queue. Each thread spins on a local variable, reducing cache coherence traffic and contention on the lock. The CLH Queue Lock provides fair lock acquisition and is suitable for scenarios where high contention is expected.

### MCSQueueLock

The MCS Queue Lock is another queue-based lock implementation that forms a virtual queue of waiting threads. In this case, each thread spins on its predecessor's state, further reducing cache coherence traffic. The MCS Queue Lock provides fair lock acquisition and is suitable for large multiprocessor systems with high contention.

## Verification Programs

### arrayBasedQueueverify

arrayBasedQueueverify is a program designed to test and compare the performance of the arrayBasedQueueLock implementation.

### CLHQueueverify

CLHQueueverify is a program designed to test and compare the performance of the CLHQueueLock implementation.

### MCSQueueverify

MCSQueueverify is a program designed to test and compare the performance of the MCSQueueLock implementation.

These programs create multiple threads that increment a shared counter while using the respective lock implementation for synchronization. It measures the throughput of the lock implementations by counting the number of operations performed within a specified time duration.

## Usage

To run the verification program, first compile the Java files:

```
javac arrayBasedQueueLock.java
```

```
javac arrayBasedQueueverify.java
```

Then, run the arrayBasedQueueverify program with the desired number of threads and duration in milliseconds:

```
java arrayBasedQueueverify <num_threads> <duration>
```

For example, to run the verification program with 4 threads and a duration of 10000 milliseconds:

```
java arrayBasedQueueverify 4 10000
```

The output will display the number of threads, number of operations, elapsed time, and throughput for the respective lock implementation.

This can be done for all threads and the output is added to the `output.dat` file in their respective folder. These steps can be run by using the script file `run.sh`

```
bash run.sh
```

Similar process can be used for other lock implementations as well.
