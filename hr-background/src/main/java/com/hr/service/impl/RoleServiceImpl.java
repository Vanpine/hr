package com.hr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hr.entity.Role;
import com.hr.mapper.RoleMapper;
import com.hr.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色服务实现类
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Override
    public List<Role> listAll() {
        return baseMapper.selectList(null);
    }

    @Override
    public List<Role> listByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }
}
