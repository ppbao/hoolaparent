package com.unisharing.hoola.hoolaclient.service.group;


import com.unisharing.hoola.hoolacommon.model.GroupModel;

public interface IClientGroupService {
	void insertGroupModel(GroupModel groupModel);//插入 用户分组信息
	void deleteGroupModel(long uid, String groupName);//删除用户分组信息
	void insertGroupRelation(String groupName, long groupUid, long fuid);//groupName 组名，groupUid 这个组属于哪个用户，fuid把某个用户加入到对应的组
	void deleteGroupRelation(String groupName, long groupUid, long fuid);//groupName 组名，groupUid 这个组属于哪个用户，fuid把某个用户加入到对应的组
	void updateGroupModel(String newGroupName, String oldGroupName, long uid);//更改组名

}
