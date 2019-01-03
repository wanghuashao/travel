package com.itheima.travel.user.service.impl;

import com.itheima.travel.user.dao.IUserDao;
import com.itheima.travel.user.domain.User;
import com.itheima.travel.user.exception.ReActiveException;
import com.itheima.travel.user.exception.RegisterErrorException;
import com.itheima.travel.user.exception.UsernameExsitException;
import com.itheima.travel.user.exception.UsernameNotNullException;
import com.itheima.travel.user.service.IUserService;
import com.itheima.travel.utils.BeanFactory;
import com.itheima.travel.utils.MailUtil;
import com.itheima.travel.utils.Md5Util;
import com.itheima.travel.utils.UuidUtil;

public class UserServiceImpl implements IUserService{
    private IUserDao userDao = (IUserDao) BeanFactory.getBean("IUserDao");
    public boolean findUserInfoByUserName(String userName) throws UsernameNotNullException, UsernameExsitException {
        // 非空判断
        if (userName == null || "".equals(userName.trim())) {
            throw new UsernameNotNullException("用户名不能为空");
        } else {
            User exisuser = userDao.findUserInfoByUserName(userName);
            if (exisuser != null) {
                throw new UsernameExsitException("用户名已存在");
            } else {
                return true;
            }
        }
    }

    /**
     * 注册功能
     *
     * @param user
     * @return
     * @throws Exception
     * @throws RegisterErrorException
     */
    public int register(User user) throws Exception, RegisterErrorException {
        user.setStatus("N"); // 新注册的用户肯定处于未激活状态！
        user.setCode(UuidUtil.getUuid());
        // 对密码进行MD5不可逆的加密
        user.setPassword(Md5Util.encodeByMd5(user.getPassword()));
        int rows = userDao.register(user);
        if (rows > 0) {
            MailUtil.sendMail(user.getEmail(), user.getCode());
            return rows;
        } else {
            throw new RegisterErrorException("注册失败！");
        }

    }

    /**
     * 用户登录
     *
     * @param username
     * @param password
     * @return boolean
     */
    public User login(String username, String password) throws Exception {
        if (username != null && !"".equals(username.trim()) && password != null && !"".equals(password)) {
            User user = userDao.findUserInfoByUserName(username);
            if (user != null) {
                if (user.getStatus().equals("N")) {   //判断是否已经激活
                    return null;
                } else {
                    String pw = user.getPassword();
                    if (pw.equals(Md5Util.encodeByMd5(password))) {
                        return user;
                    } else {
                        return null;
                    }
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public void active(String code) throws ReActiveException {
//        userDao .active(code);
        User user = userDao.findUserByCode(code);
        if(user!= null) {
            user.setStatus("Y");
            user.setCode(null);
            userDao.active(user);
        } else {
            throw new ReActiveException("激活码已经过期或者已经被激活了");
        }
    }

}
