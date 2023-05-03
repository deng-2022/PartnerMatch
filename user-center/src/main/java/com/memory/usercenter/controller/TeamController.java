package com.memory.usercenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memory.usercenter.common.BaseResponse;
import com.memory.usercenter.common.ErrorCode;
import com.memory.usercenter.common.ResultUtils;
import com.memory.usercenter.exception.BusinessException;
import com.memory.usercenter.model.entity.Team;
import com.memory.usercenter.model.entity.User;
import com.memory.usercenter.model.request.team.*;
import com.memory.usercenter.service.TeamService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 邓哈哈
 * 2023/4/20 9:39
 * Function:
 * Version 1.0
 */

@RestController
@RequestMapping("/team")
public class TeamController {
    @Resource
    private TeamService teamService;

    /**
     * 新增队伍
     *
     * @param team    新增队伍参数
     * @param request request
     * @return 新增成功与否
     */
    @PostMapping("/add")
    public BaseResponse<String> teamAdd(@RequestBody TeamAdd team, HttpServletRequest request) {
        // controller对参数的校验
        if (team == null)
            throw new BusinessException(ErrorCode.PARMS_ERROR);

        String teamAdd = teamService.teamAdd(team, request);
        return ResultUtils.success(teamAdd);
    }

    /**
     * 修改队伍
     *
     * @param team    修改队伍参数
     * @param request request
     * @return 修改成功游戏
     */
    @PostMapping("/update")
    public BaseResponse<String> teamUpdate(@RequestBody TeamUpdate team, HttpServletRequest request) {
        // controller对参数的校验
        if (team == null)
            throw new BusinessException(ErrorCode.PARMS_ERROR);

        String teamUpdate = teamService.teamUpdate(team, request);
        return ResultUtils.success(teamUpdate);
    }

    /**
     * 查询队伍
     * 分页查询
     *
     * @param team 查询队伍参数
     * @return 队伍列表
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> teamList(TeamQuery team, HttpServletRequest request) {
        // controller对参数的校验
        if (team == null)
            throw new BusinessException(ErrorCode.PARMS_ERROR);

        Page<Team> teamPage = teamService.teamList(team, request);
        return ResultUtils.success(teamPage);
    }

    /**
     * 加入队伍
     *
     * @param team    加入队伍参数
     * @param request request
     * @return 加入队伍成功
     */
    @PostMapping("/join")
    public BaseResponse<String> joinTeam(@RequestBody TeamJoin team, HttpServletRequest request) {
        // controller对参数的校验
        if (team == null)
            throw new BusinessException(ErrorCode.PARMS_ERROR);

        String joinTeam = teamService.joinTeam(team, request);
        return ResultUtils.success(joinTeam);
    }

    /**
     * 退出队伍
     *
     * @param team    退出队伍参数
     * @param request request
     * @return 退出队伍成功
     */
    @PostMapping("/quit")
    public BaseResponse<String> quitTeam(@RequestBody TeamQuit team, HttpServletRequest request) {
        // controller对参数的校验
        if (team == null)
            throw new BusinessException(ErrorCode.PARMS_ERROR);

        String joinTeam = teamService.quitTeam(team, request);
        return ResultUtils.success(joinTeam);
    }

    /**
     * 解散队伍
     *
     * @param team    解散队伍参数
     * @param request request
     * @return 解散成功与否
     */
    @PostMapping("/delete")
    public BaseResponse<String> deleteTeam(@RequestBody TeamDelete team, HttpServletRequest request) {
        // controller对参数的校验
        if (team == null) {
            throw new BusinessException(ErrorCode.PARMS_ERROR);
        }

        String deleteTeam = teamService.deleteTeam(team, request);
        return ResultUtils.success(deleteTeam);
    }

    /**
     * 获取当前队伍信息
     *
     * @param teamId 队伍id
     * @return 队伍信息
     */
    @GetMapping("/one")
    public BaseResponse<Team> getTeam(Long teamId, HttpServletRequest request) {
        // controller对参数的校验
        if (teamId == null) {
            throw new BusinessException(ErrorCode.PARMS_ERROR);
        }

        Team team = teamService.getTeam(teamId, request);
        return ResultUtils.success(team);
    }

    /**
     * 获取当前队伍信息
     *
     * @param userId 用户id
     * @return 队伍信息
     */
    @GetMapping("/joined")
    public BaseResponse<List<Team>> getJoinedTeam(Long userId, HttpServletRequest request) {
        // controller对参数的校验
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARMS_ERROR);
        }

        List<Team> joinedTeam = teamService.getJoinedTeam(userId, request);
        return ResultUtils.success(joinedTeam);
    }
}
