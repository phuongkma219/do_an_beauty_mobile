package com.phuong.myspa.utils

import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.phuong.myspa.R
import com.phuong.myspa.data.PhotoModel
import com.phuong.myspa.data.api.model.shop.ShopInfor

@BindingAdapter("android:loadImageFromUrl")
fun ImageView.loadImageFromUrl(url:String) {
   if (url.isNotEmpty()){
       Glide.with(this).load(url)
           .placeholder(R.drawable.img_thumb_bg_default)
           .error(R.drawable.img_thumb_bg_default).into(this)
   }
}

fun View.onClickWithDebounce(debounceTime:Long =1500L, action:() ->Unit){
    this.setOnClickListener(object : View.OnClickListener{
        private var lastClickTime:Long =0
        override fun onClick(v: View?) {
            if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
            else action()

            lastClickTime = SystemClock.elapsedRealtime()
        }

    })
}
@BindingAdapter("android:setHours")
fun TextView.setHours(item: ShopInfor) {
    text = item.start_time + " to " + item.end_time
}
@BindingAdapter("setTime")
fun TextView.setTime(time:String){
    text = "Time : $time"
}
@BindingAdapter("setPrice")
fun TextView.setPrice(time:String){
    text = "Price : $time $"
}
@BindingAdapter("android:setRate")
fun RatingBar.setRate(rate:Double){
    this.rating = rate.toFloat()
}
@BindingAdapter("android:bindThumbnailFile")
fun ShapeableImageView.bindThumbnailFile(photoModel: PhotoModel?) {

    if (photoModel != null) {
        Glide.with(this).load(photoModel.file)
            .placeholder(R.drawable.ic_album_detail)
            .error(R.drawable.ic_album_detail).into(this)
    } else {
        Glide.with(this).load(R.drawable.ic_album_detail)
            .placeholder(R.drawable.ic_album_detail)
            .error(R.drawable.ic_album_detail).into(this)
    }
}