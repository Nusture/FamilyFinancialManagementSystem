package com.nusture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nusture.mapper.*;
import com.nusture.pojo.*;
import com.nusture.service.CostService;
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
public class CostServiceImpl extends ServiceImpl<CostMapper, Cost> implements CostService {

    @Autowired
    private CostMapper mapper;

    @Autowired
    private FamilyMapper familyMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CostByTypeMapper costByTypeMapper;

    @Autowired
    private CostByWaterMapper costByWaterMapper;

    @Override
    public BigDecimal getTodayCost(String familyCode) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String today = sdf.format(date);
        QueryWrapper<Cost> wrapper = new QueryWrapper<>();
        wrapper.select("sum(cost_money) as costMoney ");
        wrapper.eq("date_format(create_time,'%Y%m%d')", today);
        wrapper.eq("family_code", familyCode);
        if (mapper.selectOne(wrapper) == null) {
            return new BigDecimal(0);
        } else {
            BigDecimal costByToday = mapper.selectOne(wrapper).getCostMoney();
            return costByToday;
        }
    }


    @Override
    public List<Map<String, Object>> getCostByType(String familyCode) {
        List<CostByType> costByTypeList = costByTypeMapper.selectCostByType(familyCode);
        List<Map<String, Object>> list = new ArrayList<>();
        for (CostByType c : costByTypeList) {
            Map<String, Object> map = new HashMap<>();
            map.put("money", c.getCostMoney());
            map.put("type", c.getCostType());
            map.put("user", userMapper.selectById(c.getUserId()).getUsername());
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getCostByTypeWater(String familyCode) {
        List<Map<String, Object>> list = new ArrayList<>();
        HashMap<String, Object> map;
        List<String> costTypeList = Arrays.asList("餐饮 休闲娱乐 购物 穿搭美容 水果零食 人情社交 生活日用 生活日用 宠物 养娃 运动 生活服务 住房 爱车 学习 网络虚拟 烟酒 医疗保健 金融保险 酒店旅行 转账 公益 礼金 互助保障 其他".split(" "));
        for(String s:costTypeList){
            map = new HashMap<>();
            map.put("type",s);
            map.put("value",0);
            list.add(map);
        }
        List<CostByWater> costByWaterList = costByWaterMapper.selectCostByWater(familyCode);
        BigDecimal allCost = new BigDecimal(0);
        for (CostByWater c : costByWaterList) {
            allCost = allCost.add(c.getCostMoney());
        }
        for (CostByWater c : costByWaterList) {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("type",c.getCostType());
            map1.put("value",0);
            int i = list.indexOf(map1);
            list.get(i).put("value",c.getCostMoney().divide(allCost,4,BigDecimal.ROUND_HALF_UP));
        }
        return list;
    }

    @Override
    public Map<String, Object> getAllCost(int id, String costType, String date, String account, String note, int currentPage, int pageSize) {
        try {
            QueryWrapper<Cost> wrapper = new QueryWrapper<>();
            wrapper.like(!StringUtils.isEmpty(costType), "cost_type", costType);
            wrapper.like(!StringUtils.isEmpty(account), "account", account);
            wrapper.like(!StringUtils.isEmpty(note), "note", note);
            wrapper.like(!StringUtils.isEmpty(date), "create_time", date);
            wrapper.eq("user_id", id);
            Page<Cost> page = new Page<>(currentPage, pageSize);
            mapper.selectPage(page, wrapper);
            Map<String, Object> map = new HashMap<>();
            map.put("costList", page.getRecords());
            map.put("totalCount", page.getTotal());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int changeCost(int id, BigDecimal money, String costType, String account, String note) {
        Cost cost = new Cost();
        cost.setId(id);
        cost.setCostMoney(money);
        cost.setCostType(costType);
        cost.setAccount(account);
        cost.setNote(note);
        changeMoney(id, money);
        int i = mapper.updateById(cost);
        return i;
    }

    @Override
    public int deleteCost(int id) {
        changeMoney(id, null);
        return mapper.deleteById(id);
    }

    @Override
    public int addCost(int userId, BigDecimal money, String costType, String account, String note) {
        User user = userMapper.selectById(userId);
        String familyCode = user.getFamilyCode();
        Cost cost = new Cost();
        cost.setCostMoney(money);
        cost.setCostType(costType);
        cost.setAccount(account);
        cost.setNote(note);
        cost.setFamilyCode(familyCode);
        cost.setUserId(userId);
        cost.setVersion(1);
        int i = mapper.insert(cost);

        //获取最新一条余额数据
        QueryWrapper<Family> wrapper = new QueryWrapper<>();
        wrapper.eq("family_code", familyCode);
        wrapper.orderByDesc("id");
        List<Family> familyList = familyMapper.selectList(wrapper);
        BigDecimal baseMoney = familyList.get(0).getBaseMoney();
        //将余额减去支出
        baseMoney = baseMoney.subtract(money);
        Family family = new Family();
        family.setBaseMoney(baseMoney);
        family.setFamilyCode(familyCode);
        family.setVersion(1);
        familyMapper.insert(family);
        return i;
    }

    @Override
    public Cost getCostById(int id) {
        return mapper.selectById(id);
    }

    public void changeMoney(int id, BigDecimal modifyMoney) {
        //通过id获取cost对象
        Cost cost = mapper.selectById(id);
        //通过cost对象获取userId
        Integer userId = cost.getUserId();
        BigDecimal deleteMoney;
        if (modifyMoney == null) {
            //通过cost对象获取deleteMoney，也就是要删除的钱
            deleteMoney = cost.getCostMoney();
        } else {
            BigDecimal costMoney = cost.getCostMoney();
            if (modifyMoney.compareTo(costMoney) == 0) {
                deleteMoney = new BigDecimal(0);
            } else {
                deleteMoney = costMoney.subtract(modifyMoney);
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
        //将要删除的钱加入余额中
        money = money.add(deleteMoney);
        Family family = new Family();
        family.setBaseMoney(money);
        //通过id更改余额
        family.setBaseMoney(money);
        QueryWrapper<Family> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("id", familyId);
        familyMapper.update(family, wrapper2);
    }
}
