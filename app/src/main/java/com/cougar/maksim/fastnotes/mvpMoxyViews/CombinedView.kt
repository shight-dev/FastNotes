package com.cougar.maksim.fastnotes.mvpMoxyViews

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface CombinedView : MvpView{
    fun setStartFragment(todayEvents : Boolean)
    fun setSecondFragment(data: Any?)

    @StateStrategyType(SkipStrategy::class)
    fun updateMenu(todayEvents :Boolean)
}