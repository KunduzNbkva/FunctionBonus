package com.example.appbonus.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.appbonus.Model
import com.example.appbonus.R


class SmsAdapter() : RecyclerView.Adapter<SmsAdapter.PickerItemViewHolder>() {

    private val data: ArrayList<Model> = ArrayList()
    var callback: Callback? = null
    private val clickListener = View.OnClickListener { v -> v?.let { callback?.onItemClicked(it) } }
    private var selectedItem: Int? = 0
    private var ctx: Context? = null
    var viewHolder: PickerItemViewHolder? = null


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PickerItemViewHolder {

        ctx = parent.context

        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.slide_item, parent, false)

        itemView.setOnClickListener(clickListener)
        return PickerItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PickerItemViewHolder, position: Int) {
        holder.onBind(data[position])
    }


    fun setSelectedItem(position: Int) {
        selectedItem = position
        notifyDataSetChanged()

    }


    fun setData(data: ArrayList<Model>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    interface Callback {
        fun onItemClicked(view: View)
    }

    inner class PickerItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val tvItem: TextView? = itemView?.findViewById(R.id.slide_number)



        @SuppressLint("ResourceAsColor", "SetTextI18n")
        fun onBind(model: Model) {
            tvItem?.textSize = 22f
           if (selectedItem?.equals(adapterPosition)!!) {
               setGradient(textView = tvItem!!)
                tvItem?.text = model.numberOf + model.text
            } else {
                tvItem?.setTextColor(ContextCompat.getColor(ctx!!,
                    R.color.grey
                ))
               tvItem?.paint?.shader = null
                tvItem?.text = model.numberOf + model.text.replace(model.text, "")
            }
        }

        private fun setGradient(textView: TextView){
            val textShader: Shader = LinearGradient(0f,0f,185f,textView.textSize, intArrayOf(
                ContextCompat.getColor(ctx!!,
                    R.color.orange
                ),
                ContextCompat.getColor(ctx!!,
                    R.color.yellow
                )
            ),null,Shader.TileMode.REPEAT)
            textView.paint.shader = textShader
        }

    }
}