package com.memory.usercenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memory.usercenter.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Lenovo
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2023-03-08 16:07:41
 */
public interface UserService extends IService<User> {
    long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    String userLogout(HttpServletRequest request);

    User getSafetyUser(User originUser);

    List<User> userSearch(String username, HttpServletRequest request);

    User getCurrentUser(HttpServletRequest request);

    Boolean userDelete(Long id, HttpServletRequest request);

    List<User> searchUserByTags(List<String> tagNameList);

    String userUpdate(User user, User loginUser);

    String userUpdateByAdmin(User user);

    Boolean isAdmin(User loginUser);

    Boolean isAdmin(HttpServletRequest request);

    Page<User> selectPage(long currentPage, long pageSize);
}
