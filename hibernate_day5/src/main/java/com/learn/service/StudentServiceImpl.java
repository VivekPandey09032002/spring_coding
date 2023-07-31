package com.learn.service;

import com.learn.entity.Student;
import com.learn.repository.StudentRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Transactional
@Service
public class StudentServiceImpl implements StudentService{

    private  final StudentRepo studentRepo;

    @Override
    public Optional<Student> create(Student student) {
        return studentRepo.create(student);
    }

    @Override
    public boolean remove(Long studentId) {
        return studentRepo.remove(studentId);
    }

    @Override
    public boolean update(Student student) {
        return studentRepo.update(student);
    }

    @Override
    public Optional<Student> get(Long studentId) {
        return studentRepo.get(studentId);
    }

    @Override
    public List<Student> getAll() {
        return studentRepo.getAll();
    }
}
