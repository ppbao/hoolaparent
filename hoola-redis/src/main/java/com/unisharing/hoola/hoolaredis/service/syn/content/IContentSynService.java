package com.unisharing.hoola.hoolaredis.service.syn.content;


import com.unisharing.hoola.hoolacommon.model.ContentModel;

public interface IContentSynService {
	
	/**
	 * 后台添加内容同步Redis
	 * 注意：
	 * 1、高清和流畅的地址都用同一个地址；
	 * 2、描述不支持Emoji表情
	 * 3、createTime 和 uploadTime 请用毫秒级的时间戳
	 * 4、视频时长需要输入
	 * 5、注意前台一定要上传一个视频首针图片，尺寸480*480
	 * @param
	 * @return
	 */
	public boolean synContentAdd(ContentModel model);
	
	/**
	 * 后台屏蔽内容同步Redis
	 * @param cid
	 * @return
	 */
	public boolean synContentDelete(long cid, int status);

	/**
	 * 后台屏蔽内容同步Redis
	 * @param cid
	 * @return
	 */
	public boolean synContentLike(long uid, long cid);

	/**
	 * 后台同步公共账号转发
	 * @param uid
	 * @param cid
	 * @return
	 */
	public String synContentForward(long uid, long cid);

	/**
	 * 后台对内容描述的修改
	 * @param cid
	 * @param description
	 * @return
	 */
	public boolean synContentEdit(long cid, String description);

	/**
	 * 同步播放次数
	 * @param cid
	 * @param times
	 * @return
	 */
	public boolean synContentShowTimes(long cid, int times);

	/**
	 * 同步内容新浪分享数
	 * @param cid
	 * @param count
	 * @return
	 */
	public boolean synContentSinaShareNum(long cid, int count);

	/**
	 * 同步内容朋友圈分享次数
	 * @param cid
	 * @param count
	 * @return
	 */
	public boolean synContentFriendsShareNum(long cid, int count);

}
