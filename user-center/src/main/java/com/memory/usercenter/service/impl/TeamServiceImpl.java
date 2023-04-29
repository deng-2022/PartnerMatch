package com.memory.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.memory.usercenter.common.ErrorCode;
import com.memory.usercenter.constant.TeamStatusEnum;
import com.memory.usercenter.exception.BusinessException;
import com.memory.usercenter.model.entity.Team;
import com.memory.usercenter.model.entity.User;
import com.memory.usercenter.model.entity.UserTeam;
import com.memory.usercenter.model.request.team.*;
import com.memory.usercenter.service.TeamService;
import com.memory.usercenter.service.UserTeamService;
import com.memory.usercenter.mapper.TeamMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.Optional;

import static com.memory.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.memory.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author Lenovo
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2023-04-20 09:27:14
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {
    @Resource
    private TeamMapper teamMapper;
    @Resource
    private UserTeamService userTeamService;

    /**
     * 新增队伍
     *
     * @param team 队伍
     * @return 队伍id
     */
    @Override
    @Transactional
    public String teamAdd(TeamAdd team, HttpServletRequest request) {
        // 1.是否登录，未登录不允许创建
        User loginUser = getLoginUser(request);
        if (loginUser == null)
            throw new BusinessException(ErrorCode.NOT_LOGIN);

        // 2.队伍人数 > 1 且 <= 20
        Integer maxNum = team.getMaxNum();
        if (maxNum < 1 || maxNum > 20)
            throw new BusinessException(ErrorCode.PARMS_ERROR, "队伍人数不符合要求");

        // 3.队伍标题 <= 20
        String name = team.getName();
        if (StringUtils.isBlank(name) || name.length() > 20)
            throw new BusinessException(ErrorCode.PARMS_ERROR, "队伍标题不符合要求");

        // 4.描述 <= 512
        String description = team.getDescription();
        if (StringUtils.isBlank(description) || description.length() > 512)
            throw new BusinessException(ErrorCode.PARMS_ERROR, "队伍描述不符合要求");

        // 5.status 是否公开（int）不传默认为 0（公开）
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (statusEnum == null)
            throw new BusinessException(ErrorCode.PARMS_ERROR, "队伍状态不符合要求");

        // 6.校验队伍密码
        String password = team.getPassword();
        // 6.1.如果队伍非加密, 则不允许设置密码
        if (statusEnum.getValue() != 2) {
            if (StringUtils.isNotBlank(password))
                throw new BusinessException(ErrorCode.PARMS_ERROR, "队伍密码不符合要求");
        }

        // 6.2.如果队伍加密, 一定要有密码, 且密码 <= 32
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || password.length() > 32)
                throw new BusinessException(ErrorCode.PARMS_ERROR, "队伍密码不符合要求");
        }

        // 7.当前时间 > 超时时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime))
            throw new BusinessException(ErrorCode.PARMS_ERROR, "超时时间不符合要求");

        // 8.校验用户已创建队伍数量(最多创建 5 个队伍)
        Long userId = loginUser.getId();
        QueryWrapper<Team> lqw = new QueryWrapper<>();
        lqw.eq("user_id", userId);
        long count = this.count(lqw);
        if (count >= 5)
            throw new BusinessException(ErrorCode.PARMS_ERROR, "该用户创建队伍数量超出限制");

        Team addTeam = new Team();
        BeanUtils.copyProperties(team, addTeam);

        // 9.插入队伍信息到team表
        addTeam.setId(null);
        addTeam.setUserId(userId);

        boolean teamSave = this.save(addTeam);
        if (!teamSave) throw new BusinessException(ErrorCode.UPDATE_ERROR);

        // 10.插入用户-队伍关系到user_team表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(addTeam.getId());
        userTeam.setJoinTime(new Date());

        boolean userTeamSave = userTeamService.save(userTeam);
        if (!userTeamSave) throw new BusinessException(ErrorCode.UPDATE_ERROR);

        return "新增队伍成功";
    }

    /**
     * 删除队伍
     *
     * @param id
     * @return
     */
    @Override
    public int teamDelete(long id) {
        return teamMapper.deleteById(id);
    }

    /**
     * 修改队伍
     *
     * @param team 队伍修改信息
     * @return 修改成功与否
     */
    @Override
    public String teamUpdate(TeamUpdate team, HttpServletRequest request) {
        // 校验是否登录
        User loginUser = getLoginUser(request);
        if (loginUser == null)
            throw new BusinessException(ErrorCode.NOT_LOGIN);

        // 1.校验修改权限(只有管理员或队长可以修改)
        if (!loginUser.getId().equals(team.getUserId()) && !isAdmin(loginUser))
            throw new BusinessException(ErrorCode.NO_AUTH, "非队长且非管理员");

        // 2.判断队伍是否存在
        Long teamId = team.getId();
        if (teamId == null || teamId < 0)
            throw new BusinessException(ErrorCode.PARMS_ERROR);

        Team currentTeam = this.getById(teamId);
        if (currentTeam == null)
            throw new BusinessException(ErrorCode.UPDATE_ERROR, "该队伍不存在");

        // 3.校验队伍名
        String name = team.getName();
        if (StringUtils.isBlank(name) || name.length() > 20)
            throw new BusinessException(ErrorCode.PARMS_ERROR, "队伍名不符合要求");

        // 4.校验队伍描述
        String description = team.getDescription();
        if (StringUtils.isBlank(description) || description.length() > 512)
            throw new BusinessException(ErrorCode.PARMS_ERROR, "队伍描述不符合要求");

        // 5.校验队伍状态
        Integer status = team.getStatus();
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (statusEnum == null)
            throw new BusinessException(ErrorCode.PARMS_ERROR, "队伍状态不符合要求");

        // 6.校验队伍密码
        String password = team.getPassword();
        // 6.1.如果队伍非加密, 则不允许设置密码
        if (!TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isNotBlank(password))
                throw new BusinessException(ErrorCode.PARMS_ERROR, "队伍密码不符合要求");
        }

        // 6.2.如果队伍加密, 一定要有密码, 且密码 <= 32
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || password.length() > 32)
                throw new BusinessException(ErrorCode.PARMS_ERROR, "队伍密码不符合要求");
        }

        // 7.更新队伍信息
        Team updateTeam = new Team();
        BeanUtils.copyProperties(team, updateTeam);

        boolean update = this.updateById(updateTeam);
        if (!update)
            throw new BusinessException(ErrorCode.PARMS_ERROR, "修改队伍失败");

        return "修改信息成功";
    }

    /**
     * 查询队伍(分页查询)
     *
     * @param team 查询参数
     * @return 符合条件的队伍
     */
    @Override
    public Page<Team> teamList(TeamQuery team, HttpServletRequest request) {
        // 登录校验
        User loginUser = getLoginUser(request);
        if (loginUser == null)
            throw new BusinessException(ErrorCode.NOT_LOGIN);

        QueryWrapper<Team> tqw = new QueryWrapper<>();

        // 1.根据队伍名查询
        String name = team.getName();
        if (StringUtils.isBlank(name) || name.length() > 20)
            throw new BusinessException(ErrorCode.PARMS_ERROR, "队伍名不符合要求");
        tqw.like("name", name);

        // 2.根据队伍描述查询
        String description = team.getDescription();
        if (StringUtils.isBlank(description) || description.length() > 512)
            throw new BusinessException(ErrorCode.PARMS_ERROR, "队伍描述不符合要求");
        tqw.like("description", description);

        // 3.根据队长id查询
        Long userId = team.getUserId();
        if (userId == null || userId < 0)
            throw new BusinessException(ErrorCode.PARMS_ERROR, "队长id不符合要求");
        tqw.eq("user_id", userId);

        // 4.根据最大人数查询
        Integer maxNum = team.getMaxNum();
        if (maxNum == null || maxNum < 0)
            throw new BusinessException(ErrorCode.PARMS_ERROR, "队伍最大人数不符合要求");
        tqw.eq("max_num", maxNum);

        // 5.根据队伍状态查询
        Integer status = team.getStatus();
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (statusEnum == null)
            throw new BusinessException(ErrorCode.PARMS_ERROR, "队伍状态不符合要求");
        tqw.like("status", status);

        // 6.分页查询
        Page<Team> teamPage = this.page(new Page<>(1, 5), tqw);
        if (teamPage == null)
            throw new BusinessException(ErrorCode.UPDATE_ERROR);

        return teamPage;
    }

    /**
     * 加入队伍
     *
     * @param team    加入队伍参数
     * @param request request
     * @return 加入队伍成功
     */
    @Transactional
    @Override
    public String joinTeam(TeamJoin team, HttpServletRequest request) {
        // 1.登录校验
        User loginUser = getLoginUser(request);
        if (loginUser == null)
            throw new BusinessException(ErrorCode.NOT_LOGIN);

        // 2.校验队伍是否存在
        Long teamId = team.getId();
        if (teamId == null || teamId < 0)
            throw new BusinessException(ErrorCode.PARMS_ERROR);

        Team currentTeam = this.getById(teamId);
        if (currentTeam == null)
            throw new BusinessException(ErrorCode.UPDATE_ERROR, "该队伍不存在");

        // 3.校验状态
        Integer status = team.getStatus();
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (statusEnum == null)
            throw new BusinessException(ErrorCode.PARMS_ERROR, "队伍状态不符合要求");

        String password = team.getPassword();
        // 4.1.加入加密队伍必须输入密码
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || password.length() > 32)
                throw new BusinessException(ErrorCode.PARMS_ERROR, "加入加密队伍要提供正确的密码");
        }

        // 4.2.加入公开队伍不需要密码用户加入公开队伍不需要密码
        if (TeamStatusEnum.PUBLIC.equals(statusEnum)) {
            if (StringUtils.isNotBlank(password))
                throw new BusinessException(ErrorCode.PARMS_ERROR, "加入公开队伍无需密码");
        }

        // 4.3.不能加入私有队伍
        if (TeamStatusEnum.PRIVATE.equals(statusEnum))
            throw new BusinessException(ErrorCode.PARMS_ERROR, "不能主动加入私有队伍");

        // 5.最多加入5个队伍
        QueryWrapper<UserTeam> utqw = new QueryWrapper<>();
        Long userId = loginUser.getId();
        utqw.eq("user_id", userId);
        long count = userTeamService.count(utqw);
        if (count >= 5)
            throw new BusinessException(ErrorCode.PARMS_ERROR, "该用户加入队伍已达上限");

        // 6.不能重复加入已加入的队伍
        utqw.eq("team_id", team.getId());
        count = userTeamService.count(utqw);
        if (count > 0)
            throw new BusinessException(ErrorCode.PARMS_ERROR, "您已在该队伍中");

        // 7.不能加入满员的队伍
        Integer joinNum = team.getJoinNum();
        if (joinNum >= team.getMaxNum())
            throw new BusinessException(ErrorCode.PARMS_ERROR, "该队伍已满员");

        // 8.更新team表队伍成员数量
        UpdateWrapper<Team> tuw = new UpdateWrapper<>();
        Long teamId = team.getId();
        tuw.eq("id", teamId).set("join_num", ++joinNum);
        boolean updateTeam = this.update(tuw);

        // 9.插入用户-队伍关系到user_team表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(teamId);
        userTeam.setJoinTime(new Date());

        boolean saveUserTeam = userTeamService.save(userTeam);

        if (!updateTeam || !saveUserTeam)
            throw new BusinessException(ErrorCode.UPDATE_ERROR, "用户加入队伍失败");

        return "加入队伍成功";
    }


    /**
     * 查询队伍
     *
     * @param id
     * @return
     */
    @Override
    public Team getTeam(long id) {
        return teamMapper.selectById(id);
    }

    @Override
    public String quitTeam(TeamQuit team, HttpServletRequest request) {
        // 1.校验登录
        User loginUser = getLoginUser(request);
        if (loginUser == null)
            throw new BusinessException(ErrorCode.NOT_LOGIN);

        // 2.校验队伍是否存在
        Long teamId = team.getId();
        if (teamId == null || teamId < 0)
            throw new BusinessException(ErrorCode.PARMS_ERROR);

        Team currentTeam = this.getById(teamId);
        if (currentTeam == null)
            throw new BusinessException(ErrorCode.UPDATE_ERROR, "该队伍不存在");

        // 3.校验用户状态
        Integer userStatus = loginUser.getUserStatus();
        if (userStatus == 1)
            throw new BusinessException(ErrorCode.NO_AUTH, "该账号已被封");

        // 4.校验队伍剩余人数
        Integer joinNum = team.getJoinNum();
        // 4.1.系统错误
        if (--joinNum < 0)
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "队伍人数为空");

        // 4.2.队伍人数未空
        if (--joinNum > 0) {
            UpdateWrapper<Team> tuw = new UpdateWrapper<>();
            tuw.eq("id", teamId).set("join_num", joinNum);

            // 5.校验用户是否为队长(传位)
            if (team.getUserId().equals(loginUser.getId())) {
                QueryWrapper<UserTeam> utqw = new QueryWrapper<>();
            }
        }

        // 6.删除队伍
        boolean remove = this.removeById(teamId);
        if (!remove)
            throw new BusinessException(ErrorCode.UPDATE_ERROR);

        return "退出队伍成功";
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
}




