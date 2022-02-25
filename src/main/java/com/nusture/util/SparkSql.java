package com.nusture.util;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

public class SparkSql {
    static String url = "jdbc:mysql://localhost:3306/ffms?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false";
    static String password = "989023Xbf";
    static String username = "root";
    public Dataset<Row> selectTable(String table, String sql){
        SparkConf conf = new SparkConf()
                .set("spark.testing.memory","2147480000")
                .set("numPartitions","20")
                .set("partitionColumn","year")
                .set("lowerBound", "0")
                .set("upperBound","2000");
        SparkSession sparkSession = SparkSession
                .builder()
                .appName("SparkSql")
                .master("local")
                .config(conf)
                .getOrCreate();
        //设置sparkSession数据连接
        Dataset dataSet = sparkSession.read()
                .format("jdbc")
                .option("url",url)
                .option("dbtable",table)
                .option("driver","com.mysql.cj.jdbc.Driver")
                .option("user",username)
                .option("password",password)
                .load();
        //注册临时表后才能进行select等操作，必需，否则not found in database 'default'
        dataSet.createOrReplaceTempView(table);
        Dataset<Row> sqlDF = sparkSession.sql(sql);
        return sqlDF;
    }
    public void save(Dataset<Row> dataset,String tableName){
        Properties connectionProperties = new Properties();
        connectionProperties.put("user",username);
        connectionProperties.put("password",password);
        connectionProperties.put("driver","com.mysql.cj.jdbc.Driver");

        dataset.write().mode("append").jdbc(url,tableName,connectionProperties);

    }
    public void saveOverwrite(Dataset<Row> dataset,String tableName){

        Properties connectionProperties = new Properties();
        connectionProperties.put("user",username);
        connectionProperties.put("password",password);
        connectionProperties.put("driver","com.mysql.cj.jdbc.Driver");

        dataset.write().mode("overwrite").jdbc(url,tableName,connectionProperties);

    }

}
