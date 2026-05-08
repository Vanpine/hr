package com.hr.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hr.entity.Employee;

/**
 * 员工服务接口
 */
public interface EmployeeService extends IService<Employee> {

    /**
     * 分页查询员工列表
     */
    IPage<Employee> pageList(Integer page, Integer size, String name, Long deptId);

    /**
     * 根据员工编号查询
     */
    Employee getByEmpNo(String empNo);
}
