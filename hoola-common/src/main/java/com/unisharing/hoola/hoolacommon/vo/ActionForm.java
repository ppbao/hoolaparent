package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;

public class ActionForm implements Serializable ,Comparable<ActionForm>{
	
	private static final long serialVersionUID = 8361753543378337587L;
	ContentForm content; //动作对应的内容
	SimpleUserForm	actionUser;	//产生动作的用户
	long 		time;	//动作发生时间
	int			actionType;//动作类型
	int 		top = 0;//是否置顶
	public ContentForm getContent() {
		return content;
	}
	public long getTime() {
		return time;
	}
	public int getActionType() {
		return actionType;
	}
	public void setContent(ContentForm content) {
		this.content = content;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public SimpleUserForm getActionUser() {
		return actionUser;
	}
	public void setActionUser(SimpleUserForm actionUser) {
		this.actionUser = actionUser;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	@Override
	public String toString() {
		return "ActionForm [actionType=" + actionType + ", actionUser="
				+ actionUser + ", content=" + content + ", time=" + time
				+ ", top=" + top + "]";
	}
	
	@Override
	public int compareTo(ActionForm arg) {
		if (this.content.getLikeNum() > arg.content.getLikeNum()){
			return -1;
		}else{
			if (this.content.getLikeNum() == arg.content.getLikeNum()){
				if (this.time > arg.getTime()){
					return -1;
				}else{
					if (this.time < arg.getTime()){
						return 1;
					}else{
						return 0;
					}
				}
			}else{
				return 1;
			}
		}
		
	}
	
}
