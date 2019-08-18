package com.oshacker.multithread;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class MyThread extends Thread {
    private int tid;

    public MyThread(int tid) {
        this.tid=tid;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i <10 ; i++) {
                Thread.sleep(1000);
                System.out.println(String.format("%d,%d",tid,i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//BlockingQueue同步队列
class Producer implements Runnable {

    private BlockingQueue<String> q;

    public Producer(BlockingQueue<String> q) {
        this.q=q;
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (int i = 0; i < 100; i++) {
                    Thread.sleep(1000);
                    q.put(String.valueOf(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

class Consumer implements Runnable {

    private BlockingQueue<String> q;

    public Consumer(BlockingQueue<String> q) {
        this.q=q;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(Thread.currentThread().getName()+"："+q.take());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

public class MultiThread {
    //1、Thread
    public static void testThread() {
//        for (int i = 0; i <10 ; i++) {
//            MyThread mt=new MyThread(i);
//            mt.start();
//        }

        for (int i = 0; i <10 ; i++) {
            final int i1=i;
            Thread t=new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int j = 0; j <10 ; j++) {
                            Thread.sleep(1000);
                            System.out.println(String.format("%d,%d",i1,j));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
    }

    //2、Synchronized（内置锁）
    public static Object obj1=new Object();
    public static Object obj2=new Object();
    public static void testSynchronized1() {
        synchronized (obj1) {
            try {
                for (int j = 0; j <10 ; j++) {
                    Thread.sleep(1000);
                    System.out.println(String.format("%s S1 %d",Thread.currentThread().getName(),j));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized2() {
        synchronized (obj1) {//将obj1换成obj2，体会两把不同锁的结果
            try {
                for (int j = 0; j <10 ; j++) {
                    Thread.sleep(1000);
                    System.out.println(String.format("%s S2 %d",Thread.currentThread().getName(),j));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized() {
        for (int i = 0; i <10 ; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    testSynchronized1();
                    testSynchronized2();
                }
            },String.valueOf(i)).start();
        }
    }

    //3、Atomic
    private static int counter=0;
    private static AtomicInteger atomicInteger=new AtomicInteger(0);

    //最终结果应该是100，但是结果不是。假如此时counter=7,两个线程counter++后应该是9，
    //但是假如两个线程拿到的都是7，counter++后都是8，这就会出问题。
    public static void testWithoutAtomic() {
        for (int i = 0; i <10 ; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        for (int j = 0; j < 10; j++) {
                            counter++;
                            System.out.println(counter);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    //最终的结果一定是100
    public static void testWithAtomic() {
        for (int i = 0; i <10 ; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        for (int j = 0; j < 10; j++) {
                            System.out.println(atomicInteger.incrementAndGet());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void testAtomic() {
//        testWithoutAtomic();
        testWithAtomic();
    }


    //4、BlockingQueue同步队列
    public static void testBlockingQueue() {
        BlockingQueue<String> q=new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q),"Consumer1").start();
        new Thread(new Consumer(q),"Consumer2").start();
    }

    //5、ThreadLocal
    public static ThreadLocal<Integer> threadLocalUserIds=new ThreadLocal<>();
    public static int userId;
    public static void testThreadLocal() {
//        for (int i = 0; i <10 ; i++) {
//            final int i1=i;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        threadLocalUserIds.set(i1);
//                        Thread.sleep(1000);
//                        System.out.println(String.valueOf(i1)+" "+threadLocalUserIds.get());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }).start();
//        }

        for (int i = 0; i <10 ; i++) {
            final int i1=i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        userId=i1;
                        Thread.sleep(1000);
                        System.out.println(String.valueOf(i1)+" "+userId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    //6、Executor框架
    public static void testExecutor() {
//        ExecutorService service= Executors.newSingleThreadExecutor();//单线程运行框架
        ExecutorService service= Executors.newFixedThreadPool(2);//多线程运行框架
        service.submit(new Runnable() {//注意：这里消费的线程池中的线程
            @Override
            public void run() {
                for (int i = 0; i <10 ; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("executor1："+i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        service.submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <10 ; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("executor2："+i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        service.shutdown(); //任务执行完才关闭
        while (!service.isTerminated()) {//主线程轮询查看所有的任务是否执行完毕
            try {
                Thread.sleep(1000);
                System.out.println("wait for termination...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //7、Future
    public static void testFuture1() {
        ExecutorService service=Executors.newSingleThreadExecutor();
        Future<Integer> future = service.submit(new Callable<Integer>() {//3s后程序才返回
            @Override
            public Integer call() throws Exception {
                Thread.sleep(3000);
                return 1;
            }
        });

        service.shutdown();
        try {
//            System.out.println(future.get());//卡住，3s后才拿到结果
            System.out.println(future.get(100,TimeUnit.MILLISECONDS));//超时时间为100ms
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testFuture2() {
        ExecutorService service=Executors.newSingleThreadExecutor();
        Future<Integer> future = service.submit(new Callable<Integer>() {//3s后程序才返回
            @Override
            public Integer call() throws Exception {
                Thread.sleep(3000);
                throw new IllegalArgumentException("异常");
            }
        });

        service.shutdown();
        try {
            System.out.println(future.get());//卡住，3s后捕获到异常
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
//        testThread();
//        testSynchronized();
//        testAtomic();
//        testBlockingQueue();
//        testThreadLocal();
//        testExecutor();
//        testFuture1();
       testFuture2();
    }
}
