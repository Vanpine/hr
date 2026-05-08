package com.hr.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工实体类
 */
@Data
@TableName("employee")
public class Employee {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String empNo;

    private String name;

    private Integer gender;

    private String phone;

    private String email;

    private Long deptId;

    private String position;

    private LocalDate entryDate;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
