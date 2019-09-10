package com.cougar.maksim.fastnotes.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.cougar.maksim.fastnotes.mvpMoxyViews.CombinedView
import java.util.*

@InjectViewState
class CombinedPresenter : MvpPresenter<CombinedView>() {

    var mTodayEvents: Boolean = false

    fun onNoteFragmentInteraction() {
        viewState.setStartFragment(mTodayEvents, null)
    }

    fun onNoteListFragmentInteraction(id: UUID?) {
        viewState.setSecondFragment(id)
    }

    fun todayEventClick() {
        mTodayEvents = !mTodayEvents
        viewState.updateMenu(mTodayEvents)
    }

    fun onRestoreInstance(){
        viewState.updateMenu(mTodayEvents)
    }
}