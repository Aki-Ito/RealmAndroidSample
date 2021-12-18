package app.ito.akki.realmandroidsample

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import java.text.SimpleDateFormat
import java.util.*

class ToDoAdapter (
    private val context: Context,
    private var taskList: OrderedRealmCollection<ToDo>?,
    private val autoUpdate: Boolean
        ): RealmRecyclerViewAdapter<ToDo, ToDoAdapter.ViewHolder>(taskList, autoUpdate){

    private lateinit var listener: OnCellClickListener

       class ViewHolder(view: View): RecyclerView.ViewHolder(view){
           val subjectText: TextView = view.findViewById(R.id.subjectText)
           val contentText: TextView = view.findViewById(R.id.contentText)
           val dateText: TextView = view.findViewById(R.id.dateText)
           var container: LinearLayout = view.findViewById(R.id.container)
//           val timeText: TextView = view.findViewById(R.id.timeText)
       }

    //セルの数を決める
    override fun getItemCount(): Int {
        //エルビス演算子
        //taskListに中身があればtaskListに格納されたオブジェクトの個数を返す
        //taskListに中身がなければ0を返す
        return taskList?.size ?: 0
    }

    //セルのレイアウトを引っ張ってくる。
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.list_todo_item, parent, false)
        return ViewHolder(v)
    }

    //セルの詳細を確定する
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //returnでクロージャや関数から抜け出すことができる。
        val toDo: ToDo = taskList?.get(position) ?: return
        holder.subjectText.text = toDo.subject
        holder.contentText.text = toDo.content

//        val dateToString: String = "${toDo.month }月${toDo.day}日"
//        val timeToString: String = "${toDo.hour}時${toDo.minute}分"
//        holder.dateText.text = dateToString
//        holder.timeText.text = timeToString
        holder.dateText.text =  SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.JAPANESE).format(toDo.dateTime)
        holder.container.setOnClickListener {
            listener.onItemClick(toDo)
        }
    }

    interface OnCellClickListener{
        fun onItemClick(item: ToDo)
    }

    fun setOnItemCellClickListener(listener: OnCellClickListener){
        this.listener = listener
    }


}