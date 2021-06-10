package OperatingSystem.synchronization;
/*
 * 동기화 처리 전
 */

public class JavaMonitor0 {
    static class Counter {
        public static int count = 0; //공유 데이터

        public static void increment() {
            count++;
        }
    }

    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            for (int i = 0; i < 10000; i++)
                Counter.increment();
        }
    }

    public static void main(String[] args) throws Exception {
        Thread[] threads = new Thread[5];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new MyRunnable());
            threads[i].start();
        }
        for (int i = 0; i < threads.length; i++)
            threads[i].join();
        System.out.println("counter = " + Counter.count);
    }
}
