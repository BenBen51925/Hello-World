package com.example.demo.collection.list;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;


/*在主线程中new了一个非线程安全的ArrayList，
然后开100个线程分别向这个ArrayList里面添加元素，
每个线程添加100个元素，等所有线程执行完成后，
这个ArrayList的size应该是多少？应该是10000个？*/
//
public class Main
{
    public static void main(String[] args)
    {
        // 进行10次测试
        for(int i = 0; i < 10; i++)
        {
            test();
        }
    }

    public static void test()
    {
        // 用来测试的List
        //List<Object> list = new ArrayList<Object>();
    	List<Object> list = new Vector<Object>();

        // 线程数量(1000)
        int threadCount = 100;

        // 用来让主线程等待threadCount个子线程执行完毕
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        // 启动threadCount个子线程
        for(int i = 0; i < threadCount; i++)
        {
            Thread thread = new Thread(new MyThread(list, countDownLatch));
            thread.start();
        }

        try
        {
            // 主线程等待所有子线程执行完成，再向下执行
            countDownLatch.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        // List的size
        System.out.println(list.size());
    }
}

class MyThread implements Runnable
{
    private List<Object> list;

    private CountDownLatch countDownLatch;

    public MyThread(List<Object> list, CountDownLatch countDownLatch)
    {
        this.list = list;
        this.countDownLatch = countDownLatch;
    }

    public void run()
    {
        // 每个线程向List中添加100个元素
        for(int i = 0; i < 100; i++)
        {
            list.add(new Object());
        }

        // 完成一个子线程
        countDownLatch.countDown();
    }
}