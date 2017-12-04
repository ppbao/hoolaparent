package com.unisharing.hoola.hoolaredis.service.group;



import com.unisharing.hoola.hoolacommon.vo.GroupForm;
import com.unisharing.hoola.hoolacommon.vo.SimpleUserForm;

import java.util.List;
import java.util.Set;

public interface IRedisGroupService {
	
	/**
	 * 根据用户Id取出组列表
	 * @param uid
	 * @return
	 */
	public List<GroupForm> loadGroups(long uid);
	
	/**
	 * 获取用户某组的好友用户列表
	 * @param uid
	 * @param groupName
	 * @return
	 */
	public List<SimpleUserForm> loadUsers(long uid, String groupName);


	/**
	 * 添加组信息
	 * @param groupName
	 * @param uid
	 */
	public void addGroup(String groupName, long uid);

	/**
	 * 添加用户到组里面
	 * @param groupName
	 * @param uids
	 */
	@SuppressWarnings("rawtypes")
	public void addUsers(String groupName, long uid, Set uids);

	/**
	 * 是否存在
	 * @param groupName
	 * @param uid
	 * @return
	 */
	public boolean isExist(String groupName, long uid);

	/**
	 * 编辑组信息
	 * @param oldGroupName
	 * @param newGroupName
	 * @param newUids
	 */
	public void editGroup(String oldGroupName, String newGroupName, long uid);

	/**
	 * 单个删除组成员
	 * @param uid
	 * @param groupName
	 * @param delUid
	 */
	public boolean deleteGroupUser(long uid, String groupName, long delUid);

	/**
	 * 删除组
	 * @param uid
	 * @param groupName
	 */
	public void deleteGroup(long uid, String groupName);

}
