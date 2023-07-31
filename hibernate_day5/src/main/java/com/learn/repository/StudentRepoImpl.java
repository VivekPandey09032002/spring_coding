package com.learn.repository;

import com.learn.entity.Student;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class StudentRepoImpl implements StudentRepo {

    private final SessionFactory factory;

    @Override
    public Optional<Student> create(Student student) {
        Session session = factory.getCurrentSession();
        session.save(student);
        return Optional.ofNullable(session.get(Student.class, student.getId()));
    }

    @Override
    public boolean remove(Long studentId) {
        Session session = factory.getCurrentSession();
        Student student = session.get(Student.class, studentId);
        if (student == null)
            return false;
        session.remove(student);
        return true;
    }

    @Override
    public boolean update(Student student) {
        Session session = factory.getCurrentSession();
        if (session.get(Student.class, student.getId()) == null) {
            return false;
        } else {
            session.update(student);
            return true;
        }
    }

    @Override
    public Optional<Student> get(Long studentId) {
        Session session = factory.getCurrentSession();
        return Optional.ofNullable(session.get(Student.class, studentId));
    }

    @Override
    public List<Student> getAll() {
        Session session = factory.getCurrentSession();
        Query<Student> studentQuery = session.createQuery("from Student", Student.class).setCacheable(true);
        return studentQuery.getResultList();
    }
}
