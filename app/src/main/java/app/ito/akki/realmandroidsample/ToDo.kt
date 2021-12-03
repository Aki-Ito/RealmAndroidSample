package app.ito.akki.realmandroidsample

import android.os.Build
import androidx.annotation.RequiresApi
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.time.LocalDateTime
import java.util.*

open class ToDo (
    @PrimaryKey open var id: String = UUID.randomUUID().toString(),
    open var subject: String = "",
    open var content: String = "",
//    open var year: Int = 0,
//    open var month: Int = 0,
//    open var day: Int = 0,
//    open var hour: Int = 0,
//    open var minute: Int = 0,
    open var dateTime: Date = Date(System.currentTimeMillis()),
    open var finishedContent: Boolean = false
) : RealmObject()