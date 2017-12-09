package com.unisharing.hoola.hooladatacenter.service.group;

import com.alibaba.fastjson.JSON;
import com.unisharing.hoola.hoolaclient.service.group.IClientGroupService;
import com.unisharing.hoola.hoolacommon.bean.GroupBean;
import com.unisharing.hoola.hoolacommon.model.GroupModel;
import com.unisharing.hoola.hoolaredis.key.JMSKeyManager;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupServiceImpl extends BaseRedisService implements IGroupService {
	@Autowired
	IClientGroupService clientGroupService;

	@SuppressWarnings("unchecked")
	@Override
	public void handleGroupAdd() {
		String json = null;
		GroupBean group = null;
		json = (String)jmsTemplate.boundListOps(JMSKeyManager.getGroupAddListKey()).rightPop();
		while(json != null){
			group = JSON.parseObject(json, GroupBean.class);
			if(group != null){
				GroupModel groupModel = new GroupModel();
				groupModel.setCreateTime(System.currentTimeMillis());
				groupModel.setGroupName(group.getGroupName());
				groupModel.setUid(group.getUid());
				clientGroupService.insertGroupModel(groupModel);
				//添加组成员
				String uids = group.getUids();
				if (!StringUtils.isEmpty(uids)){
					String[] fuids = uids.split(",");
					for (String fuid : fuids){
						if (!StringUtils.isEmpty(fuid)){
							try{
								clientGroupService.insertGroupRelation(group.getGroupName(), group.getUid(), Long.parseLong(fuid));
							}catch(Exception e){
								
							}
						}
					}
				}
			}
			json = (String)jmsTemplate.boundListOps(JMSKeyManager.getGroupAddListKey()).rightPop();
		}
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public void handleGroupEdit() {
		
		String json = null;
		GroupBean group = null;
		json = (String)jmsTemplate.boundListOps(JMSKeyManager.getGroupEditListKey()).rightPop();
		while(json != null){
			group = JSON.parseObject(json, GroupBean.class);
			if(group != null){
				clientGroupService.updateGroupModel(group.getGroupName(), group.getOldGroupName(), group.getUid());
			}
			json = (String)jmsTemplate.boundListOps(JMSKeyManager.getGroupEditListKey()).rightPop();
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleGroupDelete() {
		String json = null;
		GroupBean group = null;
		json = (String)jmsTemplate.boundListOps(JMSKeyManager.getGroupDeleteListKey()).rightPop();
		while(json != null){
			group = JSON.parseObject(json, GroupBean.class);
			if(group != null){
				clientGroupService.deleteGroupModel(group.getUid(), group.getGroupName());
			}
			json = (String)jmsTemplate.boundListOps(JMSKeyManager.getGroupDeleteListKey()).rightPop();
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void handleGroupUsersAdd() {
		String json = null;
		GroupBean group = null;
		json = (String)jmsTemplate.boundListOps(JMSKeyManager.getGroupUsersAddListKey()).rightPop();
		while(json != null){
			group = JSON.parseObject(json, GroupBean.class);
			if(group != null){
				String uids = group.getUids();
				String[] fuids = uids.split(",");
				String[] groups = group.getGroupName().split(",");
				for (String groupName :groups){
					if (StringUtils.isNotEmpty(groupName)){
						for (String fuid : fuids){
							if (!StringUtils.isEmpty(fuid)){
								try{
									clientGroupService.insertGroupRelation(groupName, group.getUid(), Long.parseLong(fuid));
								}catch(Exception e){
									
								}
							}
						}
					}
				}
			}
			json = (String)jmsTemplate.boundListOps(JMSKeyManager.getGroupUsersAddListKey()).rightPop();
		}
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public void handleGroupUsersDelete() {
		String json = null;
		GroupBean group = null;
		json = (String)jmsTemplate.boundListOps(JMSKeyManager.getGroupUsersDeleteListKey()).rightPop();
		while(json != null){
			group = JSON.parseObject(json, GroupBean.class);
			if(group != null){
				try{
					//单条删除
					clientGroupService.deleteGroupRelation(group.getGroupName(), group.getUid(), Long.parseLong(group.getUids()));
				}catch(Exception e){
					
				}
			}
			json = (String)jmsTemplate.boundListOps(JMSKeyManager.getGroupUsersDeleteListKey()).rightPop();
		}
		
	}


	public void setClientGroupService(IClientGroupService clientGroupService) {
		this.clientGroupService = clientGroupService;
	}
	
}
