package com.unisharing.hoola.hoolacommon.submit;

import java.io.Serializable;

/**
 * 用户反馈
 * @author wanghaihua
 *
 */
public class FeedBackSubmit implements Serializable {
	private static final long serialVersionUID = 5942596633222901409L;
	
	private String feedbackText;//反馈内容
	private String screenshotUrl;//截屏
	public String getFeedbackText() {
		return feedbackText;
	}
	public String getScreenshotUrl() {
		return screenshotUrl;
	}
	public void setFeedbackText(String feedbackText) {
		this.feedbackText = feedbackText;
	}
	public void setScreenshotUrl(String screenshotUrl) {
		this.screenshotUrl = screenshotUrl;
	}
	@Override
	public String toString() {
		return "FeedBackSubmit [feedbackText=" + feedbackText
				+ ", screenshotUrl=" + screenshotUrl + "]";
	}
	

}
