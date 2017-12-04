package com.unisharing.hoola.hoolacommon.vo;

import java.io.Serializable;

public class GroupForm implements Serializable{
	
	private static final long serialVersionUID = 3337438757208374117L;
	private String name;//组名
	private int count;//用户数
	public String getName() {
		return name;
	}
	public int getCount() {
		return count;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "GroupForm [name=" + name + ", count=" + count + "]";
	}

	
}
