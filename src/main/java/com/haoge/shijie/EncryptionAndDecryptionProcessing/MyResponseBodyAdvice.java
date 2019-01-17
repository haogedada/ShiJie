package com.haoge.shijie.EncryptionAndDecryptionProcessing;


import com.google.gson.Gson;
import com.haoge.shijie.annotation.SerializedField;
import com.haoge.shijie.util.CryptoHelper;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeff on 15/10/23.
 *
 * @SerializedField()，加密字符注解
 * @SerializedField(includes={"success","message","data"})只加密value不加密key值 不加密，但如果参数不对没写的参数或错误的不会显示
 * @SerializedField(includes={"code","message","data"},encode = false)
 * @SerializedField(excludes={"success","message","data"})去除key不显示，并加密value,同时显示value的明文和加密
 */
@Order(1)
@ControllerAdvice(basePackages = "com.haoge.shijie.controller")//这句注释或包引错位置可以关闭加密
public class MyResponseBodyAdvice implements ResponseBodyAdvice {
    //包含项
    private String[] includes = {};
    //排除项
    private String[] excludes = {};
    //要加密的字符（注意excludes的值一定包含encryptions的值）
    private String[] encryptions = {};
    //是否加密
    private boolean encode = true;

    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        //这里可以根据自己的需求
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        //重新初始化为默认值
        includes = new String[]{};
        excludes = new String[]{};
        encryptions = new String[]{};
        encode = true;
        //判断返回的对象是单个对象，还是list，还是map
        if (o == null) {
            return null;
        }
        if (methodParameter.getMethod().isAnnotationPresent(SerializedField.class)) {
            //获取注解配置的包含,去除字段,加密字段
            SerializedField serializedField = methodParameter.getMethodAnnotation(SerializedField.class);
            includes = serializedField.includes();
            excludes = serializedField.excludes();
            encryptions = serializedField.encryptions();
            //是否加密
            encode = serializedField.encode();
        }

        Object retObj = null;
        if (o instanceof List) {
            //List
            List list = (List) o;

            retObj = handleList(list);

        } else {
            //Single Object
            retObj = handleSingleObject(o);

        }
        return retObj;
    }

    /**
     * 处理返回值是单个enity对象
     *
     * @param o
     * @return
     */
    private Object handleSingleObject(Object o) {
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] fields = o.getClass().getDeclaredFields();
        for (Field field : fields) {
            //如果未配置表示全部的都返回
            if (includes.length == 0 && excludes.length == 0) {
                Object newVal = getNewVal(o, field);
                map.put(field.getName(), newVal);
            } else if (includes.length > 0) {
                //有限考虑包含字段
                if (CryptoHelper.isStringInArray(field.getName(), includes)) {
                    if (CryptoHelper.isStringInArray(field.getName(), encryptions)) {
                        String newVal = (String) getNewVal(o, field);
                        map.put(field.getName(), newVal);
                    } else {
                        field.setAccessible(true);
                        try {
                            Object val = field.get(o);
                            map.put(field.getName(), val);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                //去除字段
                if (excludes.length > 0) {
                    if (!CryptoHelper.isStringInArray(field.getName(), excludes)) {
                        Object newVal = getNewVal(o, field);
                        map.put(field.getName(), newVal);
                    }
                }
            }
        }
        //return new Gson().fromJson(map.toString(),ResponseBean.class);//加密后只有字符串没有对象
        return map;
    }

    /**
     * 处理返回值是列表
     *
     * @param list
     * @return
     */
    private List handleList(List list) {
        List retList = new ArrayList();
        for (Object o : list) {
            Map map = (Map) handleSingleObject(o);
            retList.add(map);
        }
        return retList;
    }

    /**
     * 获取加密后的新值
     *
     * @param o
     * @param field
     * @return
     */
    private Object getNewVal(Object o, Field field) {
        Object newVal = null;
        try {
            field.setAccessible(true);
            Object val = field.get(o);
            if (val != null) {
                if (encode) {
                    //加密
                    newVal = CryptoHelper.encode(new Gson().toJson(val));
                } else {
                    newVal = new Gson().toJson(val);

                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newVal;
    }

}
