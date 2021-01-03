package com.shursulei.htmlpdf;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

public class ValProduceTask {
    // 线程安全队列用以存储数据
    private LinkedBlockingQueue<HashMap<String, Object>> queue;
    private int portfolio;// 投资组合的id
    private String from;// 当前日期
    private String html;// html模版
    private String root;// 存储的根目录
    private ValProducer valProducer;// 获取相应的数据
    private String to;// 截至日期日期
    private Msg msg;//执行状态
    /*
     * 传递所需数据
     *
     * @param:缓存队列
     *
     * @param：投资组合id
     *
     * @param:查询日期
     *
     * @param:html模版
     *
     * @param：提供的数据获取方法有调用者实现传递
     *
     * @param:跟目录
     *
     * @保存文件名
     */

    public ValProduceTask(Msg msg,LinkedBlockingQueue<HashMap<String, Object>> queue, int portfolio,String html,
                          String root, ValProducer valProducer,String from,String to) {
        this.queue = queue;
        this.from = from;
        this.html = html;
        this.to=to;
        this.portfolio = portfolio;
        this.root = root;
        this.valProducer = valProducer;
        this.msg=msg;
    }

    public void run() {
        synchronized (queue) {
            Map<String, Object> data;
            if(to==null){
                data = valProducer.valToPdfTemplate(portfolio,root, html, from);
            }else{
                data = valProducer.valToPdfTemplate(portfolio,root, html, from,to);
            }
            if (data != null) {
                queue.add((HashMap<String, Object>) data);
            }else{
                Msg.State state=new Msg.State();
                state.setPortfolio(this.portfolio);
                state.setState(Msg.State.NO_DATA);
                msg.add(state);
            }
            queue.notifyAll();
        }
    }
}
