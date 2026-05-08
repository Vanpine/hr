package com.hr.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hr.entity.Permission;
import com.hr.mapper.PermissionMapper;
import com.hr.service.PermissionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Override
    public List<Permission> listAll() {
        return baseMapper.selectList(null);
    }

    @Override
    public List<Permission> treeList() {
        List<Permission> allPerms = listAll();
        return allPerms.stream()
                .filter(perm -> perm.getParentId() == 0 || perm.getParentId() == null)
                .peek(perm -> perm.setChildren(getChildren(perm.getId(), allPerms)))
                .collect(Collectors.toList());
    }

    private List<Permission> getChildren(Long parentId, List<Permission> allPerms) {
        return allPerms.stream()
                .filter(perm -> parentId.equals(perm.getParentId()))
                .peek(perm -> perm.setChildren(getChildren(perm.getId(), allPerms)))
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> listByRoleId(Long roleId) {
        return baseMapper.selectByRoleId(roleId);
    }

    @Override
    public List<Permission> listByUserId(Long userId) {
        return baseMapper.selectByUserId(userId);
    }
}
