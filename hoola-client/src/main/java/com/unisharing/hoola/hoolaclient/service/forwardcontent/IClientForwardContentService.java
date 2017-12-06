package com.unisharing.hoola.hoolaclient.service.forwardcontent;


import com.unisharing.hoola.hoolacommon.model.ForwardContentModel;

public interface IClientForwardContentService {
	void insertForwardContentModel(ForwardContentModel forwardContentModel);//增加转发
	void deleteForwardContentModel(ForwardContentModel forwardContentModel);//取消转发

}
