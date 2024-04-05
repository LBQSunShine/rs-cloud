package com.lbq.openfeign;


import com.lbq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 标签
 *
 * @author lbq
 * @since 2024-02-25
 */
@RestController
@RequestMapping("/sys/openfeign/file")
public class UserFileOpenfeign {

    @Autowired
    private UserService userService;

    @GetMapping("/getUserFiles")
    public List<String> getUserFiles() {
        return userService.getUserFiles();
    }
}

