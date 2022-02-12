package com.nusture;

import cn.dev33.satoken.util.SaResult;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class JavaSparkSqlDemo {
    public static void main(String[] args){
        SparkSession sparkSession = SparkSession
                .builder()
                .appName("JavaSparkSqlDemo")  //Sets a name for the application
                .master("local")    //Sets the Spark master URL to connect to
                .getOrCreate();     //获取或者新建一个 sparkSession
        //设置sparkSession数据连接
        Dataset costDataset = sparkSession.read()
                .format("jdbc")
                .option("url","jdbc:mysql://47.107.103.82:3306/ffms?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false")
                .option("dbtable","cost")
                .option("driver","com.mysql.cj.jdbc.Driver")
                .option("user","root")
                .option("password","989023Xbf")
                .load();
        Dataset incomeDataset = sparkSession.read()
                .format("jdbc")
                .option("url","jdbc:mysql://47.107.103.82:3306/ffms?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false")
                .option("dbtable","income")
                .option("driver","com.mysql.cj.jdbc.Driver")
                .option("user","root")
                .option("password","989023Xbf")
                .load();
        //注册临时表后才能进行select等操作，必需，否则not found in database 'default'
        costDataset.createOrReplaceTempView("cost");
        incomeDataset.createOrReplaceTempView("income");
//        costDataset.registerTempTable("user");
//        incomeDataset.registerTempTable("role");
        //SQL查询操作
        //注意：1.所有用到的表需要在option和registerTempTable注册
        Dataset<Row> sqlDF = sparkSession.sql("select * from cost");
        sqlDF.show();
        System.out.println(SaResult.data(sqlDF));

    }
}