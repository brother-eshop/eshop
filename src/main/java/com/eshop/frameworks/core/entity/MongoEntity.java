package com.eshop.frameworks.core.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;

import com.eshop.frameworks.core.util.json.JsonUtil;

@Data
@EqualsAndHashCode(callSuper = false)
public class MongoEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 数据生成时间
	 */
	@Indexed(unique = false, direction = IndexDirection.DESCENDING, name = "createTime", dropDups = true)
	private Date createTime = new Date(System.currentTimeMillis());
	private Date modifyTime;
	private String createUser;
	private String modifyUser;

	@Override
	public String toString() {
		return JsonUtil.bean2json(this);
	}

}
