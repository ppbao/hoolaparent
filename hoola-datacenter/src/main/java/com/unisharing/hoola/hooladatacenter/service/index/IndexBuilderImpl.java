package com.unisharing.hoola.hooladatacenter.service.index;


import com.unisharing.hoola.hoolaredis.key.SearchKeyManager;
import com.unisharing.hoola.hoolaredis.service.BaseRedisService;
import org.springframework.stereotype.Component;
import org.wltea.analyzer.IKSegmentation;
import org.wltea.analyzer.Lexeme;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
@Component
public class IndexBuilderImpl extends BaseRedisService implements
        IIndexBuilder {

	//private static String AUTOCOMPLETE_SUFFIX_SYMBOLE = "*";

	//private static String AUTOCOMPLETE_KEY = "suggest";
//
	//private static String CONTENT_INDEX_PREFIX = "tag:";

	/**
	 * 对内容进行分词然后
	 * 
	 * @param content
	 * @return
	 */
	private Set<String> getIndexItem(String content) {
		Set<String> keys = new HashSet<String>();
		StringReader reader = new StringReader(content);
		IKSegmentation segmentation = new IKSegmentation(reader);
		try {
			Lexeme lexeme = segmentation.next();
			while (lexeme != null) {
				String key = lexeme.getLexemeText().trim();
				if (key.length() > 1) {
					keys.add(key);
				}
				lexeme = segmentation.next();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		keys.add(content);
		return keys;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void buildSuggestIndex(Set<String> tags) {
		for (String tag : tags) {
			Set<String> indexItems = getIndexWord(getIndexItem(tag));
			indexItems.remove(tag);
			for (String indexItem : indexItems) {
				contentTemplate.boundZSetOps(SearchKeyManager.getSuggestKey()).add(indexItem, 0);
			}
			contentTemplate.boundZSetOps(SearchKeyManager.getSuggestKey()).add(
					tag + SearchKeyManager.getSymbole(), 0);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void buildTagIndex(long cid, Set<String> tags) {
		for (String tag : tags) {
			contentTemplate.boundSetOps(SearchKeyManager.getContentIndexKey(tag)).add(String.valueOf(cid));
		}
	}

	private Set<String> getIndexWord(Set<String> cutKeys) {
		Set<String> keys = new HashSet<String>();
		if (cutKeys != null) {
			for (String key : cutKeys) {
				int length = key.length();
				for (int i = 0; i < length; i++) {
					keys.add(key.substring(0, i + 1));
				}
			}
		}
		return keys;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void buildUserIndex(String nickName,long uid) {
		userTemplate.boundSetOps(SearchKeyManager.getUserNicknameKey(nickName)).add(String.valueOf(uid));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void buildUserSuggestIndex(String nickName) {
		Set<String> indexItems = getIndexWord(getIndexItem(nickName));
		indexItems.remove(nickName);
		for (String indexItem : indexItems) {
			userTemplate.boundZSetOps(SearchKeyManager.getUserSuggestKey()).add(indexItem, 0);
		}
		userTemplate.boundZSetOps(SearchKeyManager.getSuggestKey()).add(
				nickName + SearchKeyManager.getSymbole(), 0);
	}
	
	public static void main(String[] args) {
		IndexBuilderImpl builder = new IndexBuilderImpl();
		System.out.println(builder.getIndexWord(builder.getIndexItem("测试旅游")));
		//System.err.println(builder.getIndexItem("测试旅游"));
	}

	

	
}
