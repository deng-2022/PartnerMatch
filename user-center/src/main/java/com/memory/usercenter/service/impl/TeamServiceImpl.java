package com.memory.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.memory.usercenter.common.ErrorCode;
import com.memory.usercenter.constant.TeamStatusEnum;
import com.memory.usercenter.exception.BusinessException;
import com.memory.usercenter.model.entity.Team;
import com.memory.usercenter.model.entity.User;
import com.memory.usercenter.model.entity.UserTeam;
import com.memory.usercenter.model.request.TeamQuery;
import com.memory.usercenter.service.TeamService;
import com.memory.usercenter.service.UserTeamService;
import generator.mapper.TeamMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.Optional;

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
    public boolean teamAdd(Team team, HttpServletRequest request) {
//        1. 请求参数是否为空？
        if (team == null)
            throw new BusinessException(ErrorCode.PARMS_ERROR);

//        2. 是否登录，未登录不允许创建
        User loginUser = getLoginUser(request);
        if (loginUser == null)
            throw new BusinessException(ErrorCode.NOT_REGISTER);

//        3. 队伍人数 > 1 且 <= 20
        Integer maxNum = team.getMaxNum();
        if (maxNum < 1 || maxNum > 20)
            throw new BusinessException("队伍人数不符合要求", 70001, "");

//        b. 队伍标题 <= 20
        String name = team.getName();
        if (StringUtils.isBlank(name) || name.length() > 20)
            throw new BusinessException("队伍标题不符合要求", 70002, "");

//        c. 描述 <= 512
        String description = team.getDescription();
        if (StringUtils.isBlank(description) || description.length() > 512)
            throw new BusinessException("队伍描述不符合要求", 70003, "");

//        d. status 是否公开（int）不传默认为 0（公开）
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (statusEnum == null)
            throw new BusinessException("队伍状态不符合要求", 70004, "");

//        e. 如果 status 是加密状态，一定要有密码，且密码 <= 32
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || password.length() > 32)
                throw new BusinessException("队伍密码不符合要求", 70005, "");
        }
//        f.   当前时间 > 超时时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime))
            throw new BusinessException("超时时间不符合要求", 70006, "");

//        g. 校验用户最多创建 5 个队伍
        Long userId = team.getUserId();
        QueryWrapper<Team> lqw = new QueryWrapper<>();
        lqw.eq("user_id", userId);
        long count = this.count(lqw);
        if (count >= 5)
            throw new BusinessException("无法创建新的队伍", 70007, "该用户创建队伍数量超出限制");

//        4. 插入队伍信息到队伍表
        team.setId(null);
        team.setUserId(userId);
        boolean teamSave = this.save(team);

//        5. 插入用户 => 队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(team.getId());
        boolean userTeamSave = userTeamService.save(userTeam);

        return teamSave && userTeamSave;
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
     * @param team
     * @return
     */
    @Override
    public int teamUpdate(Team team) {
        return teamMapper.updateById(team);
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

    /**
     * 列举队伍
     *
     * @param teamQuery
     * @param current
     * @param pageSize
     * @return
     */
    @Override
    public Page<Team> teamList(TeamQuery teamQuery, long current, long pageSize) {
        Team team = new Team();
        BeanUtils.copyProperties(team, teamQuery);
        LambdaQueryWrapper<Team> lqw = new LambdaQueryWrapper<>(team);
        return teamMapper.selectPage(new Page<>(current, pageSize), lqw);
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




