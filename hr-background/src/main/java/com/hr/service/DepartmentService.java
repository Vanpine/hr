package com.hr.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hr.entity.Department;

import java.util.List;

/**
 * 部门服务接口
 */
public interface DepartmentService extends IService<Department> {

    /**
     * 分页查询部门列表
     */
    IPage<Department> pageList(Integer page, Integer size, String deptName);

    /**
     * 获取所有部门列表
     */
    List<Department> listAll();

    /**
     * 获取部门树形结构
     */
    List<Department> treeList();
}
