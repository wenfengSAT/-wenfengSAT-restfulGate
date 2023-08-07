package com.apigate.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.apigate.constant.ApigateRetCode;
import com.apigate.exception.BaseException;
import com.apigate.util.JsonResult;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Description： api统一异常处理
 * 
 * @author [ wenfengSAT@163.com ] on [2023年8月7日上午11:37:28]
 * @Modified By： [修改人] on [修改日期] for [修改说明]
 *
 */
@Slf4j
@Component
@RestControllerAdvice
public class ApiExceptionHandler {

	private static final String RESULTCODE = "code";

	private static final String RESULTDESC = "msg";

	/**
	 * 
	 * @Description： api统一异常处理
	 * 
	 * @author [ wenfengSAT@163.com ]
	 * @Date [2023年8月7日上午11:37:35]
	 * @param e
	 * @return
	 *
	 */
	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public JsonResult handlerException(Exception e) {
		log.error("ApiExceptionHandler.handlerException msg:{}", e);
		if (e instanceof BlockException || e.getCause() instanceof FlowException) {
			return JsonResult.error(ApigateRetCode.BUSY_SERVICE_EXCEPTION);
		} else if (e instanceof FlowException || e.getCause() instanceof FlowException) {
			return JsonResult.error(ApigateRetCode.BUSY_SERVICE_EXCEPTION);
		} else if (e instanceof HttpRequestMethodNotSupportedException) {
			return JsonResult.error(ApigateRetCode.REQUESTMETHODNOTSUPPORTED);
		} else if (e instanceof HttpMediaTypeNotSupportedException) {
			return JsonResult.error(ApigateRetCode.MEDIATYPENOTSUPPORTED);
		} else if (e instanceof BaseException) {
			return JsonResult.error(((BaseException) e).getResponseEnum().getCode(), ((BaseException) e).getMessage());
		} else {
			return JsonResult.error(ApigateRetCode.SYSTEM_EXCEPTION);
		}
	}

	/**
	 * 
	 * @Description： 统一捕获参数校验的异常抛出
	 * 
	 * @author [ wenfengSAT@163.com ]
	 * @Date [2023年8月7日上午11:37:45]
	 * @param e
	 * @return
	 *
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, Object> bindException(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();
		StringBuilder errorMesssage = new StringBuilder("参数异常:");

		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			errorMesssage.append(fieldError.getDefaultMessage()).append(",");
		}
		Map<String, Object> context = new HashMap<>();
		context.put(RESULTCODE, ApigateRetCode.ERROR_PARAM.getCode());
		context.put(RESULTDESC, errorMesssage.toString());

		return context;
	}

}
