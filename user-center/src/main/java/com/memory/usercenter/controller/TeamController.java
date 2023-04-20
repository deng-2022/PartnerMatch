package com.memory.usercenter.controller;

import com.memory.usercenter.common.BaseResponse;
import com.memory.usercenter.common.ResultUtils;
import com.memory.usercenter.model.entity.Team;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 邓哈哈
 * 2023/4/20 9:39
 * Function:
 * Version 1.0
 */

@Controller
@RequestMapping("/team")
public class TeamController {
    @PostMapping("/add")
    public BaseResponse<Boolean> teamAdd() {
        return ResultUtils.success(true);
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
