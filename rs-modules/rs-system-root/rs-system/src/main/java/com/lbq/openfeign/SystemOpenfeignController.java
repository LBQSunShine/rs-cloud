package com.lbq.openfeign;


import com.lbq.pojo.OperLog;
import com.lbq.pojo.User;
import com.lbq.service.OperLogService;
import com.lbq.service.TagService;
import com.lbq.service.UserRoleService;
import com.lbq.service.UserService;
import com.lbq.utils.IdsReq;
import com.lbq.vo.OperLogVo;
import com.lbq.vo.TagVo;
import com.lbq.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * OpenfeignApi
 *
 * @author lbq
 * @since 2024-02-25
 */
@RestController
@RequestMapping("/sys/openfeign")
public class SystemOpenfeignController {

    @Autowired
    private OperLogService operLogService;

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("/addOperLog")
    public void addOperLog(@RequestBody OperLogVo operLogVo) {
        OperLog operLog = new OperLog(operLogVo);
        operLogService.save(operLog);
    }

    @PostMapping("/getMapByIds")
    public Map<Integer, TagVo> getMapByIds(@RequestBody IdsReq idsReq) {
        return tagService.getMapByIds(idsReq.getIds());
    }

    @GetMapping("/getUserFiles")
    public List<String> getUserFiles() {
        return userService.getUserFiles();
    }

    @PostMapping("/addUserRole")
    public void addUserRole(@RequestParam(name = "userId") Integer userId, @RequestParam(name = "roleCode") String roleCode) {
        userRoleService.addUserRole(userId, roleCode);
    }

    @GetMapping("/getUserByUsername")
    public UserVo getUserByUsername(@RequestParam(name = "username") String username) {
        User user = userService.getByUsername(username);
        UserVo userVo = new UserVo();
        userVo.setId(user.getId());
        userVo.setAvatar(user.getAvatar());
        userVo.setNickname(user.getNickname());
        return userVo;
    }

    @PostMapping("/getMapByUsernames")
    public Map<String, UserVo> getMapByUsernames(@RequestBody Collection<String> usernames) {
        return userService.getMapByUsernames(usernames);
    }
}

