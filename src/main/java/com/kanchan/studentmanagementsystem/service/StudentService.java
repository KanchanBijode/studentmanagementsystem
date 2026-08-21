package com.kanchan.studentmanagementsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kanchan.studentmanagementsystem.entity.Student;
import com.kanchan.studentmanagementsystem.repository.StudentRepository;


@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public void saveStudent(Student student) {
        studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(int id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student searchStudent(String name) {

    return studentRepository.findByName(name);

    }
    public Student searchStudentById(int id) {

        return studentRepository.findById(id).orElse(null);

    }

    public void deleteStudent(int id) {
        studentRepository.deleteById(id);
    }

    // Total Students
    public long getStudentCount() {
        return studentRepository.count();
    }
    public Page<Student> getStudentsPage(int page) {

        return studentRepository.findAll(

                PageRequest.of(page, 5, Sort.by("id").ascending())

        );
    }
    public Student getStudentByEmail(String email) {

     return studentRepository.findByEmail(email);

    }
    public long getCourseCount() {

        return studentRepository.countDistinctCourses();

    }
    public List<Object[]> getCourseStatistics() {
        return studentRepository.getCourseStatistics();
    }
    public List<Student> searchStudents(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }
    public List<Student> getRecentStudents() {
        return studentRepository.findTop5ByOrderByIdDesc();
    }
    public List<Student> getStudentsByCourse(String course) {
        return studentRepository.findByCourseIgnoreCase(course);
    }
    public List<Student> getSortedStudents(String sortBy) {

        if (sortBy.equals("nameAsc")) {
            return studentRepository.findAll(Sort.by("name").ascending());
        }

        if (sortBy.equals("nameDesc")) {
            return studentRepository.findAll(Sort.by("name").descending());
        }

        if (sortBy.equals("latest")) {
            return studentRepository.findAll(Sort.by("id").descending());
        }

        if (sortBy.equals("oldest")) {
            return studentRepository.findAll(Sort.by("id").ascending());
        }

        return studentRepository.findAll();
    }
}