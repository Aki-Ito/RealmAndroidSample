package app.ito.akki.realmandroidsample

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    val realm: Realm = Realm.getDefaultInstance()
    var toDoList = readAll()


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

        val swipeToDismissTouchHelper = getSwipeToDismissTouchHelper(adapter)
        swipeToDismissTouchHelper.attachToRecyclerView(recyclerView)

    }


    private fun getSwipeToDismissTouchHelper(adapter: RecyclerView.Adapter<ToDoAdapter.ViewHolder>) =
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.ACTION_STATE_IDLE,
            ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                realm.executeTransaction {
                    val id = toDoList[viewHolder.adapterPosition]?.id
                    val task = realm.where(ToDo::class.java).equalTo("id", id).findFirst()
                    if (task != null) {
                        task.deleteFromRealm()
                    }
                }
                toDoList = realm.where(ToDo::class.java).findAll()
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )

                val itemView = viewHolder.itemView
                val background = ColorDrawable(Color.RED)
                val deleteIcon = AppCompatResources.getDrawable(
                    this@MainActivity,
                    R.drawable.ic_launcher_foreground
                )

                val iconMarginVertical = (viewHolder.itemView.height - deleteIcon!!.intrinsicHeight) / 2
                deleteIcon.setBounds(
                    itemView.left + iconMarginVertical,
                    itemView.top + iconMarginVertical,
                    itemView.left + iconMarginVertical + deleteIcon.intrinsicWidth,
                    itemView.bottom - iconMarginVertical
                )

                background.setBounds(
                    itemView.left,
                    itemView.top,
                    itemView.right + dX.toInt(),
                    itemView.bottom
                )
                background.draw(c)
                deleteIcon.draw(c)
            }
        })


   fun readAll(): RealmResults<ToDo> {
       //findAllについて
       //クエリ条件を満たすすべてのオブジェクトを検索する
       //Returns a sequence of all occurrences of a regular expression within the input string, beginning at the specified startIndex.
        return realm.where(ToDo::class.java).findAll()
   }
}