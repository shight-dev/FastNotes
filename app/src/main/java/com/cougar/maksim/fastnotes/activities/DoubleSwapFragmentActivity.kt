package com.cougar.maksim.fastnotes.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import com.arellomobile.mvp.MvpAppCompatActivity
import com.cougar.maksim.fastnotes.R

abstract class DoubleSwapFragmentActivity : MvpAppCompatActivity() {

    var landscape: Boolean = false

    //создает начальный фрагмент
    protected abstract fun createStartFragment(): Fragment

    //создает второй фрагмент
    protected abstract fun createSecondFragment(data: Any?): Fragment

    //устанавливает начальный фрагмент в контейнер
    protected abstract fun setStartFragment(intent: Intent? = null)

    //устанавливает второй фрагмент в контейнер
    protected abstract fun setSecondFragment(data: Any?)

    //устанавливает значение флага в презентер активити
    protected abstract fun setTodayEvent(intent: Intent?)

    //создает начальный фрагмент при запуске
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        landscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        setContentView(R.layout.swap_activity_fragment)
        setTodayEvent(intent)
        setStartFragment(intent)
    }
}