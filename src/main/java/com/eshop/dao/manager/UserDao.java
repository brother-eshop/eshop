package com.eshop.dao.manager;
import java.util.List;
import com.eshop.model.manager.User;
import com.eshop.frameworks.core.entity.PageEntity;
/**
 * User管理接口
 * User: spencer
 * Date: 2015-03-13
 */
public interface UserDao {

    /**
     * 添加User
     * @param user 要添加的User
     * @return id
     */
    public Long addUser(User user);
	public Long insertUser(User user);
    /**
     * 根据id删除一个User
     * @param id 要删除的id
     */
    public Long deleteUserById(Long id);
    
    public Long deleteUserByObj(User user);

	public Long deleteUserByIdList(List<Long > ids);

    /**
     * 修改User
     * @param user 要修改的User
     */
    public Long updateUser(User user);
    
    public Long updateUserByObj(User user);
    
    public Long updateUserByObjAndConditions(User s,User c);
    public void batchUpdateUser(List<User> records);

    /**
     * 根据id获取单个User对象
     * @param id 要查询的id
     * @return User
     */
    public User getUserById(Long id);
	public User getUserByObj(User user);
    /**
     * 根据条件获取User列表
     * @param user 查询条件
     * @return List<User>
     */
    public List<User> getUserList(User user);
    public List<User> getUserListByObj(User user);
    public List<User> getUserListPage(User user,Integer offset,Integer limit);
    public Integer getUserCountByObj(User user);
    public List<User> getUserPage(User user,PageEntity page);
    
    /**
    *以下为缓存查询用
    */
    public Long getUserIdByObj(User user);
    /**
     * 根据条件获取User列表
     * @param user 查询条件
     * @return List<User>
     */
    public List<Long> getUserIdList(User user);
    public List<Long> getUserIdListByObj(User user);
    public List<Long> getUserIdListPage(User user,Integer offset,Integer limit);
    public List<Long> getUserIdPage(User user,PageEntity page);
}