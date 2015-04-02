package com.eshop.service.impl.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eshop.dao.manager.ProvinceDao;
import com.eshop.frameworks.core.entity.PageEntity;
import com.eshop.model.manager.Province;
import com.eshop.service.manager.ProvinceService;
/**
 * Province管理接口
 * User: spencer
 * Date: 2015-03-14
 */
public class ProvinceServiceImpl implements ProvinceService{

 	@Autowired
    private ProvinceDao provinceDao;
    //@Autowired
	//private QueryProvider queryCache;
    /**
     * 添加Province
     * @param province 要添加的Province
     * @return id
     */
    public Long addProvince(Province province){
    	Long res = provinceDao.addProvince(province);
//    	if(res>0){
//    		queryCache.setObj("SNS_Province",String.valueOf(province.getId()),province);
//    	}
    	return res;
    }
	public Long insertProvince(Province province){
		Long res = provinceDao.insertProvince(province);
//		if(res>0){
//    		queryCache.setObj("SNS_Province",String.valueOf(province.getId()),province);
//    	}
    	return res;
	}
    /**
     * 根据id删除一个Province
     * @param codeid 要删除的id
     */
    public Long deleteProvinceById(Long codeid){
//    	queryCache.deleteObj("SNS_Province",String.valueOf(codeid));
    	return provinceDao.deleteProvinceById(codeid);
    }
	public Long deleteProvinceByObj(Province province){
//    	queryCache.deleteObj("SNS_Province",String.valueOf(province.getId()));
        return provinceDao.deleteProvinceByObj(province);
    }
    public Long deleteProvinceByIdList(List<Long > ids){
//    	for(Long  id:ids){
//    		queryCache.deleteObj("SNS_Province",String.valueOf(id));
//    	}
    	return provinceDao.deleteProvinceByIdList(ids);
    }
    /**
     * 修改Province
     * @param province 要修改的Province
     */
    public Long updateProvince(Province province){
//    	queryCache.deleteObj("SNS_Province",String.valueOf(province.getId()));
     	return provinceDao.updateProvince(province);
    }
    
    public Long updateProvinceByObj(Province province){
//    	queryCache.deleteObj("SNS_Province",String.valueOf(province.getId()));
    	return provinceDao.updateProvinceByObj(province);
    }
    
    public Long updateProvinceByObjAndConditions(Province s,Province c){
//    	queryCache.deleteObj("SNS_Province",String.valueOf(s.getId()));
    	return provinceDao.updateProvinceByObjAndConditions(s,c);
    }
	public void batchUpdateProvince(List<Province> records){
//		for(Province province:records){
//			queryCache.deleteObj("SNS_Province",String.valueOf(province.getId()));
//		}
		provinceDao.batchUpdateProvince(records);
	}
    /**
     * 根据id获取单个Province对象
     * @param codeid 要查询的id
     * @return Province
     */
    
    public Integer getProvinceCountByObj(Province province){
    	return provinceDao.getProvinceCountByObj(province);
    }
    
/**
    public Province getProvinceById(Long codeid){
    	Province province =(Province)queryCache.queryObj("SNS_Province", String.valueOf( codeid));
    	if(province==null){
    		province =provinceDao.getProvinceById( codeid);
    		if(province!=null){
    			queryCache.setObj("SNS_Province",String.valueOf( codeid),province);
    		}
    	}
    	return province;
    }
    public Province getProvinceByObj(Province province) {
        Long id = province.getId();
    	if(id==null){
    		id= provinceDao.getProvinceIdByObj(province);
    	}
    	return this.getProvinceById(id);
    }

    public List<Province> getProvinceList(Province province){
    	List<Long> ids =  provinceDao.getProvinceIdList(province);
    	List<Province> list = new ArrayList<Province>();
    	if(ids!=null&&ids.size()>0){
    		for(Long id:ids){
    			list.add(this.getProvinceById(id));
    		}
    	}
    	return list;
    }
    
    public List<Province> getProvinceListByObj(Province province){
        List<Long> ids =  provinceDao.getProvinceIdListByObj(province);
        List<Province> list = new ArrayList<Province>();
    	if(ids!=null&&ids.size()>0){
    		for(Long id:ids){
    			list.add(this.getProvinceById(id));
    		}
    	}
    	return list;
    }
    public List<Province> getProvinceListPage(Province province,Integer offset,Integer limit){
        List<Long> ids =  provinceDao.getProvinceIdListPage(province,offset,limit);
        List<Province> list = new ArrayList<Province>();
    	if(ids!=null&&ids.size()>0){
    		for(Long id:ids){
    			list.add(this.getProvinceById(id));
    		}
    	}
    	return list;
    }
    public List<Province> getProvincePage(Province province,PageEntity page) {
        List<Long> ids =  provinceDao.getProvinceIdPage(province,page);
        List<Province> list = new ArrayList<Province>();
    	if(ids!=null&&ids.size()>0){
    		for(Long id:ids){
    			list.add(this.getProvinceById(id));
    		}
    	}
    	return list;
    }
 */
    public Province getProvinceById(Long codeid){
    	return provinceDao.getProvinceById( codeid);
    }
    
     public Province getProvinceByObj(Province province) {
        return provinceDao.getProvinceByObj(province);
    }

    public List<Province> getProvinceList(Province province){
    	return provinceDao.getProvinceList(province);
    }
    
    public List<Province> getProvinceListByObj(Province province){
    	System.out.println("----------------------");
        return provinceDao.getProvinceListByObj(province);
    }
    public List<Province> getProvinceListPage(Province province,Integer offset,Integer limit){
        return provinceDao.getProvinceListPage(province,offset,limit);
    }
    
    public List<Province> getProvincePage(Province province,PageEntity page) {
        return provinceDao.getProvincePage(province,page);
    }
}