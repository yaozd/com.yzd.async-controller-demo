package com.yzd.asynccontrollerdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.async.TimeoutCallableProcessingInterceptor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.Charset;
import java.util.List;

@Configuration
public class CustomMVCConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        StringHttpMessageConverter converter = new StringHttpMessageConverter(
                Charset.forName("UTF-8"));
        return converter;
    }

    @Override
    public void configureMessageConverters(
            List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);
        converters.add(responseBodyConverter());
    }

    @Override
    public void configureContentNegotiation(
            ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }
    @Override
    public void configureAsyncSupport(final AsyncSupportConfigurer configurer) {
        //异步请求处理的超时时间
        //configurer.setDefaultTimeout(60 * 1000L);
        configurer.setDefaultTimeout(6 * 1000L);
        configurer.registerCallableInterceptors(timeoutInterceptor());
        configurer.setTaskExecutor(threadPoolTaskExecutor());
    }

    @Bean
    public TimeoutCallableProcessingInterceptor timeoutInterceptor() {
        return new TimeoutCallableProcessingInterceptor();
    }

    /***
     * 异步请求处理的线程池
     * @return
     */
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        //目前为了方便测试，所以把线程池设置的比较小，生产上线程池的大小还是要与实际业务与机器资源相匹配才适合。
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        //设置核心线程数
        threadPool.setCorePoolSize(1);
        //设置最大线程数
        threadPool.setMaxPoolSize(1);
        //线程池所使用的缓冲队列
        threadPool.setQueueCapacity(1000);
        //等待任务在关机时完成--表明等待所有线程执行完
        threadPool.setWaitForTasksToCompleteOnShutdown(true);
        // 等待时间 （默认为0，此时立即停止），并没等待xx秒后强制停止
        threadPool.setAwaitTerminationSeconds(60);
        //  线程名称前缀
        threadPool.setThreadNamePrefix("AsyncSupport-");
        // 初始化线程
        threadPool.initialize();
        return threadPool;
        /*ThreadPoolTaskExecutor t = new ThreadPoolTaskExecutor();
        t.setCorePoolSize(10);
        t.setMaxPoolSize(50);
        t.setThreadNamePrefix("YJH");
        return t;*/
    }
}
