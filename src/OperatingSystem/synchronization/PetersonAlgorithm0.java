package OperatingSystem.synchronization;

/*
Software Solutions to the Critical-Section Problem
하드웨어의 도움 없는 초기 버전. 임계 구역 문제 해결을 보장하진 못 한다.
*/
public class PetersonAlgorithm0 {
    static int count = 0;
    static int turn = 0;
    static boolean[] flag = new boolean[2];

    static class Producer implements Runnable {
        @Override
        public void run() {
            for (int k = 0; k < 10000; k++) {
                /* entry section */
                flag[0] = true;
                turn = 1;
                while (flag[1] && turn == 1) ;
                /* critical section */
                count++;
                /* exit section */
                flag[0] = false;
                /* remainder section */
            }
        }
    }

    static class Consumer implements Runnable {
        @Override
        public void run() {
            for (int k = 0; k < 10000; k++) {
                /* entry section */
                flag[1] = true;
                turn = 0;
                while (flag[0] && turn == 0) ;
                /* critical section */
                count--;
                /* exit section */
                flag[1] = false;
                /* remainder section */
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Thread t1 = new Thread(new Producer());
        Thread t2 = new Thread(new Consumer());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(PetersonAlgorithm0.count);
    }
}
