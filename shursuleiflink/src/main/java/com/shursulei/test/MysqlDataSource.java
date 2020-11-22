//package com.shursulei.test;
//
//import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
//import org.apache.flink.api.common.typeinfo.TypeInformation;
//import org.apache.flink.api.java.typeutils.RowTypeInfo;
//import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class MysqlDataSource {
//    private static final Logger log = LoggerFactory.getLogger(MysqlDataSource.class);
//
//    public static DataStream<Student> readFromDb(StreamExecutionEnvironment streamExecutionEnvironment) throws Exception {
//        //final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//        //1.定义field 类型
//        TypeInformation[] fieldTypes = new TypeInformation[]{BasicTypeInfo.INT_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.INT_TYPE_INFO};
//        //2.定义field name
//        String[] fieldNames = new String[]{"id", "name", "password", "age"};
//        //3.定义Row类型
//        RowTypeInfo rowTypeInfo = new RowTypeInfo(fieldTypes, fieldNames);
//
//        String jdbcUrl = parameterTool.get(PropertiesConstants.MYSQL_JDBC_URL);
//        log.info(jdbcUrl);
//
//        //4.定义JDBCInputFormat
//        JDBCInputFormat jdbcInputFormat = JDBCInputFormat
//                .buildJDBCInputFormat()
//                .setDrivername("com.mysql.jdbc.Driver")
//                .setDBUrl(jdbcUrl)
//                .setUsername(parameterTool.get(PropertiesConstants.MYSQL_USERNAME))
//                .setPassword(parameterTool.get(PropertiesConstants.MYSQL_PASSWORD))
//                .setQuery("select id, name, password, age from student")
//                .setRowTypeInfo(rowTypeInfo)
//                .finish();
//
//        //5.以JDBCInputFormat形式读取MySQL DB数据
//        DataStreamSource<Row> dataStreamSourceRow = streamExecutionEnvironment.createInput(jdbcInputFormat);
//
//        //阶段性验证可以正确读取
//        dataStreamSourceRow.print();
//
//        //6.将Row类型Stream转化为Entity类型
//        DataStream<Student> dataStream = dataStreamSourceRow.map(new RichMapFunction<Row, Student>() {
//            public Student map(Row value) throws Exception {
//                Student s = new Student();
//                s.setId((Integer) value.getField(0));
//                s.setName((String) value.getField(1));
//                s.setPassword((String) value.getField(2));
//                s.setAge((Integer) value.getField(3));
//                return s;
//            }
//        });
//
//        log.info("read datasource end");
//        return dataStream;
//}
