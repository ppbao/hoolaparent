package com.unisharing.hoola.hoolaclient.web;

import com.unisharing.hoola.hoolaclient.service.user.IClientUserService;
import com.unisharing.hoola.hoolacommon.HoolaErrorCode;
import com.unisharing.hoola.hoolacommon.NoticeMessage;
import com.unisharing.hoola.hoolacommon.Result;
import com.unisharing.hoola.hoolacommon.model.ActionModel;
import com.unisharing.hoola.hoolacommon.model.SettingModel;
import com.unisharing.hoola.hoolacommon.model.UserModel;
import com.unisharing.hoola.hoolacommon.submit.Pager;
import com.unisharing.hoola.hoolacommon.utils.AliyunUploadUtils;
import com.unisharing.hoola.hoolacommon.utils.HoolaConfig;
import com.unisharing.hoola.hoolacommon.utils.HoolaUtils;
import com.unisharing.hoola.hoolacommon.vo.ActionForm;
import com.unisharing.hoola.hoolacommon.vo.UserDetailForm;
import com.unisharing.hoola.hoolaredis.service.badword.IBadwordService;
import com.unisharing.hoola.hoolaredis.service.content.IRedisContentService;
import com.unisharing.hoola.hoolaredis.service.message.IRedisMessageService;
import com.unisharing.hoola.hoolaredis.service.timeline.IRedisTimelineService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisRelationshipService;
import com.unisharing.hoola.hoolaredis.service.user.IRedisUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class UserController extends BaseController {

    private static int PAGE_SIZE = 20;
    @Autowired
    IRedisTimelineService redisTimelineService;
    @Autowired
    IRedisContentService redisContentService;
    @Autowired
    IRedisRelationshipService redisRelationshipService;
    @Autowired
    IRedisMessageService redisMessageService;
    @Autowired
    IBadwordService badwordService;

    /***
     *
     * go get contents that user created
     * @param uid : userid
     * @param token : token
     * @param page :default 1
     */
    @RequestMapping(value= "/user/center/my",method = RequestMethod.GET, produces = "application/json")
    public Result myUploadContent(@RequestParam(value="uid",required = false, defaultValue = "0")long uid,
                                  @RequestParam(value="page",required = false,defaultValue = "1")int page,
                                  @RequestParam("token")String token){

        long curUid = super.getUid(token);// to retrieve current login user id
        Result result = new Result();
        if (uid == 0){
            uid = curUid;
        }
        Pager pager = new Pager();
        pager.setPage(page);
        pager.setPageSize(PAGE_SIZE);
        List<ActionForm> actions = redisContentService.loadUserContents(uid,curUid,pager);
        result.setCode(200);
        result.setData(actions);
        result.setMessage("succeed!");
        result.setTime(System.currentTimeMillis());
        result.setPages((redisContentService.countUserContents(uid)-1)/ PAGE_SIZE +1);

        return result;

    }

    /****
     *
     *
     *
     * @param uid
     * @param page
     * @param token
     * @return
     */
    @RequestMapping(value = "/user/center/like",method = RequestMethod.GET,produces = "application/json")
    public Result myLikeContent(@RequestParam(value = "uid",required = false,defaultValue = "0") long uid,
                                @RequestParam(value = "page",required = false,defaultValue ="1") int page,
                                @RequestParam(value="token") String token) {

        long curUid =super.getUid(token);
        Result result = new Result();
        if (uid ==0){
            uid = curUid;
        }
        Pager pager = new Pager();
        pager.setPage(page);
        pager.setPageSize(PAGE_SIZE);
        List<ActionForm> actions = redisContentService.loadUserLikeContents(uid,curUid,pager);

        result.setCode(200);
        result.setData(actions);
        result.setMessage("succeed!");
        result.setTime(System.currentTimeMillis());
        result.setPages((redisContentService.countUserLikeContents(uid)-1)/PAGE_SIZE +1);
        return result;
    }

    /****
     *
     *
     * @param uid
     * @param page
     * @param token
     * @return
     */
    @RequestMapping(value = "/user/center/forward",method = RequestMethod.GET, produces = "application/json")
    public  Result myForwardContent(@RequestParam(value = "uid",required = false,defaultValue = "0") long uid,
                                    @RequestParam(value = "page",required = false,defaultValue = "1")int page,
                                    @RequestParam("token") String token){
        long curUid = super.getUid(token);
        if (uid == 0){
            uid = curUid;
        }

        Pager pager = new Pager();
        pager.setPage(page);
        pager.setPageSize(PAGE_SIZE);
        List<ActionForm> actions =redisContentService.loadUserForwardContents(uid,curUid,pager);

        Result result = new Result();
        result.setCode(200);
        result.setData(actions);
        result.setMessage("succeed!");
        result.setTime(System.currentTimeMillis());
        result.setPages((redisContentService.countUserForwardContents(uid)-1)/PAGE_SIZE +1);
        return result;
    }

    /***
     *
     *
     * @param uid
     * @param page
     * @param token
     * @return
     */
    @RequestMapping(value = "/user/center/all",method = RequestMethod.GET,produces = "application/json")
    public Result myAllContent(@RequestParam(value = "uid",required = false,defaultValue = "0") long uid,
                               @RequestParam(value="page",required = false,defaultValue = "1") int page,
                               @RequestParam("token") String token){

        long curUid = super.getUid(token);
        Result result = new Result();
        if (uid ==0 ){
            uid = curUid;
        }
        Pager pager = new Pager();
        pager.setPage(page);
        pager.setPageSize(PAGE_SIZE);
        List<ActionForm> actions = redisContentService.loadUserAllContents(uid,curUid,pager);

        result.setCode(200);
        result.setMessage("succeed!");
        result.setData(actions);
        result.setTime(System.currentTimeMillis());
        result.setPages(redisContentService.countUserAllContents(uid));

        return result;

    }

    /***
     *
     *
     * @param uid
     * @param token
     * @return
     */
    @RequestMapping(value = "/user/center",method = RequestMethod.GET,produces = "application/json")
    public Result userMain(@RequestParam(value = "/user/center",required = false,defaultValue = "0") long uid,
                           @RequestParam("token") String token){
        long curUid = super.getUid(token);
        Result result = new Result();
        if (curUid == 0){
            result.setCode(HoolaErrorCode.NO_LOGIN.getCode());
            result.setData("");
            result.setMessage(HoolaErrorCode.NO_LOGIN.getMessage());
            result.setTime(System.currentTimeMillis());
            return result;
        }

        UserDetailForm user = null;
        if (curUid == uid || uid == 0 ){ //visit own center
            UserModel model = redisUserService.getUser(curUid);
            user  = model.asDetailUserForm();
            user.setFansCount(redisRelationshipService.countFans(curUid));
            user.setFollowsCount(redisRelationshipService.countFollows(curUid));
            user.setFriendsCount(redisRelationshipService.countFriends(curUid));
            int privateContentCount = redisContentService.countUserPrivateContents(curUid);
            int publicContentCount = redisContentService.countUserPublicContents(curUid);
            user.setContentsCount(privateContentCount+publicContentCount);
            user.setIsFans(0);
            user.setIsFollow(0);
            user.setIsBlack(0);
        }
        if (uid !=0 && uid != curUid){ //visit others center
            UserModel userModel = redisUserService.getUser(uid);
            user = userModel.asDetailUserForm();
            if (uid == 1){            // visit hoola center hide all information
                user.setFansCount(0);
                user.setFollowsCount(0);
                user.setFriendsCount(0);
                user.setContentsCount(redisContentService.countUserPublicContents(uid));
            }else {
                user.setFansCount(redisRelationshipService.countFans(uid));
                user.setFollowsCount(redisRelationshipService.countFollows(uid));
                user.setFriendsCount(redisRelationshipService.countFriends(uid));
                user.setContentsCount(redisContentService.countUserPublicContents(uid));
            }
            boolean hasRight = redisMessageService.hasMessageRight(curUid);
            user.setIsFans(((hasRight)||redisRelationshipService.isFollowed(uid,curUid)?1:0));
            user.setIsFollow(redisRelationshipService.isFollowed(curUid,uid)?1:0);
            user.setIsBlack(redisRelationshipService.isBlackUser(curUid,uid)?1:0);
        }
        result.setCode(200);
        result.setMessage("succeed!");
        result.setData(user);
        result.setTime(System.currentTimeMillis());
        return result;
    }

    /****
     *
     * @param page
     * @param token
     * @param type  (all, forward, create,friend,private)
     * @return
     */

    @RequestMapping(value = "/user/timeline",method = RequestMethod.GET,produces = "application/json")
    public Result timeLine(@RequestParam(value = "page",required = false,defaultValue = "1") int page,
                           @RequestParam(value = "token") String token,
                           @RequestParam(value = "type",required = false,defaultValue = "all") String type){


        long uid =super.getUid(token);
        Result result = new Result();
        if (uid == 0 ){
            result.setCode(HoolaErrorCode.NO_LOGIN.getCode());
            result.setData("");
            result.setMessage(HoolaErrorCode.NO_LOGIN.getMessage());
            result.setTime(System.currentTimeMillis());
            return result;
        }

        Pager pager = new Pager();
        pager.setPage(page);
        pager.setPageSize(PAGE_SIZE);
        List<ActionForm> actions = redisTimelineService.loadUserTimeline(uid,pager,type);
        int count = 0;
        if ("private".equals(type)){
            count = redisTimelineService.countUserPrivateContents(uid);
        }else {
            count = redisTimelineService.countUserTimeline(uid);
        }
        result.setCode(200);
        result.setData(actions);
        result.setPages((count-1)/PAGE_SIZE +1);
        result.setMessage("succeed!");
        result.setTime(System.currentTimeMillis());
        return  result;
    }

    /***
     *
     * @param page
     * @param token
     * @return
     */
    @RequestMapping(value = "/user/timeline/new",method = RequestMethod.GET,produces = "applicaton/json")
    public Result timeLineNew(@RequestParam(value = "page",required = false,defaultValue = "1")int page,
                              @RequestParam(value = "token") String token){
        long uid = super.getUid(token);
        Result result = new Result();
        if (uid == 0){
            result.setCode(HoolaErrorCode.NO_LOGIN.getCode());
            result.setData("");
            result.setMessage(HoolaErrorCode.NO_LOGIN.getMessage());
            result.setTime(System.currentTimeMillis());
            return result;
        }
        Pager pager = new Pager();
        pager.setPageSize(PAGE_SIZE);
        pager.setPage(page);
        List<ActionForm> actions = redisTimelineService.loadUserNewestTime(uid);

        int count = redisTimelineService.countUserTimeline(uid);
        result.setCode(200);
        result.setData(actions);
        result.setPages((count-1)/PAGE_SIZE +1);
        result.setMessage("succeed!");
        result.setTime(System.currentTimeMillis());
        return result;
    }

    /***
     *
     *
     * @param avatar
     * @param nickName
     * @param signature
     * @param token
     * @return
     */
    @RequestMapping(value = "/user/edit",method = RequestMethod.POST,produces = "application/json")
    public Result editUser(@RequestParam(value = "avatar",required = false) MultipartFile avatar,
                           @RequestParam("nickname") String nickName,
                           @RequestParam("signature") String signature,
                           @RequestParam("token") String token){
        Result result = valid(nickName,signature);
        if(result!= null){
            return result;
        }
        result = new Result();
        UserModel user = getUser(token);
        if (user != null && user.getStatus()== 0){
            result = new Result();
            result.setCode(HoolaErrorCode.USER_FORBID.getCode());
            result.setData("");
            result.setMessage(HoolaErrorCode.USER_FORBID.getMessage());
            result.setTime(System.currentTimeMillis());
            return result;
        }
        if (avatar != null && avatar.getSize()>0){
            String contentType = avatar.getContentType();
            try{
                if (HoolaConfig.getImageContentType().contains(contentType)) {
                    String key = AliyunUploadUtils.uploadAvatar(avatar.getInputStream(),
                            avatar.getBytes().length,
                            avatar.getContentType());

                    if (nickName != null && nickName.contains("hoola")) {
                        nickName.replace("hoola", "");
                        if (nickName.trim().length() < 2) {
                            nickName += HoolaUtils.getRamdCode();
                        }
                    }
                    user.setAvatarUrl(key);
                    user.setNickName(nickName);
                    user.setSignature(signature);
                    redisUserService.update(user);
                    result.setCode(200);
                    result.setData("");
                    result.setPages(0);
                    result.setMessage("update succeed!");
                    result.setTime(System.currentTimeMillis());
                }else{
                    result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
                    result.setData("file type is not supported!");
                    result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
                    result.setPages(0);
                    result.setTime(System.currentTimeMillis());
                }

            }catch (Exception e){
                result.setCode(HoolaErrorCode.SYSTEM_ERROR.getCode());
                result.setData(e.getMessage());
                result.setPages(0);
                result.setMessage(HoolaErrorCode.SYSTEM_ERROR.getMessage());
                result.setTime(System.currentTimeMillis());
            }
        }else {

            user.setNickName(nickName);
            user.setSignature(signature);
            redisUserService.update(user);
            result.setCode(200);
            result.setData("");
            result.setPages(0);
            result.setMessage("update succeed!");
            result.setTime(System.currentTimeMillis());
        }
        if (result.getCode()==200){
            redisJMSMessageService.insertUserEditQueue(user);
            hoolaPublisher.publish(new NoticeMessage(HoolaUtils.getIp(),HoolaUtils.getIp(),NoticeMessage.MessageType.USER_EDIT));
        }
        return  result;
    }

    @RequestMapping(value = "/user/video/delete",method = RequestMethod.POST,produces = "application/json")
    public Result deleteUserVideo(@RequestParam("token") String token){

        Result result = new Result();
        UserModel user = this.getUser(token);
        if (user != null){
            user.setVideoFaceUrl("");
            user.setVideoUrl("");
            try{
                redisUserService.update(user);
                result.setCode(200);
                result.setData("");
                result.setMessage("deleted successfully!");
                result.setTime(System.currentTimeMillis());
            }catch (Exception e){
                result.setCode(HoolaErrorCode.SYSTEM_ERROR.getCode());
                result.setData("system error");
                result.setMessage(HoolaErrorCode.SYSTEM_ERROR.getMessage());
                result.setTime(System.currentTimeMillis());
            }
        }else {
            result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
            result.setData("parameter error");
            result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
            result.setTime(System.currentTimeMillis());
        }
        return  result;
    }

    /****
     *
     *
     * @param token
     * @return
     */
    @RequestMapping(value = "/setting/get",method = RequestMethod.GET,produces = "application/json")
    public Result getSetting(@RequestParam("token") String token){
        Result result = new Result();
        long uid = this.getUid(token);
        SettingModel setting = redisUserService.getUserSetting(uid);
        if (setting == null){
            setting = new SettingModel();
        }
        result.setCode(200);
        result.setData(setting);
        result.setMessage("succeed!");
        result.setTime(System.currentTimeMillis());
        return result;
    }

    @RequestMapping(value = "/setting/like")
    public Result settingUserLikeShare(@RequestParam("like") int like,@RequestParam("token") String token){
        Result result = new Result();
        long uid = this.getUid(token);
        SettingModel setting = redisUserService.getUserSetting(uid);
        if (setting == null){
            setting = new SettingModel();
        }
        setting.setShare(like);
        redisUserService.setUserSetting(uid,setting);
        result.setCode(200);
        result.setData(setting);
        result.setMessage("succeed");
        result.setTime(System.currentTimeMillis());
        return  result;

    }

    /***
     *
     *
     * @param isCamera
     * @param token
     * @return
     */
    @RequestMapping(value = "/setting/camera",method = RequestMethod.POST,produces = "application/json")
    public Result settingIsCamera(@RequestParam("isCamera") int isCamera,@RequestParam("token") String token){
        Result result = new Result();
        long uid = this.getUid(token);
        SettingModel setting = redisUserService.getUserSetting(uid);
        if (setting == null){
            setting = new SettingModel();
        }
        setting.setIsCamera(isCamera);
        redisUserService.setUserSetting(uid,setting);

        result.setCode(200);
        result.setData(setting);
        result.setMessage("succeed!");
        result.setTime(System.currentTimeMillis());
        return result;

    }
    @RequestMapping(value = "/setting/recommend",method = RequestMethod.POST,produces = "application/json")
    public Result settingUserRecommend(int recommend,String token){
        Result result = new Result();
        long uid = this.getUid(token);
        SettingModel setting = redisUserService.getUserSetting(uid);
        if (setting == null){
            setting = new SettingModel();
        }
        setting.setRecommend(recommend);
        redisUserService.setUserSetting(uid,setting);
        result.setCode(200);
        result.setData(setting);
        result.setMessage("succeed!");
        result.setTime(System.currentTimeMillis());
        return result;
    }

    /***
     *
     * @param atMessage
     * @param likeMessage
     * @param commentMessage
     * @param message
     * @param forwardMessage
     * @param token
     * @return
     */
    @RequestMapping(value = "/setting/message",method = RequestMethod.POST,produces = "application/json")
    public Result settingUserMessage(@RequestParam(value = "atMessage",required = false,defaultValue = "1") int atMessage,
                                     @RequestParam(value = "likMessage",required = false,defaultValue = "0") int likeMessage,
                                     @RequestParam(value = "contentMessage",required = false,defaultValue = "1") int commentMessage,
                                     @RequestParam(value = "message",required = false,defaultValue = "1") int message,
                                     @RequestParam(value = "forwardMessage",required = false,defaultValue = "0") int forwardMessage,
                                     @RequestParam("token") String token){
        Result result = new Result();
        long uid = this.getUid(token);
        SettingModel setting = redisUserService.getUserSetting(uid);
        if (setting == null){
            setting = new SettingModel();
        }
        setting.setAtMessage(atMessage);
        setting.setLikeMessage(likeMessage);
        setting.setCommentMessage(commentMessage);
        setting.setMessage(message);
        setting.setForwardMessage(forwardMessage);
        redisUserService.setUserSetting(uid,setting);
        result.setCode(200);
        result.setData(setting);
        result.setMessage("succeed!");
        result.setTime(System.currentTimeMillis());
        return result;

    }
    @RequestMapping(value = "/setting/banner",method = RequestMethod.POST,produces = "application/json")
    public Result settingUserBanner(@RequestParam("banner") MultipartFile banner,String token){
        Result result = new Result();
        long uid = this.getUid(token);
        UserModel user = this.getUser(token);
        if (banner != null && banner.getSize()>0){
            String contentType = banner.getContentType();
            try{
                if (HoolaConfig.getImageContentType().contains(contentType)){
                    String key = AliyunUploadUtils.uploadUserBanner(banner.getInputStream(),banner.getBytes().length,
                            banner.getContentType());
                    result.setCode(200);
                    result.setData("");
                    result.setPages(0);
                    result.setMessage("succeed!");
                    result.setTime(System.currentTimeMillis());

                }else{
                    result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
                    result.setData("文件格式不支持！");
                    result.setPages(0);
                    result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
                    result.setTime(System.currentTimeMillis());
                }
            }catch (Exception e){
                result.setCode(HoolaErrorCode.SYSTEM_ERROR.getCode());
                result.setData(e.getMessage());
                result.setPages(0);
                result.setMessage(HoolaErrorCode.SYSTEM_ERROR.getMessage());
                result.setTime(System.currentTimeMillis());
                return  result;
            }
        }else {
            result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
            result.setData("back image is empty");
            result.setPages(0);
            result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
            result.setTime(System.currentTimeMillis());
        }
        return  result;
    }
    private Result valid(String nickName,String signature){

        Result result = null;
        if (!StringUtils.hasText(nickName)){
            result = new Result();
            result.setCode(HoolaErrorCode.PARAMETER_ERROR.getCode());
            result.setData("nickname can not be empty!");
            result.setPages(0);
            result.setMessage(HoolaErrorCode.PARAMETER_ERROR.getMessage());
            result.setTime(System.currentTimeMillis());
            return result;
        }
        String badWord = badwordService.hasBadWord(nickName);
        if (StringUtils.hasText(badWord)){
            result = new Result();
            result.setCode(HoolaErrorCode.SENSITIVE.getCode());
            result.setData("nickName contains sensitive word："+badWord);
            result.setPages(0);
            result.setMessage("nickName"+HoolaErrorCode.SENSITIVE.getMessage());
            result.setTime(System.currentTimeMillis());
            return result;
        }
        badWord = badwordService.hasBadWord(signature);
        if (StringUtils.hasText(badWord)){
            result = new Result();
            result.setCode(HoolaErrorCode.SENSITIVE.getCode());
            result.setData("signature contains sensitive word："+badWord);
            result.setPages(0);
            result.setMessage("signature"+HoolaErrorCode.SENSITIVE.getMessage());
            result.setTime(System.currentTimeMillis());
            return result;
        }
        return result;
    }






}
