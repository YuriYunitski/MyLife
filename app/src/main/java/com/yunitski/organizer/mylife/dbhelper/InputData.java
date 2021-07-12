package com.yunitski.organizer.mylife.dbhelper;

import android.provider.BaseColumns;

public class InputData {

    public static final String DB_NAME = "myLifeDb.db";
    public static final int DB_VERSION = 1;

    public static class TaskEntry implements BaseColumns {

        public static final String MORNING_TABLE = "morningTable";
        public static final String DAY_TABLE = "dayTable";
        public static final String EVENING_TABLE = "eveningTable";

        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_MORNING_TASK = "morningTask";
        public static final String COLUMN_MORNING_TASK_TIME = "morningTaskTime";
        public static final String COLUMN_MORNING_TASK_STATUS = "morningTaskStatus";
        public static final String COLUMN_MORNING_TASK_DATE = "morningTaskDate";
        public static final String COLUMN_DAY_TASK = "dayTask";
        public static final String COLUMN_DAY_TASK_TIME = "dayTaskTime";
        public static final String COLUMN_DAY_TASK_STATUS = "dayTaskStatus";
        public static final String COLUMN_DAY_TASK_DATE = "dayTaskDate";
        public static final String COLUMN_EVENING_TASK = "eveningTask";
        public static final String COLUMN_EVENING_TASK_TIME = "eveningTaskTime";
        public static final String COLUMN_EVENING_TASK_STATUS = "eveningTaskStatus";
        public static final String COLUMN_EVENING_TASK_DATE = "eveningTaskDate";
    }
}
