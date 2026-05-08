-- 更新现有用户密码为 BCrypt 加密格式
-- 默认密码：123456
-- BCrypt 哈希：$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

USE hr_system;

-- 更新所有用户密码
UPDATE `user` SET `password` = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy';

-- 确认更新结果
SELECT `id`, `username`, `real_name`, SUBSTRING(`password`, 1, 20) AS `password_prefix` FROM `user`;

-- 添加新用户（如果需要）
-- INSERT INTO `user` (`username`, `password`, `real_name`, `phone`, `email`, `status`) VALUES
-- ('hr1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '人事经理', '13800000002', 'hr1@example.com', 1);

-- 确认用户角色关联
SELECT u.id, u.username, u.real_name, r.role_name, r.role_code
FROM `user` u
LEFT JOIN `user_role` ur ON u.id = ur.user_id
LEFT JOIN `role` r ON ur.role_id = r.id
ORDER BY u.id;
