package com.example.zg.redis;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhangguo
 * @date 2020/6/3 10:18
 * @description
 */
@Data
@AllArgsConstructor
public class Person implements Serializable {
    private static final long serialVersionUID = 1136778033357469048L;
    private String name;
    private int age;
}
