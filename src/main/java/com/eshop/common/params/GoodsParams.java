package com.eshop.common.params;

import java.util.List;

import com.eshop.model.mongodb.ShopAndGoods;

public class GoodsParams {
	private List<ShopAndGoods> sgoods;

	public List<ShopAndGoods> getSgoods() {
		return sgoods;
	}

	public void setSgoods(List<ShopAndGoods> sgoods) {
		this.sgoods = sgoods;
	}
}
