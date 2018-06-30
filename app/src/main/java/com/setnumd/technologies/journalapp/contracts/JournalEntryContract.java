//package com.setnumd.technologies.journalapp.contracts;
//
//import android.content.ContentResolver;
//import android.net.Uri;
//import android.provider.BaseColumns;
//
//public class JournalEntryContract {
//      // To prevent someone from accidentally instantiating the contract class,
//        // make the constructor private.
//        private JournalEntryContract() {}
//
//
//        /* Inner class that defines the table contents */
//        public static class JournalEntry implements BaseColumns {
//
//            public static final String CONTENT_TYPE =
//                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.journalapp.entries";
//
//            public static final String CONTENT_ITEM_TYPE =
//                    ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.journalapp.entry";
//
//            public static final Uri CONTENT_URI =
//                    BASE_CONTENT_URI.buildUpon().appendPath(PATH_ENTRIES).build();
//
//
//            public static final String TABLE_NAME = "journal";
//            public static final String COLUMN_NAME_USER = "user";
//            public static final String COLUMN_NAME_TITLE = "title";
//            public static final String COLUMN_NAME_CONTENT = "content";
//        }
//    public static final String CONTENT_AUTHORITY = "com.setnumd.technologies.journalapp";
//
//    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
//
//    private static final String PATH_ENTRIES = "entries";
//    }
//
