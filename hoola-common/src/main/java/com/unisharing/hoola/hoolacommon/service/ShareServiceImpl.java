package com.unisharing.hoola.hoolacommon.service;

import com.unisharing.hoola.hoolacommon.model.OpenFriend;
import org.springframework.stereotype.Service;

import java.util.List;

//todo implement late
@Service("shareService")
public class ShareServiceImpl implements IShareService {
    public boolean shareToSina(String images, String content, String accessToken) {
        return false;
    }

    public List<OpenFriend> loadSinaFollows(String openUserId, String accessToken) {
        return null;
    }

    public List<OpenFriend> loadQQFollows(String openUserId, String token) {
        return null;
    }

    public void shareToQQWeibo(String imagePath, String content, String token, String openUserId) {

    }

    public void shareToQZone(String title, String content, String url, String playUrl, String imageUrl, String token, String openUserId) {

    }

    public void sinaFollow(String accessToken) {

    }
}
