package com.zaizi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zaizi.model.dto.picture.PictureQueryRequest;
import com.zaizi.model.dto.picture.PictureReviewRequest;
import com.zaizi.model.dto.picture.PictureUploadByBatchRequest;
import com.zaizi.model.dto.picture.PictureUploadRequest;
import com.zaizi.model.entity.Picture;
import com.zaizi.model.entity.User;
import com.zaizi.model.vo.PictureVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 8618655416913
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2025-02-25 15:16:11
*/
public interface PictureService extends IService<Picture> {

    /**
     * 上传图片
     */
    PictureVO uploadPicture(Object inputSource, PictureUploadRequest pictureUploadRequest, User loginUser);

    /**
     * 获取查询条件
     */
    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);

    /**
     * 获取图片封装(单条)
     */
    PictureVO getPictureVO(Picture picture, HttpServletRequest request);

    /**
     * 分页获取图片封装(列表)
     */
    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage, HttpServletRequest request);

    /**
     * 图片校验
     */
    void validPicture(Picture picture);

    /**
     * 图片审核
     */
    void doPictureReview(PictureReviewRequest pictureReviewRequest, User loginUser);

    /**
     * 填充审核餐宿
     */
    void fillReviewParams(Picture picture, User loginUser);

    /**
     * 批量抓取、创建图片
     */
    int uploadPictureByBatch(PictureUploadByBatchRequest pictureUploadByBatchRequest, User loginUser);

}
