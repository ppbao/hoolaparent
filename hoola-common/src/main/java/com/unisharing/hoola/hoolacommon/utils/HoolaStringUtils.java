package com.unisharing.hoola.hoolacommon.utils;

import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HoolaStringUtils {

	public static Set<String> getTags(String description){
		Set<String> tags = new HashSet<String>();
		String regex = "#[^#]+?#";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(description);
		while (matcher.find()) {
			tags.add(matcher.group(0).replaceAll("#", ""));
		}
		return tags;
	}
	
	public static Set<String> getNickNames(String description){
		Set<String> names = new HashSet<String>();
		if (StringUtils.hasText(description)){
			String regex = "@[^@|^,|^.|^(|^)|^。|^:|^，|^……|^\\s]+"; 
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(description);
			while (matcher.find()) {
				names.add(matcher.group(0).replaceAll("@", ""));
			}
		}
		return names;
	}
	
	public static String getTagString(String description){
		Set<String> tags = getTags(description);
		StringBuffer tagStr = new StringBuffer();
		if (tags != null){
			for (String tag : tags){
				tagStr.append(tag).append(";");
			}
			return tagStr.toString();
		}
		return "";
	}
	
	/**
	 * 2011-11-07T16:28:44 +0000
	 * @param DateStr
	 * @return
	 */
	public static int getExpiresIn(String expiresDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm +0000");
		try{
			Date date = sdf.parse(expiresDate);
			long times = date.getTime();
			long expiresIn = times - System.currentTimeMillis();
			return (new Long(expiresIn/1000)).intValue();
		}catch(Exception e){
			e.printStackTrace();
			return 0;
		}
	}
	
	public static String getForbiddenWord(String str){
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(HoolaStringUtils.getNickNames("@中文  @asdf    …af"));
		//System.out.println(HoolaStringUtils.getExpiresIn("2013-08-26 02:13:35 +0000"));
	}
}
