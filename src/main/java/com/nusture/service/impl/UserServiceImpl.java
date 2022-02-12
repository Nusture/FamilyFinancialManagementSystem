package com.nusture.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nusture.mapper.*;
import com.nusture.pojo.*;
import com.nusture.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nusture.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private AllCiMapper allCiMapper;

    @Autowired
    private AllUserCIMapper allUserCIMapper;

    @Autowired
    private WordCloudMapper wordCloudMapper;

    @Autowired
    private CommitRecordMapper commitRecordMapper;
    @Override
    public User getUserByName(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User user = mapper.selectOne(wrapper);
        return user;
    }

    @Override
    public User getUserById(int id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        User user = mapper.selectOne(wrapper);
        return user;
    }

    @Override
    public int registerUser(User user) {
        int i = mapper.insert(user);
        return i;
    }

    @Override
    public Map<String, Object> getAllUser(int id, String username, String phone, int currentPage, int pageSize, String birth, String gender, String address, String email) {
        HashMap<String, Object> map1 = new HashMap<>();
        try {
            User user = getUserById(id);
            String familyCode = user.getFamilyCode();
            map1.put("family_code", familyCode);
            map1.put("gender", gender);
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.like(!StringUtils.isEmpty(username), "username", username);
            wrapper.like(!StringUtils.isEmpty(phone), "phone", phone);
            wrapper.like(!StringUtils.isEmpty(address), "address", address);
            wrapper.like(!StringUtils.isEmpty(email), "email", email);
            wrapper.like(!StringUtils.isEmpty(birth), "birth", birth);
            wrapper.allEq(map1, false);
            Page<User> page = new Page<User>(currentPage, pageSize);
            mapper.selectPage(page, wrapper);
            Map<String, Object> map = new HashMap<>();
            map.put("userList", page.getRecords());
            map.put("totalCount", page.getTotal());
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int changeUser(int id, String username, String phone, String birth, String gender, String address, String email, String signature) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(birth);
            User user = new User();
            user.setId(id);
            user.setUsername(username);
            user.setPhone(phone);
            user.setBirth(date);
            user.setGender(Integer.parseInt(gender));
            user.setAddress(address);
            user.setEmail(email);
            user.setSignature(signature);
            int i = mapper.updateById(user);
            return i;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public Map<String, String> getPassword(int id) {
        User user = mapper.selectById(id);
        Map<String, String> map = new HashMap<>();
        map.put("password", user.getPassword());
        return map;
    }

    @Override
    public int changePassword(int id, String password) {
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        int i = mapper.updateById(user);
        return i;
    }

    @Override
    public List<Map<String, Object>> getAllUserCI(String familyCode) {
        List<AllUserCI> allUserCIList = allUserCIMapper.selectAllUserCI(familyCode);
        List<Map<String, Object>> list = new ArrayList<>();
        for(AllUserCI a:allUserCIList){
            Map<String,Object> map=new HashMap<>();
            map.put("money",a.getCostMoney());
            map.put("type","支出");
            map.put("user",getUserById(Integer.parseInt(a.getUserId())).getUsername());
            Map<String,Object> map1=new HashMap<>();
            map1.put("money",a.getIncomeMoney());
            map1.put("type","收入");
            map1.put("user",getUserById(Integer.parseInt(a.getUserId())).getUsername());
            list.add(map);
            list.add(map1);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getAllCI(String familyCode) {
        List<AllCi> allCiList = allCiMapper.selectAllCi(familyCode);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<Map<String, Object>> list = new ArrayList<>();
        for(AllCi a:allCiList){
            Map<String,Object> map=new HashMap<>();
            map.put("name","支出");
            map.put("year",sdf.format(a.getCreateTime()));
            map.put("money",a.getCostMoney());

            Map<String,Object> map1=new HashMap<>();
            map1.put("name","收入");
            map1.put("year",sdf.format(a.getCreateTime()));
            map1.put("money",a.getIncomeMoney());
            list.add(map);
            list.add(map1);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> wordCloud(String familyCode) {
        List<WordCloud> wordCloudList = wordCloudMapper.selectWordCloud(familyCode);
        List<Map<String, Object>> list = new ArrayList<>();
        for(WordCloud w: wordCloudList){
            Map<String,Object> map=new HashMap<>();
            map.put("name",w.getCount());
            map.put("value",w.getType());
            list.add(map);
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> commitRecord(String familyCode,Map<String,String> year) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DateUtils du = new DateUtils();
        List<Map<String, Object>> list = du.getAllYearDate(year.get("year"));
        List<CommitRecord> commitRecordList = commitRecordMapper.selectCommitRecord(familyCode);
        int index= 0;
        for(int x=0;x<commitRecordList.size();x++){
            String sYear = String.valueOf(du.getYear(commitRecordList.get(x).getCreateTime()));
            String cYear = year.get("year");
            if(cYear.equals(sYear)){
                for(int i=0;i<list.size();i++){
                    String d1 = list.get(i).get("date")+"";
                    String d2 = sdf.format(commitRecordList.get(x).getCreateTime());
                    if(d1.equals(d2)){
                        index=i;
                        break;
                    }
                }
                list.get(index).put("commits",commitRecordList.get(x).getCount());
            }
        }
        return list;
    }

}
