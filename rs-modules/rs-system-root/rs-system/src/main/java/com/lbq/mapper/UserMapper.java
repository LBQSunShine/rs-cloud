package com.lbq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lbq.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户
 *
 * @author lbq
 * @since 2024-01-21
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
