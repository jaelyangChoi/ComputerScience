package OperatingSystem.synchronization.SynchronizationExamples;

class CashBox {
    private int[] buffer; //bounded-buffer
    private int count, in, out;

    public CashBox(int bufferSize) {
        buffer = new int[bufferSize];
        count = in = out = 0;
    }
    /* 모니터 락 사용. 하나의 스레드만 들어오도록 보장 */
    synchronized public void give(int money) {
        while (count == buffer.length) { //버퍼가 다 찼으면 대기한다.
            try {
                wait(); //바쁜 대기를 하는게 아니라 consumer가 notify해주길 기다린다.
            } catch (InterruptedException e) { //wait하다가 notify() 호출되면 인터럽트 걸려서 탈출한다.
            }
        }
        buffer[in] = money;
        in = (in + 1) % buffer.length;
        count++;
        System.out.printf("여기있다, 용돈: %d원\n", money);
        notify();
    }

    synchronized public int take() throws InterruptedException {
        while (count == 0) { //버퍼가 비었으면 대기한다.
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        int money = buffer[out];
        out = (out + 1) % buffer.length;
        count--;
        System.out.printf("고마워유, 용돈: %d원\n", money);
        notify();
        return money;
    }
}

public class BoundedBufferProblem {
    public static void main(String[] args) {
        CashBox cashBox = new CashBox(1); //동일한 모니터 락(this)을 쓰기 위해
        Thread[] producers = new Thread[1];
        Thread[] consumers = new Thread[1];
        // Create threads of producers
        for (int i = 0; i < producers.length; i++) {
            producers[i] = new Thread(new ProdRunner(cashBox));
            producers[i].start();
        }
        // Create threads of consumers
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Thread(new ConsRunner(cashBox));
            consumers[i].start();
        }
    }

    static class ProdRunner implements Runnable {
        CashBox cashBox;

        public ProdRunner(CashBox cashBox) {
            this.cashBox = cashBox;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep((long) (Math.random() * 500));
                    int money = ((int) (1 + Math.random() * 9)) * 10000;
                    cashBox.give(money);
                }
            } catch (InterruptedException e) {
            }
        }
    }

    static class ConsRunner implements Runnable {
        CashBox cashBox;

        public ConsRunner(CashBox cashBox) {
            this.cashBox = cashBox;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep((long) (Math.random() * 500));
                    int money = cashBox.take();
                }
            } catch (InterruptedException e) {
            }
        }
    }
}
