/**
 * 
 */
package com.eshop.frameworks.core.dao.impl.common;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.eshop.frameworks.core.dao.DAO;
import com.eshop.frameworks.core.entity.MongoEntity;

/**
 * BasicDAO<T extends Entity,PK > 这里强制子类必须继承Entity采用公共的方法，用于检查子类是否继承，PK仅代表主键 备注：此类中所有的方法基本是针对MongoOperations中的 方法的简单调用，便于子类的直接实现
 * @author Zhang Xiao Dong
 * @param <PK>
 * @Date: 2012-4-12
 */
public abstract class AbstractBasicDAO<T extends MongoEntity, PK> implements DAO<T, PK> {
	@Resource
	private MongoTemplate mongoTemplate;

	public void createCollection(String tableName) {
		mongoTemplate.createCollection(tableName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.easyway.spring.mongodb.dao.DAO#collectionExists(java.lang.String)
	 */
	public boolean collectionExists(String tableName) {
		return mongoTemplate.collectionExists(tableName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.easyway.spring.mongodb.dao.DAO#dropCollection(java.lang.String)
	 */
	public void dropCollection(String tableName) {
		mongoTemplate.dropCollection(tableName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.easyway.spring.mongodb.dao.DAO#findOne(java.lang.String, org.springframework.data.document.mongodb.query.Criteria, java.lang.Class)
	 */
	public T findOne(Criteria criteria, Class<T> clazz) {
		return mongoTemplate.findOne(new Query(criteria), clazz);
	}

	public T findById(String id, Class<T> clazz) {
		return mongoTemplate.findById(id, clazz);
	}

	/*
	 * (non-Javadoc)
	 * @see com.easyway.spring.mongodb.dao.DAO#findOne(java.lang.String, org.springframework.data.document.mongodb.query.Query, java.lang.Class)
	 */
	public T findOne(String tableName, Query query, Class<T> clazz) {
		return mongoTemplate.findOne(query, clazz, tableName);
	}

	public T findOne(Query query, Class<T> clazz) {
		return mongoTemplate.findOne(query, clazz);
	}

	/*
	 * (non-Javadoc)
	 * @see com.leador.dao.DAO#findOne(java.lang.String, org.springframework.data.mongodb.core.query.Criteria, java.lang.Class)
	 */
	public T findOne(String tableName, Criteria criteria, Class<T> clazz) {
		return findOne(tableName, new Query(criteria), clazz);
	}

	/*
	 * (non-Javadoc)
	 * @see com.easyway.spring.mongodb.dao.DAO#findList(org.springframework.data. document.mongodb.query.Criteria, java.lang.Class)
	 */
	public List<T> findList(Criteria criteria, Class<T> clazz) {
		return mongoTemplate.find(new Query(criteria), clazz);
	}

	/*
	 * (non-Javadoc)
	 * @see com.easyway.spring.mongodb.dao.DAO#findAll( java.lang.Class)
	 */
	public List<T> findAll(Class<T> clazz) {
		return this.mongoTemplate.findAll(clazz);
	}

	/*
	 * (non-Javadoc)
	 * @see com.easyway.spring.mongodb.dao.DAO#findList(org.springframework.data. document.mongodb.query.Query, java.lang.Class)
	 */
	public List<T> findList(Query query, Class<T> clazz) {
		return mongoTemplate.find(query, clazz);
	}

	/*
	 * (non-Javadoc)
	 * @see com.easyway.spring.mongodb.dao.DAO#findAndRemove(org.springframework. data.document.mongodb.query.Query, java.lang.Class,
	 * org.springframework.data.document.mongodb.MongoReader)
	 */
	public T findAndRemove(Query query, Class<T> clazz) {
		return mongoTemplate.findAndRemove(query, clazz);
	}

	public T findAndRemove(Criteria criteria, Class<T> clazz) {
		return mongoTemplate.findAndRemove(new Query(criteria), clazz);
	}

	/*
	 * (non-Javadoc)
	 * @see com.easyway.spring.mongodb.dao.DAO#remove(java.lang.String, org.springframework.data.document.mongodb.query.Criteria, java.lang.Class)
	 */
	public void remove(String tableName, Criteria criteria, Class<T> clazz) {
		mongoTemplate.remove(new Query(criteria), clazz);
	}

	public void remove(T entity) {
		mongoTemplate.remove(entity);
	}

	public long size(Criteria criteria, Class<T> clazz) {
		return mongoTemplate.count(new Query(criteria), clazz);
	}

	public long size(Query query, Class<T> clazz) {
		return mongoTemplate.count(query, clazz);
	}

	/*
	 * (non-Javadoc)
	 * @see com.easyway.spring.mongodb.dao.DAO#save(java.lang.String, T)
	 */
	public void save(String tableName, T entity) {
		mongoTemplate.save(entity, tableName);
	}

	/*
	 * (non-Javadoc)
	 * @see com.easyway.spring.mongodb.dao.DAO#save(T, org.springframework.data.document.mongodb.MongoWriter)
	 */
	public void save(T entity) {
		mongoTemplate.save(entity);
	}

	public void insert(T entity) {
		mongoTemplate.insert(entity);
	}

	public void insertBatch(List<T> entitys, Class<T> clazz) {
		mongoTemplate.insert(entitys, clazz);
	}

	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	/**
	 * @function 更新
	 * @param query
	 * @param update
	 * @param clazz
	 */
	public void update(Query query, Update update, Class<T> clazz) {
		this.mongoTemplate.updateMulti(query, update, clazz);
	}

	/**
	 * @function 更新
	 * @param query
	 * @param update
	 * @param tableName
	 */
	public void update(Query query, Update update, String tableName) {
		this.mongoTemplate.updateMulti(query, update, tableName);
	}

	/**
	 * @function 更新
	 * @param query
	 * @param update
	 * @param clazz
	 */
	public void findAndModify(Query query, Update update, Class<T> clazz) {
		this.mongoTemplate.findAndModify(query, update, clazz);
	}

	/**
	 * @function 更新
	 * @param query
	 * @param update
	 * @param clazz
	 * @param tableName
	 */
	public void findAndModify(Query query, Update update, Class<T> clazz, String tableName) {
		this.mongoTemplate.findAndModify(query, update, clazz, tableName);
	}

}
