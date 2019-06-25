package com.cougar.maksim.fastnotes.activities

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.cougar.maksim.fastnotes.R

abstract class DoubleSwapFragmentActivity : AppCompatActivity() {

    var landscape: Boolean = false

    //создает начальный фрагмент
    protected abstract fun createStartFragment(): Fragment

    //создает второй фрагмент
    protected abstract fun createSecondFragment(data: Any?): Fragment

    //устанавливает начальный фрагмент в контейнер
    protected abstract fun setStartFragment()

    //устанавливает второй фрагмент в контейнер
    protected abstract fun setSecondFragment(data: Any?)

    //создает начальный фрагмент при запуске
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        landscape = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        setContentView(R.layout.swap_activity_fragment)

        setStartFragment()
    }
}