## Peterson Lock Implementation and Verification

peterson_lock_verify is a Java program that verifies the correctness of the peterson_lock implementation. It uses two threads to concurrently perform increment and decrement operations on a shared variable. The program uses a Peterson lock to ensure that only one thread executes its critical section at a time. The correctness of the implementation is verified by comparing the expected and actual values of the shared variable after the threads complete their operations.

### How to run

To run the program, compile both the `peterson_lock_verify.java` and `peterson_lock.java` files using the following command:

```
javac peterson_lock.java
```

```
javac peterson_lock_verify.java
```

Then, run the program using the following command:

```
java peterson_lock_verify
```

or use the bash script run.sh

```
bash run.sh
```

### Files

The following files are included in the program:

* `peterson_lock_verify.java`: This file contains the peterson_lock_verify class, which implements the Peterson lock verification program.
* `peterson_lock.java`: This file contains the `peterson_lock` class, which implements the Peterson lock algorithm.
