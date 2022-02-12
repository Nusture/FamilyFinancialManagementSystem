package com.nusture.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.nusture.pojo.Cost;
import com.nusture.service.CostService;
import com.nusture.util.SparkCalculate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/cost")
public class CostController {
    @Autowired
    private CostService costService;

    @Autowired
    private ApplicationContext applicationContext;

    //查询所有支出条目
    @PostMapping("getCost")
    public SaResult getCost(@RequestHeader String token, @RequestBody Map<String, String> map) {
        //通过token获取用户id
        int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        Map<String, Object> cost = costService.getAllCost(id, map.get("costType"), map.get("date"), map.get("account"), map.get("note"), Integer.parseInt(map.get("currentPage")), Integer.parseInt(map.get("pageSize")));
        return SaResult.data(cost);
    }

    //修改支出条目
    @PostMapping("changeCost")
    public SaResult changeCost(@RequestBody Map<String, String> map) {
        try {
            costService.changeCost(Integer.parseInt(map.get("id")), new BigDecimal(map.get("money")), map.get("costType"), map.get("account"), map.get("note"));
            CostController costController = applicationContext.getBean(CostController.class);
            costController.AsyncTask();
        } catch (Exception e) {
            System.out.println(e);
        }
        return SaResult.ok();
    }

    //删除支出条目
    @PostMapping("deleteCost")
    public SaResult deleteCost(@RequestBody Map<String, Integer> map) {
        try {
            costService.deleteCost(map.get("id"));
            CostController costController = applicationContext.getBean(CostController.class);
            costController.AsyncTask();
        } catch (Exception e) {
            System.out.println(e);
        }

        return SaResult.ok();
    }

    //添加支出条目
    @PostMapping("addCost")
    public SaResult addCost(@RequestHeader String token, @RequestBody Map<String, String> map) {
        //通过token获取用户id
        int userId = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        try {
            costService.addCost(userId, new BigDecimal(map.get("money")), map.get("costType"), map.get("account"), map.get("note"));
            CostController costController = applicationContext.getBean(CostController.class);
            costController.AsyncTask();
        } catch (Exception e) {
            System.out.println(e);
        }
        return SaResult.ok();
    }

    //查询单条支出条目
    @PostMapping("queryOne")
    public SaResult queryOne(@RequestBody Map<String, Integer> map) {
        Cost cost = costService.getCostById(map.get("id"));
        return SaResult.data(cost);
    }

    @Async
    public void AsyncTask() {
        SparkCalculate calculate = new SparkCalculate();
        calculate.startCalculate();
        System.out.println("spark执行完成！");
    }

}
