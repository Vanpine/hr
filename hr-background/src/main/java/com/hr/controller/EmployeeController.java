package com.hr.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hr.entity.Employee;
import com.hr.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 员工管理 Controller
 */
@Api(tags = "员工管理接口")
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 分页查询员工列表
     */
    @ApiOperation("分页查询员工列表")
    @GetMapping("/page")
    public Result<IPage<Employee>> page(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long deptId) {
        IPage<Employee> pageInfo = employeeService.pageList(page, size, name, deptId);
        return Result.success(pageInfo);
    }

    /**
     * 根据 ID 查询员工
     */
    @ApiOperation("根据 ID 查询员工")
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        return employee != null ? Result.success(employee) : Result.error("员工不存在");
    }

    /**
     * 根据员工编号查询
     */
    @ApiOperation("根据员工编号查询")
    @GetMapping("/no/{empNo}")
    public Result<Employee> getByEmpNo(@PathVariable String empNo) {
        Employee employee = employeeService.getByEmpNo(empNo);
        return employee != null ? Result.success(employee) : Result.error("员工不存在");
    }

    /**
     * 新增员工
     */
    @ApiOperation("新增员工")
    @PostMapping
    public Result<Boolean> add(@RequestBody Employee employee) {
        // 检查员工编号是否已存在
        Employee exists = employeeService.getByEmpNo(employee.getEmpNo());
        if (exists != null) {
            return Result.error("员工编号已存在");
        }
        boolean saved = employeeService.save(employee);
        return saved ? Result.success(true) : Result.error("新增失败");
    }

    /**
     * 修改员工
     */
    @ApiOperation("修改员工")
    @PutMapping
    public Result<Boolean> update(@RequestBody Employee employee) {
        boolean updated = employeeService.updateById(employee);
        return updated ? Result.success(true) : Result.error("修改失败");
    }

    /**
     * 删除员工
     */
    @ApiOperation("删除员工")
    @DeleteMapping("/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        boolean removed = employeeService.removeById(id);
        return removed ? Result.success(true) : Result.error("删除失败");
    }

    /**
     * 批量删除员工
     */
    @ApiOperation("批量删除员工")
    @DeleteMapping
    public Result<Boolean> batchDelete(@RequestBody Long[] ids) {
        boolean removed = employeeService.removeBatchByIds(java.util.Arrays.asList(ids));
        return removed ? Result.success(true) : Result.error("删除失败");
    }
}
