package OperatingSystem.synchronization.SynchronizationExamples;

class SharedDB {
    private int readerCount = 0;
    private boolean writeMode = false;

    public void read() {
        try {
            Thread.sleep((long) (Math.random() * 500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[reading] 동시에 읽고 있는 reader의 수: " + readerCount);
    }

    public void write() {
        try {
            Thread.sleep((long) (Math.random() * 500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("[writing] 읽고 있는 reader의 수: " + readerCount);
    }

    // 쓰기 모드가 끝날 때까지 대기
    synchronized public void acquireReadLock() {
        while (writeMode == true) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        System.out.println("SharedDB.acquireReadLock");
        readerCount++;
    }

    synchronized public void releaseReadLock() {
        System.out.println("SharedDB.releaseReadLock");
        readerCount--;
        if (readerCount == 0)
            notify(); //읽고 있는 reader가 없으면 쓰기 모드 진입 가능
    }

    synchronized public void acquireWriteLock() {
        while (readerCount > 0 || writeMode == true) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        System.out.println("SharedDB.acquireWriteLock");
        writeMode = true;
    }

    synchronized public void releaseWriteLock() {
        System.out.println("SharedDB.releaseWriteLock");
        writeMode = false;
        notifyAll(); //대기 중인 reader, writer를 모두 깨워 공정하게 경쟁하게 한다.
    }
}


public class ReadersWritersProblem {
    public static void main(String[] args) {
        SharedDB sharedDB = new SharedDB();
        Thread[] readers = new Thread[5];
        Thread[] writers = new Thread[3];
        // Create threads of producers
        for (int i = 0; i < readers.length; i++) {
            readers[i] = new Thread(new ReaderRunner(sharedDB));
            readers[i].start();
        }
        // Create threads of consumers
        for (int i = 0; i < writers.length; i++) {
            writers[i] = new Thread(new WriterRunner(sharedDB));
            writers[i].start();
        }
    }

    static class ReaderRunner implements Runnable {
        SharedDB sharedDB;

        public ReaderRunner(SharedDB sharedDB) {
            this.sharedDB = sharedDB;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep((long) (Math.random() * 500));
                    sharedDB.acquireReadLock();
                    sharedDB.read();
                    sharedDB.releaseReadLock();
                }
            } catch (InterruptedException e) {
            }
        }
    }

    static class WriterRunner implements Runnable {
        SharedDB sharedDB;

        public WriterRunner(SharedDB sharedDB) {
            this.sharedDB = sharedDB;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep((long) (Math.random() * 500));
                    sharedDB.acquireWriteLock();
                    sharedDB.write();
                    sharedDB.releaseWriteLock();
                }
            } catch (InterruptedException e) {
            }
        }
    }
}
