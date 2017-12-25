package com.unisharing.hoola.hoolaredis.service.user;


import com.unisharing.hoola.hoolacommon.model.BindModel;
import com.unisharing.hoola.hoolacommon.model.SettingModel;
import com.unisharing.hoola.hoolacommon.model.UserModel;
import com.unisharing.hoola.hoolacommon.utils.DateUtil;
import com.unisharing.hoola.hoolaredis.key.FriendShipKeyManager;
import com.unisharing.hoola.hoolaredis.key.SystemKeyManager;
import com.unisharing.hoola.hoolaredis.key.UserKeyManager;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.hash.DecoratingStringHashMapper;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.data.redis.hash.JacksonHashMapper;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.RedisMap;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.*;

@Service("userRedisService")
public class RedisUserServiceImpl extends BaseRedisService implements IRedisUserService  {

	 Log logger = LogFactory.getLog(RedisUserServiceImpl.class);


	 /**
	  * public interface HashMapper<T, K, V> {
	 Map<K, V> toHash(T var1);

     T fromHash(Map<K, V> var1);
     }
      HashMapper 接口有两个功能，一个是tohash将对象转换为一个Map,另外一个是从fromHash从map组合成一个对象
     *
     *
     * 需求：

     1，保存一个key-value形式的结构到redis

     2，把一个对象保存成hash形式的结构到redis
     代码如下：

     // 保存key-value值
     pushFrequencyTemplate.opsForValue().set("test_key", "test_value111");
     // 读取刚才保存的key-value值
     System.out.println(pushFrequencyTemplate.opsForValue().get("test_key"));


     // 把对象保存成hash
     Map<String, String> map = frequencyMapper.toHash(frequency);
     pushFrequencyTemplate.opsForHash().putAll("push_frequency", map);
     // 把刚才保存的hash读出来，并显示
     Map<String, String> redisMap = pushFrequencyTemplate.opsForHash().entries("push_frequency");
     RedisPushFrequencyEntity redisFrequency = frequencyMapper.fromHash(redisMap);
     System.out.println(redisMap);
     问题1：

     声明一个redisTemplate，测试是否可以把对象保存成hash，并从hash还原成对象。

     只设置ConnectionFactory，其它什么也不设置。代码如下：

     @Bean(name = "pushFrequencyTemplate")
     public <String, V> RedisTemplate<String, V> getPushFrequencyTemplate() {
     RedisTemplate<String, V> redisTemplate = new RedisTemplate<String, V>();
     redisTemplate.setConnectionFactory(factory);
     return redisTemplate;
     }

     *
     */

	private final HashMapper<UserModel, String, String> userMapper = new DecoratingStringHashMapper<UserModel>(
			new JacksonHashMapper<UserModel>(UserModel.class));
	private final HashMapper<BindModel, String, String> bindMapper = new DecoratingStringHashMapper<BindModel>(
			new JacksonHashMapper<BindModel>(BindModel.class));
	private final HashMapper<SettingModel,String,String> settingMapper = new DecoratingStringHashMapper<SettingModel>(
			new JacksonHashMapper<SettingModel>(SettingModel.class));
	
	
	@Override
	//HashMapper fromHash  T fromHash(Map<K, V> var1); 从一个Map中获得对象
	public UserModel getUser(long uid) {
		return userMapper.fromHash(getUserMap(uid));
		
	}
	@SuppressWarnings("unchecked")
	private RedisMap<String, String> getUserMap(long uid) {
		return new DefaultRedisMap<String, String>(UserKeyManager.getUserInfoKey(uid), userTemplate);
	}
	@SuppressWarnings("unchecked")
	private RedisMap<String, String> getBindInfoMap(long uid, int openType) {
		return new DefaultRedisMap<String, String>(UserKeyManager.getUserBindInfoKey(uid,openType), userTemplate);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void insertUser(UserModel user) {
		if (user.getUid() == 0){
			RedisAtomicLong userIdCounter = new RedisAtomicLong(UserKeyManager.getUserIdKey(),super.userTemplate.getConnectionFactory());
			long uid = userIdCounter.incrementAndGet();
			user.setUid(uid);
		}
		//绑定key以后hash操作，将UserModel对象转化为hash存储到redis
		BoundHashOperations<String, String, Object> hopts = super.userTemplate.boundHashOps(UserKeyManager.getUserInfoKey(user.getUid()));
		hopts.putAll(userMapper.toHash(user));
		
		if (StringUtils.hasText(user.getEmail())){
			//插入用户Email 和 Uid的对应关系
            //首先掉用绑定key 获得boundvalueOps，然后设置yong BoundValueOperations
			userTemplate.boundValueOps(UserKeyManager.getUserIdByEmailKey(user.getEmail())).set(String.valueOf(user.getUid()));
		}
		
		//插入用户昵称和id对应关系
		if(StringUtils.hasText(user.getNickName())){
			userTemplate.boundValueOps(UserKeyManager.getUserIdByNickName(user.getNickName().trim())).set(String.valueOf(user.getUid()));
		}
		
		//插入 Token 和 Uid 的对应关系  kv:user:token:123 uid
		userTemplate.boundValueOps(UserKeyManager.getUserIdByTokenKey(user.getToken())).set(String.valueOf(user.getUid()));
		 
		//插入新用户List
		userTemplate.boundListOps(UserKeyManager.getUserByTimeKey()).leftPush(String.valueOf(user.getUid()));
		
		
	}
	
	/*@SuppressWarnings("unchecked")
	@Override
	public void updateNickName(String nickName,long uid) {
		userTemplate.boundHashOps(UserKeyManager.getUserInfoKey(uid)).put("nickName", nickName);
	}*/
	@SuppressWarnings("unchecked")
	@Override
	public void bindUserInfo(BindModel model) {
		//插入第三方平台和自身用户ID的对应关系
		userTemplate.boundValueOps(UserKeyManager.getUserIdBy3thKey(model.getOpenType()+"", model.getOpenUid())).set(String.valueOf(model.getUid()));
		//插入绑定信息 主意 这个key的Uid 为Hoola平台ID
		userTemplate.boundHashOps(UserKeyManager.getUserBindInfoKey(model.getUid(),model.getOpenType())).putAll(bindMapper.toHash(model));
	}
	
	@Override
	public BindModel getBindInfo(long uid,int openType) {
		
		//插入绑定信息
		return bindMapper.fromHash(getBindInfoMap(uid,openType));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public UserModel getUser(int openType, String openUid) {
		String uid = (String)userTemplate.boundValueOps(UserKeyManager.getUserIdBy3thKey(openType+"", openUid)).get();
		if (uid != null){
			return this.getUser(Long.valueOf(uid));
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public UserModel getUser(String email) {
		if (email != null){
			String uid = (String)userTemplate.boundValueOps(UserKeyManager.getUserIdByEmailKey(email.toLowerCase())).get();
			if (uid != null){
				return this.getUser(Long.valueOf(uid));
			}
		}
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public long getUid(String token) {
		try{String uid = (String)userTemplate.boundValueOps(UserKeyManager.getUserIdByTokenKey(token)).get();
			return Long.valueOf(uid);
		}catch (Exception e) {
			return 0;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public SettingModel getUserSetting(long uid) {
		return settingMapper.fromHash(new DefaultRedisMap<String, String>(SystemKeyManager.getUserSettingKey(uid), userTemplate));
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<UserModel> loadAllUser() {
		List<UserModel> users = new ArrayList<UserModel>();
		Set keys = userTemplate.keys("hm:user:47834:data");
		Iterator it = keys.iterator();
		while(it.hasNext()){
			users.add(userMapper.fromHash(new DefaultRedisMap<String, String>((String)it.next(), userTemplate)));
		}
		return users;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void update(UserModel model) {
		//更新用户名和ID的对应关系
		try{
			UserModel user = this.getUser(model.getUid());
			if (user != null && user.getUid()>0 && (!user.getNickName().equals(model.getNickName()))){
				userTemplate.rename(UserKeyManager.getUserIdByNickName(user
						.getNickName().trim()), UserKeyManager
						.getUserIdByNickName(model.getNickName().trim()));
			}
		}catch (Exception e) {
			logger.error("更新用户名和ID的对应关系的名字出错！Message="+e.getMessage());
		}
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("nickName", model.getNickName());
		map.put("signature", model.getSignature());
		map.put("avatarUrl", model.getAvatarUrl());
		String videoUrl = model.getVideoUrl();
		map.put("videoUrl", videoUrl == null ? "" : videoUrl);
		String videoFaceUrl = model.getVideoFaceUrl();
		map.put("videoFaceUrl", videoFaceUrl == null ? "" : videoFaceUrl);
		userTemplate.boundHashOps(UserKeyManager.getUserInfoKey(model.getUid())).putAll(map);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void bindMobile(long uid, String mobile) {
		userTemplate.boundValueOps(UserKeyManager.getUserMobileKey(mobile)).set(String.valueOf(uid));
		
		userTemplate.boundHashOps(UserKeyManager.getUserInfoKey(uid)).put("mobile", mobile);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void updateUserLastVist(long uid, long time, String function) {
		userTemplate.boundHashOps(UserKeyManager.getUserLastVisit(uid)).put(function, String.valueOf(time));
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean mobileIsBind(String mobile) {
		
		Object obj = userTemplate.boundValueOps(UserKeyManager.getUserMobileKey(mobile)).get();
		return obj != null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void setUserSetting(long uid, SettingModel setting) {
		userTemplate.boundHashOps(UserKeyManager.getUserSettingKey(uid)).putAll(settingMapper.toHash(setting));
	}
	@SuppressWarnings("unchecked")
	@Override
	public void addUserDeviceToken(long uid, String deviceToken) {
		if (deviceToken !=null && deviceToken.trim().length() == 64){
			userTemplate.boundValueOps(UserKeyManager.getUserDeviceTokenKey(uid)).set(deviceToken);
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public Set<String> getUserDeviceToken(long uid) {
		String deviceToken = (String)userTemplate.boundValueOps(UserKeyManager.getUserDeviceTokenKey(uid)).get();
		Set<String> deviceTokens = new HashSet<String>();
		if (deviceToken != null){
			String[] tokens = deviceToken.split(",");
			for (String token : tokens){
				if (token.trim().length() == 64){
					deviceTokens.add(token);
				}
			}
		}
		return deviceTokens;
	}
	@SuppressWarnings("unchecked")
	@Override
	public UserModel getUserByMobile(String mobile) {
		String uidStr = (String)userTemplate.boundValueOps(UserKeyManager.getUserMobileKey(mobile)).get();
		try{
			long uid = Long.parseLong(uidStr);
			UserModel user = this.getUser(uid);
			return user;
		}catch (Exception e) {
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public void updateBanner(String banner, long uid) {
		userTemplate.boundHashOps(UserKeyManager.getUserInfoKey(uid)).put("backgroundUrl", banner);
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public void updateUserStatus(long uid, int status) {
		userTemplate.boundHashOps(UserKeyManager.getUserInfoKey(uid)).put("status", String.valueOf(status));
		
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public boolean isFamous(long uid) {
		if (uid == 1){
			return true;//趣拍采用拉去的方式
		}
		Boolean bool = userRelationTemplate.boundSetOps(FriendShipKeyManager.getFamousUserKey())
				.isMember(String.valueOf(uid));
		
		return bool;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void deleteUserDeviceToken(long uid) {
		userTemplate.delete(UserKeyManager.getUserDeviceTokenKey(uid));
	}
	@Override
	public long getUserId() {
		RedisAtomicLong userIdCounter = new RedisAtomicLong(
				UserKeyManager.getUserIdKey(),
				super.userTemplate.getConnectionFactory());
		long uid = userIdCounter.incrementAndGet();
		return uid;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void updateHoolaToken(long uid, String token) {
		UserModel user = this.getUser(uid);
		if (user != null && user.getUid() > 0){
			userTemplate.boundHashOps(UserKeyManager.getUserInfoKey(uid)).put("token",token);
			//插入 Token 和 Uid 的对应关系
			userTemplate.boundValueOps(UserKeyManager.getUserIdByTokenKey(token)).set(String.valueOf(user.getUid()));
		}
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public void addLikeSynMumOneDay(long uid) {
		userTemplate.boundValueOps(UserKeyManager.getUserLikeSysNumKey(uid)).increment(1);
		userTemplate.boundValueOps(UserKeyManager.getUserLikeSysNumKey(uid)).expireAt(DateUtil.nextDay(new Date()));
	}
	@SuppressWarnings("unchecked")
	@Override
	public int countLikeSynNumOneDay(long uid) {
		Object obj = userTemplate.boundValueOps(UserKeyManager.getUserLikeSysNumKey(uid)).get();
		int count = 0;
		try{
			count = Integer.parseInt(String.valueOf(obj));
		}catch (Exception e) {
			count = 0;
		}
		return count;
	}
	@SuppressWarnings("unchecked")
	@Override
	public UserModel getUserByNickName(String nickName) {
		Object obj = userTemplate.boundValueOps(UserKeyManager.getUserIdByNickName(nickName)).get();
		if (obj != null){
			try{
				long uid = Long.parseLong((String)obj);
				return this.getUser(uid);
			}catch (Exception e) {
				return null;
			}
		}
		
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void addNickName(String nickName ,long uid) {
		//插入用户昵称和id对应关系
		if(StringUtils.hasText(nickName)){
			userTemplate.boundValueOps(UserKeyManager.getUserIdByNickName(nickName.trim())).set(String.valueOf(uid));
		}
		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<SettingModel> loadAllSettings() {
		List<SettingModel> settings = new ArrayList<SettingModel>();
		Set keys = userTemplate.keys("hm:user:*:setting");
		Iterator it = keys.iterator();
		while(it.hasNext()){
			settings.add(settingMapper.fromHash(new DefaultRedisMap<String, String>((String)it.next(), userTemplate)));
		}
		return settings;
	}
	@SuppressWarnings("unchecked")
	@Override
	public void updateUserTalentInfo(long uid, int isTalent, String talentDesc) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("isTalent", String.valueOf(isTalent));
		map.put("talentDesc", talentDesc);
		userTemplate.boundHashOps(UserKeyManager.getUserInfoKey(uid)).putAll(map);
	}
}
