package com.shursulei.streaming;

//题目1：
//实现个税的计算
//1~5000 税率 0
//5001~8000 3%
//8001~17000 10%
//17001~30000 20%
//30001~40000 25%
//40001~60000 30%
//60001~85000 35%
//85001~      45%
//要求
//1. 逻辑正确，代码优雅
//2. 可扩展性，考虑区间的变化，比如说起征点从5000变成10000等等，或者说85000以上的征税50%。
//这里举个例子，比如说税前10000元，5000部分是不扣税，后面5000，3000扣税3%，2000扣税10%。

import scala.Array;

import java.util.ArrayList;

public class test {

    public static void main(String[] args){
        final double before_tax=9000;
        double start[] = new double[]{1, 5001, 8001, 17001, 30001,40001,60001,85001};
        double end[] =new double[]{5000,8000, 17000, 30000, 40000, 60000,85000,0};
        double precent[] =new double[]{0,3, 10, 20, 25, 30,35,45};
        ArrayList<Taxs> a= new ArrayList<>();

        for (int i = 0; i < start.length; i++) {
            Taxs b = new Taxs();
            b.setStart(start[i]);
            b.setEnd(end[i]);
            b.setPrecent(precent[i]);
            a.add(b);
        }
        double sum=0;
        for(int i=0;i<a.size();i++){
            if((a.get(i).getStart()-before_tax<0)&&(a.get(i).getEnd()-before_tax>0)){
                    for(int j=0;j<=i;j++){
                        sum=sum+a.get(i).calucate(before_tax,a.get(i).getStart(),a.get(i).getPrecent(),a.get(i).getEnd());
                        System.out.println(sum);
                    }
            }
        }
        System.out.println(sum);
    }


}
