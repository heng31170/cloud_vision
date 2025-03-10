package com.zaizi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zaizi.model.dto.space.SpaceAddRequest;
import com.zaizi.model.dto.space.SpaceQueryRequest;
import com.zaizi.model.entity.Space;
import com.zaizi.model.entity.User;
import com.zaizi.model.vo.SpaceVO;

/**
* @author 8618655416913
* @description 针对表【space(空间)】的数据库操作Service
* @createDate 2025-03-02 15:27:07
*/
public interface SpaceService extends IService<Space> {

    /**
     * 分页获取空间列表（封装
     */
    Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage);

    /**
     * 获取空间封装（单个
     */
    SpaceVO getSpaceVO(Space space);

    /**
     * 获取查询条件
     */
    QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest);

    /**
     * 空间鉴权
     */
    void checkSpaceAuth(User loginUser, Space space);

    /**
     * 校验空间 空间名和等级
     */
    void validSpace(Space space, boolean add);

    /**
     * 自动填充空间限额
     */
    void fillSpaceBySpaceLevel(Space space);

    /**
     * 创建空间
     */
    long addSpace(SpaceAddRequest spaceAddRequest, User loginUser);

}
