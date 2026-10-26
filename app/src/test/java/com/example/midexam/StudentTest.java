package com.example.midexam;

import org.junit.Test;
import static org.junit.Assert.*;

public class StudentTest {

    @Test
    public void testStudentGettersAndSetters() {
        Student student = new Student("123456", "John Doe", true);
        assertEquals("123456", student.getStudentId());
        assertEquals("John Doe", student.getName());
        assertTrue(student.isEnrolled());

        student.setStudentId("654321");
        student.setName("Jane Doe");
        student.setEnrolled(false);

        assertEquals("654321", student.getStudentId());
        assertEquals("Jane Doe", student.getName());
        assertFalse(student.isEnrolled());
    }
}
