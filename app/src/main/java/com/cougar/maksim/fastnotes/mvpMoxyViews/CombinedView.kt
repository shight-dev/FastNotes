package com.cougar.maksim.fastnotes.mvpMoxyViews

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(SkipStrategy::class)
interface CombinedView : MvpView{
    fun setStartFragment(todayEvents : Boolean)
    fun setSecondFragment(data: Any?)
    fun updateMenu(todayEvents :Boolean)
}