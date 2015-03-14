package com.eshop.dao.manager;
import java.util.List;
import com.eshop.model.manager.Province;
import com.eshop.frameworks.core.entity.PageEntity;
/**
 * Province管理接口
 * User: spencer
 * Date: 2015-03-14
 */
public interface ProvinceDao {

    /**
     * 添加Province
     * @param province 要添加的Province
     * @return id
     */
    public Long addProvince(Province province);
	public Long insertProvince(Province province);
    /**
     * 根据id删除一个Province
     * @param codeid 要删除的id
     */
    public Long deleteProvinceById(Long codeid);
    
    public Long deleteProvinceByObj(Province province);

	public Long deleteProvinceByIdList(List<Long > ids);

    /**
     * 修改Province
     * @param province 要修改的Province
     */
    public Long updateProvince(Province province);
    
    public Long updateProvinceByObj(Province province);
    
    public Long updateProvinceByObjAndConditions(Province s,Province c);
    public void batchUpdateProvince(List<Province> records);

    /**
     * 根据id获取单个Province对象
     * @param codeid 要查询的id
     * @return Province
     */
    public Province getProvinceById(Long codeid);
	public Province getProvinceByObj(Province province);
    /**
     * 根据条件获取Province列表
     * @param province 查询条件
     * @return List<Province>
     */
    public List<Province> getProvinceList(Province province);
    public List<Province> getProvinceListByObj(Province province);
    public List<Province> getProvinceListPage(Province province,Integer offset,Integer limit);
    public Integer getProvinceCountByObj(Province province);
    public List<Province> getProvincePage(Province province,PageEntity page);
    
    /**
    *以下为缓存查询用
    */
    public Long getProvinceIdByObj(Province province);
    /**
     * 根据条件获取Province列表
     * @param province 查询条件
     * @return List<Province>
     */
    public List<Long> getProvinceIdList(Province province);
    public List<Long> getProvinceIdListByObj(Province province);
    public List<Long> getProvinceIdListPage(Province province,Integer offset,Integer limit);
    public List<Long> getProvinceIdPage(Province province,PageEntity page);
}