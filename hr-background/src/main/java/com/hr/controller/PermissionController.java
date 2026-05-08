package com.hr.controller;

import com.hr.entity.Permission;
import com.hr.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理 Controller
 */
@Api(tags = "权限管理接口")
@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    /**
     * 获取所有权限列表
     */
    @ApiOperation("获取所有权限列表")
    @GetMapping("/list")
    public Result<List<Permission>> list() {
        return Result.success(permissionService.listAll());
    }

    /**
     * 获取树形权限列表
     */
    @ApiOperation("获取树形权限列表")
    @GetMapping("/tree")
    public Result<List<Permission>> tree() {
        return Result.success(permissionService.treeList());
    }

    /**
     * 根据 ID 查询权限
     */
    @ApiOperation("根据 ID 查询权限")
    @GetMapping("/{id}")
    public Result<Permission> getById(@PathVariable Long id) {
        Permission permission = permissionService.getById(id);
        return permission != null ? Result.success(permission) : Result.error("权限不存在");
    }

    /**
     * 新增权限
     */
    @ApiOperation("新增权限")
    @PostMapping
    public Result<Boolean> add(@RequestBody Permission permission) {
        boolean saved = permissionService.save(permission);
        return saved ? Result.success(true) : Result.error("新增失败");
    }

    /**
     * 修改权限
     */
    @ApiOperation("修改权限")
    @PutMapping
    public Result<Boolean> update(@RequestBody Permission permission) {
        boolean updated = permissionService.updateById(permission);
        return updated ? Result.success(true) : Result.error("修改失败");
    }

    /**
     * 删除权限
     */
    @ApiOperation("删除权限")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean removed = permissionService.removeById(id);
        return removed ? Result.success(true) : Result.error("删除失败");
    }

    /**
     * 获取角色的权限列表
     */
    @ApiOperation("获取角色的权限列表")
    @GetMapping("/role/{roleId}")
    public Result<List<Permission>> listByRole(@PathVariable Long roleId) {
        return Result.success(permissionService.listByRoleId(roleId));
    }

    /**
     * 获取用户的权限列表
     */
    @ApiOperation("获取用户的权限列表")
    @GetMapping("/user/{userId}")
    public Result<List<Permission>> listByUser(@PathVariable Long userId) {
        return Result.success(permissionService.listByUserId(userId));
    }

    /**
     * 获取用户的菜单树（只返回菜单类型，用于前端动态生成菜单）
     */
    @ApiOperation("获取用户的菜单树")
    @GetMapping("/menu/{userId}")
    public Result<List<Permission>> menuTree(@PathVariable Long userId) {
        List<Permission> userPerms = permissionService.listByUserId(userId);
        // 只保留菜单类型 (permType=1)
        List<Permission> menuPerms = userPerms.stream()
                .filter(p -> p.getPermType() == 1)
                .collect(java.util.stream.Collectors.toList());

        // 构建树形结构
        return Result.success(menuPerms.stream()
                .filter(p -> p.getParentId() == 0 || p.getParentId() == null)
                .peek(p -> p.setChildren(buildChildren(p.getId(), menuPerms)))
                .collect(java.util.stream.Collectors.toList()));
    }

    private List<Permission> buildChildren(Long parentId, List<Permission> allPerms) {
        return allPerms.stream()
                .filter(p -> parentId.equals(p.getParentId()))
                .peek(p -> p.setChildren(buildChildren(p.getId(), allPerms)))
                .collect(java.util.stream.Collectors.toList());
    }
}
