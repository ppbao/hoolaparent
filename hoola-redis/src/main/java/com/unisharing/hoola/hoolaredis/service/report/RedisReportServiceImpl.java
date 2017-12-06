package com.unisharing.hoola.hoolaredis.service.report;


import com.unisharing.hoola.hoolaredis.key.ReportKeyManager;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
@Service("redisReportService")
public class RedisReportServiceImpl extends BaseRedisService implements
		IRedisReportService {

	@SuppressWarnings("unchecked")
	@Override
	public void addBiaoqingReport(long cid, String biaoqingNo) {
		reportTemplate.boundSetOps(ReportKeyManager.getBiaoqingKey(biaoqingNo)).add(String.valueOf(cid));

	}
	@SuppressWarnings("unchecked")
	@Override
	public void addFilterReport(long cid, String filters) {
		String[] filterNos = filters.split(",");
		for (String filterNo : filterNos){
			if (StringUtils.hasText(filterNo.trim())){
				reportTemplate.boundSetOps(ReportKeyManager.getFilterKey(filterNo.trim())).add(String.valueOf(cid));
			}
		}
		

	}
	@SuppressWarnings("unchecked")
	@Override
	public void addMusicReport(long cid, String musicNo) {
		reportTemplate.boundSetOps(ReportKeyManager.getMusicKey(musicNo)).add(String.valueOf(cid));

	}
	@SuppressWarnings("unchecked")
	@Override
	public void addTiezhiReport(long cid, String tiezhiNo) {
		reportTemplate.boundSetOps(ReportKeyManager.getTiezhiKey(tiezhiNo)).add(String.valueOf(cid));
	}

}
