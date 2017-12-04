package cn.zjlspace;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class MyThread extends Thread {
    private int tid;

    public MyThread(int tid) {
        this.tid = tid;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(1000);
                System.out.println(String.format("R%d:%d", tid, i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
class Producer implements Runnable{
    private BlockingQueue<String> q;
    public Producer(BlockingQueue<String> q){
        this.q=q;
    }
    @Override
    public void run() {
        try{
            for (int i = 0; i < 100; i++) {
                Thread.sleep(100);
                q.put(String.valueOf(i));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

class  Consumer implements Runnable{
    private BlockingQueue<String> q;
    public Consumer(BlockingQueue<String> q){
        this.q=q;
    }

    @Override
    public void run() {
        try{
            while(true){
                System.out.println(Thread.currentThread().getName()+":"+q.take());
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}

public class MultiThread {
    private static Object obj=new Object();
    public static void testSynchronized1(){
        synchronized (obj){
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(500);
                    System.out.println(String.format("T3:%d", i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public static void testSynchronized2(){
        synchronized (obj){
            try {
                for (int i = 0; i < 10; i++) {
                    Thread.sleep(500);
                    System.out.println(String.format("T4:%d",  i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static void testSynchronized(){
        for (int i = 0; i <10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testSynchronized1();
                    testSynchronized2();
                }
            }).start();

        }
    }

    public static void testThread() {
//        for (int i = 0; i < 10; i++) {
//            new MyThread(i).start();
//        }

        for (int i = 0; i < 10; i++) {
            final int tid = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int j = 0; j < 10; j++) {
                            Thread.sleep(1000);
                            System.out.println(String.format("R%d:%d", tid, j));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();

        }
    }



    public  static void testBlockingQueue(){
        BlockingQueue<String> q=new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(q),"Producer").start();
        new Thread(new Consumer(q),"Consumer1").start();
        new Thread(new Consumer(q),"Consumer2").start();
    }

    private static int counter=0;
    private static AtomicInteger atomicInteger=new AtomicInteger(0);

    public static void sleep(int mills){
        try{
//            Thread.sleep(new Random().nextInt(mills));
            Thread.sleep(mills);

        }catch (Exception e){
            e.printStackTrace();

        }
    }
    public static void testWithAtomic(){
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                     sleep(1000);
                    for (int j = 0; j <10; j++) {
                        System.out.println(atomicInteger.incrementAndGet());

                    }
                }
            }).start();

        }
    }
    public static void testWithoutAtomic(){
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sleep(1000);
                    for (int j = 0; j <10; j++) {
                      counter++;
                        System.out.println(counter);
                    }
                }
            }).start();

        }

    }

    public static void testAtomic(){
        testWithoutAtomic();
        testWithAtomic();
    }

    private static ThreadLocal<Integer> threadLocalUserIds=new ThreadLocal<>();
    private static int userId;
    public static void testThreadLocal(){
        for (int i = 0; i < 10; i++) {
            final int finalI=i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadLocalUserIds.set(finalI);
                    sleep(1000);
                    System.out.println("ThreadLocal:"+threadLocalUserIds.get());

                }
            }).start();

        }


        for (int i = 0; i < 10; i++) {
            final int finalI=i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    userId=finalI;
                    sleep(1000);
                    System.out.println("NonThreadLocal:"+userId);

                }
            }).start();

        }
    }


    public static void testExecutor(){//线程池任务框架
//        ExecutorService service= Executors.newSingleThreadExecutor();//单线程
        ExecutorService service= Executors.newFixedThreadPool(2);//多线程，线程池
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    sleep(1000);
                    System.out.println("Execute1 "+i);

                }

            }
        });
        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    sleep(1000);
                    System.out.println("Execute2 "+i);

                }

            }
        });
        service.shutdown();
        while(!service.isTerminated()){
            sleep(1000);
            System.out.println("Wait for termination,");

        }


    }


    public static void testFutrue() {
        ExecutorService service= Executors.newSingleThreadExecutor();//单线程

        Future<Integer> future=service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                sleep(1000);
                return 1;
//                throw new IllegalArgumentException("异常！");

            }
        });
        service.shutdown();

        try{
            System.out.println(future.get(100,TimeUnit.MILLISECONDS));//设置时间点，超时

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {


//        testThread();
//        testSynchronized();
//        testBlockingQueue();
//        testAtomic();
//        testThreadLocal();
//        testExecutor();
        testFutrue();
    }
}

