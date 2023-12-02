package com.mavs.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.domain.entity.User;
import com.domain.service.UserService;

/**
 * ユーザー情報コントローラー
 */
@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    /** ユーザー情報サービス */
    @Autowired
    UserService userService;

    @GetMapping("")
    public List<User> getUser() {
        return userService.findAll();
    }
}
