package com.shursulei.netty.sevrver2;

import java.util.Map;
public class TestCRequest {

    /**用于测试CRequest类
     * @param args
     */
    public static void main(String[] args) {
    // 请求url
        String str = "index.jsp?Action=del&id=123&sort=";    
        //url页面路径
        System.out.println(CRequest.UrlPage(str));
        //url参数键值对
        String strRequestKeyAndValues="";       
        Map<String, String> mapRequest = CRequest.URLRequest(str);
        for(String strRequestKey: mapRequest.keySet()) {
            String strRequestValue=mapRequest.get(strRequestKey);
            strRequestKeyAndValues+="key:"+strRequestKey+",Value:"+strRequestValue+";";           
        }
        System.out.println(strRequestKeyAndValues);
        //获取无效键时，输出null
        System.out.println(mapRequest.get("page"));
    }

}
