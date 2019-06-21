package com.cougar.maksim.fastnotes.DataClasses

import android.arch.persistence.room.TypeConverter
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class Converters {

    @TypeConverter
    fun uuidToString(id:UUID):String{
        return id.toString()
    }

    @TypeConverter
    fun stringToUuid(string: String):UUID{
        return UUID.fromString(string)
    }

    @TypeConverter
    fun dateToLong(date:Date):Long{
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.US)
        var tempDate = date
        try {
            tempDate = simpleDateFormat.parse(simpleDateFormat.format(date))
        } catch (e: Exception) {
            //use wrong date
            //TODO rework with JodaTime or something else
        }
        return tempDate.time
    }

    @TypeConverter
    fun longToDate(long:Long):Date{
        return Date(long)
    }

    @TypeConverter
    fun statusToString(status:NoteStatus):String{
        return status.toString()
    }

    @TypeConverter
    fun stringToStatus(string:String?):NoteStatus{
        return try {
            NoteStatus.valueOf(string ?: NoteStatus.NEVER.toString())
        } catch (e: Exception) {
            NoteStatus.NEVER
        }
    }
}