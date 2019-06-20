package com.cougar.maksim.fastnotes.DbWork.Room

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity
class NoteEntity(@PrimaryKey var id:String,
            var title:String,
            var data:String,
            var date:Int,
            var status:String)