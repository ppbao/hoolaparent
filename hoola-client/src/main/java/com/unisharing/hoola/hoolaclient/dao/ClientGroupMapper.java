package com.unisharing.hoola.hoolaclient.dao;



import com.unisharing.hoola.hoolacommon.model.GroupModel;
import com.unisharing.hoola.hoolacommon.model.GroupRelationModel;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public interface ClientGroupMapper {
	void insertGroupModel(GroupModel groupModel);//插入 用户分组信息
	void deleteGroupModel(Map<String, Object> map);//删除用户分组信息
	GroupModel getGroupModel(GroupModel groupModel);//查询用户分组单一对象
	void insertGroupRelation(GroupRelationModel groupRelationModel);//把用户添加到分组
	void deleteGroupRelation(GroupRelationModel groupRelationModel);//把用户从分组中移除
	void updateGroupModel(Map<String, Object> map);//更改组名
}
