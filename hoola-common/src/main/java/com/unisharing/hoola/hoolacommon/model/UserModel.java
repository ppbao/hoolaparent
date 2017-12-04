package com.unisharing.hoola.hoolacommon.model;


import com.unisharing.hoola.hoolacommon.utils.HoolaConfig;
import com.unisharing.hoola.hoolacommon.vo.SimpleUserForm;
import com.unisharing.hoola.hoolacommon.vo.TalentForm;
import com.unisharing.hoola.hoolacommon.vo.UserDetailForm;
import com.unisharing.hoola.hoolacommon.vo.UserForm;
import org.apache.commons.lang3.StringUtils;


import java.io.Serializable;

public class UserModel implements Serializable {
	
	private static final long serialVersionUID = 6082025056235706818L;
	
	private long uid;	//用户ID
	private String email;	//Email
	private String loginPassward;	//登录密码
	private String nickName;	//昵称
	private String mobile;	//手机号码
	private String avatarUrl;	//头像
	private long createTime;	//注册时间
	private float longitude;
	private float latitude;
	private long lastLoginTime;	//最后登录时间
	private String signature;	//签名
	private String backgroundUrl;//背景图片
	private String token;
	private int roleId;//角色ID
	private String deviceToken;//设备号
	private int status = 1;//用户状态
	
	//认证信息
	private int isTalent;//是否是达人
	private String talentDesc;//达人说明
	private String videoUrl;//视频介绍
	private String videoFaceUrl;//视频封面
		
		
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLoginPassward() {
		return loginPassward;
	}
	public void setLoginPassward(String loginPassward) {
		this.loginPassward = loginPassward;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
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
	public long getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(long lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getBackgroundUrl() {
		return backgroundUrl;
	}
	public void setBackgroundUrl(String backgroundUrl) {
		this.backgroundUrl = backgroundUrl;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getDeviceToken() {
		return deviceToken;
	}
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public UserForm asUserForm(){
		UserForm form = new UserForm();
		form.setAvatar(StringUtils.isEmpty(this.avatarUrl)?"default.jpg":this.avatarUrl);
		String avatarUrl = form.getAvatar();
		if (avatarUrl.indexOf("http://") < 0) {
			form.setAvatar(HoolaConfig.getAliyunAvatarDomain() + avatarUrl);
		}
		if (StringUtils.isEmpty(this.backgroundUrl)){
			form.setBackgroundUrl("");
		}else{
			if (this.backgroundUrl.indexOf("http://") < 0){
				form.setBackgroundUrl(HoolaConfig.getAliyunAvatarDomain() + backgroundUrl);
			}else{
				form.setBackgroundUrl(this.backgroundUrl);
			}
		}
		form.setNickName(this.nickName);
		form.setUid(this.uid);
		form.setToken(this.token);
		form.setStatus(this.status);
		return form;
		
	}
	public SimpleUserForm asSimpleUserForm(){
		SimpleUserForm form = new SimpleUserForm();
		form.setAvatar(StringUtils.isEmpty(this.avatarUrl)?"default.jpg":this.avatarUrl);
		if (form.getAvatar().indexOf("http://") < 0) {
			form.setAvatar(HoolaConfig.getAliyunAvatarDomain() + form.getAvatar());
		}
		form.setNickName(this.nickName);
		form.setUid(this.uid);
		form.setSignature(this.signature);
		form.setIsTalent(this.isTalent);
		return form;
	}
	
	public UserDetailForm asDetailUserForm(){
		UserDetailForm form = new UserDetailForm();
		form.setAvatar(StringUtils.isEmpty(this.avatarUrl)?"default.jpg":this.avatarUrl);
		if (form.getAvatar().indexOf("http://") < 0) {
			form.setAvatar(HoolaConfig.getAliyunAvatarDomain() + form.getAvatar());
		}
		
		if (StringUtils.isBlank(this.getBackgroundUrl())){
			form.setBgimage("");
		}else{
			if (this.getBackgroundUrl().indexOf("http://") < 0){
				form.setBgimage(HoolaConfig.getAliyunAvatarDomain() + this.backgroundUrl);
			}else{
				form.setBgimage(this.getBackgroundUrl());
			}
		}
		form.setNickName(this.nickName);
		form.setSignature(signature);
		form.setUid(this.uid);
		form.setIsTalent(isTalent);
		
		if (StringUtils.isBlank(this.talentDesc)){
			form.setTalentDesc("");
		}else{
			form.setTalentDesc(this.talentDesc);
		}
		
		if (StringUtils.isNotEmpty(this.videoUrl) && (!"null".equalsIgnoreCase(this.videoUrl))){
			form.setVideoUrl(HoolaConfig.getAliyunAvatarDomain()+videoUrl);
		}else{
			form.setVideoUrl("");
		}
		if (StringUtils.isNotEmpty(this.videoFaceUrl) && (!"null".equalsIgnoreCase(this.videoFaceUrl))){
			form.setVideoFaceUrl(HoolaConfig.getAliyunAvatarDomain()+videoFaceUrl);
		}else{
			form.setVideoFaceUrl("");
		}
		return form;
	}
	
	public TalentForm asTalentForm(){
		TalentForm form = new TalentForm();
		form.setAvatar(StringUtils.isEmpty(this.avatarUrl)?"default.jpg":this.avatarUrl);
		if (form.getAvatar().indexOf("http://") < 0) {
			form.setAvatar(HoolaConfig.getAliyunAvatarDomain() + form.getAvatar());
		}
		form.setNickName(this.nickName);
		form.setUid(this.uid);
		return form;
	}
	
	@Override
	public String toString() {
		return "UserModel [avatarUrl=" + avatarUrl + ", backgroundUrl="
				+ backgroundUrl + ", createTime=" + createTime + ", email="
				+ email + ", lastLoginTime=" + lastLoginTime + ", latitude="
				+ latitude + ", loginPassward=" + loginPassward
				+ ", longitude=" + longitude + ", mobile=" + mobile
				+ ", nickName=" + nickName + ", roleId=" + roleId
				+ ", signature=" + signature + ", token=" + token + ", uid="
				+ uid + "]";
	}
	public int getIsTalent() {
		return isTalent;
	}
	public void setIsTalent(int isTalent) {
		this.isTalent = isTalent;
	}
	public String getTalentDesc() {
		return talentDesc;
	}
	public void setTalentDesc(String talentDesc) {
		this.talentDesc = talentDesc;
	}
	public String getVideoUrl() {
		return videoUrl;
	}
	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}
	public String getVideoFaceUrl() {
		return videoFaceUrl;
	}
	public void setVideoFaceUrl(String videoFaceUrl) {
		this.videoFaceUrl = videoFaceUrl;
	}
}
