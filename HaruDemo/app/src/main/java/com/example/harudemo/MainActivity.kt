package com.example.harudemo


import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.harudemo.databinding.ActivityMainBinding
import com.example.harudemo.fragments.*
import com.example.harudemo.service.Constant
import com.example.harudemo.service.MyReceiver
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var snsFragment: SnsFragment? = null
    private var calendarFragment: CalendarFragment? = null
    private var todoFragment: TodoFragment? = null
    private var statisticsFragment: StatisticsFragment? = null
    private var etcFragment: EtcFragment? = null
    private var alarmManager: AlarmManager? = null
    private var pendingIntent: PendingIntent? = null

    //todoinputactivty에서 메인엑티비티의 함수를 다루기 위한
    //instance 생성
    init{
        instance = this
    }

    companion object{
        private var instance:MainActivity? = null
        fun getInstance(): MainActivity? {
            return instance
        }
    }

    fun addAlarm(calendar: Calendar){
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, MyReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager!!.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    fun cancelAlarm(){
        if(alarmManager != null && pendingIntent != null) alarmManager!!.cancel(pendingIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        bottom_nav.setOnNavigationItemSelectedListener(onBottomNavItemSelectedListener)
        bottom_nav.menu.getItem(2).isChecked = true
        // TodoFragment를 가장 먼저 실행함
        snsFragment = SnsFragment.newInstance()
        todoFragment = TodoFragment.instance
        supportFragmentManager.beginTransaction().add(R.id.fragments_frame, todoFragment!!).commit()
    }



    //바텀 네비게이션 아이템 클릭 리스너
    private val onBottomNavItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
            /*
            데이터를 유지하기 위해
            snsFragment를 관리하는 FrameLayout이 따로 있고 해당 레이아웃만 보여지는 상태를 토글로 관리하여
            데이터를 유지시키고 다른 것들은 기존 방법을 유지하여 화면 전환을 한다.
            */
            when (it.itemId) {
                R.id.menu_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_sns, snsFragment!!).commit()
                    binding?.fragmentSns?.visibility = View.VISIBLE
                    binding?.fragmentsFrame?.visibility = View.GONE
                }
                R.id.menu_calendar -> {
                    calendarFragment = CalendarFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragments_frame, calendarFragment!!).commit()
                    binding?.fragmentSns?.visibility = View.GONE
                    binding?.fragmentsFrame?.visibility = View.VISIBLE
                }
                R.id.menu_todo -> {
                    todoFragment = TodoFragment.instance
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragments_frame, todoFragment!!).commit()
                    binding?.fragmentSns?.visibility = View.GONE
                    binding?.fragmentsFrame?.visibility = View.VISIBLE
                }
                R.id.menu_statistic -> {
                    statisticsFragment = StatisticsFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragments_frame, statisticsFragment!!).commit()
                    binding?.fragmentSns?.visibility = View.GONE
                    binding?.fragmentsFrame?.visibility = View.VISIBLE
                }
                R.id.menu_etc -> {
                    etcFragment = EtcFragment.newInstance()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragments_frame, etcFragment!!).commit()
                    binding?.fragmentSns?.visibility = View.GONE
                    binding?.fragmentsFrame?.visibility = View.VISIBLE
                }
            }

            true
        }
}