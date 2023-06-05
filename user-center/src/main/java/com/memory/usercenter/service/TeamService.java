package com.memory.usercenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memory.usercenter.model.entity.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.memory.usercenter.model.entity.User;
import com.memory.usercenter.model.request.team.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /**
     * 修改队伍
     *
     * @param team    修改队伍参数
     * @param request request
     * @return 修改成功游戏
     */
    String teamUpdate(TeamUpdate team, HttpServletRequest request);

    /**
     * 查询队伍
     * 分页查询
     *
     * @param teamQuery 查询队伍参数
     * @return 队伍列表
     */
    Page<Team> teamList(TeamQuery teamQuery, HttpServletRequest request);

    /**
     * 加入队伍
     *
     * @param team    加入队伍参数
     * @param request request
     * @return 加入队伍成功
     */
    String joinTeam(TeamJoin team, HttpServletRequest request);

    /**
     * 退出队伍
     *
     * @return 退出成功与否
     */
    String quitTeam(TeamQuit team, HttpServletRequest request);

    /**
     * 解散队伍
     *
     * @return 删除成功与否
     */
    String deleteTeam(TeamDelete teamDelete, HttpServletRequest request);

    /**
     * 获取当前队伍信息
     *
     * @param teamId 队伍id
     * @return 队伍信息
     */
    Team getTeam(Long teamId, HttpServletRequest request);

    /**
     * 获取已加入队伍信息
     *
     * @param userId 用户id
     * @return 队伍信息
     */
    List<Team> getJoinedTeam(Long userId, HttpServletRequest request);

    /**
     * 获取已创建队伍信息
     *
     * @param userId 用户id
     * @return 队伍信息
     */
    List<Team> getCreatedTeam(Long userId, HttpServletRequest request);

    Boolean isAdmin(User loginUser);

    User getLoginUser(HttpServletRequest request);

}
