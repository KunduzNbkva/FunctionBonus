package com.example.appbonus

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Shader.TileMode
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class PickerAdapter : RecyclerView.Adapter<PickerAdapter.PickerItemViewHolder>() {

    private val data: ArrayList<Model> = ArrayList()
    var callback: Callback? = null
    private val clickListener = View.OnClickListener { v -> v?.let { callback?.onItemClicked(it) } }
    private var selectedItem: Int? = -1
    private var ctx: Context? = null

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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PickerItemViewHolder, position: Int) {
        holder.onBind(data[position])
        //TODO set gradient to text,problem with initializing of LinearGradient
//        val textShader: Shader = LinearGradient(
//            0,
//            0,
//            0,
//            20,
//            intArrayOf(R.color.orange, R.color.yellow), floatArrayOf(0f, 1f), Shader.TileMode.CLAMP)
//        holder.tvItem?.paint?.shader = textShader

//        if (selectedItem==position){
//            val text: String = holder.tvItem?.text.toString()
//            holder.tvItem?.text = text + data[position].text
//            holder.tvItem?.setTextColor(ContextCompat.getColor(ctx!!, R.color.yellow))
//        }
//        when (selectedItem) {
//
//            position -> {
//                val text: String = holder.tvItem?.text.toString()
//                holder.tvItem?.text = text + data[position].text
//                holder.tvItem?.setTextColor(ContextCompat.getColor(ctx!!, R.color.yellow))
//            }
//
//            else -> holder.tvItem?.setTextColor(ContextCompat.getColor(ctx!!, R.color.grey))
//
//        }
        if(selectedItem==position || position == 0){
            val text: String = holder.tvItem?.text.toString()
                holder.tvItem?.text = text + data[position].text
                holder.tvItem?.setTextColor(ContextCompat.getColor(ctx!!, R.color.yellow))
        }  else
        {
            holder.tvItem?.setTextColor(ContextCompat.getColor(ctx!!, R.color.grey))
        }

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

    class PickerItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

        val tvItem: TextView? = itemView?.findViewById(R.id.slide_number)

        @SuppressLint("ResourceAsColor")
        fun onBind(model: Model) {
            tvItem?.text = model.numberOf
            tvItem?.textSize = 22f
        }

    }
}