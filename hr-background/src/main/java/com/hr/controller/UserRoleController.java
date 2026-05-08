package com.hr.controller;

import com.hr.entity.Role;
import com.hr.entity.User;
import com.hr.service.RoleService;
import com.hr.service.UserRoleService;
import com.hr.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户角色管理 Controller
 */
@Api(tags = "用户角色管理接口")
@RestController
@RequestMapping("/api/user-role")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    /**
     * 获取用户的角色列表
     */
    @ApiOperation("获取用户的角色列表")
    @GetMapping("/roles/{userId}")
    public Result<List<Role>> getRolesByUser(@PathVariable Long userId) {
        return Result.success(roleService.listByUserId(userId));
    }

    /**
     * 为用户分配角色
     */
    @ApiOperation("为用户分配角色")
    @PostMapping("/assign")
    public Result<Boolean> assignRoles(@RequestBody AssignRoleRequest request) {
        boolean result = userRoleService.assignRoles(request.getUserId(), request.getRoleIds());
        return result ? Result.success(true) : Result.error("分配失败");
    }

    /**
     * 获取所有可选角色
     */
    @ApiOperation("获取所有可选角色")
    @GetMapping("/roles/all")
    public Result<List<Role>> getAllRoles() {
        return Result.success(roleService.listAll());
    }

    /**
     * 获取用户详情（含角色信息）
     */
    @ApiOperation("获取用户详情（含角色信息）")
    @GetMapping("/user/{userId}")
    public Result<Map<String, Object>> getUserWithRoles(@PathVariable Long userId) {
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setPassword(null);

        List<Role> roles = roleService.listByUserId(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("user", user);
        result.put("roles", roles);

        return Result.success(result);
    }

    /**
     * 请求体：分配角色
     */
    @lombok.Data
    public static class AssignRoleRequest {
        private Long userId;
        private List<Long> roleIds;
    }
}
