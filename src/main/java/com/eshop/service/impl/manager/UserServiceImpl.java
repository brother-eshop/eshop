package com.eshop.service.impl.manager;

import java.util.List;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.eshop.model.manager.User;
import com.eshop.dao.manager.UserDao;
import com.eshop.service.manager.UserService;
import com.eshop.frameworks.core.entity.PageEntity;
/**
 * User管理接口
 * User: spencer
 * Date: 2015-03-13
 */
@Service("userService")
public class UserServiceImpl implements UserService{

 	@Autowired
    private UserDao userDao;
    //@Autowired
	//private QueryProvider queryCache;
    /**
     * 添加User
     * @param user 要添加的User
     * @return id
     */
    public Long addUser(User user){
    	Long res = userDao.addUser(user);
    	//if(res>0){
    	//	queryCache.setObj("SNS_User",String.valueOf(user.getId()),user);
    	//}
    	return res;
    }
	public Long insertUser(User user){
		Long res = userDao.insertUser(user);
		//if(res>0){
    	//	queryCache.setObj("SNS_User",String.valueOf(user.getId()),user);
    	//}
    	return res;
	}
    /**
     * 根据id删除一个User
     * @param id 要删除的id
     */
    public Long deleteUserById(Long id){
    	//queryCache.deleteObj("SNS_User",String.valueOf(id));
    	return userDao.deleteUserById(id);
    }
	public Long deleteUserByObj(User user){
    	//queryCache.deleteObj("SNS_User",String.valueOf(user.getId()));
        return userDao.deleteUserByObj(user);
    }
    public Long deleteUserByIdList(List<Long > ids){
    	//for(Long  id:ids){
    	//	queryCache.deleteObj("SNS_User",String.valueOf(id));
    	//}
    	return userDao.deleteUserByIdList(ids);
    }
    /**
     * 修改User
     * @param user 要修改的User
     */
    public Long updateUser(User user){
    	//queryCache.deleteObj("SNS_User",String.valueOf(user.getId()));
     	return userDao.updateUser(user);
    }
    
    public Long updateUserByObj(User user){
    	//queryCache.deleteObj("SNS_User",String.valueOf(user.getId()));
    	return userDao.updateUserByObj(user);
    }
    
    public Long updateUserByObjAndConditions(User s,User c){
    	//queryCache.deleteObj("SNS_User",String.valueOf(s.getId()));
    	return userDao.updateUserByObjAndConditions(s,c);
    }
	public void batchUpdateUser(List<User> records){
		//for(User user:records){
		//	queryCache.deleteObj("SNS_User",String.valueOf(user.getId()));
		//}
		userDao.batchUpdateUser(records);
	}
    /**
     * 根据id获取单个User对象
     * @param id 要查询的id
     * @return User
     */
    
    public Integer getUserCountByObj(User user){
    	return userDao.getUserCountByObj(user);
    }
    
/**
    public User getUserById(Long id){
    	User user =(User)queryCache.queryObj("SNS_User", String.valueOf( id));
    	if(user==null){
    		user =userDao.getUserById( id);
    		if(user!=null){
    			queryCache.setObj("SNS_User",String.valueOf( id),user);
    		}
    	}
    	return user;
    }
    public User getUserByObj(User user) {
        Long id = user.getId();
    	if(id==null){
    		id= userDao.getUserIdByObj(user);
    	}
    	return this.getUserById(id);
    }

    public List<User> getUserList(User user){
    	List<Long> ids =  userDao.getUserIdList(user);
    	List<User> list = new ArrayList<User>();
    	if(ids!=null&&ids.size()>0){
    		for(Long id:ids){
    			list.add(this.getUserById(id));
    		}
    	}
    	return list;
    }
    
    public List<User> getUserListByObj(User user){
        List<Long> ids =  userDao.getUserIdListByObj(user);
        List<User> list = new ArrayList<User>();
    	if(ids!=null&&ids.size()>0){
    		for(Long id:ids){
    			list.add(this.getUserById(id));
    		}
    	}
    	return list;
    }
    public List<User> getUserListPage(User user,Integer offset,Integer limit){
        List<Long> ids =  userDao.getUserIdListPage(user,offset,limit);
        List<User> list = new ArrayList<User>();
    	if(ids!=null&&ids.size()>0){
    		for(Long id:ids){
    			list.add(this.getUserById(id));
    		}
    	}
    	return list;
    }
    public List<User> getUserPage(User user,PageEntity page) {
        List<Long> ids =  userDao.getUserIdPage(user,page);
        List<User> list = new ArrayList<User>();
    	if(ids!=null&&ids.size()>0){
    		for(Long id:ids){
    			list.add(this.getUserById(id));
    		}
    	}
    	return list;
    }
 */
    public User getUserById(Long id){
    	return userDao.getUserById( id);
    }
    
     public User getUserByObj(User user) {
        return userDao.getUserByObj(user);
    }

    public List<User> getUserList(User user){
    	return userDao.getUserList(user);
    }
    
    public List<User> getUserListByObj(User user){
        return userDao.getUserListByObj(user);
    }
    public List<User> getUserListPage(User user,Integer offset,Integer limit){
        return userDao.getUserListPage(user,offset,limit);
    }
    
    public List<User> getUserPage(User user,PageEntity page) {
        return userDao.getUserPage(user,page);
    }
}