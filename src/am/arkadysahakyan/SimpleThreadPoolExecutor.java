package am.arkadysahakyan;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Simple implementation of the thread pool pattern.
 * @author Arkady Sahakyan
 */
public class SimpleThreadPoolExecutor {

    private final Set<SimpleThreadPoolExecutor.Worker> workers = new HashSet<>();

    private final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue();

    private final int maxWorkers;

    public SimpleThreadPoolExecutor() {
        this.maxWorkers = 6;
    }

    public SimpleThreadPoolExecutor(int maxWorkers) {
        this.maxWorkers = maxWorkers;
    }

    public final class Worker implements Runnable {
        private Runnable t = null;
        private final Thread itself = new Thread(this);

        public Worker(Runnable t) {
            this.t = t;
            itself.start();
        }

        @Override
        public void run() {
            if (t != null) t.run();
            while (!tasks.isEmpty()) {
                t = tasks.poll();
                t.run();
            }
            workers.remove(this);
        }
    }

    public synchronized void addTask(Runnable t) {
        if (workers.size() < maxWorkers) {
            workers.add(new Worker(t));
            return;
        }
        tasks.add(t);
    }
}