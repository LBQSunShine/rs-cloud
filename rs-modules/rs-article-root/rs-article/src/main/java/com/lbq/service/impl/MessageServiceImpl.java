package com.lbq.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lbq.constants.StatusConstants;
import com.lbq.context.BaseContext;
import com.lbq.mapper.MessageMapper;
import com.lbq.pojo.Message;
import com.lbq.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息
 *
 * @author lbq
 * @since 2024-07-02
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void readMessage(Message message) {
        LambdaUpdateWrapper<Message> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper
                .eq(Message::getArticleId, message.getArticleId())
                .set(Message::getStatus, StatusConstants.STATUS_1);
        super.update(updateWrapper);
    }

    @Override
    public List<Message> listMessage() {
        return messageMapper.listMessage(BaseContext.getUsername());
    }

    @Override
    public void deleteIsReadMessage() {
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Message::getStatus, StatusConstants.STATUS_1);
        super.remove(queryWrapper);
    }
}
