package com.memory.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.memory.usercenter.common.ErrorCode;
import com.memory.usercenter.exception.BusinessException;
import com.memory.usercenter.model.entity.User;
import com.memory.usercenter.service.UserService;
import com.memory.usercenter.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.memory.usercenter.common.ErrorCode.*;
import static com.memory.usercenter.constant.UserConstant.*;

/**
 * @author 邓哈哈
 * 针对表【user(用户)】的数据库操作Service实现
 * 2023-03-08 16:07:41
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisTemplate redisTemplate = new RedisTemplate<String, Object>();

    /**
     * 用户注册
     *
     * @param userAccount   账户
     * @param userPassword  密码
     * @param checkPassword 二次密码
     * @return 用户id
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode) {
        // 1.校验
        // 1.1.账户, 密码, 二次密码不能为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) throw new BusinessException(PARMS_ERROR);

        // 1.2.账户不小于4位
        if (userAccount.length() < 4) throw new BusinessException("账户不符合要求", 50000, "账户小于4位");

        // 1.3.账户不包含特殊字符
        String pattern = ".*[\\s`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\\\]+.*";
        if (Pattern.matches(pattern, userAccount)) throw new BusinessException("账户不符合要求", 50001, "账户包含特殊字符");

        // 1.4.用户密码不小于8位
        if (userPassword.length() < 8) throw new BusinessException("密码不符合要求", 60000, "用户密码小于8位");

        // 1.5.二次密码与密码相同
        if (!userPassword.equals(checkPassword)) throw new BusinessException("二次密码不符合要求", 60001, "二次密码与密码不相同");

        // 1.6.星球编号不能超过5位
        if (planetCode.length() > 5) throw new BusinessException("星球编号不符合要求", 60002, "星球编号超过5位");

        // 1.7.账户不能重复
        QueryWrapper<User> ua_lqw = new QueryWrapper<>(); // LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ua_lqw.eq("user_account", userAccount); // userLambdaQueryWrapper.eq(User::getUserAccount, userAccount);
        Long ua_count = userMapper.selectCount(ua_lqw);   // long count = this.count(lqw);
        if (ua_count > 0) throw new BusinessException("账户不符合要求", 50002, "账户重复");

        // 1.8.星球编号不能重复
        QueryWrapper<User> pc_lqw = new QueryWrapper<>();
        pc_lqw.eq("planet_code", planetCode);
        Long pc_count = userMapper.selectCount(pc_lqw);
        if (pc_count > 0) throw new BusinessException("星球编号不符合要求", 60003, "星球编号重复");

        // 2.对密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
//        System.out.println(encryptPassword);

        // 3.向数据库中插入用户数据
        User user = new User();
        //
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setPlanetCode(planetCode);
        boolean save = this.save(user);
        //插入失败
        if (!save) throw new BusinessException(ErrorCode.UPDATE_ERROR);

        return user.getId();
    }

    /**
     * 用户登录
     *
     * @param userAccount  账户
     * @param userPassword 密码
     * @param request      登录用户信息
     * @return 脱敏用户信息
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.校验
        // 1.1.账户, 密码不能为空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) throw new BusinessException(PARMS_ERROR);

        // 1.2.账户不小于4位
        if (userAccount.length() < 4) throw new BusinessException("账户不符合要求", 50000, "账户小于4位");

        // 1.3.用户密码不小于8位
        if (userPassword.length() < 8) throw new BusinessException("密码不符合要求", 60000, "用户密码小于8位");

        // 1.4.账户不包含特殊字符
        String pattern = ".*[\\s`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\\\\]+.*";
        if (Pattern.matches(pattern, userAccount)) throw new BusinessException("账户不符合要求", 50001, "账户包含特殊字符");

        // 1.5.检验该用户是否注册
        User user = new User();

        user.setUserAccount(userAccount);

        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        user.setUserPassword(encryptPassword);

        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("user_account", userAccount).eq("user_password", encryptPassword);
        User one = this.getOne(qw);

        // 1.5.1.用户未注册(包含了MP自带的逻辑删除校验)
        if (one == null) throw new BusinessException(NOT_REGISTER);

        // 2.脱敏用户信息
        User safetyUser = getSafetyUser(one);

        // 3.记录用户登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);

        // 4.返回用户信息
        return safetyUser;
    }

    /**
     * 用户注销
     *
     * @param request request
     * @return 注销成功与否(t / f)
     */
    @Override
    public String userLogout(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        // 判断对象是否为空
        if (Optional.ofNullable(user).isPresent())
//        if (user == null)
            throw new BusinessException(NULL_ERROR);

        // 移除session
        return "注销成功";
    }

    /**
     * 展示在线用户列表
     *
     * @param username 用户名
     * @return 查到的用户
     */
    @Override
    public List<User> userSearch(String username, HttpServletRequest request) {
        // 1.校验权限
        if (!isAdmin(request)) throw new BusinessException(NO_AUTH);

        // 2.判空, 默认查询全部
        QueryWrapper<User> qw = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) qw.like("username", username);

        // 3.查询
        List<User> userList = this.list(qw);

        // 4.返回脱敏的用户信息
        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
    }

    /**
     * 获取当前用户登录态
     *
     * @param request request
     * @return 当前用户信息
     */
    @Override
    public User getCurrentUser(HttpServletRequest request) {
        User currentUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        Long id = currentUser.getId();

        // 查询数据库, 获取最新信息, 而非登录时记录的信息
        return getById(id);
    }

    /**
     * @param id      用户id
     * @param request request
     * @return 删除成功与否(t / f)
     */
    @Override
    public Boolean userDelete(Long id, HttpServletRequest request) {
        // 1.校验权限
        if (!isAdmin(request)) throw new BusinessException(NO_AUTH);

        // 2.判定该用户是否存在
        if (id <= 0) throw new BusinessException(PARMS_ERROR);

        // 3.删除用户(只要配置MP的逻辑删除的话, 该删除为逻辑删除)
        return removeById(id);
    }

    /**
     * 根据标签查询用户(在内存中判断用户是个否拥有选中的标签)
     *
     * @param tagNameList 打上的标签列表
     * @return 查询到的用户
     */
    @Override
    public List<User> searchUserByTags(List<String> tagNameList) {
        // 1.默认查询全部用户
        QueryWrapper<User> uqw = new QueryWrapper<>();
        Page<User> userPage = new Page<>(1, 100);

        List<User> userList = userMapper.selectList(uqw);
        // 用户为空, 返回空列表
        if (tagNameList.size() == 0)
            return userList;

        Gson gson = new Gson();
        // 2.从查询到的用户中, 根据标签筛选出符合的用户, 组合成列表并返回
        return userList.stream().filter(user -> {
            // 2.1.获取用户标签
            String tagsStr = user.getTags();
            // 2.1.校验是否有标签
            if (StringUtils.isBlank(tagsStr)) return false;
            // 2.2.把标签从JSON字符串转换为List列表(Java实体)的
            List<String> tempTagsNameSet = gson.fromJson(tagsStr, new TypeToken<List<String>>() {
            }.getType());
            // 2.3.筛出标签不符合的用户
            for (String tagName : tagNameList) {
                if (!tempTagsNameSet.contains(tagName)) return false;
            }
            // 2.4.返回符合用户
            return true;
        }).map(this::getSafetyUser).collect(Collectors.toList());

    }


    /**
     * 修改用户信息
     *
     * @param user      要修改的用户
     * @param loginUser 当前登录用户
     * @return 修改接过信息
     */
    @Override
    public String userUpdate(User user, User loginUser) {
        // 1.1.校验管理员权限
        if (isAdmin(loginUser)) {
            // 1.2.如果是管理员, 就跳转到管理员修改用户接口, 执行修改并返回结果
            return userUpdateByAdmin(user);
        }

        // 1.3.非管理员, 就执行普通用户修改用户方法
        // 根据传回来的id, 判断当前用户是否为要修改的用户
        if (!loginUser.getId().equals(user.getId()))
            throw new BusinessException(NO_AUTH);

        userMapper.updateById(user);
        return "修改信息成功";
    }

    /**
     * 管理员修改用户信息
     *
     * @param user 要修改的用户
     * @return 修改信息成功
     */
    @Override
    public String userUpdateByAdmin(User user) {
        userMapper.updateById(user);
        return "修改信息成功";
    }

    /**
     * 展示所有用户信息
     * Redis缓存
     * 分页查询
     *
     * @param currentPage 当前页
     * @param pageSize    每页显示数
     * @return 用户列表
     */
    @Override
    public Page<User> selectPage(long currentPage, long pageSize, HttpServletRequest request) {
        // 获取当前登录用户
        User loginUser = getLoginUser(request);
        // 拿到当前登录用户的key(每个用户都有各自对应的key)
        String redisKey = String.format("memory:user:recommend:%s", loginUser.getId());
        // 查缓存
        Page<User> userPage = (Page<User>) redisTemplate.opsForValue().get(redisKey);
        // 缓存未中, 则返回用户信息
        if (userPage != null) {
            return userPage;
        }
        // 缓存未命中, 查询数据库
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        userPage = userMapper.selectPage(new Page<>(currentPage, pageSize), lqw);
        // 将查询到的用户信息写到缓存中
        try {
            redisTemplate.opsForValue().set(redisKey, userPage, 30000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("redis set key error", e);
        }
        // 返回用户数据
        return userPage;
    }

    /**
     * 校验是否为管理员
     *
     * @param loginUser 校验的用户
     * @return 校验成功与否(t / f)
     */
    @Override
    public Boolean isAdmin(User loginUser) {
        //校验是否为管理员
        return loginUser != null && loginUser.getUserRole() == ADMIN_ROLE;
    }

    /**
     * 校验是否为管理员
     *
     * @param request request
     * @return 校验成功与否(t / f)
     */
    public Boolean isAdmin(HttpServletRequest request) {
        //校验是否为管理员
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return user != null && user.getUserRole() == ADMIN_ROLE;
    }

    /**
     * 获取当前登录用户
     *
     * @param request request
     * @return 脱敏后的用户信息
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        User loginUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return getSafetyUser(loginUser);
    }


    /**
     * 用户信息脱敏
     *
     * @param originUser 原始用户
     * @return 脱敏后的用户
     */
    public User getSafetyUser(User originUser) {
        if (originUser == null) return null;

        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        safetyUser.setIsDelete(originUser.getIsDelete());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setPlanetCode(originUser.getPlanetCode());
        safetyUser.setProfile(originUser.getProfile());
        safetyUser.setTags(originUser.getTags());

        return safetyUser;
    }


    /**
     * 根据标签查询用户(SQL查询数据库)
     *
     * @param tagNameList 打上的标签列表
     * @return 查询到的用户
     */
    @Deprecated
    private List<User> searchUserByTags2(List<String> tagNameList) {
        // 1.设置查询条件
        QueryWrapper<User> uqw = new QueryWrapper<>();
        // where tags like "..." and like "..." and ......
        for (String tagName : tagNameList) {
            uqw.like("tags", tagName);
        }
        // 2.查询到符合标签的用户
        List<User> userList = userMapper.selectList(uqw);
        // 3.用户信息脱敏
        return userList.stream().map(this::getSafetyUser).collect(Collectors.toList());
    }
}




