package com.learn.todoapp

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.learn.todoapp.db.TaskContract
import com.learn.todoapp.db.TaskDbHelper


class MainActivity : AppCompatActivity() {

    private var mHelper = TaskDbHelper(this)
    private var mTaskListView: ListView? = null
    private var mAdapter: ArrayAdapter<String>? = null
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.mHelper = TaskDbHelper(this)
        this.mTaskListView = this.findViewById(R.id.list_todo)

        updateUI()

    }

    private fun updateUI() {
        val taskList = ArrayList<String>()
        val db = this.mHelper.readableDatabase
        val cursor = db.query(
            TaskContract.TaskEntry.TABLE,
            arrayOf(TaskContract.TaskEntry.COL_TASK_TITLE),
            null, null, null, null, null
        )
        while (cursor.moveToNext()) {
            val idx = cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_TITLE)
            taskList.add(cursor.getString(idx))
            Log.d(TAG, "TODO: ${cursor.getString(idx)}")
        }

        if (this.mAdapter == null) {
            mAdapter = ArrayAdapter(
                this,
                R.layout.item_todo,
                R.id.task_title,
                taskList
            )
            mTaskListView!!.adapter = mAdapter
        } else {
            mAdapter!!.clear()
            mAdapter!!.addAll(taskList)
            mAdapter!!.notifyDataSetChanged()
        }

        cursor.close()
        db.close()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_task -> {
                val taskEditText = EditText(this)
                val dialog = AlertDialog.Builder(this)
                    .setTitle("Add a new task")
                    .setMessage("What do you want to do next?")
                    .setView(taskEditText)
                    .setPositiveButton("Add") { _, _ ->
                        val task = taskEditText.text.toString()
                        Log.d(TAG, "Task to add: $task")
                        val db = mHelper?.writableDatabase
                        Log.d(TAG, "DB: $db")
                        val values = ContentValues()
                        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task)
                        db?.insertWithOnConflict(
                            TaskContract.TaskEntry.TABLE,
                            null,
                            values,
                            SQLiteDatabase.CONFLICT_REPLACE
                        )
                        db?.close()
                        updateUI()
                    }
                    .setNegativeButton("Cancel", null)
                    .create()
                dialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

