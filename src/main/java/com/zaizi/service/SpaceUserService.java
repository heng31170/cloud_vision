package com.zaizi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zaizi.model.dto.spaceuser.SpaceUserAddRequest;
import com.zaizi.model.dto.spaceuser.SpaceUserQueryRequest;
import com.zaizi.model.entity.SpaceUser;
import com.zaizi.model.vo.spaceuser.SpaceUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SpaceUserService extends IService<SpaceUser> {

    /**
     * 获取空间列表（封装
     */
    List<SpaceUserVO> getSpaceUserVOList(List<SpaceUser> spaceUserPage);

    /**
     * 获取空间封装（单个
     */
    SpaceUserVO getSpaceUserVO(SpaceUser spaceUser, HttpServletRequest request);

    /**
     * 获取查询条件
     */
    QueryWrapper<SpaceUser> getQueryWrapper(SpaceUserQueryRequest spaceUserQueryRequest);


    /**
     * 校验空间成员
     */
    void validSpaceUser(SpaceUser spaceUser, boolean add);


    /**
     * 创建空间
     */
    long addSpaceUser(SpaceUserAddRequest spaceUserAddRequest);

}
