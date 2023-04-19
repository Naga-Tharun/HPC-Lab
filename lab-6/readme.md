# TAS, TTAS, TTAS with Backoff Locks

## Test and Set Lock (TAS)

This folder contains two Java files: `TASLock.java` and `TASverify.java`.

### TASLock.java

`TASLock.java` implements a simple Test-and-Set (TAS) lock using the `java.util.concurrent.atomic.AtomicBoolean` class. The `acquireLock()` method acquires the lock by repeatedly setting the lock to `true` until it succeeds, while the `releaseLock()` method releases the lock by setting the lock to `false`.


## Test, Test and Set Lock (TTAS)

This folder contains two Java files: `TTASLock.java` and `TTASverify.java`.

### TTASLock.java

`TTASLock.java` implements a Test-and-Test-and-Set (TTAS) lock. The lock uses the `AtomicBoolean` class to acquire and release the lock. It also includes a `SPIN_LIMIT` constant that controls the number of times a thread spins before yielding to another thread. The `acquireLock` method uses two phases to acquire the lock: first, it tests if the lock is free and spins for a short time if it is not; second, it performs a test-and-set operation to acquire the lock. The `releaseLock` method simply sets the lock variable to false.


## Test, Test and Set Lock with Backoff (TTAS with Backoff)

This folder contains two Java files: `TTASBackoffLock.java` and `TTASBackoffverify.java`.

### TTASBackoffLock.java

`TTASBackoffLock.java` implements a Test-and-Test-and-Set with Backoff (TTASBackoff) lock. The lock uses the `AtomicBoolean` class to acquire and release the lock. It also includes a minimum and maximum delay range for the backoff mechanism.

The `acquireLock` method uses a while loop to try to acquire the lock. It first checks if the lock is free and spins if it is not. If the lock is free, it performs a test-and-set operation to acquire the lock. If the lock is not free, it goes into a backoff phase, where it generates a random delay using `TimeUnit.MILLISECONDS.sleep()` and updates the backoff time exponentially within the maximum delay. The lock tries to acquire again once the backoff period is complete. The `releaseLock` method simply sets the lock variable to false.


### TASverify.java, TTASverify.java, TTASBackoffverify.java

`TASverify.java` contains a simple program that uses `TASLock` to synchronize access to a shared counter variable across multiple threads. The program takes two command-line arguments: the number of threads to use and the duration of the test in milliseconds. It then creates a fixed-size thread pool using `java.util.concurrent.ExecutorService` and repeatedly executes a task that acquires the lock, increments the counter, and releases the lock. After the test is complete, the program prints the number of threads, the number of operations performed, the elapsed time, and the throughput in millions of operations per second.

To run the program, use the following command:

```
javac TASverify.java
```

```
java TASverify <num_threads> <duration_ms> 
```

where `<num_threads>` is the number of threads to use and `<duration_ms>` is the duration of the test in milliseconds.

or they can be run using

```
bash run.sh
```

in their respective folders

Similar commands are used for TTAS, TTAS with backoff locks.
