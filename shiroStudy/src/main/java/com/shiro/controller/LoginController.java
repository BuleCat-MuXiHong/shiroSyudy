package com.shiro.controller;

import com.shiro.common.Result;
import com.shiro.entity.Login.LoginRequest;
import com.shiro.entity.Login.LoginVO;
import com.shiro.entity.User;
import com.shiro.exception.BusinessException;
import com.shiro.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
@Api(tags = "登录")
@RestController
public class LoginController {


    @Resource
    private UserService userService;

    @ApiOperation("登录")
    @PostMapping("login")
    public LoginVO login(@RequestBody LoginRequest loginRequest) {
        String userName = loginRequest.getUserName();
        String password = loginRequest.getPassword();
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);

        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            // 这个没用，不要打开。若打开，在使用shiro-redis时会序列化失败，因为这个subject是
            // WebDelegatingSubject类型，没实现serializable接口。若想往session里放东西，必须
            // 实现Serializable接口。
            // 当然，本项目没有用到shiro-redis进行序列化，只是提示一下。
            // Session session = subject.getSession();
            // session.setAttribute("subject", subject);
            return this.fillResult(userName);
        } catch (AuthenticationException e) {
            throw new BusinessException("身份验证失败");
        }
    }

    private LoginVO fillResult(String userName) {
        User user = userService.lambdaQuery()
                .eq(User::getUserName, userName)
                .one();
        LoginVO loginVO = new LoginVO();
        loginVO.setUserId(user.getId());
        loginVO.setUserName(user.getUserName());
        return loginVO;
    }
}
