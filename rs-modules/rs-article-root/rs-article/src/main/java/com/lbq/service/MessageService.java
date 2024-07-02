package com.lbq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lbq.pojo.Message;

import java.util.Collection;
import java.util.List;

/**
 * 消息
 *
 * @author lbq
 * @since 2024-07-02
 */
public interface MessageService extends IService<Message> {

    void readMessage(Message message);

    List<Message> listMessage();

    void deleteIsReadMessage();

    void removeByArticleIds(Collection<Integer> articleIds);
}
