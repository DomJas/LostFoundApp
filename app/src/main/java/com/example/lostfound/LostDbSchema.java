package com.example.lostfound;

public class LostDbSchema {
    public  static final class LostTable {
        public static final String NAME = "losts";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String FOUND = "found";
            public static final String SUSPECT ="suspect";
            //Defines my database and the moving pieces of the table definition
        }
    }
}
