package com.learn.main;

import com.learn.config.HibernateConfiguration;
import com.learn.entity.Address;
import com.learn.entity.Student;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;


@Log4j2
public class CheatSheet3 {
    private static SessionFactory factory;
    public static void main(String[] args) {
        AbstractApplicationContext applicationContext =  new AnnotationConfigApplicationContext(HibernateConfiguration.class);
        applicationContext.registerShutdownHook();
        factory = applicationContext.getBean(SessionFactory.class);
        Session session =  factory.openSession();
        session.save(new Student("any name"));
        Student student =  session.get(Student.class, 1L);
        session.close();
        // Here, the student object is in a detached state
        student.setName("vivek pandey");
        // Here, reattaching to session
        session = factory.openSession();
        //making 1L id student available in the session
        Student stud = session.get(Student.class, 1L);
        session.beginTransaction();
        //session.update(student) will through exception as 1L id student already available in session
        session.merge(student);
        session.getTransaction().commit();
        session.close();
        applicationContext.close();
    }
}
