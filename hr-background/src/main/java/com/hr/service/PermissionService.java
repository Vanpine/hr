package com.hr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hr.entity.Permission;

import java.util.List;

/**
 * 权限服务接口
 */
public interface PermissionService extends IService<Permission> {

    /**
     * 获取所有权限列表
     */
    List<Permission> listAll();

    /**
     * 获取树形权限列表
     */
    List<Permission> treeList();

    /**
     * 根据角色 ID 获取权限列表
     */
    List<Permission> listByRoleId(Long roleId);

    /**
     * 根据用户 ID 获取权限列表
     */
    List<Permission> listByUserId(Long userId);
}
