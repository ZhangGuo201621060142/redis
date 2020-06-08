package com.example.zg.redis;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericObj<T> {

    public void printType(){
        Type type = this.getClass().getGenericSuperclass();
        System.out.println(type);
        System.out.println(((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
    }

    public static class StringGenericObj extends GenericObj<String> {

    }

    public static void main(String[] args) {
        GenericObj<String> interObj = new StringGenericObj();
        interObj.printType();
    }
}
