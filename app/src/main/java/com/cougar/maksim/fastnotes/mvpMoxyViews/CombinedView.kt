package com.cougar.maksim.fastnotes.mvpMoxyViews

import com.arellomobile.mvp.MvpView

interface CombinedView : MvpView{
    fun setStartFragment(todayEvents : Boolean)
    fun setSecondFragment(data: Any?)
    fun updateMenu(todayEvents :Boolean)
}