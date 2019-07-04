package com.cougar.maksim.fastnotes.mvpMoxyViews

import com.arellomobile.mvp.MvpView

interface CombinedView : MvpView{
    fun setStartFragment()
    fun setSecondFragment(data: Any?)
}