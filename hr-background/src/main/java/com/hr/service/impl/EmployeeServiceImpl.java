package com.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hr.entity.Employee;
import com.hr.mapper.EmployeeMapper;
import com.hr.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 员工服务实现类
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Override
    public IPage<Employee> pageList(Integer page, Integer size, String name, Long deptId) {
        IPage<Employee> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(name)) {
            wrapper.like(Employee::getName, name);
        }
        if (deptId != null) {
            wrapper.eq(Employee::getDeptId, deptId);
        }
        wrapper.orderByDesc(Employee::getCreateTime);
        return baseMapper.selectPage(pageInfo, wrapper);
    }

    @Override
    public Employee getByEmpNo(String empNo) {
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getEmpNo, empNo);
        return baseMapper.selectOne(wrapper);
    }
}
