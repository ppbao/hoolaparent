package com.unisharing.hoola.hooladatacenter.service.group;

public interface IGroupService {
	
	//组添加
	public void handleGroupAdd();
	//组编辑
	public void handleGroupEdit();
	//组删除
	public void handleGroupDelete();
	
	//组成员添加
	public void handleGroupUsersAdd();
	//组成员删除
	public void handleGroupUsersDelete();

}
