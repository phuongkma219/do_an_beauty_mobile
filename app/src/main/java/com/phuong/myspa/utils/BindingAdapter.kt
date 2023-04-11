package com.phuong.myspa.utils

import android.os.SystemClock
import android.view.View
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.phuong.myspa.MyApp
import com.phuong.myspa.R
import com.phuong.myspa.data.PhotoModel
import com.phuong.myspa.data.api.model.shop.ShopInfor
import com.phuong.myspa.ui.popup.ActionModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


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
    text = MyApp.resource().getString(R.string.time) +" : $time"
}
@BindingAdapter("setPrice")
fun TextView.setPrice(time:String){
    text =MyApp.resource().getString(R.string.price)+ ": $time $"
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
@BindingAdapter("android:iconForAction")
fun ImageView.iconForAction(actionModel: ActionModel) {
    if (actionModel.icon <0) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
        setImageResource(actionModel.icon)
    }

}

@BindingAdapter("setTimeComment")
fun setTimeComment(tv: TextView, time: String) {
    if (time.equals("")) {
        tv.text = MyApp.resource().getString(R.string.just_finished)
    } else {
        val simpleDateFormat = SimpleDateFormat("y-M-d'T'H:m:s", Locale.ENGLISH)
        try {
            val dateS: Date = simpleDateFormat.parse(time)
            val dateN: String =
                SimpleDateFormat("y-M-d'T'H:m:s", Locale.getDefault()).format(Date())
            val diff: Long =
                simpleDateFormat.parse(dateN).getTime() - dateS.getTime() - 7 * 60 * 60 * 1000
            val diffMinutes = diff / (60 * 1000) % 60
            val diffHours = diff / (60 * 60 * 1000)
            if (diffHours > 23) {
                val date = (diffHours / 24).toInt()
                if (date > 5) {
                    val format: DateFormat =
                        DateFormat.getDateInstance(DateFormat.MEDIUM, Locale("vi", "VN"))
                    val formatT = SimpleDateFormat("HH:mm")
                    tv.setText(format.format(dateS) + " ${MyApp.resource().getString(R.string.at)} " + formatT.format(dateS))
                } else {
                    val hour = (diffHours / 24).toInt()
                    tv.text = "$hour " + MyApp.resource().getString(R.string.days_ago)
                }
            } else if (diffHours < 23 && diffHours >= 1) {
                tv.text = "$diffHours " +MyApp.resource().getString(R.string.hours)
            } else if (diffHours < 1) {
                if (diffMinutes > 1) {
                    tv.text = "$diffMinutes " +MyApp.resource().getString(R.string.minute)
                } else {
                    tv.text = MyApp.resource().getString(R.string.just_finished)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}