package com.nusture.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.nusture.pojo.Income;
import com.nusture.service.IncomeService;
import com.nusture.util.SparkCalculate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/income")
public class incomeController {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private ApplicationContext applicationContext;

    //查询所有收入条目
    @PostMapping("getIncome")
    public SaResult getIncome(@RequestHeader String token, @RequestBody Map<String, String> map) {
        //通过token获取用户id
        int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        Map<String, Object> income = incomeService.getAllIncome(id, map.get("incomeType"), map.get("date"), map.get("account"), map.get("note"), Integer.parseInt(map.get("currentPage")), Integer.parseInt(map.get("pageSize")));
        return SaResult.data(income);
    }

    //修改收入条目
    @PostMapping("changeIncome")
    public SaResult changeIncome(@RequestBody Map<String, String> map) {
        try {
            incomeService.changeIncome(Integer.parseInt(map.get("id")), new BigDecimal(map.get("money")), map.get("incomeType"), map.get("account"), map.get("note"));
            incomeController incomeController = applicationContext.getBean(incomeController.class);
            incomeController.AsyncTask();
        } catch (Exception e) {
            System.out.println(e);
        }
        return SaResult.ok();
    }

    //删除收入条目
    @PostMapping("deleteIncome")
    public SaResult deleteIncome(@RequestBody Map<String, Integer> map) {
        try {
            incomeService.deleteIncome(map.get("id"));
            incomeController incomeController = applicationContext.getBean(incomeController.class);
            incomeController.AsyncTask();
        } catch (Exception e) {
            System.out.println(e);
        }

        return SaResult.ok();
    }

    //添加收入条目
    @PostMapping("addIncome")
    public SaResult addIncome(@RequestHeader String token, @RequestBody Map<String, String> map) {
        //通过token获取用户id
        int userId = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        try {
            incomeService.addIncome(userId, new BigDecimal(map.get("money")), map.get("incomeType"), map.get("account"), map.get("note"));
            incomeController incomeController = applicationContext.getBean(incomeController.class);
            incomeController.AsyncTask();
        } catch (Exception e) {
            System.out.println(e);
        }
        return SaResult.ok();
    }

    //查询单条收入条目
    @PostMapping("queryOne")
    public SaResult queryOne(@RequestBody Map<String, Integer> map) {
        Income income = incomeService.getIncomeById(map.get("id"));
        return SaResult.data(income);
    }

    @Async
    public void AsyncTask() {
        SparkCalculate calculate = new SparkCalculate();
        calculate.startCalculate();
        System.out.println("spark执行完成！");
    }
}
