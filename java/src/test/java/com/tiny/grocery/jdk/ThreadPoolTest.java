package com.tiny.grocery.jdk;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tiny on 2017/8/20.
 */
public class ThreadPoolTest {

    @Test
    public void test1() throws InterruptedException, ExecutionException {
        final AtomicInteger counter = new AtomicInteger(0);
        ExecutorService service = Executors.newCachedThreadPool();
        ExecutorCompletionService<AtomicInteger> completionService = new ExecutorCompletionService<>(service);
        for (int i = 0; i< 100; i++){
            final int j = i;
            completionService.submit(new Callable<AtomicInteger>() {
                @Override
                public AtomicInteger call() throws Exception {
                    Thread.sleep((100 - j) * 100);
                    counter.addAndGet(1);
                    Thread.sleep((100 - j) * 1000);
                    return counter;
                }
            });
        }
        // print result
        for (int i = 0; i< 100; i++){
            Future<AtomicInteger> future = completionService.take();
            System.out.println(future.get().intValue());
        }
    }
}
