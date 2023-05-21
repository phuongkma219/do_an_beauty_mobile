package com.phuong.myspa.data.api.model.history

data class HistoryDTO(val shop_id:String, val service_id:String,var history_type :HistoryType = HistoryType.success )
enum class HistoryType{
    fail,success,processing
}
