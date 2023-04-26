package com.memory.usercenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memory.usercenter.common.BaseResponse;
import com.memory.usercenter.common.ResultUtils;
import com.memory.usercenter.exception.BusinessException;
import com.memory.usercenter.model.entity.User;
import com.memory.usercenter.model.request.user.UserLogin;
import com.memory.usercenter.model.request.user.UserRegister;
import com.memory.usercenter.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.memory.usercenter.common.ErrorCode.PARMS_ERROR;
import static com.memory.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author 邓哈哈
 * 2023/3/10 13:23
 * Function:
 * Version 1.0
 */

@RestController
@RequestMapping("/user")
//@CrossOrigin
//@CrossOrigin(origins = "http://localhost:7070")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest 注册用户信息
     * @return id
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegister userRegisterRequest) {
        //controller对参数的校验
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode))
            throw new BusinessException(PARMS_ERROR);

        long userRegister = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        return ResultUtils.success(userRegister);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest 登录用户信息
     * @param request          request
     * @return User
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLogin userLoginRequest, HttpServletRequest request) {
        //controller对参数的校验
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword))
            throw new BusinessException(PARMS_ERROR);

        User userLogin = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(userLogin);
    }

    /**
     * 用户注销
     *
     * @param request request
     * @return 注销成功与否(t / f)
     */
    @PostMapping("/logout")
    public BaseResponse<String> userLogout(HttpServletRequest request) {
        //controller对参数的校验
        if (request == null)
            throw new BusinessException(PARMS_ERROR);

        String userLogout = userService.userLogout(request);
        return ResultUtils.success(userLogout);
    }

    /**
     * 获取当前用户登录态
     *
     * @param request request
     * @return 当前用户信息
     */
    @GetMapping("/currentUser")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        // controller对参数的校验
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (currentUser == null)
            throw new BusinessException(PARMS_ERROR);

        User user = userService.getCurrentUser(request);
        return ResultUtils.success(user);
    }

    /**
     * 展示在线用户列表
     *
     * @param username 用户名
     * @return 查到的用户
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> userSearch(String username, HttpServletRequest request) {
        // controller对参数的校验
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (StringUtils.isBlank(username) && currentUser == null)
            throw new BusinessException(PARMS_ERROR);

        List<User> userList = userService.userSearch(username, request);
        return ResultUtils.success(userList);
    }

    /**
     * 根据标签查询用户
     *
     * @param tagNameList 标签列表
     * @return 用户列表
     */
    @GetMapping("/search/tags")
    public BaseResponse<List<User>> searchByTags(@RequestParam List<String> tagNameList) {
        // controller对参数的校验
        if (CollectionUtils.isEmpty(tagNameList))
            throw new BusinessException(PARMS_ERROR);

        List<User> userList = userService.searchUserByTags(tagNameList);
        return ResultUtils.success(userList);
    }

    /**
     * 根据标签查询用户
     *
     * @return 用户列表
     */
    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommend(@RequestParam long currentPage, long pageSize, HttpServletRequest request) {
        // controller对参数的校验

        Page<User> userList = userService.selectPage(currentPage, pageSize, request);
        return ResultUtils.success(userList);
    }

    /**
     * 删除用户
     *
     * @param id      用户id
     * @param request request
     * @return true/false
     */
    @DeleteMapping("/delete")
    public BaseResponse<Boolean> userDelete(Long id, HttpServletRequest request) {
        // controller对参数的校验
        if (id == 0)
            throw new BusinessException(PARMS_ERROR);

        Boolean remove = userService.userDelete(id, request);
        return ResultUtils.success(remove);
    }

    /**
     * 修改用户信息
     *
     * @param user    要修改的用户
     * @param request 执行修改的用户
     * @return 修改成功与否(t / f)
     */
    @PostMapping("/update")
    public BaseResponse<String> userUpdate(@RequestBody User user, HttpServletRequest request) {
        // controller对参数的校验
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        System.out.println(loginUser);
        // 这里我们如此做实现:
        // 如果是管理员, 则进入管理员修改接口, 允许修改任何用户
        // 如果是普通用户, 则进入普通用户修改接口, 仅允许修改自己(当前用户)的信息

        if (user == null || loginUser == null) {
            throw new BusinessException(PARMS_ERROR);
        }
        String update = userService.userUpdate(user, loginUser);
        return ResultUtils.success(update);
    }

}
