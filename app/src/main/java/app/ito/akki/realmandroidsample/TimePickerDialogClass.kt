package app.ito.akki.realmandroidsample

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerDialogClass: DialogFragment(), TimePickerDialog.OnTimeSetListener {

    interface OnSelectedTimeListener {
        fun selectedTime(hourOfDay: Int, minute: Int)
    }

    private lateinit var listener: OnSelectedTimeListener

    //Fragmentのライフサイクルにおける最初のメソッド
    //FragmentとActivity(コンテクスト)が関連付けられた時に呼ばれる。
    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnSelectedTimeListener) {
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = Date().time
        val context = context // smart cast
        return when {
            context != null -> {
                TimePickerDialog(
                    context,
                    this, // ここでは TimePickerDialog の リスナーを渡す
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true)
            }
            else -> super.onCreateDialog(savedInstanceState)
        }
    }


    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        listener.selectedTime(hourOfDay,minute)
    }

}