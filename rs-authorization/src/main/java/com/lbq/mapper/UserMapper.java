package com.lbq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lbq.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户
 *
 * @Author: lbq
 * @Date: 2024/1/12
 * @Version: 1.0
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
