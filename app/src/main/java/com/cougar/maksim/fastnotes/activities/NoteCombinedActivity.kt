package com.cougar.maksim.fastnotes.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.cougar.maksim.fastnotes.fragments.NoteFragment
import com.cougar.maksim.fastnotes.fragments.NoteListFragment
import com.cougar.maksim.fastnotes.R
import com.cougar.maksim.fastnotes.mvpMoxyViews.CombinedView
import com.cougar.maksim.fastnotes.presenters.CombinedPresenter
import java.util.*

class NoteCombinedActivity : DoubleSwapFragmentActivity(),
        CombinedView,
        NoteListFragment.OnNoteListFragmentInteractionListener,
        NoteFragment.OnNoteFragmentInteractionListener {

    //TODO поворот экрана при открытой заметке возвращает начальное состояние активити
    //TODO не пересоздавать фрагменты

    @InjectPresenter
    lateinit var combinedPresenter: CombinedPresenter

    private var mMenu: Menu? = null

    //событие слушателя
    override fun onNoteFragmentInteraction() {
        combinedPresenter.onNoteFragmentInteraction()
        /*setStartFragment()
        if (landscape) {
            removeFragmentFromContainer(NoteFragment::class.java)
        }*/
    }

    //событие слушателя
    override fun onNoteListFragmentInteraction(id: UUID?) {
        combinedPresenter.onNoteListFragmentInteraction(id)
        //setSecondFragment(id)
    }

    //создает стартовый фрагмент
    override fun createStartFragment(todayEvents: Boolean): Fragment {
        return if (!(intent.getBooleanExtra(TODAY_EVENTS, false) || todayEvents)) {
            NoteListFragment.newInstance(false)
        } else {
            NoteListFragment.newInstance(true)
        }
    }

    //создает второй фрагмент
    override fun createSecondFragment(data: Any?): Fragment {
        var id: UUID? = null
        data?.let {
            if (data is UUID) {
                id = data
            }
        }
        //TODO rework with kotlin not null features
        return if (id != null) {
            NoteFragment.newInstance(id)
        } else {
            NoteFragment.newInstance(true)
        }
    }

    //устанавливает стартовый фрагмент в контейнер в зависимости от ориентации
    override fun setStartFragment(todayEvents: Boolean) {
        if (!landscape) {
            setStartFragmentToContainer(R.id.single_fragment_container, todayEvents)
        } else {
            setStartFragmentToContainer(R.id.main_fragment_container, todayEvents)
            removeFragmentFromContainer(NoteFragment::class.java)
        }
    }

    //устанавливает второй фрагмент в контейнер в зависимости от ориентации
    override fun setSecondFragment(data: Any?) {
        if (!landscape) {
            setSecondFragmentToContainer(data, R.id.single_fragment_container)
        } else {
            setSecondFragmentToContainer(data, R.id.second_fragment_container)
        }
    }

    //устанавливает стартовый фрагмент в определенный контейнер
    private fun setStartFragmentToContainer(containerId: Int, todayEvents: Boolean = false) {
        val fm = supportFragmentManager
        fm.beginTransaction()
                .replace(containerId, createStartFragment(todayEvents))
                .commit()
    }

    //устанавливает второй фрагмент в определенный контейнер
    private fun setSecondFragmentToContainer(data: Any?, containerId: Int) {
        val fm = supportFragmentManager
        fm.beginTransaction()
                .replace(containerId, createSecondFragment(data))
                .commit()
    }

    //удаляет все фрагменты соответствующего класса с экрана
    private fun removeFragmentFromContainer(fragmentClass: Class<out Fragment>) {
        val fm = supportFragmentManager
        val list = fm.fragments.filterIsInstance(fragmentClass)
        list.forEach {
            fm.beginTransaction()
                    .remove(it)
                    .commit()
        }
    }

    companion object {
        private const val TODAY_EVENTS = "today_events"

        //интент для использования при нажатии на уведомление
        fun newIntent(packageContext: Context, todayEvents: Boolean): Intent {
            val intent = Intent(packageContext, NoteCombinedActivity::class.java)
            if (todayEvents) {
                intent.putExtra(TODAY_EVENTS, true)
            }
            return intent
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.note_list_menu, menu)
        mMenu = menu
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        super.onPrepareOptionsMenu(menu)
        combinedPresenter.onRestoreInstance()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_item_new_note -> {
                onNoteListFragmentInteraction(null)
            }
            R.id.menu_item_today_events -> {
                combinedPresenter.todayEventClick()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //TODO стратегия не работает
    @StateStrategyType(SkipStrategy::class)
    override fun updateMenu(todayEvents: Boolean) {
        //TODO вероятно проблема с очередью команд в viewState
        if (todayEvents) {
            mMenu?.findItem(R.id.menu_item_today_events)?.setIcon(android.R.drawable.ic_delete)
        } else {
            mMenu?.findItem(R.id.menu_item_today_events)?.setIcon(android.R.drawable.ic_menu_search)
        }
        setStartFragment(todayEvents)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        //combinedPresenter.onRestoreInstance()
    }
}