package app.ito.akki.realmandroidsample

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import io.realm.Realm
import io.realm.kotlin.createObject
import kotlinx.android.synthetic.main.activity_add_to_do.*
import java.lang.IllegalArgumentException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.math.min

class AddToDoActivity : AppCompatActivity(), DatePickerDialogClass.OnSelectedDateListener, TimePickerDialogClass.OnSelectedTimeListener  {

    var yearSaved: Int? = null
    var monthSaved: Int? = null
    var dateSaved: Int? = null
    var hourSaved: Int? = null
    var minuteSaved: Int? = null
    val realm: Realm = Realm.getDefaultInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_to_do)

        val itemId = intent.getStringExtra("itemID")

        if (itemId != null){
            val toDoItem: ToDo? = realm.where(ToDo::class.java)
                .equalTo("id", itemId)
                .findFirst()

            subjectEditText.setText(toDoItem?.subject)
            ContentEditText.setText(toDoItem?.content)
        }



        datePickButton.setOnClickListener {
            showDatePickerDialog()
        }

        timePickButton.setOnClickListener {
            showTimePickerDialog()
        }

        saveButton.setOnClickListener {
            if (yearSaved != null && monthSaved != null && dateSaved != null && hourSaved != null && minuteSaved != null){
                realm.executeTransaction {

                    val itemId = intent.getStringExtra("itemID")



                    if (itemId != null){

                        val toDoItem: ToDo? = realm.where(ToDo::class.java)
                            .equalTo("id", itemId)
                            .findFirst()

                        toDoItem?.subject = subjectEditText.text.toString()
                        toDoItem?.content = ContentEditText.text.toString()

                        val intToDateTime: LocalDateTime = LocalDateTime.of(
                            yearSaved!!,
                            monthSaved!!,
                            dateSaved!!,
                            hourSaved!!,
                            minuteSaved!!
                        )
                        val zdt = intToDateTime.atZone(ZoneId.systemDefault());
                        val date = Date.from(zdt.toInstant())
                        toDoItem?.dateTime = date

                    } else {
                        val newToDo: ToDo =
                            it.createObject(ToDo::class.java, UUID.randomUUID().toString())
                        newToDo.subject = subjectEditText.text.toString()
                        newToDo.content = ContentEditText.text.toString()
//
                        val intToDateTime: LocalDateTime = LocalDateTime.of(
                            yearSaved!!,
                            monthSaved!!,
                            dateSaved!!,
                            hourSaved!!,
                            minuteSaved!!
                        )
                        val zdt = intToDateTime.atZone(ZoneId.systemDefault());
                        val date = Date.from(zdt.toInstant())
                        newToDo.dateTime = date
                    }

                }
            }
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        realm.close()
    }

    private fun showDatePickerDialog(){
        val datePickerDialogClass = DatePickerDialogClass()
        datePickerDialogClass.show(supportFragmentManager, null)
    }

    private fun showTimePickerDialog(){
        val timePickerDialogClass = TimePickerDialogClass()
        timePickerDialogClass.show(supportFragmentManager, null)
    }

    override fun selectedDate(year: Int, month: Int, date: Int) {
        yearSaved = year
        monthSaved = month
        dateSaved = date
    }

    override fun selectedTime(hourOfDay: Int, minute: Int) {
        hourSaved = hourOfDay
        minuteSaved = minute
    }
}