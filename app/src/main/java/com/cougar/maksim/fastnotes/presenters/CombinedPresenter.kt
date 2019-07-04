package com.cougar.maksim.fastnotes.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.cougar.maksim.fastnotes.mvpMoxyViews.CombinedView
import java.util.*

@InjectViewState
class CombinedPresenter : MvpPresenter<CombinedView>() {
    fun onNoteFragmentInteraction(){
        viewState.setStartFragment()
    }
    fun onNoteListFragmentInteraction(id: UUID?){
        viewState.setSecondFragment(id)
    }
}