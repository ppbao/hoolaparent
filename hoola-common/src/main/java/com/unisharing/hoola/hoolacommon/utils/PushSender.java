package com.unisharing.hoola.hoolacommon.utils;

import javapns.notification.*;
import javapns.notification.transmission.NotificationProgressListener;
import javapns.notification.transmission.NotificationThread;
import javapns.notification.transmission.NotificationThreads;
import org.apache.log4j.Logger;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class PushSender {
	private static Logger log = Logger.getLogger(PushSender.class.getName());
	private static boolean test = false;
	private static String password = "123";
	private static String testCertKeyPath = "qupai_push_dev_123.p12";
	private static String productCertKeyPath = "qupai_push_dev_123.p12";
	private static String inner_productCerKeyPath = HoolaConfig.getCertPath();

	/**
	 * 
	 * @param token
	 *            手机唯一标识
	 * @param content
	 *            //推送内容
	 */
	public static void send(String token, String content,int count,String url) {
		ClassLoader cloader = (new PushSender()).getClass().getClassLoader();
		String path = cloader.getResource("").getPath();
		String keystore = inner_productCerKeyPath;// 证书路径和证书名
		if (test) {
			keystore = path + testCertKeyPath;// 证书路径和证书名
		}

		try {
			// 建立与Apple服务器连接
			AppleNotificationServer server = new AppleNotificationServerBasicImpl(
					keystore, password, !test);
			List<PayloadPerDevice> list = new ArrayList<PayloadPerDevice>();
			PushNotificationPayload payload = new PushNotificationPayload();
			if (content != null){
				payload.addAlert(content);
			}
			payload.addSound("default");// 声音
			payload.addBadge(count);// 图标小红圈的数值
			payload.addCustomDictionary("url", url);// 添加字典
			PayloadPerDevice pay = new PayloadPerDevice(payload, token);// 将要推送的消息和手机唯一标识绑定
			list.add(pay);

			NotificationThreads work = new NotificationThreads(server, list, 1);//
			work.setListener(DEBUGGING_PROGRESS_LISTENER);// 对线程的监听，一定要加上这个监听
			work.start(); // 启动线程
			work.waitForAllThreads();// 等待所有线程启动完成
		} catch (Exception e) {
			log.error("消息推送出错！"+e.getMessage());
		}
	}
	
	public static void batchsendMessage(List<PayloadPerDevice> list){
		ClassLoader cloader = (new PushSender()).getClass().getClassLoader();
		String path = cloader.getResource("").getPath();
		String keystore = inner_productCerKeyPath;// 证书路径和证书名
		if (test) {
			keystore = path + testCertKeyPath;// 证书路径和证书名
		}
		try {
			if (list != null && list.size() > 0){
				// 建立与Apple服务器连接
				AppleNotificationServer server = new AppleNotificationServerBasicImpl(
						keystore, password, !test);
				NotificationThreads work = new NotificationThreads(server, list, 5);//
				
				System.out.println("============="+Thread.currentThread().getName()+","+work.hashCode());
				
				work.setListener(DEBUGGING_PROGRESS_LISTENER);// 对线程的监听，一定要加上这个监听
				work.start(); // 启动线程
				work.waitForAllThreads();// 等待所有线程启动完成
			}
		} catch (Exception e) {
			log.error("消息推送出错！"+e.getMessage());
		}
	}
	
	
	
	
	
	/**
	 * 
	 * @param tokens
	 *            手机唯一标识
	 * @param content
	 *            //推送内容
	 */
	public static void batchSend(List<String> tokens, String content,int count,String url) {
		ClassLoader cloader = (new PushSender()).getClass().getClassLoader();
		String path = cloader.getResource("").getPath();
		String keystore = inner_productCerKeyPath;// 证书路径和证书名
		if (test) {
			keystore = path + testCertKeyPath;// 证书路径和证书名
		}

		try {
			// 建立与Apple服务器连接
			AppleNotificationServer server = new AppleNotificationServerBasicImpl(
					keystore, password, !test);
			List<PayloadPerDevice> list = new ArrayList<PayloadPerDevice>();
			PushNotificationPayload payload = new PushNotificationPayload();
			if (content != null){
				payload.addAlert(content);
			}
			payload.addSound("default");// 声音
			payload.addBadge(count);// 图标小红圈的数值
			payload.addCustomDictionary("url", "qupai://"+url);// 添加字典
			for (String token : tokens){
				PayloadPerDevice pay = new PayloadPerDevice(payload, token);// 将要推送的消息和手机唯一标识绑定
				list.add(pay);
			}
			NotificationThreads work = new NotificationThreads(server, list, 10);//
			work.setListener(DEBUGGING_PROGRESS_LISTENER);// 对线程的监听，一定要加上这个监听
			work.start(); // 启动线程
			work.waitForAllThreads();// 等待所有线程启动完成
		} catch (Exception e) {
			log.error("消息推送出错！"+e.getMessage());
		}
	}

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTestCertKeyPath() {
		return testCertKeyPath;
	}

	public void setTestCertKeyPath(String testCertKeyPath) {
		this.testCertKeyPath = testCertKeyPath;
	}

	public String getProductCertKeyPath() {
		return productCertKeyPath;
	}

	public void setProductCertKeyPath(String productCertKeyPath) {
		this.productCertKeyPath = productCertKeyPath;
	}

	public static void main(String... strings) {
		boolean test = true;
		ClassLoader cloader = (new PushSender()).getClass().getClassLoader();
		String path = cloader.getResource("").getPath();
		String keystore = "/home/tiger/pushkey/qupai_air_distribution_123.p12";// 证书路径和证书名
		int threadThreads = 5; // 线程数
		try {
			long begin = System.currentTimeMillis();
			
			// 建立与Apple服务器连接
			AppleNotificationServer server = new AppleNotificationServerBasicImpl(
					keystore, "123", true);
			List<PayloadPerDevice> list = new ArrayList<PayloadPerDevice>();
			
			for (int i = 1;i<=50;i++){
				PushNotificationPayload payload = new PushNotificationPayload();
				payload.addAlert("短趣推送:测试推送"+i);
				//payload.addCustomAlertBody("提示提示:how are you !");
				
				payload.addSound("default");// 声音
				payload.addBadge(i);// 图标小红圈的数值
				payload.addCustomDictionary("url", "qupai://comment/list");// 添加字典
				PayloadPerDevice pay = new PayloadPerDevice(payload,
						"7d6d1e8ee2cff23d86e99f57cdc3375243685b92ba9e9f946bd1ae555517b020");// 将要推送的消息和手机唯一标识绑定
				list.add(pay);
			}
			NotificationThreads work = new NotificationThreads(server, list,
					threadThreads);//
			//work.setListener(DEBUGGING_PROGRESS_LISTENER);// 对线程的监听，一定要加上这个监听
			work.start(); // 启动线程
			work.waitForAllThreads();// 等待所有线程启动完成
			
			long end = System.currentTimeMillis();
			System.out.println(end - begin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*List<PushedNotification> notifications3;
		try {
			notifications3 = Push
					.alert("短趣消息推送调试，收到给我回个短信谢谢。--海华", keystore, "123456",
							false,
							"8ff6fba5136040c780e4050b25d093ebdfc527ae9418cd923aeed823fe0339cf");
			printPushedNotifications(notifications3);
		} catch (CommunicationException e) {
			e.printStackTrace();
		} catch (KeystoreException e) {
			e.printStackTrace();
		}*/

	}

	@SuppressWarnings("unused")
	private static Payload createComplexPayload(int type, String content,
												int count) {
		PushNotificationPayload complexPayload = PushNotificationPayload
				.complex();
		try {
			complexPayload.addCustomDictionary("type", type);
			complexPayload.addAlert(content);
			complexPayload.addSound("default");// 声音
			complexPayload.addBadge(count);// 图标小红圈的数值
		} catch (JSONException e) {
			System.out.println("Error creating complex payload:");
			e.printStackTrace();
		}
		return complexPayload;
	}

	private static void printPushedNotifications(
			List<PushedNotification> notifications) {
		List<PushedNotification> failedNotifications = PushedNotification
				.findFailedNotifications(notifications);
		List<PushedNotification> successfulNotifications = PushedNotification
				.findSuccessfulNotifications(notifications);
		int failed = failedNotifications.size();
		int successful = successfulNotifications.size();

		if (successful > 0 && failed == 0) {
			printPushedNotifications("All notifications pushed successfully ("
					+ successfulNotifications.size() + "):",
					successfulNotifications);
		} else if (successful == 0 && failed > 0) {
			printPushedNotifications("All notifications failed ("
					+ failedNotifications.size() + "):", failedNotifications);
		} else if (successful == 0 && failed == 0) {
			System.out
					.println("No notifications could be sent, probably because of a critical error");
		} else {
			printPushedNotifications("Some notifications failed ("
					+ failedNotifications.size() + "):", failedNotifications);
			printPushedNotifications("Others succeeded ("
					+ successfulNotifications.size() + "):",
					successfulNotifications);
		}
	}

	private static void printPushedNotifications(String description,
			List<PushedNotification> notifications) {
		System.out.println(description);
		for (PushedNotification notification : notifications) {
			try {
				System.out.println("  " + notification.toString() + "||"
						+ notification.getDevice().getToken());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 线程监听
	public static final NotificationProgressListener DEBUGGING_PROGRESS_LISTENER = new NotificationProgressListener() {
		public void eventThreadStarted(NotificationThread notificationThread) {
			System.out.println("   [EVENT]: thread #"
					+ notificationThread.getThreadNumber() + " started with "
					+ " devices beginning at message id #"
					+ notificationThread.getFirstMessageIdentifier());
		}

		public void eventThreadFinished(NotificationThread thread) {
			System.out.println("   [EVENT]: thread #"
					+ thread.getThreadNumber() + " finished: pushed messages #"
					+ thread.getFirstMessageIdentifier() + " to "
					+ thread.getLastMessageIdentifier() + " toward "
					+ " devices");
		}

		public void eventConnectionRestarted(NotificationThread thread) {
			System.out.println("   [EVENT]: connection restarted in thread #"
					+ thread.getThreadNumber() + " because it reached "
					+ thread.getMaxNotificationsPerConnection()
					+ " notifications per connection");
		}

		public void eventAllThreadsStarted(
				NotificationThreads notificationThreads) {
			System.out.println("   [EVENT]: all threads started: "
					+ notificationThreads.getThreads().size());
		}

		public void eventAllThreadsFinished(
				NotificationThreads notificationThreads) {
			System.out.println("   [EVENT]: all threads finished: "
					+ notificationThreads.getThreads().size());
		}

		public void eventCriticalException(
				NotificationThread notificationThread, Exception exception) {
			System.out.println("   [EVENT]: critical exception occurred: "
					+ exception);
		}
	};

}
