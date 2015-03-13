package com.eshop.frameworks.core.dao.impl.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.eshop.common.util.ObjectUtils;
import com.eshop.frameworks.core.dao.common.GenericDao;
import com.eshop.frameworks.core.datasource.ReadWriteDataSourceDecision;
import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.frameworks.core.entity.PageOL;

/**
 * @ClassName GenericDaoImpl
 * @package net.sns.framework.core.common.dao
 * @description 数据库基类 供其他dao继承
 * @author spencer
 * @Create Date: 2013-2-27 上午11:05:17
 */
@Repository("genericDao")
public abstract class GenericDaoImpl implements GenericDao {

	public SqlSession readWriteSqlSession;

	// public SqlSession writeSqlSession;
	//
	// public SqlSession readSqlSession;
	//
	// public SqlSession getReadSqlSession() {
	// return readSqlSession;
	// }
	//
	// public SqlSession getWriteSqlSession() {
	// return writeSqlSession;
	// }
	//
	// @Resource(name = "sqlSessionRead")
	// public void setReadSqlSession(SqlSession sqlSession) {
	// this.readSqlSession = sqlSession;
	// }
	//
	// @Resource(name = "sqlSessionWrite")
	// public void setWriteSqlSession(SqlSession sqlSession) {
	// this.writeSqlSession = sqlSession;
	// }
	@Resource(name = "readWriteSqlSession")
	public void setWriteSqlSession(SqlSession sqlSession) {
		this.readWriteSqlSession = sqlSession;
	}

	@Override
	public Long insert(String sqlKey, Object object) {
		ReadWriteDataSourceDecision.markWrite();
		Long l = Long.valueOf(readWriteSqlSession.insert(sqlKey, object));
		ReadWriteDataSourceDecision.reset();
		return l;
	}

	@Override
	public Long delete(String sqlKey, Object object) {
		ReadWriteDataSourceDecision.markWrite();
		Long l = Long.valueOf(readWriteSqlSession.delete(sqlKey, object));
		ReadWriteDataSourceDecision.reset();
		return l;
	}

	@Override
	public Long update(String key, Object object) {
		ReadWriteDataSourceDecision.markWrite();
		Long l = Long.valueOf(readWriteSqlSession.update(key, object));
		ReadWriteDataSourceDecision.reset();
		return l;
	}

	@Override
	public <T> T selectOne(String sqlKey, Object params) {
		ReadWriteDataSourceDecision.markRead();
		T selectOne = null;
		List<T> list = selectList(sqlKey, params);
		if (!ObjectUtils.isNull(list)) {
			selectOne = list.get(0);
		}
		ReadWriteDataSourceDecision.reset();
		return selectOne;
	}
	
	@Override
	public <T> T selectOneFromWriter(String sqlKey, Object params) {
		ReadWriteDataSourceDecision.markWrite();
		T selectOne = null;
		List<T> list = selectList(sqlKey, params);
		if (!ObjectUtils.isNull(list)) {
			selectOne = list.get(0);
		}
		ReadWriteDataSourceDecision.reset();
		return selectOne;
	}

	@Override
	public <T> List<T> selectList(String sqlKey, Object params) {
		ReadWriteDataSourceDecision.markRead();
		List<T> list = readWriteSqlSession.selectList(sqlKey, params);
		ReadWriteDataSourceDecision.reset();
		return list;
	}

	/**
	 * 分页查询时使用
	 * @return
	 */
	@Override
	public <T> List<T> queryForListPage(String sqlKey, Object params, PageEntity page) {

		/**
		 * 分页时需要2个sql。在正常sql后面加pageCount为计算count的sql 如：customre.getcustomreByTime必须命名为customre.getcustomreByTimeCount
		 */

		// 查询总行数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("e", params);
		PageOL pageOL = new PageOL();
		pageOL.setOffsetPara((page.getCurrentPage() - 1) * page.getPageSize());
		pageOL.setLimitPara(page.getPageSize());
		map.put("page", pageOL);

		Integer objectscount = this.selectOne(sqlKey + "Count", map);

		if (objectscount == null || objectscount == 0) {
			page.setTotalResultSize(0);
			int totalPageSize = (page.getTotalResultSize() - 1) / page.getPageSize() + 1;
			page.setTotalPageSize(totalPageSize);
			return null;
		} else {
			page.setTotalResultSize(objectscount);
			int totalPageSize = (page.getTotalResultSize() - 1) / page.getPageSize() + 1;
			page.setTotalPageSize(totalPageSize);
			return this.selectList(sqlKey, map);
		}

	}

}
