package com.unisharing.hoola.hoolaclient.web.interceptor;

import com.alibaba.fastjson.JSON;

import com.unisharing.hoola.hoolacommon.HoolaErrorCode;
import com.unisharing.hoola.hoolacommon.Result;
import com.unisharing.hoola.hoolaredis.service.user.IRedisUserService;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

public class AuthInterceptor extends HandlerInterceptorAdapter {

	@Resource
	IRedisUserService redisUserService;
	
	private Set notInterceptorUrls;

	@Override
	public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
		
		String query = request.getRequestURI();
		if (notInterceptorUrls.contains(query)){
			return true;
		}else{
			String token = null;
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (!isMultipart) {
				token = request.getParameter("token");
			} else {
				DefaultMultipartHttpServletRequest mReq = (DefaultMultipartHttpServletRequest)request;
				token = mReq.getParameter("token");
			}
			long uid = redisUserService.getUid(token);
			if (uid > 0) {
				return true;
			} else {
				response.setContentType("application/json;charset=UTF-8");
				Result result = new Result();
				result.setCode(HoolaErrorCode.NO_LOGIN.getCode());
				result.setData("need login in！");
				result.setMessage(HoolaErrorCode.NO_LOGIN.getMessage());
				result.setTime(System.currentTimeMillis());
				response.getWriter().print(JSON.toJSONString(result));
				return false;
			}
		}
	}

	public void setNotInterceptorUrls(Set notInterceptorUrls) {
		this.notInterceptorUrls = notInterceptorUrls;
	}

}
