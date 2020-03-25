package com.haoge.shijie.exception;


import com.haoge.shijie.pojo.response.ResponseBean;
import com.haoge.shijie.service.VideoService;
import org.apache.shiro.ShiroException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private VideoService videoService;

    // Log4j日志处理(@author: rico)
    //private static final Logger log = Logger.getLogger(GlobalExceptionHandler.class);

    /**
     * 判断是否是ajax请求
     *
     * @param httpServletRequest
     * @return boolean is ajax
     */
    public static boolean isAjax(HttpServletRequest httpServletRequest) {
        return (httpServletRequest.getHeader("X-Requested-With") != null &&
                "XMLHttpRequest".equals(httpServletRequest.getHeader("X-Requested-With").toString()));
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public @ResponseBody
    ResponseBean handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        // log.error("could_not_read_json...", e);
        // return new response().failure("无法读取JSON");
        return new ResponseBean(431, "unable to read JSON", null);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public @ResponseBody
    ResponseBean handleValidationException(MethodArgumentNotValidException e) {
        // log.error("parameter_validation_exception...", e);
        // return new response().failure("参数验证异常");
        System.out.println("参数验证异常");
        return new ResponseBean(400, "parameter validation exception", null);
    }

    /**
     * 405 - Method Not Allowed。HttpRequestMethodNotSupportedException
     * 是ServletException的子类,需要Servlet API支持
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseBean handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        // log.error("request_method_not_supported...", e);
        //  return new response().failure("不支持请求方法或请求方法错误");
        return new ResponseBean(405, "request method not supported", null);
    }

    /**
     * 415 - Unsupported Media Type。HttpMediaTypeNotSupportedException
     * 是ServletException的子类,需要Servlet API支持
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    public @ResponseBody
    ResponseBean handleHttpMediaTypeNotSupportedException(Exception e) {
        //   return new response().failure("不支持内容类型");
        return new ResponseBean(415, "content type not supported", null);
    }

    // 捕捉shiro的异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public @ResponseBody
    ResponseBean handle401(ShiroException e) {
        return new ResponseBean(401, e.getMessage(), null);
    }

    // 捕捉其他所有异常

    // 捕捉UnauthorizedException
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public @ResponseBody
    ResponseBean handle401(UnauthorizedException e) {
        //权限不足异常
        return new ResponseBean(401, "Unauthorized", null);
    }

    /**
     * 500 - Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ResponseBean globalException(HttpServletRequest request, Exception ex) {
        if (ex.getMessage().equals("Required request part 'coverfile' is not present")) {
            boolean success = videoService.modifyVideo(request);
            if (success) {
                return new ResponseBean().successMethod();
            }
        }
        return new ResponseBean().failMethod(getStatus(request).value(), ex.getMessage());
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}
