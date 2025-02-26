package com.zaizi.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zaizi.model.dto.picture.PictureQueryRequest;
import com.zaizi.model.dto.picture.PictureUploadRequest;
import com.zaizi.model.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zaizi.model.entity.User;
import com.zaizi.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

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
    PictureVO uploadPicture(MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest, User loginUser);

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

}
