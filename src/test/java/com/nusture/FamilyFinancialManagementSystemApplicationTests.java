package com.nusture;

import com.nusture.mapper.*;
import com.nusture.pojo.AllCi;
import com.nusture.pojo.MoneyChange;
import com.nusture.util.DateUtils;
import com.nusture.util.SparkCalculate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@SpringBootTest
class FamilyFinancialManagementSystemApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CostMapper costMapper;

    @Autowired
    private FamilyMapper familyMapper;
    @Autowired
    private AllCiMapper allCiMapper;
    @Autowired
    private MoneyChangeMapper moneyChangeMapper;

    @Test
    void contextLoads() {
        System.out.println(f(2, 1));
    }

    int f(int x,int y){
        if((x>0)&&(y>0)){
            return (f(x-1,y)+f(x,y-1));
        }
        else{
            return (x+y);
        }
    }

}
