package com.unisharing.hoola.hoolacommon.model;


import java.io.Serializable;
import java.util.List;

/**
 * 所有实体 Bean 对象都继承的一个父类对象
 * @author wjian
 *
 */
public class BaseModel implements Serializable{

	private static final long serialVersionUID = -1831203364146227807L;

	public Object[] params;
	private List<Object> objList; 
	private int goToPage = 1; 
	private int totalPage = 0; 
	private int pageSize = 5; 
	private int totalNum = 0;  
	int start = 0; 
	int end = 0;
	
	public Object[] getParams() {
		return params;
	}
	public void setParams(Object[] params) {
		this.params = params;
	}
	public List<Object> getObjList() {
		return objList;
	}
	public void setObjList(List<Object> objList) {
		this.objList = objList;
	}
	public int getGoToPage() {
		return goToPage;
	}
	public void setGoToPage(int goToPage) {
		this.goToPage = goToPage;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}

	public void computerTotalPage(int count) {
		setTotalNum(count);
		if ((count % pageSize) > 0) {
			totalPage = totalNum / pageSize + 1;
		} else {
			totalPage = totalNum / pageSize;
		}
		if (getGoToPage() > getTotalPage()) {
			goToPage = getTotalPage();
		}
		start = (goToPage - 1) * pageSize;
		end = goToPage * pageSize;
		if(getGoToPage() == 0 ) {
			this.setGoToPage(1);
		}
		if(getTotalPage() == 0) {
			this.setTotalPage(1);
		}
	}
	
}

