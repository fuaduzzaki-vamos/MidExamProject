package com.example.midexam;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etStudentName;
    private EditText etStudentId;
    private CheckBox cbHasEnrolled;
    private Button btnAdd;
    private Button btnEdit;
    private Button btnDelete;
    private Button btnView;
    private ListView lvStudents;
    private TextView tvStudentCount;
    private TextView tvEmptyState;

    private DatabaseHelper dbHelper;
    private StudentAdapter studentAdapter;
    private List<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this);

        initViews();
        setupListeners();
        loadStudentData();
    }

    private void initViews() {
        etStudentName = findViewById(R.id.etStudentName);
        etStudentId = findViewById(R.id.etStudentId);
        cbHasEnrolled = findViewById(R.id.cbHasEnrolled);
        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);
        lvStudents = findViewById(R.id.lvStudents);
        tvStudentCount = findViewById(R.id.tvStudentCount);
        tvEmptyState = findViewById(R.id.tvEmptyState);
    }

    private void setupListeners() {
        btnAdd.setOnClickListener(v -> addStudent());
        btnEdit.setOnClickListener(v -> editStudent());
        btnDelete.setOnClickListener(v -> deleteStudent());
        btnView.setOnClickListener(v -> {
            loadStudentData();
            if (studentList.isEmpty()) {
                Toast.makeText(this, "No students found in database.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Displaying " + studentList.size() + " student(s).", Toast.LENGTH_SHORT).show();
            }
        });

        lvStudents.setOnItemClickListener((parent, view, position, id) -> {
            Student selectedStudent = studentList.get(position);
            if (selectedStudent != null) {
                etStudentId.setText(selectedStudent.getStudentId());
                etStudentName.setText(selectedStudent.getName());
                cbHasEnrolled.setChecked(selectedStudent.isEnrolled());
            }
        });
    }

    private void addStudent() {
        String name = etStudentName.getText() != null ? etStudentName.getText().toString().trim() : "";
        String studentId = etStudentId.getText() != null ? etStudentId.getText().toString().trim() : "";
        boolean isEnrolled = cbHasEnrolled.isChecked();

        if (TextUtils.isEmpty(studentId)) {
            Toast.makeText(this, "Please enter Student ID.", Toast.LENGTH_SHORT).show();
            etStudentId.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter Student Name.", Toast.LENGTH_SHORT).show();
            etStudentName.requestFocus();
            return;
        }

        if (dbHelper.isStudentIdExists(studentId)) {
            Toast.makeText(this, "Error: Student ID must be UNIQUE! Student ID already exists.", Toast.LENGTH_LONG).show();
            return;
        }

        Student student = new Student(studentId, name, isEnrolled);
        boolean success = dbHelper.addStudent(student);

        if (success) {
            Toast.makeText(this, "Student added successfully!", Toast.LENGTH_SHORT).show();
            clearInputs();
            loadStudentData();
        } else {
            Toast.makeText(this, "Failed to add student.", Toast.LENGTH_SHORT).show();
        }
    }

    private void editStudent() {
        String name = etStudentName.getText() != null ? etStudentName.getText().toString().trim() : "";
        String studentId = etStudentId.getText() != null ? etStudentId.getText().toString().trim() : "";
        boolean isEnrolled = cbHasEnrolled.isChecked();

        if (TextUtils.isEmpty(studentId)) {
            Toast.makeText(this, "Please enter Student ID to edit.", Toast.LENGTH_SHORT).show();
            etStudentId.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter Student Name.", Toast.LENGTH_SHORT).show();
            etStudentName.requestFocus();
            return;
        }

        if (!dbHelper.isStudentIdExists(studentId)) {
            Toast.makeText(this, "Error: Student ID not found in database.", Toast.LENGTH_SHORT).show();
            return;
        }

        Student student = new Student(studentId, name, isEnrolled);
        boolean success = dbHelper.updateStudent(student);

        if (success) {
            Toast.makeText(this, "Student updated successfully!", Toast.LENGTH_SHORT).show();
            clearInputs();
            loadStudentData();
        } else {
            Toast.makeText(this, "Failed to update student.", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteStudent() {
        String studentId = etStudentId.getText() != null ? etStudentId.getText().toString().trim() : "";

        if (TextUtils.isEmpty(studentId)) {
            Toast.makeText(this, "Please enter Student ID to delete.", Toast.LENGTH_SHORT).show();
            etStudentId.requestFocus();
            return;
        }

        if (!dbHelper.isStudentIdExists(studentId)) {
            Toast.makeText(this, "Error: Student ID not found in database.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = dbHelper.deleteStudent(studentId);

        if (success) {
            Toast.makeText(this, "Student deleted successfully!", Toast.LENGTH_SHORT).show();
            clearInputs();
            loadStudentData();
        } else {
            Toast.makeText(this, "Failed to delete student.", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadStudentData() {
        studentList = dbHelper.getAllStudents();
        studentAdapter = new StudentAdapter(this, studentList);
        lvStudents.setAdapter(studentAdapter);

        if (tvStudentCount != null) {
            tvStudentCount.setText(studentList.size() + " Total");
        }

        if (tvEmptyState != null) {
            if (studentList.isEmpty()) {
                tvEmptyState.setVisibility(View.VISIBLE);
                lvStudents.setVisibility(View.GONE);
            } else {
                tvEmptyState.setVisibility(View.GONE);
                lvStudents.setVisibility(View.VISIBLE);
            }
        }
    }

    private void clearInputs() {
        etStudentName.setText("");
        etStudentId.setText("");
        cbHasEnrolled.setChecked(false);
    }
}
