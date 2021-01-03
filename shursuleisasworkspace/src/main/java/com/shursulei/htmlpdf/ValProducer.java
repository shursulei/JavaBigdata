package com.shursulei.htmlpdf;

import java.util.Map;

import java.util.Map;

public  abstract class ValProducer {
    /*
     * 将月结单数据保存到数据模版中
     *
     * @param:portfolio中id
     *
     * @param:保存文件的根目录
     *
     * @param:htmlFile的位置
     * @param:更新日期
     */
    protected abstract Map<String, Object> valToPdfTemplate(int portfolio,String root, String html,String date);
    /*
     * 将月结单数据保存到数据模版中
     *
     * @param:portfolio中id
     *
     * @param:保存文件的根目录
     *
     * @param:htmlFile的位置
     *
     * @param:更新日期
     * @param:截至日期
     */
    protected abstract Map<String, Object> valToPdfTemplate(int portfolio, String root, String html,String from,String to);
}
