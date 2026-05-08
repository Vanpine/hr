package com.hr.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hr.entity.Department;
import com.hr.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门管理 Controller
 */
@Api(tags = "部门管理接口")
@RestController
@RequestMapping("/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 分页查询部门列表
     */
    @ApiOperation("分页查询部门列表")
    @GetMapping("/page")
    public Result<IPage<Department>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String deptName) {
        IPage<Department> pageInfo = departmentService.pageList(page, size, deptName);
        return Result.success(pageInfo);
    }

    /**
     * 获取所有部门列表
     */
    @ApiOperation("获取所有部门列表")
    @GetMapping("/list")
    public Result<List<Department>> list() {
        return Result.success(departmentService.listAll());
    }

    /**
     * 获取部门树形结构
     */
    @ApiOperation("获取部门树形结构")
    @GetMapping("/tree")
    public Result<List<Department>> tree() {
        return Result.success(departmentService.treeList());
    }

    /**
     * 根据 ID 查询部门
     */
    @ApiOperation("根据 ID 查询部门")
    @GetMapping("/{id}")
    public Result<Department> getById(@PathVariable Long id) {
        Department dept = departmentService.getById(id);
        return dept != null ? Result.success(dept) : Result.error("部门不存在");
    }

    /**
     * 新增部门
     */
    @ApiOperation("新增部门")
    @PostMapping
    public Result<Boolean> add(@RequestBody Department department) {
        boolean saved = departmentService.save(department);
        return saved ? Result.success(true) : Result.error("新增失败");
    }

    /**
     * 修改部门
     */
    @ApiOperation("修改部门")
    @PutMapping
    public Result<Boolean> update(@RequestBody Department department) {
        boolean updated = departmentService.updateById(department);
        return updated ? Result.success(true) : Result.error("修改失败");
    }

    /**
     * 删除部门
     */
    @ApiOperation("删除部门")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        // 检查是否有子部门
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Department::getParentId, id);
        long count = departmentService.count(wrapper);
        if (count > 0) {
            return Result.error("存在子部门，无法删除");
        }
        boolean removed = departmentService.removeById(id);
        return removed ? Result.success(true) : Result.error("删除失败");
    }
}
