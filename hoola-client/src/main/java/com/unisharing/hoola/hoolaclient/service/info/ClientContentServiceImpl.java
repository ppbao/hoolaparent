package com.unisharing.hoola.hoolaclient.service.info;



import com.unisharing.hoola.hoolaclient.dao.ClientContentMapper;
import com.unisharing.hoola.hoolacommon.model.*;
import com.unisharing.hoola.hoolacommon.utils.HoolaStringUtils;
import com.unisharing.hoola.hoolaredis.service.content.IRedisContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
@Service("clientContentService")
public class ClientContentServiceImpl implements IClientContentService {
    @Autowired
	ClientContentMapper clientContentMapper;
	@Autowired
	private IRedisContentService redisContentService;

	public ClientContentMapper getClientContentMapper() {
		return clientContentMapper;
	}

	public void setClientContentMapper(ClientContentMapper clientContentMapper) {
		this.clientContentMapper = clientContentMapper;
	}
	
	

	@Override
	public void updateContentStatusToDelete(long cid) {
		ContentModel contentModel=clientContentMapper.getContentRecommend(cid);
		if(contentModel!=null){
			clientContentMapper.deleteRecommendModel(cid);
			redisContentService.deleteContentFindList(cid);
		}
		clientContentMapper.updateContent(cid);
	}
	
	

	@Override
	public ContentModel getContentModel(long cid) {
		return clientContentMapper.getContentModel(cid);
	}

	@Override
	public void updateContentMd5(ContentModel contentModel) {
		clientContentMapper.updateContentMd5(contentModel);
	}
	

	@Override
	public void updateContentShowTimes(long cid, int st) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("cid", cid);
		map.put("st", st);
		clientContentMapper.updateContentShowTimes(map);
	}
	

	@Override
	public void updateContentSinaShareNum(long cid, int num) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("cid", cid);
		map.put("num", num);
		clientContentMapper.updateContentSinaShareNum(map);
	}

	@Override
	public void updateContentQuanShareNum(long cid, int num) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("cid", cid);
		map.put("num", num);
		clientContentMapper.updateContentQuanShareNum(map);	
	}

	@Override
	public void insertContentInfo(ContentModel contentModel) {
		clientContentMapper.insertContentInfo(contentModel);
		ContentTagModel contentTagModel = new ContentTagModel();
		contentTagModel.setCid(contentModel.getCid());
		String description = contentModel.getDescription() ;
		String substr = "";
		Set<String> set = HoolaStringUtils.getTags(description);
		for (Iterator<String> iterator = set.iterator(); iterator
				.hasNext();) {
			substr = iterator.next().replaceAll("#", "").trim();
			TagModel tagModel = clientContentMapper.selectTagInfo(substr);
			if (tagModel == null) {
				TagModel tag = new TagModel();
				tag.setTagText(substr);
				clientContentMapper.insertTagInfo(tag);
				contentTagModel.setTid(tag.getTid());
			} else {
				contentTagModel.setTid(tagModel.getTid());
			}
			clientContentMapper.insertContentTag(contentTagModel);
		}
		if(contentModel.getActiveId()!=0){
			SubjectContentModel subjectContentModel=new SubjectContentModel();
			subjectContentModel.setSid(contentModel.getActiveId());
			subjectContentModel.setCid(contentModel.getCid());
			clientContentMapper.insertSubjectContent(subjectContentModel);
		}
		
	}

	@Override
	public void insertShareContent(LogShareModel logShareModel) {
		clientContentMapper.insertShareContent(logShareModel);
		
	}

	@Override
	public void insertShowContent(LogShowModel logShowModel) {
		clientContentMapper.insertShowContent(logShowModel);
		
	}

	public void setRedisContentService(IRedisContentService redisContentService) {
		this.redisContentService = redisContentService;
	}
	
	
	
}
