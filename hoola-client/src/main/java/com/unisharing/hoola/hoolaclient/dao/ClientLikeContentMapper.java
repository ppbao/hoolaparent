package com.unisharing.hoola.hoolaclient.dao;



import com.unisharing.hoola.hoolacommon.model.LikeContentModel;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public interface ClientLikeContentMapper {
	
	void insertLikeContentModel(LikeContentModel likeContentModel);//插入喜欢表
	void updateContentLikeNum(Map<String, Object> map);//更新评论数
	void deleteLikeContentModel(LikeContentModel likeContentModel);//取消喜欢

}
