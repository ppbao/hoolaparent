package com.unisharing.hoola.hoolaredis.service.report;

public interface IRedisReportService {
	
	/**
	 * 统计背景音乐数据
	 * @param cid
	 * @param musicNo
	 */
	public void addMusicReport(long cid, String musicNo);

	/**
	 * 统计表情使用数据
	 * @param cid
	 * @param biaoqingNo
	 */
	public void addBiaoqingReport(long cid, String biaoqingNo);

	/**
	 * 统计贴纸使用数据
	 * @param cid
	 * @param tiezhiNo
	 */
	public void addTiezhiReport(long cid, String tiezhiNo);

	/**
	 * 统计滤镜使用数据
	 * @param cid
	 * @param filterNo
	 */
	public void addFilterReport(long cid, String filterNo);

}
