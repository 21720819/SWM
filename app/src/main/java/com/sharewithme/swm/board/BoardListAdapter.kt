package com.sharewithme.swm.board

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.sharewithme.swm.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class BoardListAdapter(private val boardList : MutableList<BoardModel>) : BaseAdapter() {

    override fun getCount(): Int {
        return boardList.size
    }
    override fun getItem(position: Int): Any {
        return boardList[position]
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(parent?.context).inflate(R.layout.board_list_item, parent, false)
        }
        val itemLinearLayoutView = view?.findViewById<LinearLayout>(R.id.itemView)
        val title = view?.findViewById<TextView>(R.id.tv_postTitle)
        val price = view?.findViewById<TextView>(R.id.tv_postPrice)
        val place = view?.findViewById<TextView>(R.id.tv_postPlace)
        val dateTime = view?.findViewById<TextView>(R.id.tv_postDateTime)
        val nickName = view?.findViewById<TextView>(R.id.tv_nickname)
        val postedTime = view?.findViewById<TextView>(R.id.tv_postedTime)
        val totalNum = view?.findViewById<TextView>(R.id.tv_totalNum)
        val wirttenTime= boardList[position].time

        val schoolName = view?.findViewById<TextView>(R.id.tv_schoolName)

        title!!.text = boardList[position].title
        price!!.text = "${boardList[position].price}원"
        place!!.text = boardList[position].place
        dateTime!!.text = boardList[position].datetime
        totalNum!!.text = "전체 ${boardList[position].totalNum}명"
        nickName!!.text = boardList[position].nickname
        postedTime!!.text = changeTime(wirttenTime)
        schoolName!!.text = "(${boardList[position].schoolName})"

        return view!!
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun changeTime(writtenTime : String): String? {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")
    val convertedTime: LocalDateTime = LocalDateTime.parse(writtenTime, formatter)
    val currentTime = LocalDateTime.now().withNano(0)
    val differ = ChronoUnit.SECONDS.between(convertedTime,currentTime) //날짜 차이 계산

    val view: String?

    val SEC = 60
    val MIN = 60
    val HOUR = 24

    if (differ <  SEC ) {//1분전 (60초) 보다 작은 경우
        view = "방금 전"
    }

    else if (differ >= SEC && differ < SEC * MIN){//초가 1분이상 1시간 전일 경우
        val minute = differ / SEC
        view = minute.toString() + "분 전"

    }

    else if (differ >= SEC * MIN && differ < SEC * MIN * HOUR ){ //하루가 지나기 전인 경우
        val hour = convertedTime.hour
        val minute = convertedTime.minute
        val hourFormat: String?
        val minuteFormat: String?

        if (hour < 10) {//10시간 전은 0을 붙인다.
            hourFormat = "0$hour"
        }
        else {
            hourFormat = hour.toString()
        }

        if (minute < 10) {//10분 전까지는 0을 붙인다.
            minuteFormat = "0$minute"
        }
        else{
            minuteFormat = minute.toString()
        }

        view = "$hourFormat:$minuteFormat"
    }

    else{//하루 후
        val year = convertedTime.year.toString()
        val month = convertedTime.monthValue
        val monthFormat: String?
        val datehFormat: String?
        if (month < 10) {//1월에서 9월은 앞에 0을 붙여 출력한다.
            monthFormat = "0$month"
        }
        else {
            monthFormat = month.toString()
        }
        val date = convertedTime.dayOfMonth
        if (date < 10) {//9일까지는 앞에 0을 붙인다
            datehFormat = "0$date"
        }
        else{
            datehFormat = date.toString()
        }
        view = "$year-$monthFormat-$datehFormat"
    }
    return view
}