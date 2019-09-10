package com.cougar.maksim.fastnotes.mvpMoxyViews

import android.content.Intent
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface CombinedView : MvpView{
    fun setStartFragment(todayEvents : Boolean, intent: Intent?)
    fun setSecondFragment(data: Any?)

    @StateStrategyType(SkipStrategy::class)
    fun updateMenu(todayEvents :Boolean)
}