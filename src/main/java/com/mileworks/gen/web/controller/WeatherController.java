package com.mileworks.gen.web.controller;

import com.mileworks.gen.common.controller.BaseController;
import com.mileworks.gen.common.domain.MKConstant;
import com.mileworks.gen.common.domain.MKResponse;
import com.mileworks.gen.common.exception.MKException;
import com.mileworks.gen.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@Slf4j
@Validated
@RestController
@RequestMapping("weather")
public class WeatherController extends BaseController {

    @GetMapping
    @RequiresPermissions("weather:view")
    public MKResponse queryWeather(@NotBlank(message = "{required}") String areaId) throws MKException {
        try {
            String data = HttpUtil.sendPost(MKConstant.MEIZU_WEATHER_URL, "cityIds=" + areaId);
            return new MKResponse().data(data);
        } catch (Exception e) {
            String message = "天气查询失败";
            log.error(message, e);
            throw new MKException(message);
        }
    }
}
