package com.example.midexam;

public class Student {
    private String studentId;
    private String name;
    private boolean isEnrolled;

    public Student() {
    }

    public Student(String studentId, String name, boolean isEnrolled) {
        this.studentId = studentId;
        this.name = name;
        this.isEnrolled = isEnrolled;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnrolled() {
        return isEnrolled;
    }

    public void setEnrolled(boolean enrolled) {
        isEnrolled = enrolled;
    }
}
