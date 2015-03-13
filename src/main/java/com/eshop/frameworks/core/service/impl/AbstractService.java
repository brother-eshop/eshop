package com.eshop.frameworks.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.entity.MongoEntity;
import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.frameworks.core.service.Service;

/**
 * @param <T>
 * @param <PK>
 * @author Zhang Xiao Dong
 * @Date: 2012-4-12
 */
public abstract class AbstractService<T extends MongoEntity, PK> implements Service<T, PK> {

	public abstract DAO<T, PK> getDao();

	public boolean collectionExists(String tableName) {
		return getDao().collectionExists(tableName);
	}

	public void createCollection(String tableName) {
		getDao().createCollection(tableName);
	}

	public void dropCollection(String tableName) {
		getDao().dropCollection(tableName);
	}

	public T findAndRemove(Query query, Class<T> clazz) {
		return getDao().findAndRemove(query, clazz);
	}

	public T findAndRemove(Criteria criteria, Class<T> clazz) {
		return getDao().findAndRemove(criteria, clazz);
	}

	public List<T> findAll(Class<T> clazz) {
		return getDao().findAll(clazz);
	}

	/**
	 * @function 查询数量
	 * @param clazz
	 * @return
	 */
	public long size(Class<T> clazz) {
		return getDao().size(new Query(), clazz);
	}

	/**
	 * @function 查询数量
	 * @param clazz
	 * @return
	 */
	public long size(Query query, Class<T> clazz) {
		return getDao().size(query, clazz);
	}

	/**
	 * @function 查询数量
	 * @param clazz
	 * @return
	 */
	public long size(Criteria criteria, Class<T> clazz) {
		return getDao().size(new Query(criteria), clazz);
	}

	public List<T> findList(Criteria criteria, Class<T> clazz) {
		return getDao().findList(criteria, clazz);
	}

	public List<T> findList(Query query, Class<T> clazz) {
		return getDao().findList(query, clazz);
	}

	public List<T> limitFind(int pageSize, int forward, Class<T> clazz) {
		Query query = new Query();
		return limitFind(pageSize, forward, query, clazz);
	}

	public List<T> limitFInd(Query query, PageEntity page, Class<T> clazz) {
		long size = page.getTotalResultSize();
		if (size == 0l) {
			size = size(query, clazz);
			if (size == 0) {
				return new ArrayList<T>();
			}
			page.setTotalResultSize((int) size);
		}
		query.skip(page.getStartRow()).limit(page.getPageSize());
		return getDao().findList(query, clazz);
	}

	public List<T> limitFind(int pageSize, int forward, Query query, Class<T> clazz) {
		long size = size(query, clazz);
		int skip = (long) ((forward - 1) * pageSize) > size ? (int) size : (forward - 1) * pageSize;
		query.skip(skip).limit(pageSize);
		return getDao().findList(query, clazz);
	}

	public T findOne(Query query, Class<T> clazz) {
		return getDao().findOne(query, clazz);
	}

	public T findOne(String tableName, Criteria criteria, Class<T> clazz) {
		return getDao().findOne(tableName, criteria, clazz);
	}

	public T findById(String id, Class<T> clazz) {
		return getDao().findById(id, clazz);
	}

	public T findOne(String tableName, Query query, Class<T> clazz) {
		return getDao().findOne(tableName, query, clazz);
	}

	public void remove(String tableName, Criteria criteria, Class<T> clazz) {
		getDao().remove(tableName, criteria, clazz);
	}

	public void remove(T entity) {
		getDao().remove(entity);
	}

	public void removeById(String id, Class<T> clazz) {
		getDao().remove(getDao().findById(id, clazz));
	}

	public void save(String tableName, T entity) {
		getDao().save(tableName, entity);
	}

	public void save(T entity) {
		getDao().save(entity);
	}

	public void insert(T entity) {
		getDao().insert(entity);
	}

	public void insertBatch(List<T> entitys, Class<T> clazz) {
		getDao().insertBatch(entitys, clazz);
	}

	/**
	 * @function 更新
	 * @param query
	 * @param update
	 * @param clazz
	 */
	public void update(Query query, Update update, Class<T> clazz) {
		getDao().update(query, update, clazz);
	}

	/**
	 * @function 更新
	 * @param query
	 * @param update
	 * @param tableName
	 */
	public void update(Query query, Update update, String tableName) {
		getDao().update(query, update, tableName);
	}

	/**
	 * @function 更新
	 * @param query
	 * @param update
	 * @param clazz
	 */
	public void findAndModify(Query query, Update update, Class<T> clazz) {
		getDao().findAndModify(query, update, clazz);
	}

	/**
	 * @function 更新
	 * @param query
	 * @param update
	 * @param clazz
	 * @param tableName
	 */
	public void findAndModify(Query query, Update update, Class<T> clazz, String tableName) {
		getDao().findAndModify(query, update, clazz, tableName);
	}

	/**
	 * @function 更新
	 * @param query
	 * @param update
	 * @param clazz
	 */
	public void update(String whereKey, Object whereValue, String updateKey, Object updateValue, Class<T> clazz) {
		getDao().update(new Query(Criteria.where(whereKey).is(whereValue)), Update.update(updateKey, updateValue), clazz);
	}
}