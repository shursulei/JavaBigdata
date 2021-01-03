package com.shursulei.springbase;

import org.springframework.stereotype.Service;

//@Service
public class FunctionService {
    public String sayHello(String word){
        return "hello"+word;
    }
}