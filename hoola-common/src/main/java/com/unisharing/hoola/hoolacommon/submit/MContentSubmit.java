package com.unisharing.hoola.hoolacommon.submit;


import com.unisharing.hoola.hoolacommon.utils.DateUtil;

import java.io.Serializable;
import java.util.Date;

public class MContentSubmit extends Pager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7058827308039597293L;
	
	private String key;//按关键字
	private String nickName;//按发布者
	private String zt;//按内容状态
	private String uploadTimeQ;//发布时间起
	private String uploadTimeZ;//发布时间至
	private long uploadTimeQL;
	private long uploadTimeZL;
	//private List<Integer> cidList;//cid列表
	private String cid;
	private String type;//推送位置
	private String tssjStr;
	private long tssj;
	private String tslx;//0 立即推送：1定时推送
	private int recommend;//"荐标志" 1表示勾选
	private String uid;
	private int cxrk;//程序入口1直接从内容列表里进，2从标签管理里进，3从用户列表中的内容数进，4从话题管理里进
	private long tid;
	private long sid;//话题ID
	private String pxtj;
	private String pxlx;
	private String isPrivate; 
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getZt() {
		return zt;
	}
	public void setZt(String zt) {
		this.zt = zt;
	}
	public String getUploadTimeQ() {
		return uploadTimeQ;
	}
	public void setUploadTimeQ(String uploadTimeQ) {
		this.uploadTimeQ = uploadTimeQ;
	}
	public String getUploadTimeZ() {
		return uploadTimeZ;
	}
	public void setUploadTimeZ(String uploadTimeZ) {
		this.uploadTimeZ = uploadTimeZ;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public long getUploadTimeQL() {
		return uploadTimeQL;
	}
	public void setUploadTimeQL(long uploadTimeQL) {
		this.uploadTimeQL = uploadTimeQL;
	}
	public long getUploadTimeZL() {
		return uploadTimeZL;
	}
	public void setUploadTimeZL(long uploadTimeZL) {
		this.uploadTimeZL = uploadTimeZL;
	}
	public long toLong(String date) throws Exception{
		Date timeDate= DateUtil.parse(date, "yyyy-MM-dd HH:mm:ss");
		return timeDate.getTime();
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTssjStr() {
		return tssjStr;
	}
	public void setTssjStr(String tssjStr) {
		this.tssjStr = tssjStr;
	}
	public long getTssj() {
		return tssj;
	}
	public void setTssj(long tssj) {
		this.tssj = tssj;
	}
	public String getTslx() {
		return tslx;
	}
	public void setTslx(String tslx) {
		this.tslx = tslx;
	}
	public int getRecommend() {
		return recommend;
	}
	public void setRecommend(int recommend) {
		this.recommend = recommend;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public int getCxrk() {
		return cxrk;
	}
	public void setCqrk(int cxrk) {
		this.cxrk = cxrk;
	}
	public long getTid() {
		return tid;
	}
	public void setTid(long tid) {
		this.tid = tid;
	}
	public void setCxrk(int cxrk) {
		this.cxrk = cxrk;
	}
	public String getPxtj() {
		return pxtj;
	}
	public void setPxtj(String pxtj) {
		this.pxtj = pxtj;
	}
	public String getPxlx() {
		return pxlx;
	}
	public void setPxlx(String pxlx) {
		this.pxlx = pxlx;
	}
	public String getIsPrivate() {
		return isPrivate;
	}
	public void setIsPrivate(String isPrivate) {
		this.isPrivate = isPrivate;
	}
	public long getSid() {
		return sid;
	}
	public void setSid(long sid) {
		this.sid = sid;
	}
	
	
}
