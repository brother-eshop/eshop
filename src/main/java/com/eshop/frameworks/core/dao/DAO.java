//
//                   _ooOoo_
//                  o8888888o
//                  88" . "88
//                  (| -_- |)
//                  O\  =  /O
//               ____/`---'\____
//             .'  \\|     |//  `.
//            /  \\|||  :  |||//  \
//           /  _||||| -:- |||||-  \
//           |   | \\\  -  /// |   |
//           | \_|  ''\---/''  |   |
//           \  .-\__  `-`  ___/-. /
//         ___`. .'  /--.--\  `. . __
//      ."" '<  `.___\_<|>_/___.'  >'"".
//      | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//     \  \ `-.   \_ __\ /__ _/   .-` /  /
//======`-.____`-.___\_____/___.-`____.-'======
//                   `=---='
//
//.............................................
//         佛祖保佑                  BUG辟易
//         心外无法                  法外无心
// 佛曰:
//         写字楼里写字间，写字间里程序员；
//         程序人员写程序，又拿程序换酒钱。
//         酒醒只在网上坐，酒醉还来网下眠；
//         酒醉酒醒日复日，网上网下年复年。
//         但愿老死电脑间，不愿鞠躬老板前；
//         奔驰宝马贵者趣，公交自行程序员。
//         别人笑我忒疯癫，我笑自己命太贱；
//         不见满街漂亮妹，哪个归得程序员？
package com.eshop.frameworks.core.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.eshop.frameworks.core.entity.MongoEntity;

/**
 * 数据库访问层底层接口类
 * @author Zhang Xiao Dong
 * @Date: 2012-4-18
 * @param <T>
 * @param <PK>
 */
public interface DAO<T extends MongoEntity, PK> {

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
	public T findOne(Query query, Class<T> clazz);

	public abstract T findOne(String tableName, Criteria criteria, Class<T> clazz);

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
	 * 查询一个Collection中的对象数
	 * @param tableName
	 *            Collection对象
	 * @param query
	 *            查询的条件
	 * @param clazz
	 *            T对象的Class
	 */
	public abstract long size(Criteria criteria, Class<T> clazz);

	/**
	 * 查询一个Collection中的对象数
	 * @param tableName
	 *            Collection对象
	 * @param criteria
	 *            查询的条件
	 * @param clazz
	 *            T对象的Class
	 */
	public abstract long size(Query query, Class<T> clazz);

	/**
	 * @function 查询所有
	 * @param clazz
	 * @return
	 */
	public abstract List<T> findAll(Class<T> clazz);

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

	/**
	 * 删除
	 * @param entity
	 *            T对象的Class
	 */
	public abstract void remove(T entity);

	/**
	 * 保存Mongo对象的方法
	 * @param tableName
	 *            Collection对象
	 * @param entity
	 *            T对象的Class
	 */
	public abstract void save(String tableName, T entity);

	/**
	 * 保存Mongo对象的方法
	 * @param entity
	 *            T对象的Class
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

}
