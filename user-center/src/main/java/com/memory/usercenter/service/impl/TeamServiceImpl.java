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
import com.memory.usercenter.model.request.team.TeamAddRequest;
import com.memory.usercenter.model.request.team.TeamQuery;
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
     * @param teamAddRequest 队伍
     * @return 队伍id
     */
    @Override
    @Transactional
    public String teamAdd(TeamAddRequest teamAddRequest, HttpServletRequest request) {
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest, team);

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

        // 6.如果 status 是加密状态，一定要有密码，且密码 <= 32
        String password = team.getPassword();
        if (TeamStatusEnum.SECRET.equals(statusEnum)) {
            if (StringUtils.isBlank(password) || password.length() > 32)
                throw new BusinessException(ErrorCode.PARMS_ERROR, "队伍密码不符合要求");
        }
        // 7.当前时间 > 超时时间
        Date expireTime = team.getExpireTime();
        if (new Date().after(expireTime))
            throw new BusinessException(ErrorCode.PARMS_ERROR, "超时时间不符合要求");

        // 8. 校验用户最多创建 5 个队伍
        Long userId = loginUser.getId();
        QueryWrapper<Team> lqw = new QueryWrapper<>();
        lqw.eq("user_id", userId);
        long count = this.count(lqw);
        if (count >= 5)
            throw new BusinessException(ErrorCode.PARMS_ERROR, "该用户创建队伍数量超出限制");

        // 9.插入队伍信息到队伍表
        team.setId(null);
        team.setUserId(userId);
        boolean teamSave = this.save(team);
        if (!teamSave) throw new BusinessException(ErrorCode.UPDATE_ERROR);

        // 10.插入用户 => 队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(team.getId());
        boolean userTeamSave = userTeamService.save(userTeam);
        if (!userTeamSave) throw new BusinessException(ErrorCode.UPDATE_ERROR);

        return "新增队伍成功";
    }

    /**
     * 查询队伍(分页查询)
     *
     * @param teamQuery 查询参数
     * @return 符合条件的队伍
     */
    @Override
    public Page<Team> teamList(TeamQuery teamQuery) {
        Team team = new Team();
        BeanUtils.copyProperties(teamQuery, team);
        QueryWrapper<Team> tqw = new QueryWrapper<>();
        // 根据队伍名查询
        String name = teamQuery.getName();
        if (StringUtils.isNotBlank(name) && name.length() <= 20)
            tqw.like("name", name);

        // 根据队伍描述查询
        String description = teamQuery.getDescription();
        if (StringUtils.isNotBlank(description) && description.length() <= 512)
            tqw.like("description", description);

        // 根据队长id查询
        Long userId = teamQuery.getUserId();
        if (userId != null && userId > 0)
            tqw.eq("user_id", userId);

        // 根据最大人数查询
        Integer maxNum = teamQuery.getMaxNum();
        if (maxNum != null && maxNum > 0)
            tqw.eq("max_num", maxNum);

        // 根据队伍状态查询
        Integer status = teamQuery.getStatus();
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if (statusEnum != null)
            tqw.like("status", status);

        return teamMapper.selectPage(new Page<>(1, 5), tqw);
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




