package com.shursulei.interview;

public class A {
    public static void  main(String args[]) throws Exception {
        A a=new A();
        a.method();

    }

    private void method() throws Exception{
        try{
            System.out.println("A");
            int i=10/0;
        }catch (Exception e){
            System.out.println("B");
        }finally {
            System.out.println("D");
            throw new Exception("M");
        }
    }
}
