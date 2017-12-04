package com.unisharing.hoola.hoolacommon.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiUtils {

	public static String encodeEmoji(String input) {
		if (input == null || input.length() <= 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		int[] codePoints = toCodePointArray(input);
		for (int i = 0; i < codePoints.length; i++) {
			if (EmojiConfig.getEmojiSet().contains(codePoints[i])){
				result.append("<e>").append(codePoints[i]).append("</e>");
			}else{
				result.append(Character.toChars(codePoints[i]));
			}
		}
		return result.toString();
	}
	
	/*public static String filterEmoji(String input) {
		if (input == null || input.length() <= 0) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		int[] codePoints = toCodePointArray(input);
		for (int i = 0; i < codePoints.length; i++) {
			if (!EmojiConfig.getEmojiSet().contains(codePoints[i])){
				result.append(Character.toChars(codePoints[i]));
			}
		}
		return result.toString();
	}*/
	
	public static String decodeEmoji(String input) {
		if (input == null || input.length() <= 0) {
			return "";
		}
		Set<String> emojis = getEmojis(input);
		for (String emoji : emojis){
			input = input.replaceAll(emoji, new String(Character.toChars(getEmojiUnicode(emoji))));
		}
		return input;
	}
	
	private static Set<String> getEmojis(String description){
		Set<String> emoji = new HashSet<String>();
		String regex = "<e>.*?</e>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(description);
		while (matcher.find()) {
			emoji.add(matcher.group(0));
		}
		return emoji;
	}
	
	/**
	 * 
	 * @param input [e]125812[/e]
	 * @return
	 */
	private static int getEmojiUnicode(String input){
		int index1 = input.indexOf(">")+1;
		int index2 = input.lastIndexOf("<");
		try{
			return Integer.parseInt(input.substring(index1,index2));
		}catch (Exception e) {
			return 0;
		}
		
	}

	private static int[] toCodePointArray(String str) {
		char[] ach = str.toCharArray();
		int len = ach.length;
		int[] acp = new int[Character.codePointCount(ach, 0, len)];
		int j = 0;
		for (int i = 0, cp; i < len; i += Character.charCount(cp)) {
			cp = Character.codePointAt(ach, i);
			acp[j++] = cp;
		}
		return acp;
	}
	
	 /**
     * 检测是否有emoji字符
     * @param source
     * @return 一旦含有就抛出
     */
    public static boolean containsEmoji(String source) {
        if (StringUtils.isBlank(source)) {
            return false;
        }

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {
                //do nothing，判断到了这里表明，确认有表情字符
                return true;
            }
        }

        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     * @param source
     * @return
     */
    public static String filterEmoji(String source) {

        if (!containsEmoji(source)) {
            return source;//如果不包含，直接返回
        }
        //到这里铁定包含
        StringBuilder buf = null;

        int len = source.length();

        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);

            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }

                buf.append(codePoint);
            } else {
            }
        }

        if (buf == null) {
            return source;//如果没有找到 emoji表情，则返回源字符串
        } else {
            if (buf.length() == len) {//这里的意义在于尽可能少的toString，因为会重新生成字符串
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }

    }
	
}
