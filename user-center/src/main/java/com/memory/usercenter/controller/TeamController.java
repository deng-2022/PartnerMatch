package com.memory.usercenter.controller;

import com.memory.usercenter.common.BaseResponse;
import com.memory.usercenter.common.ErrorCode;
import com.memory.usercenter.common.ResultUtils;
import com.memory.usercenter.exception.BusinessException;
import com.memory.usercenter.model.entity.Team;
import com.memory.usercenter.model.request.TeamAddRequest;
import com.memory.usercenter.service.TeamService;
import org.springframework.stereotype.Controller;
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
     * @param teamAddRequest 新增队伍信息
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

    @GetMapping("/list/page")
    public BaseResponse<List<Team>> teamList() {
        return ResultUtils.success(new ArrayList<Team>());
    }

}
