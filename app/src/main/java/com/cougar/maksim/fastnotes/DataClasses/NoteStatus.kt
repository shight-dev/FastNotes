package com.cougar.maksim.fastnotes.DataClasses

import android.content.res.Resources
import com.cougar.maksim.fastnotes.R

enum class NoteStatus(val stringVal:String) {
    //TODO problem with resources
    ALWAYS(/*Resources.getSystem().getString(R.string.always)*/"Always"),
    NEVER(/*Resources.getSystem().getString(R.string.never)*/"Never"),
    ALL_BEFORE_DATE(/*Resources.getSystem().getString(R.string.all_before_day)*/"All before date"),
    AT_DAY(/*Resources.getSystem().getString(R.string.at_day)*/ "At day")
}