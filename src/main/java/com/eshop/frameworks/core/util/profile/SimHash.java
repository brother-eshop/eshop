package com.eshop.frameworks.core.util.profile;

import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class SimHash {
	private static Logger log = Logger.getLogger(SimHash.class);
	private static final int NGRAM_SIZE = 3;
	
	private static MurmurHash hash = new MurmurHash();
	
	public static float getJaccard(String text, String otherText) {
		HashSet<String> ngrams = createNgram(text, NGRAM_SIZE);
		HashSet<String> otherNgrams = createNgram(otherText, NGRAM_SIZE);
		int total = ngrams.size() + otherNgrams.size();
		otherNgrams.retainAll(ngrams);
		return otherNgrams.size() * 1f / (total - otherNgrams.size());
	}

	public static float getJaccard(List<Integer> mh1, List<Integer> mh2) {
		HashSet<Integer> common = new HashSet<Integer>();
		common.addAll(mh1);
		common.retainAll(mh2);
		
		return common.size() * 1f / Math.max(mh1.size(), mh2.size());
	}
	
	public static List<Integer> calcMinHash(String text) {
		HashSet<String> ngrams = createNgram(text, NGRAM_SIZE);
		return calcMinHash(ngrams);
	}

	public static List<Integer> calcMinHash(HashSet<String> set) {
		PriorityQueue<Integer, Integer> minpq = PriorityQueue.make(10, SortUtils.comp(Integer.class), SortUtils.comp(Integer.class));
		for (String s: set) {
			long h = hash.hash(s) & 0x7FFFFFFF;
			minpq.add((int)h, (int)h);
		}
		return minpq.values();
	}

	public static HashSet<String> createNgram(String text, int n) {
		HashSet<String> tokens = new HashSet<String>();
		List<List<String>> sentences = TextUtility.splitSentenceSet(text);
		for (List<String> sentence: sentences) {
			for (int i = 0; i < sentence.size(); i++) {
				if (isDigit(sentence.get(i))) sentence.set(i, "#");
			}
			for (int i = 0; i + n < sentence.size(); i++) {
				String token = StringUtils.join(sentence.subList(i, i+n), "");
				tokens.add(token);
			}
		}
		return tokens;
	}

	private static boolean isDigit(String word) {
		return Character.isDigit(word.charAt(0));
	}
}
