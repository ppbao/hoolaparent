package com.unisharing.hoola.hoolacommon.submit;

import com.unisharing.hoola.hoolacommon.model.BindModel;
import com.unisharing.hoola.hoolacommon.model.UserModel;
import com.unisharing.hoola.hoolacommon.vo.UserForm;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 注册时提交的用户类
 * @author wanghaihua
 *
 */
public class UserSubmit implements Serializable {

	private static final long serialVersionUID = 7800507080278981751L;
	private MultipartFile avatar;// 头像文件
	private String email;	// 绑定的邮箱
	private String loginPassword;	//登录密码
	private String nickName;	//昵称
	private String avatarUrl;	//头像
	private float longitude;	//经度
	private float latitude;		//纬度
	
	 
	private int openType;	//开放平台类型
	private String openUid;	//开放平台用户ID
	private String accessToken;	//访问Token
	private String refreshToken;	//刷新token
	private String expiresDate;//过期时间 yyyy-MM-dd HH:ss:mm +0000
	private String deviceToken;//设备号Iphone
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public int getOpenType() {
		return openType;
	}
	public void setOpenType(int openType) {
		this.openType = openType;
	}
	
	public String getOpenUid() {
		return openUid;
	}
	public void setOpenUid(String openUid) {
		this.openUid = openUid;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	
	
	public MultipartFile getAvatar() {
		return avatar;
	}
	public void setAvatar(MultipartFile avatar) {
		this.avatar = avatar;
	}
	
	public UserModel asUserModel(){
		UserModel model = new UserModel();
		model.setAvatarUrl(this.avatarUrl);
		model.setBackgroundUrl("");
		model.setCreateTime(System.currentTimeMillis());
		if (this.email != null){
			model.setEmail(this.email.trim().toLowerCase());
		}else{
			model.setEmail("");
		}
		model.setLastLoginTime(0);
		model.setLatitude(latitude);
		model.setLoginPassward(this.loginPassword);
		model.setLongitude(longitude);
		model.setMobile("");
		if (StringUtils.isNotBlank(this.nickName)){
			model.setNickName(this.nickName.trim());//去掉前后空格
		}
		model.setRoleId(0);
		model.setSignature("");
		if (deviceToken != null){
			model.setDeviceToken(deviceToken);
		}else{
			model.setDeviceToken("");
		}
		return model;
	}
	
	public BindModel asBindModel(){
		BindModel model = new BindModel();
		model.setAccessToken(this.accessToken);
		model.setCreatetime(System.currentTimeMillis());
		model.setExpiresDate(expiresDate);
		model.setOpenType(this.openType);
		model.setOpenUid(this.openUid);
		model.setRefreshToken(this.refreshToken);
		model.setOpenNickName(this.nickName);
		return model;
	}
	
	public UserForm asUserForm(){
		UserForm form = new UserForm();
		form.setAvatar(this.avatarUrl);
		form.setNickName(nickName);
		return form;
	}
	
	
	
	@Override
	public String toString() {
		return "UserSubmit [accessToken=" + accessToken + ", avatarUrl="
				+ avatarUrl + ", expiresDate=" + expiresDate + ", latitude="
				+ latitude + ", longitude=" + longitude + ", nickName="
				+ nickName + ", openType=" + openType + ", openUid="
				+ openUid + ", refreshToken=" + refreshToken + "]";
	}
	public String getExpiresDate() {
		return expiresDate;
	}
	public void setExpiresDate(String expiresDate) {
		this.expiresDate = expiresDate;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
}
