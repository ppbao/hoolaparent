package com.unisharing.hoola.hoolacommon;

import java.io.Serializable;

public class Result implements Serializable {

	private static final long serialVersionUID = 736968089362283709L;
	long time;
	Object data;
	int code;
	String message;
	int pages;//data返回列表时，pages则为页数
	int total = 0;//记录总数

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

}
