package com.hr.controller;

import com.hr.entity.Permission;
import com.hr.entity.Role;
import com.hr.entity.User;
import com.hr.service.PermissionService;
import com.hr.service.RoleService;
import com.hr.service.UserService;
import com.hr.util.PasswordEncoder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户管理 Controller
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 用户登录
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        User loginUser = userService.login(user.getUsername(), user.getPassword());
        if (loginUser != null) {
            loginUser.setPassword(null); // 不返回密码

            // 返回用户信息、角色编码列表和权限列表
            Map<String, Object> result = new HashMap<>();
            result.put("user", loginUser);
            result.put("roleCodes", loginUser.getRoleCodes()); // 角色编码列表，用于前端判断显示哪些菜单

            // 获取用户的权限列表
            List<Permission> permissions = permissionService.listByUserId(loginUser.getId());
            List<String> permCodes = permissions.stream()
                    .map(Permission::getPermCode)
                    .collect(Collectors.toList());
            result.put("permCodes", permCodes); // 权限编码列表，用于按钮级别权限控制

            return Result.success(result);
        }
        return Result.error("用户名或密码错误");
    }

    /**
     * 根据用户 ID 获取角色编码（用于前端判断显示哪些菜单）
     */
    @ApiOperation("获取用户角色编码")
    @GetMapping("/{userId}/roles")
    public Result<List<String>> getRoleCodes(@PathVariable Long userId) {
        List<String> roleCodes = userService.getRoleCodesByUserId(userId);
        return Result.success(roleCodes);
    }

    /**
     * 获取用户列表
     */
    @ApiOperation("获取用户列表")
    @GetMapping("/list")
    public Result<List<User>> list() {
        return Result.success(userService.list());
    }

    /**
     * 根据 ID 查询用户
     */
    @ApiOperation("根据 ID 查询用户")
    @GetMapping("/{id}")
    public Result<User> getById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user != null) {
            user.setPassword(null);
        }
        return user != null ? Result.success(user) : Result.error("用户不存在");
    }

    /**
     * 根据用户名查询
     */
    @ApiOperation("根据用户名查询")
    @GetMapping("/username/{username}")
    public Result<User> getByUsername(@PathVariable String username) {
        User user = userService.getByUsername(username);
        if (user != null) {
            user.setPassword(null);
        }
        return user != null ? Result.success(user) : Result.error("用户不存在");
    }

    /**
     * 新增用户
     */
    @ApiOperation("新增用户")
    @PostMapping
    public Result<Boolean> add(@RequestBody User user) {
        // 检查用户名是否已存在
        User exists = userService.getByUsername(user.getUsername());
        if (exists != null) {
            return Result.error("用户名已存在");
        }
        // 使用 BCrypt 加密密码
        if (user.getPassword() != null) {
            user.setPassword(PasswordEncoder.encode(user.getPassword()));
        }
        boolean saved = userService.save(user);
        return saved ? Result.success(true) : Result.error("新增失败");
    }

    /**
     * 修改用户
     */
    @ApiOperation("修改用户")
    @PutMapping
    public Result<Boolean> update(@RequestBody User user) {
        // 处理密码
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(PasswordEncoder.encode(user.getPassword()));
        } else {
            user.setPassword(null);
        }
        boolean updated = userService.updateById(user);
        return updated ? Result.success(true) : Result.error("修改失败");
    }

    /**
     * 删除用户
     */
    @ApiOperation("删除用户")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean removed = userService.removeById(id);
        return removed ? Result.success(true) : Result.error("删除失败");
    }

    /**
     * 修改用户状态
     */
    @ApiOperation("修改用户状态")
    @PutMapping("/{id}/status")
    public Result<Boolean> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        user.setStatus(status);
        boolean updated = userService.updateById(user);
        return updated ? Result.success(true) : Result.error("修改失败");
    }
}
