package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ResultHot implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long cid;
	private long uid;
	private int zs;
	private int num;
	public long getCid() {
		return cid;
	}
	public void setCid(long cid) {
		this.cid = cid;
	}
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public int getZs() {
		return zs;
	}
	public void setZs(int zs) {
		this.zs = zs;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof ResultHot){
			ResultHot result=(ResultHot)obj;
			if(this.uid==result.getUid()){
				return true;
			}
		}
		return false;
	}
	
	
	public static void main(String[] args) {
		List<ResultHot> list=new ArrayList<ResultHot>();
		ResultHot result=new ResultHot();
		ResultHot result2=new ResultHot();
		ResultHot result3=new ResultHot();
		ResultHot result4=new ResultHot();
		result.setUid(2);
		result.setCid(3);
		result.setZs(5);
		
		result2.setUid(2);
		result2.setCid(34);
		result2.setZs(23);
		
		result3.setUid(5);
		result3.setCid(32);
		result3.setZs(21);
		
		result4.setUid(7);
		result4.setCid(39);
		result4.setZs(98);
		
		list.add(result2);
		list.add(result3);
		list.add(result4);
		
		System.out.println(list);
		System.out.println(list.contains(result));

		
		
		
	}
	
	class ComparatorResult implements Comparator<ResultHot>{

		@Override
		public int compare(ResultHot o1, ResultHot o2) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public boolean equals(Object obj) {
			// TODO Auto-generated method stub
			return super.equals(obj);
		}
		
	}
}
