package com.itheima.travel.user.dao.impl;

import com.itheima.travel.user.dao.IUserDao;
import com.itheima.travel.user.domain.User;
import com.itheima.travel.utils.DRUIDUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.Arrays;

public class UserDaoImpl implements IUserDao{
    private static DataSource dataSource = DRUIDUtils.getDataSource();
    private static JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    /**
     * 根据用户名查找用户所有信息
     * @param userName
     * @return
     */
    public User findUserInfoByUserName(String userName) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        String sql = "select * from tab_user where username=?";
        try{
            User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class),userName);
            return user;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 注册
     * @param user
     * @return
     */
    public int register(User user) {
        String sql = "insert into tab_user values(?,?,?,?,?,?,?,?,?,?)";
        //3.设置实际参数
        Object[] params = {null,user.getUsername(),user.getPassword(),user.getName(),user.getBirthday(),user.getSex(),
                user.getTelephone(),user.getEmail(),user.getStatus(),user.getCode()};
        //4.执行更新操作
        int rows = jdbcTemplate.update(sql, params);
        System.out.println(rows);
        // 返回
        return rows;
    }

    /**
     * 根据激活码查找用户
     * @param code
     * @return
     */
    public User findUserByCode(String code) {
        String sql = "select * from tab_user where code=?";
        try
        {
            User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), code);
            return user;
        } catch (Exception e) {
            return null;
        }

    }

    public void active(User user) {
//        String sql = "update tab_user set STATUS = ?,code=? where uid = ?";
//
//        Object[] params = {user.getStatus(),user.getCode(),user.getUid()};
//
//
//        System.out.println(Arrays.toString(params));
//        jdbcTemplate.update(sql, params);
        String sql = "update tab_user set status=?,code=? where uid=?";
        //3.设置实际参数
        Object[] params = {user.getStatus(),user.getCode(),user.getUid()};
        System.out.println("params:"+ Arrays.toString(params));
        //4.执行更新操作
        jdbcTemplate.update(sql, params);
    }


    public static void main(String[] args) {
        User user = new User();
        user.setUid(4);
        user.setStatus("Y");
        user.setCode(null);
       new UserDaoImpl().active(user);

    }


}
