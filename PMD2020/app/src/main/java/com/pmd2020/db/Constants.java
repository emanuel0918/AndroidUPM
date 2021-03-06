package com.pmd2020.db;

public class Constants {
    public static final String DB_CREATE_TABLE_ARTICLE = "CREATE TABLE article (\n" +
            "\t_id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\ttitle_a TEXT,\n" +
            "\tsubtitle_a TEXT,\n" +
            "\tcategory_a TEXT,\n" +
            "\tabstract_a TEXT,\n" +
            "\tbody_a TEXT,\n" +
            "\timage_a TEXT,\n" +
            "\tdesc_a TEXT\n" +
            "\t\n" +
            ");";
    public static final String DB_TABLE_NAME="article";
    public static final String DB_TABLE_FIELD_TITLE="title_a";
    public static final String DB_TABLE_FIELD_SUBTITLE="subtitle_a";
    public static final String DB_TABLE_FIELD_CATEGORY="category_a";
    public static final String DB_TABLE_FIELD_ABSTRACT="abstract_a";
    public static final String DB_TABLE_FIELD_BODY="body_a";
    public static final String DB_TABLE_FIELD_IMAGE="image_a";
    public static final String DB_TABLE_FIELD_DESCRIPTION="desc_a";
}
