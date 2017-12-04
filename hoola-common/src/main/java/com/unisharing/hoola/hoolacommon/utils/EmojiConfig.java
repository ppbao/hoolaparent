package com.unisharing.hoola.hoolacommon.utils;

import java.util.HashSet;
import java.util.Set;

public class EmojiConfig {
	
	public static Set<Integer> emojiSet = new HashSet<Integer>();

	public static Set<Integer> getEmojiSet() {
		return emojiSet;
	}

	public static void setEmojiSet(Set<Integer> emojiSet) {
		EmojiConfig.emojiSet = emojiSet;
	}


	
}
