package com.learn.repository;


import com.learn.entity.Student;

import java.util.List;
import java.util.Optional;


public interface StudentRepo {
   Optional<Student> create(Student student);
   boolean remove(Long studentId);
   boolean update(Student t);
   Optional<Student> get(Long  studentId);
   List<Student> getAll();
}
