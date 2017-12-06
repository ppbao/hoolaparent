package com.unisharing.hoola.hoolaclient.dao;


import com.unisharing.hoola.hoolacommon.model.ReportModel;
import org.springframework.stereotype.Component;
@Component

public interface ClientReportMapper {
	
	
	void insertReportModel(ReportModel reportModel);//插入举报信息表

}
