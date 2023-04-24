package com.memory.usercenter.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.memory.usercenter.common.BaseResponse;
import com.memory.usercenter.common.ErrorCode;
import com.memory.usercenter.common.ResultUtils;
import com.memory.usercenter.exception.BusinessException;
import com.memory.usercenter.model.entity.Team;
import com.memory.usercenter.model.request.team.TeamAddRequest;
import com.memory.usercenter.model.request.team.TeamQuery;
import com.memory.usercenter.service.TeamService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
     * @param teamAddRequest 新增队伍参数
     * @param request        request
     * @return 新增成功与否
     */
    @PostMapping("/add")
    public BaseResponse<String> teamAdd(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
        // controller对参数的校验
        if (teamAddRequest == null)
            throw new BusinessException(ErrorCode.PARMS_ERROR);

        String teamAdd = teamService.teamAdd(teamAddRequest, request);
        return ResultUtils.success(teamAdd);
    }

    @DeleteMapping("/delete")
    public BaseResponse<Boolean> teamDelete() {
        return ResultUtils.success(true);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> teamUpdate() {
        return ResultUtils.success(true);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeam() {
        return ResultUtils.success(new Team());
    }

    /**
     * 查询队伍
     * 分页查询
     *
     * @param teamQuery 查询队伍参数
     * @return 队伍列表
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> teamList(TeamQuery teamQuery) {
        // controller对参数的校验

        Page<Team> teamPage = teamService.teamList(teamQuery);
        return ResultUtils.success(teamPage);
    }

}
