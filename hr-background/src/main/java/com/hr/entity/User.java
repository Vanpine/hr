package com.hr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户实体类
 */
@Data
@TableName("user")
public class User {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String realName;

    private String phone;

    private String email;

    private String avatar;

    private Integer status;

    /**
     * 用户角色编码列表（非数据库字段，登录时填充）
     * 包含：ADMIN(管理员)、HR_MANAGER(部门负责人)、EMPLOYEE(员工)
     */
    @TableField(exist = false)
    private List<String> roleCodes;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
