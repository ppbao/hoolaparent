package com.unisharing.hoola.hooladatacenter.service.user;

public interface IUserService extends Runnable {
	
	/**
	 * 用户注册
	 * @param uid
	 */
	public void handleUserRegister();
	
	/**
	 * 用户编辑
	 */
	public void handleUserEdit();
	
	/**
	 * 处理用户登录信息
	 */
	public void handleLogin();

	/**
	 * 绑定第三方平台数据
	 */
	public void handleUserBinding(long uid);
	
	/**
	 * 关注用户UserRegister
	 */
	public void handleFollow();
	
	/**
	 * 取消关注
	 */
	public void handleUnfollow();
	
	/**
	 * 处理邀请
	 */
	public void handleInvite();
	/**
	 * 通讯录上传
	 */
	public void handleMobileUp();
	
	/**
	 * 同步微博
	 */
	public void handleSynWeibo();
	
}
