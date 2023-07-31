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
public class CheatSheet2 {
    private static SessionFactory factory;
    public static void main(String[] args) {
        AbstractApplicationContext applicationContext =  new AnnotationConfigApplicationContext(HibernateConfiguration.class);
        applicationContext.registerShutdownHook();
        factory = applicationContext.getBean(SessionFactory.class);
        Session session =  factory.openSession();
        session.beginTransaction();
        Student student = new Student("vivek pandey");
        Address address = new Address("address 1");
        student.setAddress(address);
        address.setStudent(student);
        session.persist(student);
        session.getTransaction().commit();
        log.info("student inserted!!!!!!!!!!!!");
//      inserted a record
        session.beginTransaction();
        Student student1 = session.get(Student.class,1L);
        log.info("persistence state {},",student1);
//        session.remove(student1);
        Student student2 = new Student("new student");
        student2.setId(1L);
        log.info("detached  state student");
        session.save(student2);
        log.info("error aana chahiya {},",student1);
        session.getTransaction().commit();
        session.close();
        applicationContext.close();
    }
}
