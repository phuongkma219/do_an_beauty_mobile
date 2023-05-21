package com.phuong.myspa.data.api.model.cart

import com.phuong.myspa.data.api.model.shop.Item
import com.phuong.myspa.data.api.model.shop.ShopInfor

class DataCart (var shop: ShopInfor, var items : MutableList<Item>)