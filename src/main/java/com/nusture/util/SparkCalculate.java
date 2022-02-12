package com.nusture.util;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;


public class SparkCalculate {

    public void moneyChange() {
        SparkSql sparkSql = new SparkSql();
        String sql = "SELECT f.base_money,f.family_code,f.create_time FROM family f JOIN (SELECT MAX(a.create_time)as max_time FROM family as a  GROUP BY DATE(create_time)) as b  ON  b.max_time=f.create_time";
        Dataset<Row> familyTable = sparkSql.selectTable("family", sql);
        sparkSql.saveOverwrite(familyTable.orderBy("create_time"), "money_change");
    }

    public void getAllCI() {
        SparkSql sparkSql = new SparkSql();
        String sql = "SELECT sum(cost_money) as cost_money, 0 as income_money, family_code, date_trunc('MONTH',create_time) as create_time FROM cost  GROUP BY date_trunc('MONTH',create_time),family_code";
        Dataset<Row> costTable = sparkSql.selectTable("cost", sql);
        String sql2 = "SELECT 0 as cost_money, sum(income_money) as income_money,family_code,date_trunc('MONTH',create_time) as create_time FROM income  GROUP BY date_trunc('MONTH',create_time),family_code";
        Dataset<Row> incomeTable = sparkSql.selectTable("income", sql2);
        Dataset<Row> result = costTable.union(incomeTable);
        sparkSql.saveOverwrite(result.orderBy("create_time"), "all_ci");
    }

    public void getAllUserCI() {
        SparkSql sparkSql = new SparkSql();
        String sql = "SELECT sum(cost_money) as cost_money,0 as income_money,user_id,family_code FROM cost GROUP BY user_id,family_code";
        Dataset<Row> costTable = sparkSql.selectTable("cost", sql);
        String sql2 = "SELECT 0 as cost_money,sum(income_money) as income_money,user_id,family_code FROM income GROUP BY user_id,family_code";
        Dataset<Row> incomeTable = sparkSql.selectTable("income", sql2);
        Dataset<Row> result = costTable.union(incomeTable);
        sparkSql.saveOverwrite(result.orderBy("user_id"), "all_user_ci");
    }

    public void costByType() {
        SparkSql sparkSql = new SparkSql();
        String sql = "SELECT sum(cost_money) as cost_money,cost_type,user_id,family_code FROM cost GROUP BY cost_type,user_id,family_code";
        Dataset<Row> costTable = sparkSql.selectTable("cost", sql);
        sparkSql.saveOverwrite(costTable.orderBy("user_id"), "cost_by_type");
    }

    public void incomeByType() {
        SparkSql sparkSql = new SparkSql();
        String sql = "SELECT sum(income_money) as income_money,income_type,user_id,family_code FROM income GROUP BY income_type,user_id,family_code";
        Dataset<Row> incomeTable = sparkSql.selectTable("income", sql);
        sparkSql.saveOverwrite(incomeTable.orderBy("user_id"), "income_by_type");
    }

    public void incomeByWater() {
        SparkSql sparkSql = new SparkSql();
        String sql = "SELECT sum(income_money) as income_money,income_type,family_code FROM income GROUP BY income_type,family_code";
        Dataset<Row> incomeTable = sparkSql.selectTable("income", sql);
        sparkSql.saveOverwrite(incomeTable.orderBy("family_code"), "income_by_water");
    }

    public void costByWater() {
        SparkSql sparkSql = new SparkSql();
        String sql = "SELECT sum(cost_money) as cost_money,cost_type,family_code FROM cost GROUP BY cost_type,family_code";
        Dataset<Row> costTable = sparkSql.selectTable("cost", sql);
        sparkSql.saveOverwrite(costTable.orderBy("family_code"), "cost_by_water");
    }

    public void wordCloud() {
        SparkSql sparkSql = new SparkSql();
        String sql = "SELECT COUNT(1) as count,cost_type as type,family_code FROM cost GROUP BY cost_type,family_code";
        Dataset<Row> costTable = sparkSql.selectTable("cost", sql);
        String sql2 = "SELECT COUNT(1) as count,income_type as type ,family_code FROM income GROUP BY income_type,family_code";
        Dataset<Row> incomeTable = sparkSql.selectTable("income", sql2);
        Dataset<Row> result = costTable.union(incomeTable);
        sparkSql.saveOverwrite(result.orderBy("family_code"), "word_cloud");
    }

    public void commitRecord() {
        SparkSql sparkSql = new SparkSql();
        String sql1 = "select COUNT(1) as count, family_code, date_trunc('DAY',create_time) as create_time from cost GROUP BY date_trunc('DAY',create_time),family_code";
        String sql2 = "select COUNT(1) as count, family_code, date_trunc('DAY',create_time) as create_time from income GROUP BY date_trunc('DAY',create_time),family_code";
        Dataset<Row> union = sparkSql.selectTable("cost", sql1).unionAll(sparkSql.selectTable("income", sql2));
        sparkSql.saveOverwrite(union.orderBy("create_time"), "commit_record");
    }
    public void startCalculate(){
        moneyChange();
        getAllCI();
        getAllUserCI();
        costByType();
        costByWater();
        incomeByType();
        incomeByWater();
        wordCloud();
        commitRecord();
    }
}
