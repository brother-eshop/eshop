package com.eshop.dao.impl.manager;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.eshop.model.manager.Province;
import com.eshop.dao.manager.ProvinceDao;
import org.springframework.stereotype.Repository;
import com.eshop.frameworks.core.dao.impl.common.GenericDaoImpl;
import com.eshop.common.util.BeanMapConvertor;
import com.eshop.frameworks.core.entity.PageEntity;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
/**
 *
 * Province
 * User: spencer
 * Date: 2015-03-14
 */
public class ProvinceDaoImpl extends GenericDaoImpl implements ProvinceDao{

    public Long addProvince(Province province) {
        return this.insert("com.eshop.model.manager.ProvinceMapper.createProvince",province);
    }
	public Long insertProvince(Province province){
		return this.insert("com.eshop.model.manager.ProvinceMapper.insertProvince",province);
	}
    public Long deleteProvinceById(Long codeid){
        return this.delete("com.eshop.model.manager.ProvinceMapper.deleteProvinceById",codeid);
    }
    public Long deleteProvinceByObj(Province province){
        return this.delete("com.eshop.model.manager.ProvinceMapper.deleteProvinceByObj",province);
    }
    
	public Long deleteProvinceByIdList(List<Long > ids){
		return this.delete("com.eshop.model.manager.ProvinceMapper.deleteProvinceByIdList",ids);
	}
    public Long updateProvince(Province province) {
        return this.update("com.eshop.model.manager.ProvinceMapper.updateProvince",province);
    }
    
    public Long updateProvinceByObj(Province province){
    	return this.update("com.eshop.model.manager.ProvinceMapper.updateProvinceByObj",province);
    }
    
    public Long updateProvinceByObjAndConditions(Province s,Province c){
    	Map<String,Province> map = new HashMap<String,Province>();
    	map.put("s",s);
    	map.put("c",c);
    	return this.update("com.eshop.model.manager.ProvinceMapper.updateProvinceByObjAndConditions",map);
    }
	public void batchUpdateProvince(List<Province> records){
		this.update("com.eshop.model.manager.ProvinceMapper.batchUpdateProvince",records);
	}
    public Province getProvinceById(Long codeid) {
        return this.selectOneFromWriter("com.eshop.model.manager.ProvinceMapper.getProvinceById",codeid);
    }
    
    public Province getProvinceByObj(Province province) {
        return this.selectOne("com.eshop.model.manager.ProvinceMapper.getProvinceByObj",province);
    }

    public List<Province> getProvinceList(Province province) {
        return this.selectList("com.eshop.model.manager.ProvinceMapper.getProvinceList",province);
    }
    
    public List<Province> getProvinceListByObj(Province province){
        return this.selectList("com.eshop.model.manager.ProvinceMapper.getProvinceListByObj",province);
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Province> getProvinceListPage(Province province,Integer offset,Integer limit){
    	try {
			Map map = BeanMapConvertor.convertBean(province);
			map.put("offset",offset);
    		map.put("limit",limit);
        	return this.selectList("com.eshop.model.manager.ProvinceMapper.getProvinceListByMap",map);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return null;
    }
    public Integer getProvinceCountByObj(Province province){
    	return this.selectOne("com.eshop.model.manager.ProvinceMapper.getProvinceCountByObj", province);
    }
    
    public List<Province> getProvincePage(Province province,PageEntity page) {
        Integer objectscount = getProvinceCountByObj(province);
        if (objectscount == null || objectscount == 0) {
            page.setTotalResultSize(0);
            int totalPageSize = (page.getTotalResultSize() - 1) / page.getPageSize() + 1;
            page.setTotalPageSize(totalPageSize);
            return null;
        } else {
            page.setTotalResultSize(objectscount);
            int totalPageSize = (page.getTotalResultSize() - 1) / page.getPageSize() + 1;
            page.setTotalPageSize(totalPageSize);
            return getProvinceListPage(province,(page.getCurrentPage() - 1) * page.getPageSize(),page.getPageSize());
        }
    }
    
    
    
     /**
    *以下为缓存查询用
    */
    public Long getProvinceIdByObj(Province province) {
        return this.selectOne("com.eshop.model.manager.ProvinceMapper.getProvinceIdByObj",province);
    }

    public List<Long> getProvinceIdList(Province province) {
        return this.selectList("com.eshop.model.manager.ProvinceMapper.getProvinceIdList",province);
    }
    
    public List<Long> getProvinceIdListByObj(Province province){
        return this.selectList("com.eshop.model.manager.ProvinceMapper.getProvinceIdListByObj",province);
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Long> getProvinceIdListPage(Province province,Integer offset,Integer limit){
    	try {
			Map map = BeanMapConvertor.convertBean(province);
			map.put("offset",offset);
    		map.put("limit",limit);
        	return this.selectList("com.eshop.model.manager.ProvinceMapper.getProvinceIdListByMap",map);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return null;
    }
    public List<Long> getProvinceIdPage(Province province,PageEntity page) {
        Integer objectscount = getProvinceCountByObj(province);
        if (objectscount == null || objectscount == 0) {
            page.setTotalResultSize(0);
            int totalPageSize = (page.getTotalResultSize() - 1) / page.getPageSize() + 1;
            page.setTotalPageSize(totalPageSize);
            return null;
        } else {
            page.setTotalResultSize(objectscount);
            int totalPageSize = (page.getTotalResultSize() - 1) / page.getPageSize() + 1;
            page.setTotalPageSize(totalPageSize);
            return getProvinceIdListPage(province,(page.getCurrentPage() - 1) * page.getPageSize(),page.getPageSize());
        }
    }
}
