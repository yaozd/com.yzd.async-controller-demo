

### -异步请求处理异常总结
```
1.客户端关闭连接-（此时处理结果无法发送到客户端）
当前异步处理-线程名称=执行成功 thread id is : AsyncSupport-1
org.apache.catalina.connector.ClientAbortException: java.io.IOException: 远程主机强迫关闭了一个现有的连接。

2.正常处理过程中的异常
java.lang.ArithmeticException: / by zero
2018-10-26 11:40:29.510  WARN 13304 --- [nio-8080-exec-5] .m.m.a.ExceptionHandlerExceptionResolver : Resolved [java.lang.ArithmeticException: / by zero]

3.异步请求的超时异常
当前线程名称 thread id is : http-nio-8080-exec-4
org.springframework.web.context.request.async.AsyncRequestTimeoutException
2018-10-26 13:42:35.200  WARN 19108 --- [nio-8080-exec-6] .m.m.a.ExceptionHandlerExceptionResolver : Resolved [org.springframework.web.context.request.async.AsyncRequestTimeoutException]

4.异步请求处理的线程池队列已满造成的。
did not accept task: org.springframework.web.context.request.async.WebAsyncManager
org.springframework.core.task.TaskRejectedException: Executor [java.util.concurrent.ThreadPoolExecutor@6794485e[Running, pool size = 1, active threads = 1, queued tasks = 1000, completed tasks = 3003]] did not accept task: org.springframework.web.context.request.async.WebAsyncManager$5@632af98c
如何解决呢？
1. 控制提交的任务数量，即提交的任务数量不要超过它当前能处理的能力 (这里可以用生产者消费者的模式来解决)
2. 确保不要在shutdown()之后在执行任务
3. 可以用LinkedBlockingQueue代替ArrayBlockingQueue,因为LinkedBlockingQueue可以设成无界的，但是需要注意，设成无界后最终可能发生OOM（内存溢出），
所以要保证第一二点。
参考：
RejectedExecutionException 分析
https://www.cnblogs.com/youtianhong/p/6398388.html

5.HttpMediaTypeNotAcceptableException: Could not find acceptable representation
https://blog.csdn.net/liubinblog/article/details/78656259
错误原因:请求contenttype=application/json;charset=UTF-8,而这里的produces=text/html
原来代码：
@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "text/html; charset=UTF-8")
解决方法：去掉 produces = "text/html; charset=UTF-8"这段代码或者设置produces=application/json;charset=UTF-8

```
### -异步请求处理的线程池队列大小设置

(过小影响性能，过大内存溢出，异步请求处理的超时时间也需要考虑进去。寻找一个在异步请求处理时间的最大值)

```
异步请求处理的线程池队列已满造成的。
如何解决呢？
1. 控制提交的任务数量，即提交的任务数量不要超过它当前能处理的能力 (这里可以用生产者消费者的模式来解决)
2. 确保不要在shutdown()之后在执行任务
3. 可以用LinkedBlockingQueue代替ArrayBlockingQueue,因为LinkedBlockingQueue可以设成无界的，但是需要注意，设成无界后最终可能发生OOM（内存溢出），
所以要保证第一二点。
```
### -参考文档
 - [实战Spring Boot 2.0系列(四) - 使用WebAsyncTask处理异步任务](https://blog.csdn.net/baidu_22254181/article/details/80789090)
 - [Spring Boot实现异步请求（Servlet 3.0）](https://www.jb51.net/article/112246.htm)
 - [spring线程池ThreadPoolTaskExecutor与阻塞队列BlockingQueue](https://www.cnblogs.com/lic309/p/4186880.html)
 - [Spring MVC - Intercepting Async Requests using AsyncHandlerInterceptor](https://www.logicbig.com/tutorials/spring-framework/spring-web-mvc/async-intercept.html)
 
 ### -使用场景

 ```
1.支付成功通知把异步变为同步
支付成功通知
支付成功会通过异步回调通知调用方
可以通过Redis作为中间节点，进行循环查询
最多查询5次，第1次sleep 2秒后查询，后面的1秒查询（为的是保证尽可能快的通知到客户）
```