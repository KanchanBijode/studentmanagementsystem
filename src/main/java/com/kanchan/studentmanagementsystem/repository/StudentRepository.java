package com.kanchan.studentmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.kanchan.studentmanagementsystem.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    Student findByName(String name);

    Student findByEmail(String email);

    List<Student> findByNameContainingIgnoreCase(String name);

    List<Student> findByCourseIgnoreCase(String course);

    @Query("SELECT COUNT(DISTINCT s.course) FROM Student s")
    long countDistinctCourses();

    @Query("""
        SELECT s.course, COUNT(s)
        FROM Student s
        GROUP BY s.course
    """)
    List<Object[]> getCourseStatistics();

    List<Student> findTop5ByOrderByIdDesc();
}