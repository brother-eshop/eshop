package com.eshop.dao.impl.manager;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.eshop.model.manager.User;
import com.eshop.dao.manager.UserDao;
import org.springframework.stereotype.Repository;
import com.eshop.frameworks.core.dao.impl.common.GenericDaoImpl;
import com.eshop.common.util.BeanMapConvertor;
import com.eshop.frameworks.core.entity.PageEntity;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
/**
 *
 * User
 * User: spencer
 * Date: 2015-03-13
 */
 @Repository("userDao")
public class UserDaoImpl extends GenericDaoImpl implements UserDao{

    public Long addUser(User user) {
        return this.insert("com.eshop.model.manager.UserMapper.createUser",user);
    }
	public Long insertUser(User user){
		return this.insert("com.eshop.model.manager.UserMapper.insertUser",user);
	}
    public Long deleteUserById(Long id){
        return this.delete("com.eshop.model.manager.UserMapper.deleteUserById",id);
    }
    public Long deleteUserByObj(User user){
        return this.delete("com.eshop.model.manager.UserMapper.deleteUserByObj",user);
    }
    
	public Long deleteUserByIdList(List<Long > ids){
		return this.delete("com.eshop.model.manager.UserMapper.deleteUserByIdList",ids);
	}
    public Long updateUser(User user) {
        return this.update("com.eshop.model.manager.UserMapper.updateUser",user);
    }
    
    public Long updateUserByObj(User user){
    	return this.update("com.eshop.model.manager.UserMapper.updateUserByObj",user);
    }
    
    public Long updateUserByObjAndConditions(User s,User c){
    	Map<String,User> map = new HashMap<String,User>();
    	map.put("s",s);
    	map.put("c",c);
    	return this.update("com.eshop.model.manager.UserMapper.updateUserByObjAndConditions",map);
    }
	public void batchUpdateUser(List<User> records){
		this.update("com.eshop.model.manager.UserMapper.batchUpdateUser",records);
	}
    public User getUserById(Long id) {
        return this.selectOneFromWriter("com.eshop.model.manager.UserMapper.getUserById",id);
    }
    
    public User getUserByObj(User user) {
        return this.selectOne("com.eshop.model.manager.UserMapper.getUserByObj",user);
    }

    public List<User> getUserList(User user) {
        return this.selectList("com.eshop.model.manager.UserMapper.getUserList",user);
    }
    
    public List<User> getUserListByObj(User user){
        return this.selectList("com.eshop.model.manager.UserMapper.getUserListByObj",user);
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<User> getUserListPage(User user,Integer offset,Integer limit){
    	try {
			Map map = BeanMapConvertor.convertBean(user);
			map.put("offset",offset);
    		map.put("limit",limit);
        	return this.selectList("com.eshop.model.manager.UserMapper.getUserListByMap",map);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return null;
    }
    public Integer getUserCountByObj(User user){
    	return this.selectOne("com.eshop.model.manager.UserMapper.getUserCountByObj", user);
    }
    
    public List<User> getUserPage(User user,PageEntity page) {
        Integer objectscount = getUserCountByObj(user);
        if (objectscount == null || objectscount == 0) {
            page.setTotalResultSize(0);
            int totalPageSize = (page.getTotalResultSize() - 1) / page.getPageSize() + 1;
            page.setTotalPageSize(totalPageSize);
            return null;
        } else {
            page.setTotalResultSize(objectscount);
            int totalPageSize = (page.getTotalResultSize() - 1) / page.getPageSize() + 1;
            page.setTotalPageSize(totalPageSize);
            return getUserListPage(user,(page.getCurrentPage() - 1) * page.getPageSize(),page.getPageSize());
        }
    }
    
    
    
     /**
    *以下为缓存查询用
    */
    public Long getUserIdByObj(User user) {
        return this.selectOne("com.eshop.model.manager.UserMapper.getUserIdByObj",user);
    }

    public List<Long> getUserIdList(User user) {
        return this.selectList("com.eshop.model.manager.UserMapper.getUserIdList",user);
    }
    
    public List<Long> getUserIdListByObj(User user){
        return this.selectList("com.eshop.model.manager.UserMapper.getUserIdListByObj",user);
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Long> getUserIdListPage(User user,Integer offset,Integer limit){
    	try {
			Map map = BeanMapConvertor.convertBean(user);
			map.put("offset",offset);
    		map.put("limit",limit);
        	return this.selectList("com.eshop.model.manager.UserMapper.getUserIdListByMap",map);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return null;
    }
    public List<Long> getUserIdPage(User user,PageEntity page) {
        Integer objectscount = getUserCountByObj(user);
        if (objectscount == null || objectscount == 0) {
            page.setTotalResultSize(0);
            int totalPageSize = (page.getTotalResultSize() - 1) / page.getPageSize() + 1;
            page.setTotalPageSize(totalPageSize);
            return null;
        } else {
            page.setTotalResultSize(objectscount);
            int totalPageSize = (page.getTotalResultSize() - 1) / page.getPageSize() + 1;
            page.setTotalPageSize(totalPageSize);
            return getUserIdListPage(user,(page.getCurrentPage() - 1) * page.getPageSize(),page.getPageSize());
        }
    }
}
