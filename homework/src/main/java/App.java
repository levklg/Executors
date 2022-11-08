import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class App {
    private final Lock lock = new ReentrantLock();
    int count = 1;
    boolean revers = false;
    String messege1 = "Поток 1: ";
    String messege2 = "Поток 2: ";
    String messege = "Поток 1: ";
    private Condition conditionMet = lock.newCondition();
    private String lastThread = "Поток 1: ";
    private boolean start = false;

    public static void main(String[] args) throws InterruptedException {


        new App().go();


    }

    private void go() throws InterruptedException {
        App app = new App();
        var thread1 = new Thread(() -> app.counter(count));
        thread1.setName("Поток 1: ");

        var thread2 = new Thread(() -> app.counter(count));
        thread2.setName("Поток 2: ");

        thread1.start();
        thread2.start();

    }

    public void counter(int count) {

        while (true) {
            if (Thread.currentThread().getName().equals(messege)) {
                run(count);
                if (count == 10) revers = true;
                if (count == 0) revers = false;
                if (!revers) count = count + 1;
                if (revers) count = count - 1;
                if (Thread.currentThread().getName().equals("Поток 1: ")) {
                    messege = messege2;
                }
                if (Thread.currentThread().getName().equals("Поток 2: ")) {
                    messege = messege1;
                }

            }
        }
    }

    public void run(int count) {

        lock.lock();
        try {

            conditionMet.await(1, TimeUnit.SECONDS);
            System.out.println("  " + Thread.currentThread().getName() + " " + count);
            conditionMet.signal();


        } catch (InterruptedException e) {
            System.out.println(e);
        } finally {
            lock.unlock();
        }


    }


}
