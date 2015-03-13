package com.eshop.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public interface QueryProvider {

	public List<?> queryList(String module, String key);

	public void setList(String module, String key, List<?> list);

	public Serializable queryObj(String module, String key);

	public void setObj(String module, String key, Serializable obj);

	public void deleteObj(String module, String key);

	public HashMap<String, Serializable> queryMap(String module, String key);

	public void setMap(String module, String key,
			HashMap<String, Serializable> map);

	public void clear(String module);

}
