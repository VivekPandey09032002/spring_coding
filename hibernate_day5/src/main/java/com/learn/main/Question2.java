package com.learn.main;

import com.learn.config.HibernateConfiguration;
import com.learn.entity.Student;
import com.learn.service.StudentService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class Question2 {



    public static void main(String[] args) {
        AbstractApplicationContext applicationContext =  new AnnotationConfigApplicationContext(HibernateConfiguration.class);
        applicationContext.registerShutdownHook();
        SessionFactory factory = applicationContext.getBean(SessionFactory.class);

        Runnable runnable1 = () ->
        {
            Session session = factory.openSession();
            System.out.println("thread 1 start");
            Student student =  session.get(Student.class,7L);
            session.evict(student);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
//            updated name will be printed when we do session.evit()
//            old name will be printed even though change by antother transaction
            System.out.println("thread 1: updated name: " +session.load(Student.class,7L));
//            System.out.println("thread 1: updated name: " +session.get(Student.class,7L));
            System.out.println("thread 1 end");
            session.close();
        };
        Runnable runnable2 = () ->
        {
            Session session = factory.openSession();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("thread 2 start");
            session.beginTransaction();
            Student student = session.load(Student.class,7L);
//            Student student = session.get(Student.class,7L);
            student.setName("amit kumar");
            session.save(student);
            session.getTransaction().commit();
            System.out.println("thread 2 changed name: " + student);
            System.out.println("thread 2 end");
            session.close();
        };
        Thread thread1 = new Thread(runnable1);
        Thread thread2 = new Thread(runnable2);
        thread1.start();
        thread2.start();
    }
}
