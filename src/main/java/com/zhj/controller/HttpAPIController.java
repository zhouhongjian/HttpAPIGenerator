package com.zhj.controller;

import com.zhj.InvokeResult;
import com.zhj.ServiceInvoker;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by zhj on 2017/11/26.
 */
@RestController
public class HttpAPIController {

    @Resource
    private ServiceInvoker invoker;

    /**
     * @param className 类的全限定名 fully qualified name of the desired class
     * @param methodName 方法名
     * @param requestParam GET和POST方法的所有kv
     * */
    @RequestMapping("api/{className}/{methodName}")
    public String api(@PathVariable String className, @PathVariable String methodName,
                      @RequestParam Map requestParam){

        if (!StringUtils.isEmpty(requestParam.get("params"))){
//            Map<String,String> serviceParamsStr = JSON.parseObject((String) requestParam.get("params"),new TypeReference<Map<String,String>>(){});
            String serviceParamsStr = (String) requestParam.get("params");
            InvokeResult<String> result =  invoker.invoke(className,methodName,serviceParamsStr);
        }

        //1、获取spring上下文
        //2、根据上下文去搜索对应的bean
        //3、通过反射调用这个bean对应的方法
        //4、获取返回结果，进行json化

        System.out.println("test");
        return "{aaa:111}";
    }

}
