package com.eshop.frameworks.core.lucene;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.eshop.common.constant.CoreConstant;
import com.eshop.common.util.script.ExcuteSysScript;
import com.eshop.common.util.string.StringUtils;
import com.eshop.frameworks.core.entity.PageEntity;

public class LuceneUtil {
	Logger log = Logger.getLogger(LuceneUtil.class);
	private static Directory directory = null;
	private static Analyzer analyzer = null;

	static {
		File file = new File(CoreConstant.indexFilePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			directory = FSDirectory.open(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		analyzer = new IKAnalyzer();
	}

	/**
	 * 创建索引* Description： 其中map的key为文档的id
	 */
	public static void createIndex(Map<String, Document> map) throws Exception {
		IndexWriter indexWriter = null;
		try {
			if (map != null) {
				IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_9, analyzer);
				indexWriter = new IndexWriter(directory, indexWriterConfig);
				Set<String> keySet = map.keySet();
				Iterator<String> iterator = keySet.iterator();
				String id;
				while (iterator.hasNext()) {
					id = iterator.next();
					indexWriter.updateDocument(new Term("id", id), map.get(id));
				}
				indexWriter.commit();
				indexWriter.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (indexWriter != null)
				indexWriter.close();
		}

	}

	/***
	 * Description： 更新索引
	 * @param id
	 *            * @param title* @param content
	 */
	public static void update(String id, Document document) throws Exception {
		IndexWriter indexWriter = null;
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_9, analyzer);
		indexWriter = new IndexWriter(directory, indexWriterConfig);
		Term term = new Term("id", id);
		indexWriter.updateDocument(term, document);
		indexWriter.commit();
		indexWriter.close();
	}

	/***
	 * Description：按照ID进行索引
	 * @param id
	 * @throws IOException
	 */
	public static void delete(String id) throws IOException {
		IndexWriter indexWriter = null;
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_9, analyzer);
		indexWriter = new IndexWriter(directory, indexWriterConfig);
		Term term = new Term("id", id);
		indexWriter.deleteDocuments(term);
		indexWriter.commit();
		indexWriter.close();
		ExcuteSysScript.excute(CoreConstant.scriptFilePath);
	}

	/***
	 * Description：查询
	 * @param where
	 *            查询条件* @param scoreDoc 分页时用
	 * @throws IOException
	 * @throws ParseException
	 * @throws InvalidTokenOffsetsException
	 */
	@SuppressWarnings("rawtypes")
	public static List<Map<String, Object>> search(String[] fields, String category, Map<String, String> must, String keyword, PageEntity page)
			throws IOException, ParseException, InvalidTokenOffsetsException {
		IndexSearcher indexSearcher = null;
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		IndexReader indexReader = null;
		indexReader = DirectoryReader.open(directory);
		indexSearcher = new IndexSearcher(indexReader);
		TopDocsCollector results = TopScoreDocCollector.create(indexReader.maxDoc(), false);
		BooleanQuery booleanQuery = new BooleanQuery();
		MultiPhraseQuery multiquery = new MultiPhraseQuery();
		Term[] terms = null;
		if (StringUtils.isNotEmpty(category)) {// 类型
			String[] columnIds = category.split(",");
			terms = new Term[columnIds.length];
			for (int i = 0; i < columnIds.length; i++) {
				terms[i] = new Term("type", columnIds[i]);
			}
			multiquery.add(terms);
			booleanQuery.add(multiquery, BooleanClause.Occur.MUST);
		}
		Query query = null;
		if (must != null) {
			Set<String> keySet = must.keySet();
			Iterator<String> iterator = keySet.iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				booleanQuery.add(new TermQuery(new Term(key, must.get(key))), BooleanClause.Occur.MUST);
			}
		}
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_4_9, fields, analyzer);
		keyword = QueryParser.escape(keyword);
		query = queryParser.parse(keyword);
		booleanQuery.add(query, BooleanClause.Occur.MUST);
		indexSearcher.search(booleanQuery, results);
		int totalCount = results.getTotalHits();
		page.setTotalResultSize(totalCount);
		TopDocs tds = results.topDocs(page.getStartRow(), page.getPageSize());
		ScoreDoc[] scoreDocs = tds.scoreDocs;
		Formatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
		Scorer fragmentScorer = new QueryScorer(query);
		Highlighter highlighter = new Highlighter(formatter, fragmentScorer);
		Fragmenter fragmenter = new SimpleFragmenter(100);
		highlighter.setTextFragmenter(fragmenter);
		for (ScoreDoc scDoc : scoreDocs) {
			Document document = indexSearcher.doc(scDoc.doc);
			Map<String, Object> doc = new HashMap<String, Object>();
			for (String field : fields) {
				String fieldValue = document.get(field);
				TokenStream tokenStream = analyzer.tokenStream(field, new StringReader(fieldValue));
				doc.put(field, highlighter.getBestFragment(tokenStream, fieldValue));
			}
			doc.put("document", document);
			result.add(doc);
		}
		indexReader.close();
		return result;
	}
	public static List<Map<String, Object>> searchAndSort(Sort sort,String[] fields, String category, Map<String, String> must, String keyword, PageEntity page)
			throws IOException, ParseException, InvalidTokenOffsetsException {
		IndexSearcher indexSearcher = null;
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		IndexReader indexReader = null;
		indexReader = DirectoryReader.open(directory);
		indexSearcher = new IndexSearcher(indexReader);
		BooleanQuery booleanQuery = new BooleanQuery();
		MultiPhraseQuery multiquery = new MultiPhraseQuery();
		Term[] terms = null;
		if (StringUtils.isNotEmpty(category)) {// 类型
			String[] columnIds = category.split(",");
			terms = new Term[columnIds.length];
			for (int i = 0; i < columnIds.length; i++) {
				terms[i] = new Term("type", columnIds[i]);
			}
			multiquery.add(terms);
			booleanQuery.add(multiquery, BooleanClause.Occur.MUST);
		}
		Query query = null;
		if (must != null) {
			Set<String> keySet = must.keySet();
			Iterator<String> iterator = keySet.iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				booleanQuery.add(new TermQuery(new Term(key, must.get(key))), BooleanClause.Occur.MUST);
			}
		}
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_4_9, fields, analyzer);
		keyword = QueryParser.escape(keyword);
		query = queryParser.parse(keyword);
		booleanQuery.add(query, BooleanClause.Occur.MUST);
		TopFieldCollector c = TopFieldCollector.create(sort, page.getCurrentPage()*page.getPageSize(), false, false, false, false);
		indexSearcher.search(booleanQuery, c);
		int totalCount = c.getTotalHits();
		page.setTotalResultSize(totalCount);
		ScoreDoc[] scoreDocs = c.topDocs(page.getStartRow(), page.getPageSize()).scoreDocs;
		Formatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
		Scorer fragmentScorer = new QueryScorer(query);
		Highlighter highlighter = new Highlighter(formatter, fragmentScorer);
		Fragmenter fragmenter = new SimpleFragmenter(100);
		highlighter.setTextFragmenter(fragmenter);
		for (ScoreDoc scDoc : scoreDocs) {
			Document document = indexSearcher.doc(scDoc.doc);
			Map<String, Object> doc = new HashMap<String, Object>();
			for (String field : fields) {
				String fieldValue = document.get(field);
				TokenStream tokenStream = analyzer.tokenStream(field, new StringReader(fieldValue));
				doc.put(field, highlighter.getBestFragment(tokenStream, fieldValue));
			}
			doc.put("document", document);
			result.add(doc);
		}
		indexReader.close();
		return result;
	}

	@SuppressWarnings("rawtypes")
	public static Integer getSearchCount(String[] fields, String category, Map<String, String> must, String keyword) throws IOException, ParseException {
		IndexSearcher indexSearcher = null;
		IndexReader indexReader = null;
		indexReader = DirectoryReader.open(directory);
		indexSearcher = new IndexSearcher(indexReader);
		TopDocsCollector results = TopScoreDocCollector.create(indexReader.maxDoc(), false);
		BooleanQuery booleanQuery = new BooleanQuery();
		MultiPhraseQuery multiquery = new MultiPhraseQuery();
		Term[] terms = null;
		if (StringUtils.isNotEmpty(category)) {// 类型
			String[] columnIds = category.split(",");
			terms = new Term[columnIds.length];
			for (int i = 0; i < columnIds.length; i++) {
				terms[i] = new Term("type", columnIds[i]);
			}
			multiquery.add(terms);
			booleanQuery.add(multiquery, BooleanClause.Occur.MUST);
		}
		Query query = null;
		if (must != null) {
			Set<String> keySet = must.keySet();
			Iterator<String> iterator = keySet.iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				booleanQuery.add(new TermQuery(new Term(key, must.get(key))), BooleanClause.Occur.MUST);
			}
		}
		MultiFieldQueryParser queryParser = new MultiFieldQueryParser(Version.LUCENE_4_9, fields, analyzer);
		keyword = QueryParser.escape(keyword);
		query = queryParser.parse(keyword);
		booleanQuery.add(query, BooleanClause.Occur.MUST);
		indexSearcher.search(booleanQuery, results);
		indexReader.close();
		return results.getTotalHits();
	}

	public static void testAanlyzer(Analyzer analyzer, String text) throws Exception {
		long start = System.currentTimeMillis();
		TokenStream ts = analyzer.tokenStream("content", new StringReader(text));
		CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
		System.out.println("分词效果如下：");
		int i = 0;
		ts.reset();
		while (ts.incrementToken()) {
			i++;
			System.out.println(new String(term.buffer(), 0, term.length()));
		}
		long usetime = System.currentTimeMillis() - start;
		System.out.println("共分词=" + i + ",共耗时=" + usetime + "毫秒。");
	}
}