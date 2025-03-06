package com.zaizi.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zaizi.exception.BusinessException;
import com.zaizi.exception.ErrorCode;
import com.zaizi.exception.ThrowUtils;
import com.zaizi.mapper.SpaceMapper;
import com.zaizi.model.dto.space.SpaceAddRequest;
import com.zaizi.model.entity.Space;
import com.zaizi.model.entity.User;
import com.zaizi.model.enums.SpaceLevelEnum;
import com.zaizi.service.SpaceService;
import com.zaizi.service.UserService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.Optional;


@Service
public class SpaceServiceImpl extends ServiceImpl<SpaceMapper, Space> implements SpaceService{

    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private UserService userService;
    @Resource
    private RedissonClient redissonClient;


    /**
     * 空间鉴权
     */
    @Override
    public void checkSpaceAuth(User loginUser, Space space) {
        if(!space.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无空间权限");
        }
    }

    /**
     * 校验空间 校验空间名和空间等级
     */
    @Override
    public void validSpace(Space space, boolean add) {
        ThrowUtils.throwIf(space == null, ErrorCode.PARAMS_ERROR);
        // 获取空间等级
        String spaceName = space.getSpaceName();
        Integer spaceLevel = space.getSpaceLevel();
        SpaceLevelEnum spaceLevelEnum = SpaceLevelEnum.getEnumByValue(spaceLevel);
        // 新增空间
        if(add) {
            if(StrUtil.isBlank(spaceName)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间名不能为空");
            }
            if(spaceLevel == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间等级不能为空");
            }
        }
        // 修改空间
        if(StrUtil.isBlank(spaceName) && spaceName.length() > 30) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间名字过长");
        }
        if(spaceLevel != null && spaceLevelEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "空间等级不存在");
        }

    }

    /**
     * 自动填充空间限额
     */
    @Override
    public void fillSpaceBySpaceLevel(Space space) {
        SpaceLevelEnum spaceLevelEnum = SpaceLevelEnum.getEnumByValue(space.getSpaceLevel());
        if(spaceLevelEnum != null) {
            long maxCount = spaceLevelEnum.getMaxCount();
            if(space.getMaxCount() == null) {
                space.setMaxCount(maxCount);
            }
            long maxSize = spaceLevelEnum.getMaxSize();
            if(space.getMaxSize() == null) {
                space.setMaxSize(maxSize);
            }
        }
    }

    /**
     * 创建空间
     */
    @Override
    public long addSpace(SpaceAddRequest spaceAddRequest, User loginUser) {
        // 转space
        Space space = new Space();
        BeanUtils.copyProperties(spaceAddRequest, space);
        // 设置默认值
        if(StrUtil.isBlank(spaceAddRequest.getSpaceName())) {
            space.setSpaceName("默认空间");
        }
        if(spaceAddRequest.getSpaceLevel() == null) {
            space.setSpaceLevel(SpaceLevelEnum.COMMON.getValue());
        }
        // 填充空间 校验
        fillSpaceBySpaceLevel(space);
        validSpace(space, true);
        Long userId = loginUser.getId();
        space.setUserId(userId);
        // 鉴权
        if(SpaceLevelEnum.COMMON.getValue() != spaceAddRequest.getSpaceLevel() && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 加锁 redisson 分布式锁
        RLock spaceLock = redissonClient.getLock("space_lock:" + userId);
        spaceLock.lock();
        try {
            Long newSpaceId = transactionTemplate.execute(status -> {
                boolean exists = this.lambdaQuery()
                        .eq(Space::getUserId, userId).exists();
                ThrowUtils.throwIf(exists, ErrorCode.OPERATION_ERROR, "每个用户只能拥有一个空间");
                // 操作数据库
                boolean res = this.save(space);
                ThrowUtils.throwIf(!res, ErrorCode.OPERATION_ERROR);
                return space.getId();
            });
            return Optional.ofNullable(newSpaceId).orElse(-1L);
        } finally {
            spaceLock.unlock();
        }

    }



}




