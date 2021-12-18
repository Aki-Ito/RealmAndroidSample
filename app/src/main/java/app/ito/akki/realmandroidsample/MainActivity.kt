package app.ito.akki.realmandroidsample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    val realm: Realm = Realm.getDefaultInstance()
    val toDoList = readAll()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = ToDoAdapter(this, toDoList, true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fab.setOnClickListener {
            val intent = Intent(this, AddToDoActivity::class.java)
            startActivity(intent)
        }

        adapter.setOnItemCellClickListener(
            object: ToDoAdapter.OnCellClickListener
        {
            override fun onItemClick(item: ToDo){
                val toAddActivity = Intent(this@MainActivity, AddToDoActivity::class.java)
                toAddActivity.putExtra("itemID", item.id)
                startActivity(toAddActivity)
            }
        })
    }

   fun readAll(): RealmResults<ToDo>{
       //findAllについて
       //クエリ条件を満たすすべてのオブジェクトを検索する
       //Returns a sequence of all occurrences of a regular expression within the input string, beginning at the specified startIndex.
        return realm.where(ToDo::class.java).findAll()
   }
}