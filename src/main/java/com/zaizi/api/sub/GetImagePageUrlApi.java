package com.zaizi.api.sub;


import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.zaizi.exception.BusinessException;
import com.zaizi.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GetImagePageUrlApi {

    /**
     * 获取页面地址
     */
    public static String getImagePageUrl(String imageUrl) {
        // 1. 准备请求参数
        Map<String, Object> formDate = new HashMap<>();
        formDate.put("image",imageUrl);
        formDate.put("tn","pc");
        //formDate.put("from","pc");
        formDate.put("image_source","PC_UPLOAD_URL");
        long uptime = System.currentTimeMillis();
        String url = "https://graph.baidu.com/upload?uptime=" + uptime;

        try {
            // 2. 发送请求
            HttpResponse httpResponse = HttpRequest.post(url)
                    .form(formDate)
                    .timeout(5000)
                    .execute();
            // 是否响应
            if(HttpStatus.HTTP_OK != httpResponse.getStatus()) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "响应失败");
            }
            // 解析响应
            String responseBody = httpResponse.body();
            Map<String,Object> res = JSONUtil.toBean(responseBody, Map.class);
            // 处理响应
            if(res == null || !Integer.valueOf(0).equals(res.get("status"))) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "响应处理失败");
            }
            Map<String, Object> data = (Map<String, Object>) res.get("data");
            String rawUrl = (String) data.get("url");
            // 解析url
            String searchResult = URLUtil.decode(rawUrl, StandardCharsets.UTF_8);
            if(searchResult == null) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "无返回结果");
            }
            return searchResult;
        } catch (Exception e) {
            log.error("搜索失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "搜索失败");
        }

    }

    public static void main(String[] args) {
        // 测试搜图功能
        String imageUrl = "https://www.codefather.cn/logo.png";
        String res = getImagePageUrl(imageUrl);
        System.out.println("搜索成功，结果url:" + res);
    }

}
