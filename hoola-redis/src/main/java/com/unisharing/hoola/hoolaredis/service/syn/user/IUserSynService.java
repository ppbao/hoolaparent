package com.unisharing.hoola.hoolaredis.service.syn.user;


import com.unisharing.hoola.hoolacommon.model.UserModel;

public interface IUserSynService {
	
	/**
	 * 同步用户添加
	 * @param user
	 * @return
	 */
	public boolean synUserAdd(UserModel user);
	
	/**
	 * 用户禁言
	 * @param uid
	 * @return
	 */
	public boolean synUserForbid(long uid);
	
	/**
	 * 用户解禁
	 * @param uid
	 * @return
	 */
	public boolean synUserUnforbid(long uid);
	
	/**
	 * 同步用户关注 uid 关注 fid
	 * @param uid  
	 * @param fid
	 * @return
	 */
	public boolean synFollow(long uid, long fid);

	/**
	 * 同步用户取消关注  uid 取消关注 fid
	 * @return
	 */
	public boolean sysUbFollow(long uid, long fid);

	/**
	 * 同步用户达人认证信息
	 * @param uid
	 * @param isTalent
	 * @param talentDesc
	 * @return
	 */
	public boolean sysTalentInfo(long uid, int isTalent, String talentDesc);

}
