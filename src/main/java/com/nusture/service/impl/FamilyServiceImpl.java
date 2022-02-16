package com.nusture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nusture.mapper.MoneyChangeMapper;
import com.nusture.pojo.Family;
import com.nusture.mapper.FamilyMapper;
import com.nusture.pojo.MoneyChange;
import com.nusture.service.FamilyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class FamilyServiceImpl extends ServiceImpl<FamilyMapper, Family> implements FamilyService {
    @Autowired
    private FamilyMapper mapper;

    @Autowired
    private MoneyChangeMapper moneyChangeMapper;
    @Override
    public List<Map<String,Object>> getBaseMoneyAll(String familyCode) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<MoneyChange> moneyChangeList = moneyChangeMapper.selectMoneyChange(familyCode);
        ArrayList<Map<String,Object>> list = new ArrayList<>();
        for(MoneyChange m:moneyChangeList){
            Map<String, Object> map = new HashMap<>();
            map.put("date",sdf.format(m.getCreateTime()));
            map.put("money",m.getBaseMoney());
            list.add(map);
        }
        return list;
    }

    @Override
    public Map<String,String> getMoneyByCode(String familyCode) {
        HashMap<String, String> map = new HashMap<>();
        QueryWrapper<Family> wrapper = new QueryWrapper<>();
        wrapper.eq("family_code",familyCode);
        wrapper.orderByDesc("id");
        List<Family> familyList = mapper.selectList(wrapper);
        Family family = familyList.get(0);
        BigDecimal moneyB = family.getBaseMoney();
        BigDecimal tenThousand = new BigDecimal("10000");
        String money=moneyB.divide(new BigDecimal(1),2,BigDecimal.ROUND_HALF_UP)+"元";
        if(moneyB.compareTo(new BigDecimal("100000"))==1){
            BigDecimal[] divideAndRemainder = moneyB.divideAndRemainder(tenThousand);
            BigDecimal remainder = divideAndRemainder[1];
            if(remainder.compareTo(new BigDecimal(0))==0){
                money=moneyB.divide(tenThousand,0,BigDecimal.ROUND_HALF_UP)+"万元";

            }else{
                money=moneyB.divide(tenThousand,2,BigDecimal.ROUND_HALF_UP)+"万元";
            }
        }
        map.put("money",money);
        map.put("fullMoney",moneyB+"");
        return map;
    }

    @Override
    public int changeMoney(String familyCode,BigDecimal money) {
        //获取最新一条余额数据
        QueryWrapper<Family> wrapper = new QueryWrapper<>();
        wrapper.eq("family_code",familyCode);
        wrapper.orderByDesc("id");
        List<Family> familyList = mapper.selectList(wrapper);
        Family family = familyList.get(0);
        //从最新一条余额数据获取id
        Integer id = family.getId();
        //通过id更改余额
        family.setBaseMoney(money);
        QueryWrapper<Family> wrapper2 = new QueryWrapper<>();
        wrapper2.eq("id",id);
        int i = mapper.update(family, wrapper2);
        return i;
    }

    @Override
    public int addBaseMoney(String familyCode, BigDecimal money) {
        Family family = new Family();
        family.setBaseMoney(money);
        family.setFamilyCode(familyCode);
        int i = mapper.insert(family);
        return i;
    }
}
