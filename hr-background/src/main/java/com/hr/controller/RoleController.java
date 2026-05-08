package com.hr.controller;

import com.hr.entity.Role;
import com.hr.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理 Controller
 */
@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/api/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 获取所有角色列表
     */
    @ApiOperation("获取所有角色列表")
    @GetMapping("/list")
    public Result<List<Role>> list() {
        return Result.success(roleService.listAll());
    }

    /**
     * 根据 ID 查询角色
     */
    @ApiOperation("根据 ID 查询角色")
    @GetMapping("/{id}")
    public Result<Role> getById(@PathVariable Long id) {
        Role role = roleService.getById(id);
        return role != null ? Result.success(role) : Result.error("角色不存在");
    }

    /**
     * 新增角色
     */
    @ApiOperation("新增角色")
    @PostMapping
    public Result<Boolean> add(@RequestBody Role role) {
        // 检查角色编码是否已存在
        List<Role> allRoles = roleService.listAll();
        for (Role r : allRoles) {
            if (r.getRoleCode().equals(role.getRoleCode())) {
                return Result.error("角色编码已存在");
            }
        }
        boolean saved = roleService.save(role);
        return saved ? Result.success(true) : Result.error("新增失败");
    }

    /**
     * 修改角色
     */
    @ApiOperation("修改角色")
    @PutMapping
    public Result<Boolean> update(@RequestBody Role role) {
        boolean updated = roleService.updateById(role);
        return updated ? Result.success(true) : Result.error("修改失败");
    }

    /**
     * 删除角色
     */
    @ApiOperation("删除角色")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean removed = roleService.removeById(id);
        return removed ? Result.success(true) : Result.error("删除失败");
    }

    /**
     * 获取用户的角色列表
     */
    @ApiOperation("获取用户的角色列表")
    @GetMapping("/user/{userId}")
    public Result<List<Role>> listByUser(@PathVariable Long userId) {
        return Result.success(roleService.listByUserId(userId));
    }
}
