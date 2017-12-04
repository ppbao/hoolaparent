package com.unisharing.hoola.hoolacommon.submit;

import java.io.Serializable;
import java.util.List;

public class Pager implements Serializable{
	
	private static final long serialVersionUID = 809752474971455262L;

	long totalNumber;//记录数
	
	int pageSize = 20;//每页记录数
	
	private long previousCursor = 0; //上一页指针

	private long nextCursor = 0;//下一页指针
	
	private int page;//当前页码
	
	private long totalPage;//总页数
	
	private long pageStart;
	
	private List<?> objList;

	public long getTotalNumber() {
		return totalNumber;
	}

	public long getPreviousCursor() {
		return previousCursor;
	}

	public long getNextCursor() {
		return nextCursor;
	}

	public void setTotalNumber(long totalNumber) {
		this.totalNumber = totalNumber;
	}

	public void setPreviousCursor(long previousCursor) {
		this.previousCursor = previousCursor;
	}

	public void setNextCursor(long nextCursor) {
		this.nextCursor = nextCursor;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	
	
	public List<?> getObjList() {
		return objList;
	}

	public void setObjList(List<?> objList) {
		this.objList = objList;
	}

	public long getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(long totalPage) {
		this.totalPage = totalPage;
	}
	

	public long getPageStart() {
		return pageStart;
	}

	public void setPageStart(long pageStart) {
		this.pageStart = pageStart;
	}

	//yangdm
	public void computerTotalPage(long count){
		setTotalNumber(count);//设置记录总数
		//设置总页数
		if((count % pageSize)>0){
			totalPage=totalNumber/pageSize+1;
		}else{
			totalPage=totalNumber/pageSize;
		}
		//当前的页数大于总页数时，设置当前页等于总页数
		if(getPage()>getTotalPage()){
			page=(int)totalPage;
		}
		if(getPage()==0){
			setPage(1);
		}
		pageStart=(page-1)*pageSize;
	}
	
}
