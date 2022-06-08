package org.onyx.moneymoney.binding

import android.os.Build
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import org.onyx.moneymoney.R
import java.math.BigDecimal

class BindAdapter {
    companion object {

        /**
         * 根据字符串获取图片
         */
        @JvmStatic
        @BindingAdapter("img_name")
        fun setImg(iv: ImageView, imgName: String) {
            val context = iv.context
            val resId: Int = if (TextUtils.isEmpty(imgName)) {
                context.resources.getIdentifier("ic_repast", "drawable", context.packageName)
            } else context.resources.getIdentifier(imgName, "drawable", context.packageName)
            iv.setImageResource(resId)
        }

        /**
         * 按需隐藏/显示imageView
         */
        @JvmStatic
        @BindingAdapter("show")
        fun show(iv: ImageView, isChecked: Int) {
            if (isChecked == 0) {
                iv.visibility = View.INVISIBLE
            } else iv.visibility = View.VISIBLE
        }

        /**
         * 首页显示的金额文字颜色处理
         */
        @RequiresApi(Build.VERSION_CODES.M)
        @JvmStatic
        @BindingAdapter("color_by_type")
        fun setTextColorByType(tv: TextView, type: Int) {
            // 0 为不统计，1 为正常统计
            // 3 支出， 5 收入
            // 3 不统计支出，4 正常统计支出， 5 不统计收入， 6 正常统计收入 
            when (type) {
                6 -> tv.setTextColor(tv.context.getColor(R.color.text_color_income))
                4 -> tv.setTextColor(tv.context.getColor(R.color.text_color_pay))
                3 -> tv.setTextColor(tv.context.getColor(R.color.text_color_ignore))
                5 -> tv.setTextColor(tv.context.getColor(R.color.text_color_ignore))
            }
        }

        /**
         * 记录是否统计，位于数字键盘右上角，关联了图标/字体颜色、文字内容
         */
        @JvmStatic
        @BindingAdapter("counted_select_state")
        fun setCountedState(rl: RelativeLayout, state: Int) {
            rl.isSelected = state == 1
        }

        /**
         * 添加记录时记录分类的选中标志
         */
        @JvmStatic
        @BindingAdapter("record_type_isSelect")
        fun setRecordTypeState(ly: LinearLayout, state: Int) {
            ly.isSelected = state == 1
        }

        /**
         * 首页金额的显示处理，正数添加+
         */
        @JvmStatic
        @BindingAdapter("money_text")
        fun setMoneyText(tv: TextView, value: BigDecimal) {
            if (value.toFloat() == 0f)
                tv.text = "0"
            else if (value.toFloat() < 0)
                tv.text = String.format(value.toString())
            else
                tv.text = String.format("+$value")
        }

        /**
         * 首页/统计的选中标志
         */
        @JvmStatic
        @BindingAdapter("navi_select")
        fun setMoneyText(ly: LinearLayout, isSelected: Boolean) {
            ly.isSelected = isSelected
        }
    }
}