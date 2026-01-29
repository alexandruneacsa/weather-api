package com.weather.demo.aop.logging;

import static com.weather.demo.utils.Constants.Common.QUESTION_MARK;

import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.stream.Collectors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.weather.demo.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RestControllerLogger {
	
	@Before("execution(* com.weather.demo.controller..*(..))")
	public void loggingBefore(final JoinPoint joinPoint) {
		
		final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		
		log.info(getOperation(signature));
	}
	
	@AfterThrowing(pointcut = "within(com.weather.demo.controller..*)", throwing = "ex")
	public void logAfterThrowing(JoinPoint joinPoint, final Throwable ex) {
		
		logRequest(joinPoint);
		log.error(ex.getMessage(), ex);
	}
	
	private void logRequest(final JoinPoint joinPoint) {
		
		final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		
		if(log.isDebugEnabled()) {
			final String className = joinPoint.getTarget().getClass().getSimpleName();
			final String methodName = joinPoint.getSignature().getName();
			final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			log.debug("{} -> {}() | {} {} | body: {}", className, methodName, request.getMethod(),
				request.getRequestURI() + getParams(request), getRequestBody(signature, joinPoint));
		}
	}
	
	private String getParams(final HttpServletRequest request) {
		
		if(request.getParameterMap().isEmpty()) {
			return Constants.Common.EMPTY;
		}
		return QUESTION_MARK + request.getParameterMap().entrySet()
			.stream()
			.map(e -> e.getKey() + "=" + String.join(",", e.getValue()))
			.collect(Collectors.joining("&"));
	}
	
	private static String getRequestBody(final MethodSignature signature, final JoinPoint joinPoint) {
		
		final Annotation[][] annotationMatrix = signature.getMethod().getParameterAnnotations();
		int index = -1;
		final Object[] args = joinPoint.getArgs();
		for(Annotation[] annotations : annotationMatrix) {
			index++;
			for(Annotation annotation : annotations) {
				if((annotation instanceof RequestBody)) {
					if(args == null || index < 0 || index >= args.length) {
						return Constants.Common.EMPTY;
					}
					final Object arg = args[index];
					return Objects.requireNonNullElse(arg, Constants.Common.EMPTY).toString();
				}
			}
		}
		return Constants.Common.EMPTY;
	}
	
	private static String getOperation(final MethodSignature signature) {
		
		final var operation = signature.getMethod().getAnnotation(Operation.class);
		return operation != null ? operation.summary() : signature.getMethod().getName();
	}
}
