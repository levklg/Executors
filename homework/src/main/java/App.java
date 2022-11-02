

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class App {
    private final Lock lock = new ReentrantLock();
    int count = 1;
    private Condition conditionMet = lock.newCondition();
    boolean revers = false;
    public static void  main(String[] args) throws InterruptedException {
       new App().go();

    }

    private void go() throws InterruptedException {
        App app = new App();
        var thread1 = new Thread(() -> app.counter(count));
        thread1.setName("Поток 1: ");

        var thread2 = new Thread(() -> app.counter(count));
        thread2.setName("Поток 2: ");

        thread1.start();
        sleep();
        thread2.start();

        thread1.join();
        thread2.join();



    }





    private static void sleep() {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(1));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void counter(int count) {

        while(true) {
            run(count);
            if(count == 10) revers = true;
            if(count == 0) revers = false;
            if(!revers)count = count+1;
            if(revers)count = count-1;
        }
    }

    public void run(int count) {

        lock.lock();
        try {
            conditionMet.await(2, TimeUnit.SECONDS);
            System.out.println(Thread.currentThread().getName() + " " + count );
            conditionMet.signal();
            sleep();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


}
