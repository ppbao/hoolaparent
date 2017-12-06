package com.unisharing.hoola.hoolaclient.service.likecontent;



import com.unisharing.hoola.hoolaclient.dao.ClientLikeContentMapper;
import com.unisharing.hoola.hoolacommon.model.LikeContentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service("clientLikeContentService")
public class ClientLikeContentServiceImpl implements IClientLikeContentService {
    @Autowired
	private ClientLikeContentMapper clientLikeContentMapper;
	
	@Override
	public void insertLikeContentModel(LikeContentModel likeContentModel) {
		clientLikeContentMapper.insertLikeContentModel(likeContentModel);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("cid", likeContentModel.getCid());
		map.put("num", -1);//评论数+1 这里数据库是更新的是-减，所以这里传了一个负数
		clientLikeContentMapper.updateContentLikeNum(map);

	}

	@Override
	public void deleteLikeContentModel(LikeContentModel likeContentModel) {
		clientLikeContentMapper.deleteLikeContentModel(likeContentModel);
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("cid", likeContentModel.getCid());
		map.put("num", 1);//评论数-1
		clientLikeContentMapper.updateContentLikeNum(map);

	}

	public ClientLikeContentMapper getClientLikeContentMapper() {
		return clientLikeContentMapper;
	}

	public void setClientLikeContentMapper(
			ClientLikeContentMapper clientLikeContentMapper) {
		this.clientLikeContentMapper = clientLikeContentMapper;
	}
	
	

}
