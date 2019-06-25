package com.cougar.maksim.fastnotes.Activities

import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.cougar.maksim.fastnotes.R

abstract class SingleSwapFragmentActivity : AppCompatActivity() {

    var landscape:Boolean = false

    //var mCurrentState: Int = -1

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
        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)


        setStartFragment()
        /*fragment = fragment ?: createStartFragment()

        fm.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()*/

        //mCurrentState = 0
    }
}