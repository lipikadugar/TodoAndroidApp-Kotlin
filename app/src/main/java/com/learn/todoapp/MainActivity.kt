package com.learn.todoapp

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.learn.todoapp.db.TaskContract
import com.learn.todoapp.db.TaskDbHelper

class MainActivity : AppCompatActivity() {

    private val mHelper: TaskDbHelper? = null
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
                        val values = ContentValues()
                        values.put(TaskContract.TaskEntry.COL_TASK_TITLE, task)
                        db?.insertWithOnConflict(TaskContract.TaskEntry.TABLE,
                            null,
                            values,
                            SQLiteDatabase.CONFLICT_REPLACE)
                        db?.close()
                    }
                    .setNegativeButton("Cancel", null)
                    .create()
                dialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

