package com.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hr.entity.Role;
import com.hr.entity.User;
import com.hr.mapper.UserMapper;
import com.hr.service.RoleService;
import com.hr.service.UserService;
import com.hr.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private RoleService roleService;

    @Override
    public User getByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    public User login(String username, String password) {
        User user = getByUsername(username);
        if (user == null) {
            return null;
        }
        // 使用 BCrypt 验证密码
        if (!PasswordEncoder.matches(password, user.getPassword())) {
            return null;
        }
        // 填充用户角色信息
        List<String> roleCodes = getRoleCodesByUserId(user.getId());
        user.setRoleCodes(roleCodes);
        return user;
    }

    @Override
    public List<String> getRoleCodesByUserId(Long userId) {
        List<Role> roles = roleService.listByUserId(userId);
        return roles.stream()
                .map(Role::getRoleCode)
                .collect(Collectors.toList());
    }
}
