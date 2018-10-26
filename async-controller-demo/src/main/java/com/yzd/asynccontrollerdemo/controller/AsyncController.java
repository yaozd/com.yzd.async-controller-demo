package com.yzd.asynccontrollerdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

/***
 * 异步请求处理
 */
@RestController
@RequestMapping("async")
public class AsyncController {
    @RequestMapping("hello")
    public Callable<String> hello() {
        // 这么做的好处避免web server的连接池被长期占用而引起性能问题，
        // 调用后生成一个非web的服务线程来处理，增加web服务器的吞吐量。
        return () -> {
            return "小单 - " + System.currentTimeMillis();
        };
    }

    /***
     * 模拟业务处理时间
     * @return
     */
    @RequestMapping("test")
    public Callable<String> test() {
        System.out.println("当前线程名称 thread id is : " + Thread.currentThread().getName());
        // 这么做的好处避免web server的连接池被长期占用而引起性能问题，
        // 调用后生成一个非web的服务线程来处理，增加web服务器的吞吐量。
        return () -> {
            //模拟长时间任务
            Thread.sleep(3 * 1000L);
            int a=0;
            System.out.println("当前异步处理-线程名称=执行成功 thread id is : " + Thread.currentThread().getName());
            //int b=1/a;
            return "小单 - " + System.currentTimeMillis();
        };
    }

    /***
     * 模拟异常情况
     * @return
     */
    @RequestMapping("testException")
    public Callable<String> testException() {
        System.out.println("当前线程名称 thread id is : " + Thread.currentThread().getName());
        // 这么做的好处避免web server的连接池被长期占用而引起性能问题，
        // 调用后生成一个非web的服务线程来处理，增加web服务器的吞吐量。
        return () -> {
            Thread.sleep(1 * 1000L);
            int a=0;
            System.out.println("当前异步处理-线程名称=执行成功 thread id is : " + Thread.currentThread().getName());
            //模拟异常
            int b=1/a;
            return "小单 - " + System.currentTimeMillis();
        };
    }
    @Autowired
    ThreadPoolTaskExecutor executor;

    /***
     * 查看异步请求处理当前线程情况
     * @return
     */
    @RequestMapping("threadInfo")
    public String threadInfo(){
        //当前活动的总任务数（正在处理的任务）
        Integer count= executor.getActiveCount();
        //当前缓冲队列中任务数（等待处理的任务）
        Integer queueSize= executor.getThreadPoolExecutor().getQueue().size();
        return "当前活动的总任务数（正在处理的任务）getActiveCount="+ count.toString()+
                ";当前缓冲队列中任务数（等待处理的任务）queueSize="+queueSize;
    }
}
