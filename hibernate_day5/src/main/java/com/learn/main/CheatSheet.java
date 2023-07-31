package com.learn.main;

import com.learn.config.HibernateConfiguration;
import com.learn.entity.Student;
import com.learn.service.StudentService;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;


@Log4j2
public class CheatSheet {
    private static SessionFactory factory;
    public static void main(String[] args) {
        AbstractApplicationContext applicationContext =  new AnnotationConfigApplicationContext(HibernateConfiguration.class);
        applicationContext.registerShutdownHook();
        factory = applicationContext.getBean(SessionFactory.class);
        // Transient state(session -> no , database -> no)
        Student student = new Student("anup kumar");
        Session session =  factory.openSession();
        session.beginTransaction();
        // Persistent state(session -> yes , database -> yes)
        session.save(student);
        session.getTransaction().commit();

        session.beginTransaction();
        // detached state(session -> no, database -> yes) -> not completely removed
        session.remove(student);
        /*
          * since student is not available in session hibernate will create a select query
          * to get the student object
         */
        log.info("getting the object using select query");
        // Persistent state(session yes)
        student = session.get(Student.class,2L);
        System.out.println(student);
        // update the database according to the objects inside session
        // again student with given id will insert in database
        session.getTransaction().commit();
        session.close();
        applicationContext.close();
    }
}
