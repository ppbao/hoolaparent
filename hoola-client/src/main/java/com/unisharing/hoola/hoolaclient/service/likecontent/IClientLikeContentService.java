package com.unisharing.hoola.hoolaclient.service.likecontent;


import com.unisharing.hoola.hoolacommon.model.LikeContentModel;

public interface IClientLikeContentService {
	void insertLikeContentModel(LikeContentModel likeContentModel);//增加喜欢
	void deleteLikeContentModel(LikeContentModel likeContentModel);//取消喜欢

}
