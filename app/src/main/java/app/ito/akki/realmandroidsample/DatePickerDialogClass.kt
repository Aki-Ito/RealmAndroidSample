package app.ito.akki.realmandroidsample

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerDialogClass: DialogFragment(), DatePickerDialog.OnDateSetListener {
    //ActivityやFragmentに選択結果を渡すためのリスナー
    interface OnSelectedDateListener {
        fun selectedDate(year: Int,month: Int,date: Int)
    }

    private lateinit var listener: OnSelectedDateListener

    //Fragmentのライフサイクルにおける最初のメソッド
    //FragmentとActivity(コンテクスト)が関連付けられた時に呼ばれる。
    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnSelectedDateListener) {
            listener = context
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = Date().time
        val context = context
        return when{
            context != null -> {
                DatePickerDialog(
                    context,
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE)
                )
            }
            else -> super.onCreateDialog(savedInstanceState)
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        listener.selectedDate(year,month,dayOfMonth)
    }

    companion object {
        private val TAG = DatePickerDialogClass::class.java.simpleName
    }
}