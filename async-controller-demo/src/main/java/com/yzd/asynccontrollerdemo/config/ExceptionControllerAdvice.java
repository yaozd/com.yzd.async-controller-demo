package com.yzd.asynccontrollerdemo.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionControllerAdvice {

    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map errorHandler(Exception ex) {
        System.out.println("异常--"+ex.toString());
        ex.printStackTrace();
        System.out.println("异常--ex.printStackTrace()");
        Map map = new HashMap();
        map.put("code", 100);
        map.put("msg", ex.getMessage());
        return map;
    }

    /***
     * 异步请求的超时异常
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = AsyncRequestTimeoutException.class)
    public Map errorHandlerForAsyncRequestTimeoutException(Exception ex) {
        //HTTP状态码408 Request Timeout 请求超时
        System.out.println(ex.toString());
        Map map = new HashMap();
        map.put("code", 408);
        map.put("msg", "AsyncRequestTimeoutException:Request Timeout 请求超时");
        return map;
    }
}

