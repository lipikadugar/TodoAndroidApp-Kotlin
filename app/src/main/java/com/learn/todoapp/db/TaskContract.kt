package com.learn.todoapp.db

import android.provider.BaseColumns

class TaskContract {
    class TaskEntry:BaseColumns {
        companion object {
            const val ID = "ID"
            const val TABLE = "tasks"
            const val COL_TASK_TITLE = "title"
        }
    }
    companion object {
        const val DB_NAME = "com.learn.todoapp.db"
        const val DB_VERSION = 1
    }
}