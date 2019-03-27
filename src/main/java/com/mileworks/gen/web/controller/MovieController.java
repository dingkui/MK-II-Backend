package com.mileworks.gen.web.controller;

import com.mileworks.gen.common.domain.MKConstant;
import com.mileworks.gen.common.domain.MKResponse;
import com.mileworks.gen.common.exception.MKException;
import com.mileworks.gen.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@Slf4j
@Validated
@RestController
@RequestMapping("movie")
public class MovieController {

    private String message;

    @GetMapping("hot")
    public MKResponse getMoiveHot() throws MKException {
        try {
            String data = HttpUtil.sendSSLPost(MKConstant.TIME_MOVIE_HOT_URL, "locationId=328");
            return new MKResponse().data(data);
        } catch (Exception e) {
            message = "获取热映影片信息失败";
            log.error(message, e);
            throw new MKException(message);
        }
    }

    @GetMapping("coming")
    public MKResponse getMovieComing() throws MKException {
        try {
            String data = HttpUtil.sendSSLPost(MKConstant.TIME_MOVIE_COMING_URL, "locationId=328");
            return new MKResponse().data(data);
        } catch (Exception e) {
            message = "获取即将上映影片信息失败";
            log.error(message, e);
            throw new MKException(message);
        }
    }

    @GetMapping("detail")
    public MKResponse getDetail(@NotBlank(message = "{required}") String id) throws MKException {
        try {
            String data = HttpUtil.sendSSLPost(MKConstant.TIME_MOVIE_DETAIL_URL, "locationId=328&movieId=" + id);
            return new MKResponse().data(data);
        } catch (Exception e) {
            message = "获取影片详情失败";
            log.error(message, e);
            throw new MKException(message);
        }
    }

    @GetMapping("comments")
    public MKResponse getComments(@NotBlank(message = "{required}") String id) throws MKException {
        try {
            String data = HttpUtil.sendSSLPost(MKConstant.TIME_MOVIE_COMMENTS_URL, "movieId=" + id);
            return new MKResponse().data(data);
        } catch (Exception e) {
            message = "获取影片评论失败";
            log.error(message, e);
            throw new MKException(message);
        }
    }
}
