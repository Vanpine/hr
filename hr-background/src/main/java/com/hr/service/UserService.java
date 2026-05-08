package com.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hr.entity.User;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {

    /**
     * 根据用户名查询用户
     */
    User getByUsername(String username);

    /**
     * 用户登录
     */
    User login(String username, String password);
}
