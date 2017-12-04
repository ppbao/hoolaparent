package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;

/**
 * 系统通过匹配推送的用户
 * @author tiger
 *
 */
public class SystemRmdUserForm implements Serializable ,Comparable<SystemRmdUserForm>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8117886432859405884L;
	long uid;//用户id
	String nickName; //昵称
	String avatarUrl; //头像
	String signature; //签名
	String screenName; //第三方平台用户名
	int friendType;//平台类型
	int isNew;//是否新的
	long time;//匹配时间
	
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
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getScreenName() {
		return screenName;
	}
	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	public int getIsNew() {
		return isNew;
	}
	public void setIsNew(int isNew) {
		this.isNew = isNew;
	}
	public int getFriendType() {
		return friendType;
	}
	public void setFriendType(int friendType) {
		this.friendType = friendType;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	
	public boolean equals(SystemRmdUserForm obj) {
        if (obj == null){
        	return false;
        }
        if (this.getUid() == obj.getUid()){
        	return true;
        }
        return false;
   }
	
	@Override
	public int compareTo(SystemRmdUserForm arg) {
		if (this.isNew > arg.getIsNew()){
			return -1;
		}else{
			if (this.isNew < arg.getIsNew()){
				return 1;
			}else{
				return 0;
			}
		}
	}
	
	/*public static void main(String[] args) {
		
		List<SystemRmddUserForm> list = new ArrayList<SystemRmddUserForm>();
		SystemRmddUserForm a1 = new SystemRmddUserForm();
		a1.setIsNew(0);
		a1.setNickName("1");
		list.add(a1);
		SystemRmddUserForm a2 = new SystemRmddUserForm();
		a2.setIsNew(1);
		a2.setNickName("2");
		list.add(a2);
		for(SystemRmddUserForm test : list){
			System.out.println(test.getNickName());
		}
		
		Collections.sort(list);  
		
		for(SystemRmddUserForm test : list){
			System.out.println(test.getNickName());
		}
		
	}*/
}
