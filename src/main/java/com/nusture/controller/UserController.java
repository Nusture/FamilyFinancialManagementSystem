package com.nusture.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.nusture.service.FamilyService;
import com.nusture.pojo.User;
import com.nusture.service.CostService;
import com.nusture.service.IncomeService;
import com.nusture.service.UserService;
import com.nusture.util.SparkCalculate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private CostService costService;

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private UserService userService;

    @Autowired
    private FamilyService familyService;

    @Autowired
    private ApplicationContext applicationContext;

    //获取当日收支
    @PostMapping("today")
    public SaResult today(@RequestHeader String token) {
        //通过token获取用户id
        int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        //通过id查询用户
        User user = userService.getUserById(id);
        //统计今日收支金额
        BigDecimal todayCost = costService.getTodayCost(user.getFamilyCode());
        BigDecimal todayIncome = incomeService.getTodayIncome(user.getFamilyCode());
        HashMap<String, BigDecimal> map = new HashMap<>();
        map.put("todayCost", todayCost);
        map.put("todayIncome", todayIncome);
        //将数据传给前端
        return SaResult.data(map);
    }

    //获取所有时间总资产的变化
    @PostMapping("moneyChange")
    public SaResult moneyChange(@RequestHeader String token) {
        //通过token获取用户id
        int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        //通过id查询用户
        User user = userService.getUserById(id);
        //通过familyCode查询家庭基础资金
        List<Map<String,Object>> moneyChangeList = familyService.getBaseMoneyAll(user.getFamilyCode());
        return SaResult.data(moneyChangeList);
    }


    //通过家庭码获取支出类别统计
    @PostMapping("costByType")
    public SaResult costByType(@RequestHeader String token) {
        //通过token获取用户id
        int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        //通过id查询用户
        User user = userService.getUserById(id);
        //通过familyCode查询所有时间支出
        List<Map<String, Object>> costTypeList = costService.getCostByType(user.getFamilyCode());
        return SaResult.data(costTypeList);
    }

    //通过家庭码获取支出类别统计水波图
    @PostMapping("costByTypeWater")
    public SaResult costByTypeWater(@RequestHeader String token) {
        //通过token获取用户id
        int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        //通过id查询用户
        User user = userService.getUserById(id);
        //通过familyCode查询所有时间支出
        List<Map<String, Object>> costTypeList = costService.getCostByTypeWater(user.getFamilyCode());
        return SaResult.data(costTypeList);
    }

    //通过家庭码获取收入类别统计
    @PostMapping("incomeByType")
    public SaResult incomeByType(@RequestHeader String token) {
        //通过token获取用户id
        int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        //通过id查询用户
        User user = userService.getUserById(id);
        //通过familyCode查询所有时间支出
        List<Map<String, Object>> incomeTypeList = incomeService.getIncomeByType(user.getFamilyCode());
        return SaResult.data(incomeTypeList);
    }

    //通过家庭码获取收入类别统计水波图
    @PostMapping("incomeByTypeWater")
    public SaResult incomeByTypeWater(@RequestHeader String token) {
        //通过token获取用户id
        int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        //通过id查询用户
        User user = userService.getUserById(id);
        //通过familyCode查询所有时间支出
        List<Map<String, Object>> costTypeList = incomeService.getIncomeByTypeWater(user.getFamilyCode());
        return SaResult.data(costTypeList);
    }

    //通过家庭码获取该家庭所有用户
    @PostMapping("getAllUser")
    public SaResult getAllUser(@RequestHeader String token, @RequestBody Map<String, String> map) {
        //通过token获取用户id
        int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        Map<String, Object> userMap = userService.getAllUser(id, map.get("username"), map.get("phone"), Integer.parseInt(map.get("currentPage")), Integer.parseInt(map.get("pageSize")), map.get("birth"), map.get("gender"), map.get("address"), map.get("email"));

        return SaResult.data(userMap);
    }

    //获取家庭码和余额
    @PostMapping("getFCMoney")
    public SaResult getFamilyCode(@RequestHeader String token) {
        //通过token获取用户id
        int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        User user = userService.getUserById(id);
        String familyCode = user.getFamilyCode();
        Map<String, String> money = familyService.getMoneyByCode(familyCode);
        HashMap<String, Object> FCMap = new HashMap<>();
        FCMap.put("familyCode", familyCode);
        FCMap.put("money", money.get("money"));
        FCMap.put("fullMoney", money.get("fullMoney"));
        return SaResult.data(FCMap);
    }

    //更改余额
    @PostMapping("changeMoney")
    public SaResult changeMoney(@RequestHeader String token, @RequestBody Map<String, String> map) {
        try{
            //通过token获取用户id
            UserController userController = applicationContext.getBean(UserController.class);
            int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
            User user = userService.getUserById(id);
            String familyCode = user.getFamilyCode();
            familyService.changeMoney(familyCode, new BigDecimal(map.get("money")));
            userController.AsyncTask();
        }catch (Exception e){
            System.out.println(e);
        }
        return SaResult.ok();
    }

    //获取用户信息
    @PostMapping("getUserById")
    public SaResult getUserById(@RequestHeader String token) {
        //通过token获取用户id
        int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        User user = userService.getUserById(id);
        return SaResult.data(user);
    }

    //修改用户信息
    @PostMapping("changeUser")
    public SaResult changeUser(@RequestHeader String token, @RequestBody Map<String, String> map) {
        //通过token获取用户id
        int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        userService.changeUser(id, map.get("username"), map.get("phone"), map.get("birth"), map.get("gender"), map.get("address"), map.get("email"), map.get("signature"));
        return SaResult.ok();
    }

    //获取密码
    @PostMapping("getPassword")
    public SaResult getPassword(@RequestHeader String token) {
        //通过token获取用户id
        int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        Map<String, String> password = userService.getPassword(id);
        return SaResult.data(password);
    }

    //修改密码
    @PostMapping("changePassword")
    public SaResult changePassword(@RequestHeader String token, @RequestBody Map<String, String> map) {
        //通过token获取用户id
        int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        userService.changePassword(id, map.get("password"));
        StpUtil.logout();
        return SaResult.ok();
    }

    //所有时间各用户收入支出
    @PostMapping("getAllUserCI")
    public SaResult getAllUserCI(@RequestHeader String token) {
        //通过token获取用户id
        int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        //通过id查询用户
        User user = userService.getUserById(id);
        List<Map<String, Object>> list = userService.getAllUserCI(user.getFamilyCode());
        return SaResult.data(list);
    }

    //收支趋势
    @PostMapping("getAllCI")
    public SaResult getAllCI(@RequestHeader String token) {
        //通过token获取用户id
        int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        //通过id查询用户
        User user = userService.getUserById(id);
        List<Map<String, Object>> list = userService.getAllCI(user.getFamilyCode());
        return SaResult.data(list);
    }
    //词云
    @PostMapping("wordCloud")
    public SaResult wordCloud(@RequestHeader String token) {
        //通过token获取用户id
        int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        //通过id查询用户
        User user = userService.getUserById(id);
        List<Map<String, Object>> list = userService.wordCloud(user.getFamilyCode());
        return SaResult.data(list);
    }

    //收支记录日历
    @PostMapping("commitRecord")
    public SaResult commitRecord(@RequestHeader String token,@RequestBody Map<String,String> year) throws ParseException {
        //通过token获取用户id
        int id = Integer.parseInt((String) StpUtil.getLoginIdByToken(token));
        //通过id查询用户
        User user = userService.getUserById(id);
        List<Map<String, Object>> list = userService.commitRecord(user.getFamilyCode(),year);
        return SaResult.data(list);
    }

    @Async
    public void AsyncTask() {
        SparkCalculate calculate = new SparkCalculate();
        calculate.startCalculate();
        System.out.println("spark执行完成！");
    }
}
