package com.learn.main;

import com.learn.config.HibernateConfiguration;
import com.learn.entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class Test {



    public static void main(String[] args) {
        AbstractApplicationContext applicationContext =  new AnnotationConfigApplicationContext(HibernateConfiguration.class);
        applicationContext.registerShutdownHook();
        SessionFactory factory = applicationContext.getBean(SessionFactory.class);
        Session session = factory.openSession();
        session.beginTransaction();
        Student ss =session.get(Student.class, 7l);
        System.out.println("Get Age from Database:"+ ss.getName());
        //evict the object
        session.evict(ss);
        ss.setName("name changed");
        session.flush(); // commit
        ss = session.get(Student.class, 7l);
        System.out.println("Age after evict:"+ ss.getName());
        //merge the object
        Student newStudent = new Student("error must through");
        newStudent.setId(7l);
        ss.setName("again update the name");
        session.update(newStudent);
        session.flush();
        ss = session.get(Student.class, 7l);
        System.out.println("Age after merge:"+ ss.getName());
        session.getTransaction().commit();
        session.close();
        applicationContext.close();
    }
}
