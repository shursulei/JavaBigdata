/**
 * TODO * @version 1.0 * @author shursulei * @date 2021/1/25 18:50
 */

// # // ## 请在下方描述你的面试题内容( 支持Markdown )
// # // 写一个类似微信红包算法，假设把 money 元随机分给 n 个人，不要有余额剩余，尽量考虑公平性和合理性。
// # // 推荐二倍均值法
// # // 设抢到的金额为X，剩余的红包金额为T，剩余的红包数量为N
// # // X=Math.random()*(T/N*2);
// # // 为了保证精度，可以考虑使用BigDecimal，保留两位小数
// # // 必须定义 `ShowMeBug` 入口类和 `public static void main(String[] args)` 入口方法
// # import java.math.*;
// # public class ShowMeBug {
// #   public static void main(String[] args) {
// #     System.out.println("微信红包算法：");
// #     dealRedPackage(13.14,7);
// #   }
// #   //请补充下列函数内容
// #   public static void dealRedPackage(double money,int n){

// #   }
// # }
// for i in range(3):
//   print('hello world')

// 必须定义 `ShowMeBug` 入口类和 `public static void main(String[] args)` 入口方法
//public class ShowMeBug {
//    public static void main(String[] args) {
//        System.out.println("Hello World!");
//    }
//}

public class test {
    public static void main(String[] args) {
        dealRedPackage(13.14,7);
    }
    public static void dealRedPackage(double money,int n){
        if(n==1){
            System.out.println(money+":"+n);
            System.out.println("分配结束");
        }else
        {
            //金额控制？
            System.out.println(money);
            dealRedPackage(money-Math.random()*(money/n*2),n-1);
        }
    }
}
