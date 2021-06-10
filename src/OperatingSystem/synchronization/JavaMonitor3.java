package OperatingSystem.synchronization;
/*
 * 상호배제 위반 사례
 */

public class JavaMonitor3 {
    static class Counter {
        public static int count = 0; //공유 데이터

        public void increment() {
            synchronized (this) { //모니터 락을 this로부터 얻는다. this가 다르면 동기화x
                count++;
            }
        }
    }

    static class MyRunnable implements Runnable {
        private Counter counter;

        public MyRunnable(Counter counter) {
            this.counter = counter;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10000; i++)
                counter.increment();
        }
    }

    public static void main(String[] args) throws Exception {
        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new MyRunnable(new Counter())); //객체가 다르기 때문에 동기화 x
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++)
            threads[i].join();
        System.out.println("counter = " + Counter.count);
    }
}
