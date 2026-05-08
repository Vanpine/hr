package com.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hr.entity.Role;

import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService extends IService<Role> {

    /**
     * 获取所有角色列表
     */
    List<Role> listAll();

    /**
     * 根据用户 ID 获取角色列表
     */
    List<Role> listByUserId(Long userId);
}
