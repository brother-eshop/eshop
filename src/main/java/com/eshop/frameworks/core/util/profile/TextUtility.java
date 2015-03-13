package com.eshop.frameworks.core.util.profile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class TextUtility {
	public enum CharType { Alpha, Digit, Letter, Other}
		
	private static final HashSet<String> noizeTags = new HashSet<String>(Arrays.asList(
		"html", "head", "body", "frameset", "script", "noscript", "style", "meta", "link", "title", "frame", "ul", "tr", "td", "dl",
		"noframes", "section", "nav", "aside", "hgroup", "header", "footer", "pre", "div", "address", "figure", "figcaption",
		"form", "fieldset", "ins", "del", "table", "caption", "thead", "tfoot", "tbody", "colgroup", "col", "video",
		"audio", "canvas", "details", "menu", "plaintext"
	));
	
	public static final HashSet<Character> prePosChars = new HashSet<Character>(Arrays.asList(
		'一','不','与','也','了','于','仍','从','以','但','使','则','却','又','及','在','地','对','就','并','当','很','得','成','或','把','时',
		'是','比','的','着','等','给','而','虽','被','让','还', '和', '这', '那', '万', '亿', '十', '更', '为', '来', '都','将','你','我','他',
		'她','它','个', '太', '最', '之'
	));
	
	public static HashSet<String> keepPuncs = new HashSet<String>(Arrays.asList(",", ".", "，", "。"));
	
    /**
     * A mapping from HTML codes for escaped special characters to their unicode
     * character equivalents.
     */
    private static final Map<String,String> HTML_CODES = new HashMap<String,String>();

    private static final Map<String,String> LATIN1_CODES = new HashMap<String,String>();

	private TextUtility() {
	}

	public static boolean isNoizeTags(String tag) {
		return noizeTags.contains(tag);
	}
	
	public static int diff(String str1, String str2) {
		int[][] d = new int[str1.length()+1][str2.length()+1];
		for (int i = 0; i <= str1.length(); i++) d[i][0] = i;
		for (int j = 0; j <= str2.length(); j++) d[0][j] = j;
		
		for (int j = 1; j <= str2.length(); j++) {
			for (int i = 1; i <= str1.length(); i++) {
				if (str1.charAt(i-1) == str2.charAt(j-1)) d[i][j] = d[i-1][j-1];
				else d[i][j] = min(d[i][j-1]+1, d[i-1][j]+1, d[i-1][j-1]+1);
			}
		}
		
		return d[str1.length()][str2.length()];
	}
	
	private static int min(int x1, int x2, int x3) {
		int x = x1;
		if (x > x1) x = x1;
		if (x > x2) x = x3;
		return x;
	}

	/**
     * Returns the provided string where all HTML special characters
     * (e.g. <pre>&nbsp;</pre>) have been replaced with their utf8 equivalents.
     *
     * @param source a String possibly containing escaped HTML characters
     */
	public static final String unescapeHTML(String source) {
		StringBuilder sb = new StringBuilder(source.length());

		// position markers for the & and ;
		int start = -1, end = -1;

		// the end position of the last escaped HTML character
		int last = 0;

		start = source.indexOf("&");
		end = source.indexOf(";", start);

		while (start > -1 && end > start) {
			String encoded = source.substring(start, end + 1);
			String decoded = HTML_CODES.get(encoded);

			// if encoded form wasn't in the HTML codes, try checking to see if
			// it was a Latin-1 code
			if (decoded == null) {
				decoded = LATIN1_CODES.get(encoded);
			}

			if (decoded != null) {
				// append the string containing all characters from the last
				// escaped
				// character to the current one
				String s = source.substring(last, start);
				sb.append(s).append(decoded);
				last = end + 1;
			}

			start = source.indexOf("&", end);
			end = source.indexOf(";", start);
		}
		// if there weren't any substitutions, don't both to create a new String
		if (sb.length() == 0)
			return source;

		// otherwise finish the substitution by appending all the text from the
		// last substitution until the end of the string
		sb.append(source.substring(last));
		return sb.toString();
	}
	
	public static String formatNum(String txt) {
		StringBuffer sb = new StringBuffer();
		boolean prevDigit = false;
		for (int i = 0; i < txt.length(); i++) {
			char ch = txt.charAt(i);
			if (Character.isDigit(ch)) {
				if (!prevDigit) sb.append("#");
				prevDigit = true;
			} else {
				sb.append(ch);
				prevDigit = false;
			}
		}
		return sb.toString();
	}
	
	public static int countLetters(String text) {
		int total = 0;
		boolean isLastAlpha = false;
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (Character.isLetter(ch)) {
				if (!isLastAlpha || !isAlphaOrDigit(ch)) total++;
			}
			if (Character.isLetter(ch) && isAlphaOrDigit(ch)) isLastAlpha = true;
			else isLastAlpha = false;
		}
		return total;
	}
	
	public static List<String> splitWords(String text) {
		return splitWords(text, new HashSet<String>());
	}
	
	public static List<String> splitWords(String text, HashSet<String> keepPuncs) {
		List<String> words = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (isAlphaOrDigit(ch)) {
				sb.append(ch);
				
			} else if (Character.isLetter(ch)) {
				if (sb.length() > 0) {
					words.add(sb.toString());
					sb.setLength(0);
				}
				words.add(String.valueOf(ch));
			} else {
				if (sb.length() > 0) {
					words.add(sb.toString());
					sb.setLength(0);
				}
				String punc = String.valueOf(ch);
				if (keepPuncs.contains(punc)) words.add(punc);
			}
		}
		
		if (sb.length() > 0) words.add(sb.toString());
		return words;
	}

	public static int countNumWords(String text) {
		int total = text.length();
		boolean isLastLetter = false;
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (isAlphaOrDigit(ch)) {
				if (isLastLetter) total -= 1;
				isLastLetter = true;
			} else {
				if (!Character.isLetter(ch)) total -= 1;
				isLastLetter = false;
			}
		}
		return total;
	}
	
	public static int countPuncs(String text) {
		int nPuncs = 0;
		
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (ch == ',' || ch == '.') {
				if (i + 1 == text.length()) nPuncs++;
				else if (Character.isWhitespace(text.charAt(i+1))) nPuncs++;
			}
			if (ch == '，' || ch == '。') nPuncs++;
		}
		return nPuncs;
	}
	
	public static int parseInt(String text, int d) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (ch >= '0' && ch <= '9') sb.append(ch);
			else if (sb.length() > 0) return Integer.parseInt(sb.toString());
		}
		if (sb.length() > 0) return Integer.parseInt(sb.toString());
		return d;
	}

	public static boolean isAlphaOrDigit(char ch) {
		if (ch >= 'a' && ch <= 'z') return true;
		if (ch >= 'A' && ch <= 'Z') return true;
		if (ch >= '0' && ch <= '9') return true;
		return false;
	}
	
	public static boolean isFirstTwoUpperCase(String text) {
		boolean hasUpperCase = false;
		for (int i = 0; i < 2 && i < text.length(); i++) {
			char ch = text.charAt(i);
			int type = Character.getType(ch);
			if (type == Character.UPPERCASE_LETTER) {
				hasUpperCase = true;
				break;
			}
		}
		return hasUpperCase;
	}
	
	public static boolean isFirstAscii(String text) {
		char ch = text.charAt(0);
		int type = Character.getType(ch);
		return type == Character.UPPERCASE_LETTER || type == Character.LOWERCASE_LETTER;
	}
	
	public static boolean isWhitespace(char ch) {
		int type = Character.getType(ch);
		return Character.isWhitespace(ch) || type == Character.SPACE_SEPARATOR;
	}
	
	public static boolean isUsefulKeyword(String word) {
		if (TextUtility.isFirstAscii(word)) {
			if (word.contains(" ") || word.length() >= 3) return true;
		}
		else if (word.length() >= 3) return true;
		return false;
	}
	
	public static boolean isAllUpperCase(String text) {
		boolean allUpperCase = true;
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			int type = Character.getType(ch);
			if (type != Character.UPPERCASE_LETTER && type != Character.DECIMAL_DIGIT_NUMBER && ch != '-') allUpperCase = false;
		}
		return allUpperCase;
	}
	
	public static boolean isNormalWord(String text) {
		boolean normal = true;
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			int type = Character.getType(ch);
			if (i == 0 && type != Character.UPPERCASE_LETTER) normal = false;
			if (i > 0 && type != Character.LOWERCASE_LETTER) normal = false;
		}
		return normal;
	}
	
	public static void main(String[] args) {
		String t = ",.?!，。？！";
		for (int i = 0; i < t.length(); i++) {
			char ch = t.charAt(i);
			System.out.println(Character.getType(ch) == Character.OTHER_PUNCTUATION);
		}
//		String text = "发表于 6 天, 前";
//		System.out.println(splitWords(text, new HashSet<String>(Arrays.asList("，", "。"))));
//		System.out.println(countNumWords(text));
//		System.out.println(text.length());
//		System.out.println(TextUtility.isUsefulKeyword("iPad"));
//		System.out.println(TextUtility.isUsefulKeyword("Google"));
//		System.out.println(TextUtility.isUsefulKeyword("手机"));
	}
	
	public static String deduplicateSentence(String text) {
		StringBuffer sb = new StringBuffer();
		List<String> sentences = splitSentences(text);
		HashSet<String> set = new HashSet<String>();
		for (String sentence: sentences) {
			if (set.contains(sentence)) {
				//System.out.println(sentence);
				continue;
			}
			sb.append(sentence);
			set.add(sentence);
		}
		return sb.toString();
	}
	
	public static List<String> splitSentences(String text) {
		List<String> sentences = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		boolean prevEnd = false;
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			sb.append(ch);
			if (ch == '。' || (prevEnd && Character.isWhitespace(ch))) {
				String sentence = sb.toString().trim();
				if (sentence.length() > 1) {
					sentences.add(sentence);
					sb.setLength(0);
				}
			}
			if (ch == '.' || Character.isWhitespace(ch)) prevEnd = true;
			else prevEnd = false;
		}
		if (sb.length() > 0) {
			String sentence = sb.toString().trim();
			if (sentence.length() > 1) {
				sentences.add(sentence);
			}
		}
		
		return sentences;
	}

	public static List<List<String>> splitSentenceSet(String text) {
		StringBuffer sb = new StringBuffer();
		List<List<String>> sentences = new ArrayList<List<String>>();
		List<String> current = new ArrayList<String>();
		
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			int type = Character.getType(ch);
			
			if (type != Character.LOWERCASE_LETTER && type != Character.UPPERCASE_LETTER && ch != '’' && ch != '-' && type != Character.DECIMAL_DIGIT_NUMBER) {
				if (sb.length() > 1 && sb.charAt(0) != '-') current.add(sb.toString());
				sb.setLength(0);
			}
			if (type == Character.LOWERCASE_LETTER || type == Character.UPPERCASE_LETTER || ch == '’' || ch == '-' || type == Character.DECIMAL_DIGIT_NUMBER) {
				sb.append(ch);
			}
			if (type == Character.OTHER_LETTER && !prePosChars.contains(ch)) {
				current.add(String.valueOf(ch));
			}
			if (type >= Character.DASH_PUNCTUATION && type <= Character.OTHER_PUNCTUATION || prePosChars.contains(ch)) {
				if (current.size() > 1) sentences.add(current);
				if (current.size() == 1 && current.get(0).length() > 1) sentences.add(current);
				current = new ArrayList<String>();
			}
		}
		if (current.size() > 1) sentences.add(current);
		if (current.size() == 1 && current.get(0).length() > 1) sentences.add(current);
		return sentences;
	}
	
	static {
		HTML_CODES.put("&nbsp;"," ");
		HTML_CODES.put("&Agrave;","À");
		HTML_CODES.put("&Aacute;","Á");
		HTML_CODES.put("&Acirc;","Â");
		HTML_CODES.put("&Atilde;","Ã");
		HTML_CODES.put("&Auml;","Ä");
		HTML_CODES.put("&Aring;","Å");
		HTML_CODES.put("&AElig;","Æ");
		HTML_CODES.put("&Ccedil;","Ç");
		HTML_CODES.put("&Egrave;","È");
		HTML_CODES.put("&Eacute;","É");
		HTML_CODES.put("&Ecirc;","Ê");
		HTML_CODES.put("&Euml;","Ë");
		HTML_CODES.put("&Igrave;","Ì");
		HTML_CODES.put("&Iacute;","Í");
		HTML_CODES.put("&Icirc;","Î");
		HTML_CODES.put("&Iuml;","Ï");
		HTML_CODES.put("&ETH;","Ð");
		HTML_CODES.put("&Ntilde;","Ñ");
		HTML_CODES.put("&Ograve;","Ò");
		HTML_CODES.put("&Oacute;","Ó");
		HTML_CODES.put("&Ocirc;","Ô");
		HTML_CODES.put("&Otilde;","Õ");
		HTML_CODES.put("&Ouml;","Ö");
		HTML_CODES.put("&Oslash;","Ø");
		HTML_CODES.put("&Ugrave;","Ù");
		HTML_CODES.put("&Uacute;","Ú");
		HTML_CODES.put("&Ucirc;","Û");
		HTML_CODES.put("&Uuml;","Ü");
		HTML_CODES.put("&Yacute;","Ý");
		HTML_CODES.put("&THORN;","Þ");
		HTML_CODES.put("&szlig;","ß");
		HTML_CODES.put("&agrave;","à");
		HTML_CODES.put("&aacute;","á");
		HTML_CODES.put("&acirc;","â");
		HTML_CODES.put("&atilde;","ã");
		HTML_CODES.put("&auml;","ä");
		HTML_CODES.put("&aring;","å");
		HTML_CODES.put("&aelig;","æ");
		HTML_CODES.put("&ccedil;","ç");
		HTML_CODES.put("&egrave;","è");
		HTML_CODES.put("&eacute;","é");
		HTML_CODES.put("&ecirc;","ê");
		HTML_CODES.put("&euml;","ë");
		HTML_CODES.put("&igrave;","ì");
		HTML_CODES.put("&iacute;","í");
		HTML_CODES.put("&icirc;","î");
		HTML_CODES.put("&iuml;","ï");
		HTML_CODES.put("&eth;","ð");
		HTML_CODES.put("&ntilde;","ñ");
		HTML_CODES.put("&ograve;","ò");
		HTML_CODES.put("&oacute;","ó");
		HTML_CODES.put("&ocirc;","ô");
		HTML_CODES.put("&otilde;","õ");
		HTML_CODES.put("&ouml;","ö");
		HTML_CODES.put("&oslash;","ø");
		HTML_CODES.put("&ugrave;","ù");
		HTML_CODES.put("&uacute;","ú");
		HTML_CODES.put("&ucirc;","û");
		HTML_CODES.put("&uuml;","ü");
		HTML_CODES.put("&yacute;","ý");
		HTML_CODES.put("&thorn;","þ");
		HTML_CODES.put("&yuml;","ÿ");
		HTML_CODES.put("&lt;","<");
		HTML_CODES.put("&gt;",">");
		HTML_CODES.put("&quot;","\"");
		HTML_CODES.put("&amp;","&");

		LATIN1_CODES.put("&#039;", "'");
		LATIN1_CODES.put("&#160;", " ");
		LATIN1_CODES.put("&#162;", "¢");
		LATIN1_CODES.put("&#164;", "¤");
		LATIN1_CODES.put("&#166;", "¦");
		LATIN1_CODES.put("&#168;", "¨");
		LATIN1_CODES.put("&#170;", "ª");
		LATIN1_CODES.put("&#172;", "¬");
		LATIN1_CODES.put("&#174;", "®");
		LATIN1_CODES.put("&#176;", "°");
		LATIN1_CODES.put("&#178;", "²");
		LATIN1_CODES.put("&#180;", "´");
		LATIN1_CODES.put("&#182;", "¶");
		LATIN1_CODES.put("&#184;", "¸");
		LATIN1_CODES.put("&#186;", "º");
		LATIN1_CODES.put("&#188;", "¼");
		LATIN1_CODES.put("&#190;", "¾");
		LATIN1_CODES.put("&#192;", "À");
		LATIN1_CODES.put("&#194;", "Â");
		LATIN1_CODES.put("&#196;", "Ä");
		LATIN1_CODES.put("&#198;", "Æ");
		LATIN1_CODES.put("&#200;", "È");
		LATIN1_CODES.put("&#202;", "Ê");
		LATIN1_CODES.put("&#204;", "Ì");
		LATIN1_CODES.put("&#206;", "Î");
		LATIN1_CODES.put("&#208;", "Ð");
		LATIN1_CODES.put("&#210;", "Ò");
		LATIN1_CODES.put("&#212;", "Ô");
		LATIN1_CODES.put("&#214;", "Ö");
		LATIN1_CODES.put("&#216;", "Ø");
		LATIN1_CODES.put("&#218;", "Ú");
		LATIN1_CODES.put("&#220;", "Ü");
		LATIN1_CODES.put("&#222;", "Þ");
		LATIN1_CODES.put("&#224;", "à");
		LATIN1_CODES.put("&#226;", "â");
		LATIN1_CODES.put("&#228;", "ä");
		LATIN1_CODES.put("&#230;", "æ");
		LATIN1_CODES.put("&#232;", "è");
		LATIN1_CODES.put("&#234;", "ê");
		LATIN1_CODES.put("&#236;", "ì");
		LATIN1_CODES.put("&#238;", "î");
		LATIN1_CODES.put("&#240;", "ð");
		LATIN1_CODES.put("&#242;", "ò");
		LATIN1_CODES.put("&#244;", "ô");
		LATIN1_CODES.put("&#246;", "ö");
		LATIN1_CODES.put("&#248;", "ø");
		LATIN1_CODES.put("&#250;", "ú");
		LATIN1_CODES.put("&#252;", "ü");
		LATIN1_CODES.put("&#254;", "þ");
		LATIN1_CODES.put("&#34;", "\"");
		LATIN1_CODES.put("&#38;", "&");
		LATIN1_CODES.put("&#8217;", "'");
	}
}
