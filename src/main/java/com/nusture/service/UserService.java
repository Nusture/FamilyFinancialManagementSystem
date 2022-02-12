package com.nusture.service;

import com.nusture.pojo.Cost;
import com.nusture.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Nusture
 * @since 2021-12-28
 */
public interface UserService extends IService<User> {
    User getUserByName(String name);

    User getUserById(int id);

    int registerUser(User user);

    Map<String, Object> getAllUser(int id, String username, String phone, int currentPage, int pageSize,String birth,String gender,String address,String email);

    int changeUser(int id, String username, String phone, String birth,String gender,String address,String email,String signature);

    Map<String, String> getPassword(int id);

    int changePassword(int id, String password);

    List<Map<String,Object>> getAllUserCI(String familyCode);

    List<Map<String,Object>> getAllCI(String familyCode);

    List<Map<String,Object>> wordCloud(String familyCode);

    List<Map<String,Object>> commitRecord(String familyCode,Map<String,String> year) throws ParseException;
}
