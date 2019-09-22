package com.cougar.maksim.fastnotes.presenters

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.cougar.maksim.fastnotes.AppState
import com.cougar.maksim.fastnotes.mvpMoxyViews.CombinedView
import java.util.*

@InjectViewState
class CombinedPresenter : MvpPresenter<CombinedView>() {

    private var id:UUID? = null;

    fun onNoteFragmentInteraction() {
        viewState.setStartFragment(null)
    }

    fun onNoteListFragmentInteraction(id: UUID?) {
        viewState.setSecondFragment(id)
    }

    fun todayEventClick() {
        AppState.actualNotes = !AppState.actualNotes
        viewState.updateMenu(AppState.actualNotes)
    }

    fun onRestoreInstance(){
        viewState.updateMenu(AppState.actualNotes)
    }

    fun editFragmentOpen(id:UUID?){
        this.id = id
    }

    fun getId():UUID?{
        return id
    }
}