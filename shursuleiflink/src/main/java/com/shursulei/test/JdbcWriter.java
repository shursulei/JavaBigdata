package com.shursulei.test;

import com.shursulei.tool.JDBCKey;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import scala.Tuple1;
import scala.Tuple2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class JdbcWriter extends RichSinkFunction<Tuple2<String,String>> {
    private Connection connection;
    private PreparedStatement preparedStatement;

    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        // 加载JDBC驱动
        Class.forName(JDBCKey.Driver);
        // 获取数据库连接
        connection = DriverManager.getConnection(JDBCKey.URL,JDBCKey.Username,JDBCKey.Password);//写入mysql数据库
        preparedStatement = connection.prepareStatement(JDBCKey.source_sql);//insert sql在配置文件中
        super.open(parameters);
    }

    @Override
    public void close() throws Exception {
        super.close();
        if(preparedStatement != null){
            preparedStatement.close();
        }
        if(connection != null){
            connection.close();
        }
        super.close();
    }


    public void invoke(Tuple2<String,String> value, Context context) throws Exception {
        try {
            String name = value._1;//获取JdbcReader发送过来的结果
            preparedStatement.setString(1,name);
            preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}