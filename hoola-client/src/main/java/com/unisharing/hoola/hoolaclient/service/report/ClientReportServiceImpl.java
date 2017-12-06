package com.unisharing.hoola.hoolaclient.service.report;


import com.unisharing.hoola.hoolaclient.dao.ClientReportMapper;
import com.unisharing.hoola.hoolacommon.model.ReportModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("clientReportService")
public class ClientReportServiceImpl implements IClientReportService {

	@Autowired
	ClientReportMapper clientReportMapper;
	
	@Override
	public void insertReportModel(ReportModel reportModel) {
		clientReportMapper.insertReportModel(reportModel);

	}

	public ClientReportMapper getClientReportMapper() {
		return clientReportMapper;
	}

	public void setClientReportMapper(ClientReportMapper clientReportMapper) {
		this.clientReportMapper = clientReportMapper;
	}
	
	
	
	

}
