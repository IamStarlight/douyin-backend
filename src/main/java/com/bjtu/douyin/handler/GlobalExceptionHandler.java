package com.bjtu.douyin.handler;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import com.bjtu.douyin.exception.ServiceException;
import com.bjtu.douyin.model.Result;
import jakarta.validation.ConstraintViolationException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 如果抛出的是ServiceException,则调用该方法
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public ResponseEntity<Result> handle(ServiceException e) {
        int code = e.getCode();
        if (code == HttpStatus.OK.value())
            //200
            return new ResponseEntity<>(Result.error(e.getCode(), e.getMessage()), HttpStatus.OK);
        else if (code == HttpStatus.BAD_REQUEST.value())
            //400
            return new ResponseEntity<>(Result.error(e.getCode(), e.getMessage()), HttpStatus.BAD_REQUEST);
        else if (code == HttpStatus.UNAUTHORIZED.value())
            //401
            return new ResponseEntity<>(Result.error(e.getCode(), e.getMessage()), HttpStatus.UNAUTHORIZED);
        else if (code == HttpStatus.FORBIDDEN.value())
            //403
            return new ResponseEntity<>(Result.error(e.getCode(), e.getMessage()), HttpStatus.FORBIDDEN);
        else if (code == HttpStatus.NOT_FOUND.value())
            //404
            return new ResponseEntity<>(Result.error(e.getCode(), e.getMessage()), HttpStatus.NOT_FOUND);
        else if (code == HttpStatus.INTERNAL_SERVER_ERROR.value())
            //500
            return new ResponseEntity<>(Result.error(e.getCode(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        else
            //501
            return new ResponseEntity<>(Result.error(e.getCode(), e.getMessage()), HttpStatus.NOT_IMPLEMENTED);
    }

    /**
     * 普通参数(非 java bean)校验出错时抛出 ConstraintViolationException 异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<Result> handle(ConstraintViolationException e) {
        return ResponseEntity.badRequest().body(Result.error(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    /**
     * 将请求体解析并绑定到 java bean 时，如果出错，则抛出 MethodArgumentNotValidException 异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Result> handle(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(Result.error(HttpStatus.BAD_REQUEST.value(), e.getBindingResult().getFieldError().getDefaultMessage()));
    }

    /**
     * 表单绑定到 java bean 出错时抛出 BindException 异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler({BindException.class})
    @ResponseBody
    public ResponseEntity<Result> handleBindException(BindException e) {
        return ResponseEntity.badRequest().body(Result.error(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

    /**
     * Redis未连接报错
     *
     * @param e
     * @return
     */
    @ExceptionHandler({RedisConnectionFailureException.class})
    @ResponseBody
    public ResponseEntity<Result> handleRedisConnectionException(RedisConnectionFailureException e) {
        return ResponseEntity.internalServerError().body(Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    /**
     * OSSException
     *
     * @param e
     * @return
     */
    @ExceptionHandler({OSSException.class})
    @ResponseBody
    public ResponseEntity<Result> handleOSSException(OSSException e) {
        return ResponseEntity.internalServerError().body(
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Caught an OSSException, which means your request made it to OSS, "
                            + "but was rejected with an error response for some reason."+"\n"+
                        "Error Message:" + e.getErrorMessage()+"\n"+
                                "Error Code:" + e.getErrorCode()+"\n"+
                                "Request ID:" + e.getRequestId()+"\n"+
                                "Host ID:" + e.getHostId()));
    }

    /**
     * ClientException
     *
     * @param e
     * @return
     */
    @ExceptionHandler({ClientException.class})
    @ResponseBody
    public ResponseEntity<Result> handleClientException(ClientException e) {
        return ResponseEntity.internalServerError().body(
                Result.error(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Caught a ClientException, which means the client encountered "
                                + "a serious internal problem while trying to communicate with OSS, "
                                + "such as not being able to access the network."+"\n"+
                                "Error Message:" + e.getMessage()));
    }

}

