package com.zaizi.controller;


import com.zaizi.annotation.AuthCheck;
import com.zaizi.common.BaseResponse;
import com.zaizi.common.DeleteRequest;
import com.zaizi.common.ResultUtils;
import com.zaizi.constant.UserConstant;
import com.zaizi.exception.BusinessException;
import com.zaizi.exception.ErrorCode;
import com.zaizi.exception.ThrowUtils;
import com.zaizi.model.dto.space.SpaceAddRequest;
import com.zaizi.model.dto.space.SpaceEditRequest;
import com.zaizi.model.dto.space.SpaceLevel;
import com.zaizi.model.dto.space.SpaceUpdateRequest;
import com.zaizi.model.entity.Space;
import com.zaizi.model.entity.User;
import com.zaizi.model.enums.SpaceLevelEnum;
import com.zaizi.service.SpaceService;
import com.zaizi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/space")
public class SpaceController {

    @Resource
    private SpaceService spaceService;
    @Resource
    private UserService userService;

    /**
     * 创建空间
     */
    @PostMapping("/add")
    public BaseResponse<Long> addSpace(@RequestBody SpaceAddRequest spaceAddRequest,
                                          HttpServletRequest request) {
        ThrowUtils.throwIf(spaceAddRequest == null, ErrorCode.PARAMS_ERROR);
        // 获取用户信息
        User loginUser = userService.getLoginUser(request);
        long res = spaceService.addSpace(spaceAddRequest,loginUser);
        return ResultUtils.success(res);
    }

    /**
     * 删除空间
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteSpace(@RequestBody DeleteRequest deleteRequest,
                                             HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取用户
        User loginUser = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断空间是否存在
        Space space = spaceService.getById(id);
        ThrowUtils.throwIf(space == null, ErrorCode.OPERATION_ERROR);
        // 鉴权
        spaceService.checkSpaceAuth(loginUser, space);
        // 操作
        boolean res = spaceService.removeById(deleteRequest);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 编辑空间 仅能编辑名字
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editSpace(@RequestBody SpaceEditRequest spaceEditRequest,
                                           HttpServletRequest request) {
        if (spaceEditRequest == null || spaceEditRequest.getId() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 转space
        Space space = new Space();
        BeanUtils.copyProperties(spaceEditRequest, space);
        // 编辑时间
        space.setEditTime(new Date());
        // 校验
        spaceService.validSpace(space, false);
        // 判断空间是否存在
        long id = spaceEditRequest.getId();
        Space oldSpace = spaceService.getById(id);
        ThrowUtils.throwIf(oldSpace == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人和管理员能编辑
        User loginUser = userService.getLoginUser(request);
        spaceService.checkSpaceAuth(loginUser, space);
        // 操作数据库
        boolean res = spaceService.updateById(space);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }


    /**
     * 更新空间 等级、名字，仅管理员可用
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateSpace(@RequestBody SpaceUpdateRequest spaceUpdateRequest) {
        if (spaceUpdateRequest == null || spaceUpdateRequest.getId() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 转space
        Space space = new Space();
        BeanUtils.copyProperties(spaceUpdateRequest, space);
        // 自动填充
        spaceService.fillSpaceBySpaceLevel(space);
        // 校验
        spaceService.validSpace(space, false);
        // 是否存在
        long id = spaceUpdateRequest.getId();
        Space oldSpace = spaceService.getById(id);
        ThrowUtils.throwIf(oldSpace == null, ErrorCode.NOT_FOUND_ERROR);
        // 操作数据库
        boolean res = spaceService.updateById(space);
        ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 查询空间级别列表
     */
    @GetMapping("/list/level")
    public BaseResponse<List<SpaceLevel>> listSpaceLevel() {
        // 获取所有枚举类
        List<SpaceLevel> spaceLevelList = Arrays.stream(SpaceLevelEnum.values())
                .map(spaceLevelEnum -> new SpaceLevel(
                        spaceLevelEnum.getValue(),
                        spaceLevelEnum.getText(),
                        spaceLevelEnum.getMaxCount(),
                        spaceLevelEnum.getMaxSize()
                )).collect(Collectors.toList());
        return ResultUtils.success(spaceLevelList);
    }

}
