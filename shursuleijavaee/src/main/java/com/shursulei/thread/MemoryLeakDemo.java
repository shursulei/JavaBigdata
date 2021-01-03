package com.shursulei.thread;

import java.util.HashMap;
import java.util.Map;

public class MemoryLeakDemo {
    static class Key{
        Integer id;
        Key(Integer id){
            this.id=id;
        }
        public int hascode(){
            return id.hashCode();
        }
        public static void main(String[] args){
            Map m=new HashMap();
            while (true)
                for (int i = 0; i <100 ; i++)
                    if (!m.containsKey(new Key(i)))
                        m.put(new Key(i),"Number:"+i);
//                        System.out.println("Number:"+i);
        }
    }
}
