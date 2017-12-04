package com.unisharing.hoola.hoolacommon;

public enum HoolaErrorCode {
	SYSTEM_ERROR{
		@Override
		public int getCode() {
			return 10001;
		}
		
		@Override
		public String getMessage() {
			return "系统错误！";
		}
	},
	TOKEN_ERROR{
		@Override
		public int getCode() {
			return 10002;
		}
		
		@Override
		public String getMessage() {
			return "Token 无效或已经过期！";
		}
	},
	PARAMETER_ERROR{
		@Override
		public int getCode() {
			return 10003;
		}
		
		@Override
		public String getMessage() {
			return "参数错误！";
		}
	},
	RIGHT_ERROR{
		@Override
		public int getCode() {
			return 10004;
		}
		
		@Override
		public String getMessage() {
			return "权限错误！";
		}
	},
	BLACKUSER{
		@Override
		public int getCode() {
			return 10005;
		}
		
		@Override
		public String getMessage() {
			return "黑名单！";
		}
	},
	NO_LOGIN{
		@Override
		public int getCode() {
			return 10101;
		}
		
		@Override
		public String getMessage() {
			return "未登录！";
		}
	},
	SENSITIVE{
		@Override
		public int getCode() {
			return 10102;
		}
		
		@Override
		public String getMessage() {
			return "包含敏感词！";
		}
	},
	EMAIL_USED{
		@Override
		public int getCode() {
			return 20101;
		}
		
		@Override
		public String getMessage() {
			return "邮箱已经被注册！";
		}
	},
	EMAIL_NOT_EXIST{
		@Override
		public int getCode() {
			return 20102;
		}
		
		@Override
		public String getMessage() {
			return "手机号码或者邮箱不存在！";
		}
	},
	EMAIL_ERROR{
		@Override
		public int getCode() {
			return 20103;
		}
		
		@Override
		public String getMessage() {
			return "邮箱格式错误！";
		}
	},
	MOBILE_USED{
		@Override
		public int getCode() {
			return 20104;
		}
		
		@Override
		public String getMessage() {
			return "手机号码已经绑定！";
		}
	},
	MOBILE_CODE_ERROR{
		@Override
		public int getCode() {
			return 20105;
		}
		
		@Override
		public String getMessage() {
			return "验证码错误！";
		}
	},
	PASSWARD_ERROR{
		@Override
		public int getCode() {
			return 20201;
		}
		
		@Override
		public String getMessage() {
			return "密码格式错误！";
		}
	},
	NICKNAME_ERROR{
		@Override
		public int getCode() {
			return 20301;
		}
		
		@Override
		public String getMessage() {
			return "昵称格式错误！";
		}
	},
	LOGIN_ERROR{
		@Override
		public int getCode() {
			return 20401;
		}
		
		@Override
		public String getMessage() {
			return "用户名密码不正确！";
		}
	},
	USER_NOT_EXIST{
		@Override
		public int getCode() {
			return 20402;
		}
		
		@Override
		public String getMessage() {
			return "该用户不存在！";
		}
	},
	ACCOUNT_EXIST{
		@Override
		public int getCode() {
			return 20403;
		}
		
		@Override
		public String getMessage() {
			return "该账号已经被绑定了！";
		}
	},
	BIND_SAME{
		@Override
		public int getCode() {
			return 20404;
		}
		
		@Override
		public String getMessage() {
			return "该平台已经绑定了其他帐号，请用原来帐号进行绑定！";
		}
	},
	USER_FORBID{
		@Override
		public int getCode() {
			return 20405;
		}
		
		@Override
		public String getMessage() {
			return "该用户已经被禁言，请联系管理员！";
		}
	},
	CONTENT_DESC_ERROR{
		@Override
		public int getCode() {
			return 20501;
		}
		
		@Override
		public String getMessage() {
			return "内容描述格式有误！";
		}
	},
	GROUP_EXIST{
		@Override
		public int getCode() {
			return 20601;
		}
		
		@Override
		public String getMessage() {
			return "该组已经存在！";
		}
	},
	GROUP_NOT_EXIST{
		@Override
		public int getCode() {
			return 20602;
		}
		
		@Override
		public String getMessage() {
			return "该组不存在！";
		}
	},
	GROUP_DEFAULT{
		@Override
		public int getCode() {
			return 20603;
		}
		
		@Override
		public String getMessage() {
			return "该组为默认组！";
		}
	},
	CONTENT_DELETE{
		@Override
		public int getCode() {
			return 30001;
		}
		
		@Override
		public String getMessage() {
			return "该视频已经被删除，不能进行操作";
		}
	}
	;
	
	/**
	 * 得到操作结果编码
	 * 
	 * @return
	 */
	public abstract int getCode();
	
	/**
	 * 得到操作结果结果描述
	 * 
	 * @return
	 */
	public abstract String getMessage();

}
