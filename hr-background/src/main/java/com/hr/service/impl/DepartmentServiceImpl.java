package com.hr.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hr.entity.Department;
import com.hr.mapper.DepartmentMapper;
import com.hr.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门服务实现类
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    @Override
    public IPage<Department> pageList(Integer page, Integer size, String deptName) {
        IPage<Department> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<Department> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(deptName)) {
            wrapper.like(Department::getDeptName, deptName);
        }
        wrapper.orderByAsc(Department::getSort);
        return baseMapper.selectPage(pageInfo, wrapper);
    }

    @Override
    public List<Department> listAll() {
        return baseMapper.selectList(null);
    }

    @Override
    public List<Department> treeList() {
        List<Department> allDepts = listAll();
        return allDepts.stream()
                .filter(dept -> dept.getParentId() == 0 || dept.getParentId() == null)
                .peek(dept -> dept.setChildren(getChildren(dept.getId(), allDepts)))
                .collect(Collectors.toList());
    }

    private List<Department> getChildren(Long parentId, List<Department> allDepts) {
        return allDepts.stream()
                .filter(dept -> parentId.equals(dept.getParentId()))
                .peek(dept -> dept.setChildren(getChildren(dept.getId(), allDepts)))
                .collect(Collectors.toList());
    }
}
