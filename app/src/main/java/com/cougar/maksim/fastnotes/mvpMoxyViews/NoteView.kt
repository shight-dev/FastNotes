package com.cougar.maksim.fastnotes.mvpMoxyViews

import com.arellomobile.mvp.MvpView

interface NoteView : MvpView {
    fun updateDateBtn(s: String)
    fun updateStatusBtn(s: String)
    fun updateTitle(s: String)
    fun updateData(s: String)
    fun updateNotify(b: Boolean)
}