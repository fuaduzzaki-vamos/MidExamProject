package com.example.midexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentManagement.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_STUDENTS = "students";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_IS_ENROLLED = "is_enrolled";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_STUDENTS + " (" +
                    COLUMN_STUDENT_ID + " TEXT PRIMARY KEY, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_IS_ENROLLED + " INTEGER NOT NULL);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }

    public boolean isStudentIdExists(String studentId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_STUDENTS,
                new String[]{COLUMN_STUDENT_ID},
                COLUMN_STUDENT_ID + "=?",
                new String[]{studentId},
                null, null, null
        );
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean addStudent(Student student) {
        if (isStudentIdExists(student.getStudentId())) {
            return false;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_ID, student.getStudentId());
        values.put(COLUMN_NAME, student.getName());
        values.put(COLUMN_IS_ENROLLED, student.isEnrolled() ? 1 : 0);

        long result = db.insert(TABLE_STUDENTS, null, values);
        return result != -1;
    }

    public boolean updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, student.getName());
        values.put(COLUMN_IS_ENROLLED, student.isEnrolled() ? 1 : 0);

        int rows = db.update(
                TABLE_STUDENTS,
                values,
                COLUMN_STUDENT_ID + "=?",
                new String[]{student.getStudentId()}
        );
        return rows > 0;
    }

    public boolean deleteStudent(String studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(
                TABLE_STUDENTS,
                COLUMN_STUDENT_ID + "=?",
                new String[]{studentId}
        );
        return rows > 0;
    }

    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_STUDENTS + " ORDER BY " + COLUMN_STUDENT_ID + " ASC", null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(COLUMN_STUDENT_ID);
                int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
                int enrolledIndex = cursor.getColumnIndex(COLUMN_IS_ENROLLED);

                String id = (idIndex != -1) ? cursor.getString(idIndex) : "";
                String name = (nameIndex != -1) ? cursor.getString(nameIndex) : "";
                boolean enrolled = (enrolledIndex != -1) && (cursor.getInt(enrolledIndex) == 1);

                studentList.add(new Student(id, name, enrolled));
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return studentList;
    }
}
