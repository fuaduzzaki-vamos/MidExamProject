package com.example.midexam;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {

    private final Context context;
    private final List<Student> students;

    public StudentAdapter(@NonNull Context context, @NonNull List<Student> students) {
        super(context, 0, students);
        this.context = context;
        this.students = students;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        }

        Student currentStudent = students.get(position);

        TextView tvStudentId = listItem.findViewById(R.id.tvStudentId);
        TextView tvStudentName = listItem.findViewById(R.id.tvStudentName);
        TextView tvEnrolledStatus = listItem.findViewById(R.id.tvEnrolledStatus);

        if (currentStudent != null) {
            tvStudentId.setText("ID: " + currentStudent.getStudentId());
            tvStudentName.setText(currentStudent.getName());

            if (currentStudent.isEnrolled()) {
                tvEnrolledStatus.setText("Enrolled");
                tvEnrolledStatus.setBackgroundResource(R.drawable.bg_status_enrolled);
                tvEnrolledStatus.setTextColor(Color.parseColor("#2E7D32"));
            } else {
                tvEnrolledStatus.setText("Not Enrolled");
                tvEnrolledStatus.setBackgroundResource(R.drawable.bg_status_not_enrolled);
                tvEnrolledStatus.setTextColor(Color.parseColor("#C62828"));
            }
        }

        return listItem;
    }
}
