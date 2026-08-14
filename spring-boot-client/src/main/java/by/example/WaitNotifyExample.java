package by.example;

public class WaitNotifyExample {

    private static final Object lock = new Object();
    private static boolean ready = false;

    public static void main(String[] args) {

        Thread waiter = new Thread(() -> {
            synchronized (lock) {
                while (!ready) {          // re-check in a loop, not an `if`
                    try {
                        System.out.println("Waiter: not ready, waiting...");
                        lock.wait();      // releases the lock and sleeps
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println("Waiter: got the signal, continuing!");
            }
        });

        Thread notifier = new Thread(() -> {
            synchronized (lock) {
                ready = true;
                System.out.println("Notifier: set ready = true, notifying...");
                lock.notify();            // wakes the waiting thread
            }
        });

        waiter.start();
        notifier.start();
    }
}
