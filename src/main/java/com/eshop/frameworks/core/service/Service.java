/**
 * 
 */
package com.eshop.frameworks.core.service;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.eshop.frameworks.core.entity.PageEntity;

/**
 * @Project: jietu
 * @author Zhang Xiao Dong
 * @Date: 2012-4-18
 */
public interface Service<T, PK> {

	/**
	 * 创建Mongodb中一个tableName的Collection
	 * @param tableName
	 *            Collection的名称
	 */
	public abstract void createCollection(String tableName);

	/**
	 * 检查当前mongodb中以tableName命名的Collection是否存在
	 * @param tableName
	 * @return
	 */
	public abstract boolean collectionExists(String tableName);

	/**
	 * 删除Mongodb中一个Collection表集合
	 * @param tableName
	 *            collection的名称
	 */
	public abstract void dropCollection(String tableName);

	/**
	 * 查询一个Collection中的对象
	 * @param tableName
	 *            Collection对象
	 * @param criteria
	 *            查询的条件
	 * @param clazz
	 *            T对象的Class
	 */
	public abstract T findOne(String tableName, Criteria criteria, Class<T> clazz);

	public T findOne(Query query, Class<T> clazz);

	/**
	 * 查询一个Collection中的对象
	 * @param tableName
	 *            Collection对象
	 * @param query
	 *            查询的条件
	 * @param clazz
	 *            T对象的Class
	 */
	public abstract T findOne(String tableName, Query query, Class<T> clazz);

	public abstract T findById(String id, Class<T> clazz);

	/**
	 * @function 查询所有
	 * @param clazz
	 * @return
	 */
	public abstract List<T> findAll(Class<T> clazz);

	/**
	 * @function 查询数量
	 * @param clazz
	 * @return
	 */
	public abstract long size(Class<T> clazz);

	/**
	 * @function 查询数量
	 * @param clazz
	 * @return
	 */
	public abstract long size(Query query, Class<T> clazz);

	/**
	 * @function 查询数量
	 * @param clazz
	 * @return
	 */
	public abstract long size(Criteria criteria, Class<T> clazz);

	/**
	 * 查询符合条件的Collection中的多个对象
	 * @param criteria
	 *            查询条件
	 * @param clazz
	 *            T对象的Class
	 * @return
	 */

	public abstract List<T> findList(Criteria criteria, Class<T> clazz);

	/**
	 * 查询符合条件的Collection中的多个对象
	 * @param pageSize
	 *            每页记录数
	 * @param currentPage
	 *            当前页码，从1开始。
	 * @param clazz
	 *            T对象的Class
	 * @return
	 */
	public abstract List<T> limitFind(int pageSize, int currentPage, Class<T> clazz);

	/**
	 * 查询符合条件的Collection中的多个对象
	 * @param criteria
	 *            查询条件
	 * @param clazz
	 *            T对象的Class
	 * @return
	 */
	public abstract List<T> findList(Query query, Class<T> clazz);

	/**
	 * @param query
	 * @param clazz
	 * @param mongoReader
	 * @return
	 */
	public abstract T findAndRemove(Query query, Class<T> clazz);

	public abstract T findAndRemove(Criteria criteria, Class<T> clazz);

	/**
	 * 查询符合条件的对象集合
	 * @param tableName
	 * @param criteria
	 * @param clazz
	 */
	public abstract void remove(String tableName, Criteria criteria, Class<T> clazz);

	public abstract void remove(T entity);

	public abstract void removeById(String id, Class<T> clazz);

	/**
	 * 保存Mongo对象的方法
	 * @param tableName
	 *            Collection对象
	 * @param entity
	 *            T对象的Class
	 */
	public abstract void save(String tableName, T entity);

	/**
	 * 保存Mongo中Collection的对象
	 * @param entity
	 */
	public abstract void save(T entity);

	public abstract void insert(T entity);

	public abstract void insertBatch(List<T> entitys, Class<T> clazz);

	/**
	 * @function 更新
	 * @param query
	 * @param update
	 * @param clazz
	 */
	public abstract void update(Query query, Update update, Class<T> clazz);

	/**
	 * @function 更新
	 * @param query
	 * @param update
	 * @param tableName
	 */
	public abstract void update(Query query, Update update, String tableName);

	/**
	 * @function 更新
	 * @param query
	 * @param update
	 * @param clazz
	 */
	public abstract void findAndModify(Query query, Update update, Class<T> clazz);

	/**
	 * @function 更新
	 * @param query
	 * @param update
	 * @param clazz
	 * @param tableName
	 */
	public abstract void findAndModify(Query query, Update update, Class<T> clazz, String tableName);

	/**
	 * @function 更新
	 * @param query
	 * @param update
	 * @param clazz
	 */
	public void update(String whereKey, Object whereValue, String updateKey, Object updateValue, Class<T> clazz);

	public List<T> limitFind(int pageSize, int forward, Query query, Class<T> clazz);

	public List<T> limitFInd(Query query, PageEntity page, Class<T> clazz);
}
