package com.unisharing.hoola.hoolacommon.model;

import java.io.Serializable;

public class FilterWordModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1405278632627884798L;
	
	private int id;
	private String filterText;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFilterText() {
		return filterText;
	}
	public void setFilterText(String filterText) {
		this.filterText = filterText;
	}
	
}
