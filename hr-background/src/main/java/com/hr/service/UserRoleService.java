package com.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hr.entity.UserRole;

import java.util.List;

/**
 * 用户角色关联服务接口
 */
public interface UserRoleService extends IService<UserRole> {

    /**
     * 根据用户 ID 获取角色 ID 列表
     */
    List<Long> getRoleIdsByUserId(Long userId);

    /**
     * 为用户分配角色
     */
    boolean assignRoles(Long userId, List<Long> roleIds);
}
