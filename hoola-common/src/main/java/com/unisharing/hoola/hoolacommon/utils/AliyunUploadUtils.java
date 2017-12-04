package com.unisharing.hoola.hoolacommon.utils;

import com.aliyun.openservices.ClientConfiguration;
import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.OSSObject;
import com.aliyun.openservices.oss.model.ObjectMetadata;
import com.aliyun.openservices.oss.model.PutObjectResult;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;

public class AliyunUploadUtils {
	
//	public static String VIDEO_DOMAIN = "http://duanqu-video.bucket.oss.aliyuncs.com/";
//	public static String VIDEO_HD_DOMAIN = "http://duanqu-video-hd.bucket.oss.aliyuncs.com/";
//	public static String THUMBNAIL_DOMAIN = "http://duanqu-thumbnail.bucket.oss.aliyuncs.com/";
//	public static String AVATAR_DOMAIN = "http://duanqu-avatar.bucket.oss.aliyuncs.com/";
	

	private static final String OSS_ENDPOINT = "http://oss.aliyuncs.com/";
	private final static String ACCESS_ID = "u58zpr7g39l7ak8ynonmnxzi";
	private final static String ACCESS_KEY = "98pZP1p9jqgAxRdMRmsHyzUpyfc=";
	private static OSSClient client = null;

//	private static String VIDEO_BUCKET = "duanqu-video";
//	private static String VIDEO_BUCKET_HD = "duanqu-video-hd";
//	private static String THUMBNAIL_BUCKET = "duanqu-thumbnail";
//	private static String AVATAR_BUCKET = "duanqu-avatar";
	static {
		ClientConfiguration config = new ClientConfiguration();
		client = new OSSClient(OSS_ENDPOINT, ACCESS_ID, ACCESS_KEY, config);
	}

	/**
	 * 上传流畅版本视频
	 * @param input
	 * @param length
	 * @param contentType
	 * @return
	 * @throws Exception
	 */
	public static String uploadVideo(InputStream input, int length,
			String contentType) throws Exception {

		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(length);
		String folder = HoolaUtils.getFolder();
		String fileName = UUID.randomUUID().toString();
		String key = folder + "/" + fileName + ".mp4";
		objectMeta.setContentType(contentType);
		PutObjectResult result = client.putObject(HoolaConfig.getAliyunVideoBucketName(), key, input, objectMeta);
		if (result != null && result.getETag() != null){
			return key;
		} else {
			return null;
		}
	}
	
	/**
	 * 上传高清视频
	 * @param input
	 * @param length
	 * @param contentType
	 * @return
	 * @throws Exception
	 */
	public static String uploadHDVideo(InputStream input, int length,
			String contentType) throws Exception {
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(length);
		String folder = HoolaUtils.getFolder();
		String fileName = UUID.randomUUID().toString();
		String key = folder + "/" + fileName + ".mp4";
		objectMeta.setContentType(contentType);
		PutObjectResult result = client.putObject(HoolaConfig.getAliyunHDVideoBucketName(), key, input, objectMeta);
		if (result != null && result.getETag() != null){
			return key;
		} else {
			return null;
		}
		
	}
	
	/**
	 * 上传视频首图
	 * @param input
	 * @param length
	 * @param contentType
	 * @return
	 * @throws Exception
	 */
	public static String uploadThumbnail(InputStream input, long length,
			String contentType) throws Exception {

		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(length);
		String folder = HoolaUtils.getFolder();
		String fileName = UUID.randomUUID().toString();
		String key = folder + "/" + fileName+ ".jpg";
		objectMeta.setContentType(contentType);
		PutObjectResult result = client.putObject(HoolaConfig.getAliyunThumbnailBucketName(), key, input, objectMeta);
		if (result != null && result.getETag() != null){
			return key;
		} else {
			return null;
		}
		
	}
	/**
	 * 后台上传活动视频
	 * @param input
	 * @param length
	 * @param contentType
	 * @return
	 * @throws Exception
	 */
	public static String uploadSystemHDVideo(InputStream input, int length,
			String contentType) throws Exception {
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(length);
		String folder = HoolaUtils.getFolder();
		String fileName = UUID.randomUUID().toString();
		String key = folder + "/" + fileName + ".mp4";
		objectMeta.setContentType(contentType);
		PutObjectResult result = client.putObject(HoolaConfig.getAliyunHDVideoBucketName(), key, input, objectMeta);
		if (result != null && result.getETag() != null){
			return key;
		} else {
			return null;
		}
		
	}
	
	/**
	 * 后台上传视频首图
	 * @param input
	 * @param length
	 * @param contentType
	 * @return
	 * @throws Exception
	 */
	public static String uploadSystemThumbnail(InputStream input, int length,
			String contentType) throws Exception {

		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(length);
		String folder = HoolaUtils.getFolder();
		String fileName = UUID.randomUUID().toString();
		String key = folder + "/" + fileName+ ".jpg";
		objectMeta.setContentType(contentType);
		PutObjectResult result = client.putObject(HoolaConfig.getAliyunSystemImagesBucketName(), key, input, objectMeta);
		if (result != null && result.getETag() != null){
			return key;
		} else {
			return null;
		}
		
	}
	
	/**
	 * 上传头像
	 * @param input
	 * @param length
	 * @param contentType
	 * @return
	 * @throws Exception
	 */
	public static String uploadAvatar(InputStream input, int length,
			String contentType) throws Exception {
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(length);
		String folder = HoolaUtils.getFolder();
		String fileName = UUID.randomUUID().toString();
		String key = folder + "/" + fileName + ".jpg";
		objectMeta.setContentType(contentType);
		PutObjectResult result = client.putObject(HoolaConfig.getAliyunAvatarBucketName(), key, input, objectMeta);
		if (result != null && result.getETag() != null){
			return key;
		} else {
			return null;
		}
	}
	
	/**
	 * 上传头像
	 * @param input
	 * @param length
	 * @param contentType
	 * @return
	 * @throws Exception
	 */
	public static String uploadUserBanner(InputStream input, int length,
			String contentType) throws Exception {
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(length);
		String folder = "banner/" + HoolaUtils.getFolder();
		String fileName = UUID.randomUUID().toString();
		String key = folder + "/" + fileName + ".jpg";
		objectMeta.setContentType(contentType);
		PutObjectResult result = client.putObject(HoolaConfig.getAliyunAvatarBucketName(), key, input, objectMeta);
		if (result != null && result.getETag() != null){
			return key;
		} else {
			return null;
		}
	}
	
	/**
	 * 后台运营图片存储
	 * @param input
	 * @param length
	 * @param contentType
	 * @return
	 * @throws Exception
	 */
	public static String uploadSystemImages(InputStream input, int length,
			String contentType) throws Exception {
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(length);
		String folder = HoolaUtils.getFolder();
		String fileName = UUID.randomUUID().toString();
		String key = folder + "/" + fileName + ".jpg";
		objectMeta.setContentType(contentType);
		PutObjectResult result = client.putObject(HoolaConfig.getAliyunSystemImagesBucketName(), key, input, objectMeta);
		if (result != null && result.getETag() != null){
			return key;
		} else {
			return null;
		}
	}
	
	/**
	 * 后台上传背景音乐
	 * @param input
	 * @param length
	 * @param contentType
	 * @return
	 * @throws Exception
	 */
	public static String uploadSystemMusic(InputStream input, int length,
			String contentType) throws Exception {
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(length);
		String fileName = UUID.randomUUID().toString();
		String key = "music" + "/" + fileName + ".mp3";
		objectMeta.setContentType(contentType);
		PutObjectResult result = client.putObject(HoolaConfig.getAliyunSystemImagesBucketName(), key, input, objectMeta);
		if (result != null && result.getETag() != null){
			return key;
		} else {
			return null;
		}
	}
	/**
	 * 后台上传展示图标
	 * @param input
	 * @param length
	 * @param contentType
	 * @return
	 * @throws Exception
	 */
	public static String uploadSystemIcon(InputStream input, int length,
			String contentType,int type) throws Exception {
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(length);
		String fileName = UUID.randomUUID().toString();
		String folder="";
		if(type==1){
			folder="music";
		}else if(type==2){
			folder="expression";
		}else{
			folder="paster";
		}
		String key = folder + "/" + fileName + ".png";
		objectMeta.setContentType(contentType);
		PutObjectResult result = client.putObject(HoolaConfig.getAliyunSystemImagesBucketName(), key, input, objectMeta);
		if (result != null && result.getETag() != null){
			return key;
		} else {
			return null;
		}
	}
	/**
	 * 后台上传资源包
	 * @param input
	 * @param length
	 * @param contentType
	 * @return
	 * @throws Exception
	 */
	public static String uploadSystemResource(InputStream input, int length,
			String contentType,int type,String fileName) throws Exception {
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(length);
		//String fileName = UUID.randomUUID().toString();
		String folder="";
		if(type==1){
			folder="music";
		}else if(type==2){
			folder="expression";
		}else{
			folder="paster";
		}
		String key = folder + "/" + fileName;
		objectMeta.setContentType(contentType);
		PutObjectResult result = client.putObject(HoolaConfig.getAliyunSystemImagesBucketName(), key, input, objectMeta);
		if (result != null && result.getETag() != null){
			return key;
		} else {
			return null;
		}
	}
	
	/**
	 * 上传用户自我介绍视频
	 * @param input
	 * @param length
	 * @param contentType
	 * @return
	 */
	public static String uploadUserVideo(InputStream input,int length, String contentType){
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(length);
		String fileName = UUID.randomUUID().toString();
		String key = "video" + "/" + fileName + ".mp4";
		objectMeta.setContentType(contentType);
		PutObjectResult result = client.putObject(HoolaConfig.getAliyunAvatarBucketName(), key, input, objectMeta);
		if (result != null && result.getETag() != null){
			return key;
		} else {
			return null;
		}
	}
	
	
	/**
	 * 上传用户自我介绍视频封面
	 * @param input
	 * @param length
	 * @param contentType
	 * @return
	 */
	public static String uploadUserVideoFace(InputStream input,int length, String contentType){
		ObjectMetadata objectMeta = new ObjectMetadata();
		objectMeta.setContentLength(length);
		String fileName = UUID.randomUUID().toString();
		String key = "video" + "/" + fileName + ".jpg";
		objectMeta.setContentType(contentType);
		PutObjectResult result = client.putObject(HoolaConfig.getAliyunAvatarBucketName(), key, input, objectMeta);
		if (result != null && result.getETag() != null){
			return key;
		} else {
			return null;
		}
	}
	
	
	public static OSSObject getVideo(String key){
		OSSObject obj = client.getObject(HoolaConfig.getAliyunHDVideoBucketName(), key);
		return obj;
	}
	
	public static byte[] getThumbnail(String key){
		try{
			OSSObject obj = client.getObject(HoolaConfig.getAliyunThumbnailBucketName(), key);
			InputStream is = obj.getObjectContent();
			ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			for (int n; (n = is.read(b)) != -1;) {
				out.write(b, 0, n);
			}
			is.close();
			out.close();
			return out.toByteArray();
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		try {
			System.out
					.println(HoolaSecurity
							.getHash(AliyunUploadUtils
									.getVideo(
											"20130710/5ebc3b39-8a4b-4b1e-8351-450599d821bf.mp4")
									.getObjectContent()));
			long end = System.currentTimeMillis();
			System.out.println(end - time);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
