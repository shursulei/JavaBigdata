package com.shursulei.streaming;

public class ExceptionMatch {
    public static void main(String[] args) {
        top("[()]");
    }
    public static void top(String expression) {
        char[] biao = expression.toCharArray();// 将字符串转化成字符数组
        // 遍历表达式中所有字符
        System.out.println(biao.length);
        if (biao.length % 2 != 0) {
            System.out.println("括号不匹配 - -");
        }else {
            for (int i = 0; i < biao.length; i++) {
                System.out.println(biao[biao.length -1- i]);
                if (String.valueOf(biao[i]).equals(String.valueOf(biao[biao.length -1- i]))) {
                    System.out.println("括号匹配");
                }
            }


        }
    }}
