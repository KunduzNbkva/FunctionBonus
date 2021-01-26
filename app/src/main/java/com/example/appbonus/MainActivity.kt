package com.example.appbonus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appbonus.adapters.MegabitesAdapter
import com.example.appbonus.customViews.LinkEnabledTextView
import com.example.appbonus.customViews.PickerLayoutManager


const val TYPE_CURRENT_POS_MEGA=0
const val TYPE_CURRENT_POS_MIN=1
const val TYPE_CURRENT_POS_SMS=2

class MainActivity : AppCompatActivity()  {
    private val megabytesList = ArrayList<Model>()
    private val minutesList = ArrayList<Model>()
    private val smsList = ArrayList<Model>()
    private lateinit var megabitesPickerRV: RecyclerView
    private lateinit var minutesPickerRV: RecyclerView
    private lateinit var smsPickerRV: RecyclerView
    private lateinit var megabitesAdapter: MegabitesAdapter
    private lateinit var minutesAdapter: MegabitesAdapter
    private lateinit var smsAdapter: MegabitesAdapter
    private var currentPosMega=0
    private var currentPosMin=0
    private var currentPosSms=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews(){
        val megabitesButton=findViewById<Button>(R.id.changeToMb_btn)
        val minutesButton=findViewById<Button>(R.id.changeToMin_btn)
        val smsButton=findViewById<Button>(R.id.changeToSms_btn)

        initLinkedTextView()

        megabitesPickerRV = findViewById(R.id.rv_megabites_picker)
        minutesPickerRV = findViewById(R.id.rv_minutes_picker)
        smsPickerRV = findViewById(R.id.rv_sms_picker)

        megabytesList.addAll(
            listOf(
                Model("10", "Mб", "30","*800*6*10#"),
                Model("20", "Mб", "50", "*800*6*20#"),
                Model("50", "Mб", "100", "*800*6*50#"),
                Model("120", "Mб", "200", "*800*6*120#"),
                Model("360", "Mб", "400","*800*6*360#")
            )
        )
        minutesList.addAll(
            listOf(
                Model("5", "мин", "30","*800*1*5#"),
                Model("10", "мин", "50", "*800*1*10#"),
                Model("25", "мин", "100", "*800*1*25#"),
                Model("60", "мин", "200", "*800*1*60#"),
                Model("180", "мин", "400","*800*1*180#")
            )
        )
        smsList.addAll(
            listOf(
                Model("10", "Sms", "30","*800*2*10#"),
                Model("20", "Sms", "50", "*800*2*20#"),
                Model("50", "Sms", "100", "*800*2*50#"),
                Model("120", "Sms", "200", "*800*2*120#"),
                Model("360", "Sms", "400","*800*2*360#"))
        )

        getCurrentPos()

        megabitesAdapter = MegabitesAdapter()
        minutesAdapter = MegabitesAdapter()
        smsAdapter = MegabitesAdapter()

        megabitesButton.setOnClickListener { changeBtn(megabytesList, currentPosMega) }
        minutesButton.setOnClickListener { changeBtn(minutesList, currentPosMin) }
        smsButton.setOnClickListener { changeBtn(smsList, currentPosSms) }

        setPickers(megabitesPickerRV,R.id.megabites_in_points,megabytesList,megabitesAdapter)
        setPickers(minutesPickerRV,R.id.minutes_in_points,minutesList,minutesAdapter)
        setPickers(smsPickerRV,R.id.sms_in_points,smsList,smsAdapter)
    }

    private fun initLinkedTextView() {
        val textView =
            findViewById<View>(R.id.linkedTextView) as LinkEnabledTextView
        textView.setOnTextLinkClickListener(this::onTextLinkClick)
        textView.text = getText(R.string.dop_text)
    }

    private fun setPickers(rv:RecyclerView,view:Int,list:ArrayList<Model>, adapter:MegabitesAdapter) {
        // Setting the padding such that the items will appear in the middle of the screen
        val padding: Int = ScreenUtils.getScreenWidth(this) / 2 - ScreenUtils.dpToPx(this, 60)
        rv.setPadding(padding, 0, padding, 0)
        setLayoutManager(rv,view,list, adapter)
        setAdapter(rv, adapter, list)
    }

    private fun setLayoutManager(rv: RecyclerView,view:Int,list:ArrayList<Model>, adapter:MegabitesAdapter ) {
        // Setting layout manager
        rv.layoutManager = PickerLayoutManager(
            this
        ).apply {
            callback = object : PickerLayoutManager.OnItemSelectedListener {
                override fun onItemSelected(layoutPosition: Int) {
                    adapter.setSelectedItem(layoutPosition)
                    findViewById<TextView>(view).text =
                        list[layoutPosition].points
                }
            }
        }
    }

    private fun setAdapter(
        rv: RecyclerView,
        sliderAdapter: MegabitesAdapter,
        list: ArrayList<Model>
    ) {
        // Setting Adapter
        rv.adapter = sliderAdapter.apply {
            setData(list)
            callback = object : MegabitesAdapter.Callback {
                override fun onItemClicked(view: View) {
                    rv.smoothScrollToPosition(
                        rv.getChildLayoutPosition(view)
                    )
                }
            }
        }

    }

    private fun getCurrentPos() {
        setPosListener(megabitesPickerRV, TYPE_CURRENT_POS_MEGA)
        setPosListener(minutesPickerRV, TYPE_CURRENT_POS_MIN)
        setPosListener(smsPickerRV, TYPE_CURRENT_POS_SMS)
    }

    private fun setPosListener(rv:RecyclerView, posType:Int){
        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val linearLayoutManager: LinearLayoutManager? =
                    recyclerView.layoutManager as LinearLayoutManager?
                linearLayoutManager?.findFirstVisibleItemPosition()?.let {
                    when(posType){
                        TYPE_CURRENT_POS_MEGA -> currentPosMega = it
                        TYPE_CURRENT_POS_MIN -> currentPosMin = it
                        TYPE_CURRENT_POS_SMS -> currentPosSms = it
                    }
                }
            }
        })
    }

    private fun changeBtn(list:ArrayList<Model>, currentPos: Int){
            val number = Uri.fromParts("tel",list[currentPos].ussd,null)
            val dialIntent = Intent(Intent.ACTION_DIAL,number)
            startActivity(dialIntent)
    }

    fun onTextLinkClick(
        textView: View?,
        clickedString: String
    ) {
        val ussdCode =
            clickedString.substring(0, clickedString.indexOf("#")) + Uri.encode("#")
        startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$ussdCode")))
    }
}
