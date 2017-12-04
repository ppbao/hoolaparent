package com.unisharing.hoola.hoolacommon.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class VideoTools {

	/**
	 * 返回压缩好的视频Url
	 * 
	 * @param file
	 * @return
	 */
	public static boolean compressionFile(File file, String outPath) {
		if (!file.exists()) {
			return false;
		}
		File outFile = new File(outPath);
		if (!outFile.getParentFile().exists()){
			outFile.getParentFile().mkdirs();
		}
		
		List<String> commend = new ArrayList<String>();// 用来进行视频截图参数设置
		commend.add("ffmpeg");// 设置转换器所在位置
		commend.add("-y");// 覆盖掉输出目录中的同名文件
		commend.add("-i");// 设置要输入的文件
		commend.add(file.getPath());// 输入的文件
		commend.add("-strict");//
		commend.add("experimental");
		commend.add("-r");// 
		commend.add("15");// 设置帧数
		commend.add("-b:v");// 
		commend.add("500");// 码率设置为张之家
		commend.add("-b:a");// 设置记录时间
		commend.add("32k");// 音频码率
		commend.add("-s");//缩小尺寸
		commend.add("480x480");
		commend.add(outPath);// 要输入的文件的位置
		try {
			ProcessBuilder builder = new ProcessBuilder();
			builder.command(commend);
			builder.redirectErrorStream(true);
			Process p = builder.start();
			BufferedReader buf = null; // 保存ffmpeg的输出结果流
			String line = null;
			buf = new BufferedReader(new InputStreamReader(p.getInputStream())); // 缓冲读入
			StringBuffer sb = new StringBuffer();
			while ((line = buf.readLine()) != null) {
				sb.append(line); // 保存ffmpeg的输出结果流
				continue;
			}
			int ret = p.waitFor();// 这里线程阻塞，将等待外部转换进程运行成功运行结束后，才往下执行
			System.out.println("视频压缩成功！");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 转化视频为Gif
	 * @param in	视频文件
	 * @param picFloderName	抽取图片文件夹名称
	 * @param gifFileName	合成的Gif文件名
	 * @return
	 */
	public static boolean convert2Gif(File in,String picFloderName,String gifFileName){
		try {
			if (in != null && in.exists()){
				String shellPath = HoolaConfig.getConvertShellPath();
				String resultPath = HoolaConfig.getConvertResultPath();
				Process process = Runtime.getRuntime().exec(
						shellPath + "convert2gif.sh " + in.getPath() + " "
								+ resultPath + picFloderName + " " + resultPath
								+ gifFileName);
				process.waitFor();
				return true;
			}	
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return false;
	}

//	protected static Runnable handle1 = new Runnable() {
//		@Override
//		public void run() {
//			long begin = System.currentTimeMillis();
//			for (int i = 1; i < 1000; i += 4) {
//				VideoTools.compressionFile(new File(
//						"/home/wanghaihua/桌面/yanan/123.mp4"),
//						"/home/wanghaihua/桌面/test/test_1_" + i + ".mp4");
//			}
//			long end = System.currentTimeMillis();
//			System.out.println("begin:"+begin+",end:"+end+"time1:" + (end - begin));
//		}
//	};
//
//	protected static Runnable handle2 = new Runnable() {
//
//		@Override
//		public void run() {
//			long begin = System.currentTimeMillis();
//			for (int i = 0; i < 1000; i += 4) {
//				VideoTools.compressionFile(new File(
//						"/home/wanghaihua/share/IMG_2.mp4"),
//						"/home/wanghaihua/桌面/test/test_2_" + i + ".mp4");
//			}
//			long end = System.currentTimeMillis();
//			System.out.println("begin:"+begin+",end:"+end+"time2:" + (end - begin));
//		}
//	};
//
//	protected static Runnable handle3 = new Runnable() {
//		@Override
//		public void run() {
//			long begin = System.currentTimeMillis();
//			for (int i = 2; i < 1000; i += 4) {
//				VideoTools.compressionFile(new File(
//						"/home/wanghaihua/share/IMG_3.mp4"),
//						"/home/wanghaihua/桌面/test/test_2_" + i + ".mp4");
//			}
//			long end = System.currentTimeMillis();
//			System.out.println("begin:"+begin+",end:"+end+"time3:" + (end - begin));
//		}
//	};
//
//
//	protected static Runnable handle4 = new Runnable() {
//		@Override
//		public void run() {
//			long begin = System.currentTimeMillis();
//			for (int i = 3; i < 1000; i += 4) {
//				VideoTools.compressionFile(new File(
//						"/home/wanghaihua/share/IMG_4.mp4"),
//						"/home/wanghaihua/桌面/test/test_4_" + i + ".mp4");
//			}
//			long end = System.currentTimeMillis();
//			System.out.println("begin:"+begin+",end:"+end+"time4:" + (end - begin));
//		}
//	};

	public static void main(String... strings) {
		
		VideoTools.compressionFile(new File(
		"/home/wanghaihua/桌面/yanan/123.mp4"),
		"/home/wanghaihua/桌面/test/test_1.mp4");
		/*System.out.println("===============handle1.start();===============");
		Thread thread1 = new Thread(handle1);
		thread1.setName("thread1");
		thread1.start();*/
		
		/*System.out.println("===============handle2.start();===============");
		Thread thread2 = new Thread(handle2);
		thread2.setName("thread2");
		thread2.start();
		
		System.out.println("===============handle3.start();===============");
		Thread thread3 = new Thread(handle3);
		thread3.setName("thread3");
		thread3.start();
		
		System.out.println("===============handle4.start();===============");
		Thread thread4 = new Thread(handle4);
		thread4.setName("thread4");
		thread4.start();*/
		
	}
}
