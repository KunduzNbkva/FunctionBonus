package com.example.appbonus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val data = ArrayList<Model>()
    private lateinit var rvHorizontalPicker: RecyclerView
    private lateinit var sliderAdapter: PickerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        data.addAll(
            listOf(
                Model("5", "мин", "30"),
                Model("10", "мин", "50"),
                Model("25", "мин", "100"),
                Model("60", "мин", "200"),
                Model("180", "мин", "400")
            )
        )
        setHorizontalPicker()
    }

    private fun setHorizontalPicker() {
        rvHorizontalPicker = findViewById(R.id.rv_horizontal_picker)
        // Setting the padding such that the items will appear in the middle of the screen
        val padding: Int = ScreenUtils.getScreenWidth(this) / 2 - ScreenUtils.dpToPx(this, 60)
        rvHorizontalPicker.setPadding(padding, 0, padding, 0)

        setLayoutManager()

        setAdapter()
    }

    private fun setLayoutManager() {
        // Setting layout manager
        rvHorizontalPicker.layoutManager = PickerLayoutManager(this).apply {
            callback = object : PickerLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    sliderAdapter.setSelectedItem(layoutPosition)
                    findViewById<TextView>(R.id.selected_item_points).text =
                        data[layoutPosition].points
                }
            }
        }
    }

    private fun setAdapter() {
        // Setting Adapter
        sliderAdapter = PickerAdapter()
        rvHorizontalPicker.adapter = sliderAdapter.apply {
            setData(data)
            callback = object : PickerAdapter.Callback {
                override fun onItemClicked(view: View) {
                    rvHorizontalPicker.smoothScrollToPosition(
                        rvHorizontalPicker.getChildLayoutPosition(view)
                    )
                }
            }
        }
    }
}
