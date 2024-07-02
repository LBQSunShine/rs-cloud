package com.lbq.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lbq.pojo.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 消息
 *
 * @author lbq
 * @since 2024-07-02
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

    List<Message> listMessage(@Param("username") String username);
}
