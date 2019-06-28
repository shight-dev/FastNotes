package com.cougar.maksim.fastnotes.activities

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.cougar.maksim.fastnotes.fragments.NoteFragment
import com.cougar.maksim.fastnotes.fragments.NoteListFragment
import com.cougar.maksim.fastnotes.R
import java.util.*

class NoteCombinedActivity : DoubleSwapFragmentActivity(),
        NoteListFragment.OnNoteListFragmentInteractionListener,
        NoteFragment.OnNoteFragmentInteractionListener {

    //TODO поворот экрана при открытой заметке возвращает начальное состояние активити

    //событие слушателя
    override fun onNoteFragmentInteraction() {
        setStartFragment()
        if (landscape) {
            removeFragmentFromContainer(NoteFragment::class.java)
        }
    }

    //событие слушателя
    override fun onNoteListFragmentInteraction(id: UUID?) {
        setSecondFragment(id)
    }

    //создает стартовый фрагмент
    override fun createStartFragment(): Fragment {
        return if (!intent.getBooleanExtra(TODAY_EVENTS, false)) {
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
    override fun setStartFragment() {
        if (!landscape) {
            setStartFragmentToContainer(R.id.single_fragment_container)
        } else {
            setStartFragmentToContainer(R.id.main_fragment_container)
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
    private fun setStartFragmentToContainer(containerId: Int) {
        val fm = supportFragmentManager
        fm.beginTransaction()
                .replace(containerId, createStartFragment())
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


}