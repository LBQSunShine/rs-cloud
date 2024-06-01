package com.lbq.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lbq.function.ActionFunction;
import com.lbq.pojo.User;
import com.lbq.service.UserService;
import com.lbq.utils.IdsReq;
import com.lbq.utils.StringFormatUtils;
import com.lbq.vo.PageVo;
import com.lbq.vo.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 用户
 *
 * @author lbq
 * @since 2024-01-21
 */
@RestController
@RequestMapping("/sys/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/page")
    public R<?> page(PageVo pageVo, @RequestParam(name = "keyword", required = false) String keyword) {
        Page<User> res = userService.page(pageVo, keyword);
        return R.success(res);
    }

    @PostMapping("/edit")
    public R<?> edit(@RequestBody User user) {
        userService.edit(user);
        return R.success("修改成功!");
    }

    @PostMapping("/upload")
    public R<?> upload(MultipartFile file) {
        String upload = userService.upload(file);
        return R.success("上传成功!", upload);
    }


    @PostMapping("/enable")
    public R<?> enable(@RequestBody IdsReq idsReq) {
        List<String> message = this.action(idsReq.getIds(), "启用", (user) -> {
            userService.enable(user);
        });
        return R.success(message);
    }

    @PostMapping("/disable")
    public R<?> disable(@RequestBody IdsReq idsReq) {
        List<String> message = this.action(idsReq.getIds(), "禁用", (user) -> {
            userService.disable(user);
        });
        return R.success(message);
    }

    @GetMapping("/hasAuth")
    public R<?> hasAuth(HttpServletRequest request) {
        return R.success(userService.hasAuth());
    }

    private List<String> action(Collection<Integer> ids, String option, ActionFunction<User> fun) {
        List<User> users = userService.listByIds(ids);
        List<String> message = new ArrayList<>();
        for (User user : users) {
            String username = user.getUsername();
            try {
                fun.callback(user);
                String format = StringFormatUtils.format("账号{}{}成功！", username, option);
                message.add(format);
            } catch (Exception e) {
                String format = StringFormatUtils.format("账号{}{}失败！失败原因：{}", username, option, e.getMessage());
                message.add(format);
            }
        }
        return message;
    }

}

