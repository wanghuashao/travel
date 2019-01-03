package com.itheima.travel.user.service;

import com.itheima.travel.user.domain.User;
import com.itheima.travel.user.exception.ReActiveException;
import com.itheima.travel.user.exception.RegisterErrorException;
import com.itheima.travel.user.exception.UsernameExsitException;
import com.itheima.travel.user.exception.UsernameNotNullException;

public interface IUserService {
    boolean findUserInfoByUserName(String userName) throws UsernameNotNullException, UsernameExsitException;

    User login(String username, String password) throws Exception;

    void active(String code) throws ReActiveException;

    int register(User user) throws Exception, RegisterErrorException;
}
