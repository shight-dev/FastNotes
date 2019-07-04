package com.cougar.maksim.fastnotes.mvpMoxyViews

import com.arellomobile.mvp.MvpView

interface NoteListView : MvpView{
    fun setMenuItemDelete()
    fun setMenuItemSearch()
    fun updateAdapterElement(position: Int)
    fun updateAdapterDataset()
    fun updateUI()
}