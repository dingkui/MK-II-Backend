package com.mileworks.gen.common.handler;

import com.mileworks.gen.common.domain.MKResponse;
import com.mileworks.gen.common.exception.LimitAccessException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.mileworks.gen.common.exception.MKException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Set;

@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MKResponse handleException(Exception e) {
        log.error("系统内部异常，异常信息：", e);
        return new MKResponse().message("系统内部异常");
    }

    @ExceptionHandler(value = MKException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MKResponse handleParamsInvalidException(MKException e) {
        log.error("系统错误：{}", e.getMessage());
        return new MKResponse().message(e.getMessage());
    }

    /**
     * 统一处理请求参数校验(实体对象传参)
     *
     * @param e BindException
     * @return MKResponse
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MKResponse validExceptionHandler(BindException e) {
        StringBuilder message = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(StringPool.COMMA);
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return new MKResponse().message(message.toString());

    }

    /**
     * 统一处理请求参数校验(普通传参)
     *
     * @param e ConstraintViolationException
     * @return MKResponse
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MKResponse handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), StringPool.DOT);
            message.append(pathArr[1]).append(violation.getMessage()).append(StringPool.COMMA);
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return new MKResponse().message(message.toString());
    }

    @ExceptionHandler(value = LimitAccessException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public MKResponse handleLimitAccessException(LimitAccessException e) {
        log.warn(e.getMessage());
        return new MKResponse().message(e.getMessage());
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handleUnauthorizedException(Exception e) {
        log.error("权限不足，{}", e.getMessage());
    }
}
