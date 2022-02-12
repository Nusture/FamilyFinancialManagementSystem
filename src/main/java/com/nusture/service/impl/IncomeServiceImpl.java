package com.nusture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nusture.mapper.*;
import com.nusture.pojo.*;
import com.nusture.service.IncomeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Nusture
 * @since 2021-12-28
 */
@Service
public class IncomeServiceImpl extends ServiceImpl<IncomeMapper, Income> implements IncomeService {
    @Autowired
    private IncomeMapper mapper;

    @Autowired
    private FamilyMapper familyMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private IncomeByTypeMapper incomeByTypeMapper;

    @Autowired
    private IncomeByWaterMapper incomeByWaterMapper;
    @Override
    public BigDecimal getTodayIncome(String familyCode) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(date);
        QueryWrapper<Income> wrapper = new QueryWrapper<>();
        wrapper.select("sum(income_money) as incomeMoney ");
        wrapper.eq("date_format(create_time,'%Y%m%d')", today);
        wrapper.eq("family_code", familyCode);
        if (mapper.selectOne(wrapper) == null) {
            return new BigDecimal(0);
        } else {
            BigDecimal incomeByToday = mapper.selectOne(wrapper).getIncomeMoney();
            return incomeByToday;
        }
    }

    @Override
    public List<Map<String, Object>> getIncomeByType(String familyCode) {
        List<IncomeByType> incomeByTypeList = incomeByTypeMapper.selectIncomeByType(familyCode);
        List<Map<String, Object>> list = new ArrayList<>();
        for(IncomeByType i:incomeByTypeList){
            Map<String,Object> map=new HashMap<>();
            map.put("money",i.getIncomeMoney());
            map.put("type",i.getIncomeType());
            map.put("user",userMapper.selectById(i.getUserId()).getUsername());
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getIncomeByTypeWater(String familyCode) {
        List<Map<String,Object>> list = new ArrayList<>();
        HashMap<String, Object> map;
        List<String> incomeTypeList = Arrays.asList("工资 兼职 投资理财 人情社交 奖金补贴 报销 生意 卖二手 生活费 中奖 转账 保险理赔 其他".split(" "));
        for(String s:incomeTypeList){
            map = new HashMap<>();
            map.put("type",s);
            map.put("value",0);
            list.add(map);
        }
        List<IncomeByWater> incomeByWaterList = incomeByWaterMapper.selectIncomeByWater(familyCode);
        BigDecimal allIncome = new BigDecimal(0);
        for (IncomeByWater i : incomeByWaterList) {
            allIncome = allIncome.add(i.getIncomeMoney());
        }
        for (IncomeByWater i : incomeByWaterList) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("type",i.getIncomeType());
            map1.put("value",0);
            int index = list.indexOf(map1);
            list.get(index).put("value",i.getIncomeMoney().divide(allIncome,4,BigDecimal.ROUND_HALF_UP));
        }
        return list;
    }

    @Override
    public Map<String, Object> getAllIncome(int id, String incomeType, String date, String account, String note, int currentPage, int pageSize) {
        try {
            QueryWrapper<Income> wrapper = new QueryWrapper<>();
            wrapper.like(!StringUtils.isEmpty(incomeType), "income_type", incomeType);
            wrapper.like(!StringUtils.isEmpty(account), "account", account);
            wrapper.like(!StringUtils.isEmpty(note), "note", note);
            wrapper.like(!StringUtils.isEmpty(date), "create_time", date);
            wrapper.eq("user_id", id);
            Page<Income> page = new Page<>(currentPage, pageSize);
            mapper.selectPage(page, wrapper);
            Map<String, Object> map = new HashMap<>();
            map.put("incomeList", page.getRecords());
            map.put("totalCount", page.getTotal());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int changeIncome(int id, BigDecimal money, String incomeType, String account, String note) {
        Income income = new Income();
        income.setId(id);
        income.setIncomeMoney(money);
        income.setIncomeType(incomeType);
        income.setAccount(account);
        income.setNote(note);
        changeMoney(id, money);
        int i = mapper.updateById(income);
        return i;
    }

    @Override
    public int deleteIncome(int id) {
        changeMoney(id, null);
        return mapper.deleteById(id);
    }

    @Override
    public int addIncome(int userId, BigDecimal money, String incomeType, String account, String note) {
        User user = userMapper.selectById(userId);
        String familyCode = user.getFamilyCode();
        Income income = new Income();
        income.setIncomeMoney(money);
        income.setIncomeType(incomeType);
        income.setAccount(account);
        income.setNote(note);
        income.setFamilyCode(familyCode);
        income.setUserId(userId);
        income.setVersion(1);
        int i = mapper.insert(income);

        //获取最新一条余额数据
        QueryWrapper<Family> wrapper = new QueryWrapper<>();
        wrapper.eq("family_code", familyCode);
        wrapper.orderByDesc("id");
        List<Family> familyList = familyMapper.selectList(wrapper);
        BigDecimal baseMoney = familyList.get(0).getBaseMoney();
        //将余额增加收入
        baseMoney = baseMoney.add(money);
        Family family = new Family();
        family.setBaseMoney(baseMoney);
        family.setFamilyCode(familyCode);
        family.setVersion(1);
        familyMapper.insert(family);
        return i;
    }

    @Override
    public Income getIncomeById(int id) {
        return mapper.selectById(id);
    }

    public void changeMoney(int id, BigDecimal modifyMoney) {
        //通过id获取income对象
        Income income = mapper.selectById(id);
        //通过income对象获取userId
        Integer userId = income.getUserId();
        BigDecimal addMoney;
        if (modifyMoney == null) {
            addMoney = income.getIncomeMoney();
        } else {
            BigDecimal incomeMoney = income.getIncomeMoney();
            if (modifyMoney.compareTo(incomeMoney) == 0) {
                addMoney = new BigDecimal(0);
            } else {
                addMoney = incomeMoney.subtract(modifyMoney);
            }
        }
        //通过userId获取familyCode
        String familyCode = userMapper.selectById(userId).getFamilyCode();
        //获取最新一条余额数据
        QueryWrapper<Family> wrapper = new QueryWrapper<>();
        wrapper.eq("family_code", familyCode);
        wrapper.orderByDesc("id");
        List<Family> familyList = familyMapper.selectList(wrapper);
        //获取最后一条余额数据的id
        Integer familyId = familyList.get(0).getId();
        //获取最后一条余额数据的余额
        BigDecimal money = familyList.get(0).getBaseMoney();
        money = money.subtract(addMoney);
        Family family = new Family();
        family.setBaseMoney(money);
        //通过id更改余额
        family.setBaseMoney(money);
        QueryWrapper<Family> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("id", familyId);
        familyMapper.update(family, wrapper2);
    }
}
