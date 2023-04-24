package com.memory.usercenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memory.usercenter.model.entity.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.memory.usercenter.model.entity.User;
import com.memory.usercenter.model.request.team.TeamAddRequest;
import com.memory.usercenter.model.request.team.TeamQuery;

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
     * @param teamAddRequest 新增队伍信息
     * @param request        request
     * @return 新增成功与否
     */
    String teamAdd(TeamAddRequest teamAddRequest, HttpServletRequest request);

    int teamDelete(long id);

    int teamUpdate(Team team);

    Team getTeam(long id);

    Page<Team> teamList(TeamQuery teamQuery);

    User getLoginUser(HttpServletRequest request);
}
