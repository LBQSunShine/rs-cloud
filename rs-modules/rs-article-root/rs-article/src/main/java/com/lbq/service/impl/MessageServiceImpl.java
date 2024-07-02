package com.lbq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.mapper.MessageMapper;
import com.lbq.pojo.Message;
import com.lbq.service.MessageService;
import org.springframework.stereotype.Service;

/**
 * 消息
 *
 * @author lbq
 * @since 2024-07-02
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

}
