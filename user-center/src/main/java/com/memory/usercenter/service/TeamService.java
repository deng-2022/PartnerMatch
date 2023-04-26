package com.memory.usercenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memory.usercenter.model.entity.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.memory.usercenter.model.entity.User;
import com.memory.usercenter.model.request.team.TeamAdd;
import com.memory.usercenter.model.request.team.TeamQuery;
import com.memory.usercenter.model.request.team.TeamUpdate;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lenovo
 * @description 针对表【team(队伍)】的数据库操作Service
 * @createDate 2023-04-20 09:27:14
 */
public interface TeamService extends IService<Team> {
    /**
     * 新增队伍
     *
     * @param team    新增队伍信息
     * @param request request
     * @return 新增成功与否
     */
    String teamAdd(TeamAdd team, HttpServletRequest request);

    int teamDelete(long id);

    /**
     * 修改队伍
     *
     * @param team    修改队伍参数
     * @param request request
     * @return 修改成功游戏
     */
    String teamUpdate(TeamUpdate team, HttpServletRequest request);

    Team getTeam(long id);

    /**
     * 查询队伍
     * 分页查询
     *
     * @param teamQuery 查询队伍参数
     * @return 队伍列表
     */
    Page<Team> teamList(TeamQuery teamQuery, HttpServletRequest request);

    Boolean isAdmin(User loginUser);

    User getLoginUser(HttpServletRequest request);
}
