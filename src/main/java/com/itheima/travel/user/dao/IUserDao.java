package com.itheima.travel.user.dao;

import com.itheima.travel.user.domain.User;

public interface IUserDao {

    User findUserInfoByUserName(String userName);

    int register(User user);

    User findUserByCode(String code);

    void active(User user);
}
