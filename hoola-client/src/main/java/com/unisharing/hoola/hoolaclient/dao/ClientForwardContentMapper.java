package com.unisharing.hoola.hoolaclient.dao;



import com.unisharing.hoola.hoolacommon.model.ForwardContentModel;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public interface ClientForwardContentMapper {
	
	void insertForwardContentModel(ForwardContentModel forwardContentModel);//插入转发表
	void updateContentForwardNum(Map<String, Object> map);//更新转发数
	void deleteForwardContentModel(ForwardContentModel forwardContentModel);//取消转发

}
