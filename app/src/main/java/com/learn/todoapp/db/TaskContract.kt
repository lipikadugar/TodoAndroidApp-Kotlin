package com.learn.todoapp.db

import android.provider.BaseColumns

class TaskContract {
    class TaskEntry:BaseColumns {
        companion object {
            val _ID: Any? = null
            val TABLE = "tasks"
            val COL_TASK_TITLE = "title"
        }
    }
    companion object {
        val DB_NAME = "com.learn.todoapp.db"
        val DB_VERSION = 1
    }
}