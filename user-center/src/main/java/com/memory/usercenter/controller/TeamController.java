package com.memory.usercenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memory.usercenter.common.BaseResponse;
import com.memory.usercenter.common.ErrorCode;
import com.memory.usercenter.common.ResultUtils;
import com.memory.usercenter.exception.BusinessException;
import com.memory.usercenter.model.entity.Team;
import com.memory.usercenter.model.request.team.TeamAdd;
import com.memory.usercenter.model.request.team.TeamQuery;
import com.memory.usercenter.model.request.team.TeamUpdate;
import com.memory.usercenter.service.TeamService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

    @DeleteMapping("/delete")
    public BaseResponse<Boolean> teamDelete() {
        return ResultUtils.success(true);
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

    @GetMapping("/get")
    public BaseResponse<Team> getTeam() {
        return ResultUtils.success(new Team());
    }
}
