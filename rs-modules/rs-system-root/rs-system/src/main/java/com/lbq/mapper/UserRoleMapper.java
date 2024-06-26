package com.lbq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lbq.pojo.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户角色关系
 *
 * @author lbq
 * @since 2024-01-21
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    Integer hasAuth(@Param("username") String username);
}
