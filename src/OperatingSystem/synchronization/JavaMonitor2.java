package OperatingSystem.synchronization;
/*
 * Monitor는 언어 수준에서 제공하는 동기화 처리 기술이다.
 * Java의 Monitor 사용법을 익혀보자.
 * 먼저 Synchronized 키워드. 임계 구역만 설정하면 lock 획득, 반환 작업은 알아서 해준다!
 */

public class JavaMonitor2 {
    static class Counter {
        public static int count = 0; //공유 데이터
        private static Object object = new Object(); // 모니터락을 획득하기 위한 용도의 의미없는 객체

        public static void increment() {
            synchronized (object) { // 임계 영역 구간을 좁히기 위해
                count++;
            }
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
