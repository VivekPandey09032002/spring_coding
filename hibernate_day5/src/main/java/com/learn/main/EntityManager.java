package com.learn.main;

import com.learn.config.HibernateConfiguration;
import com.learn.entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public class EntityManager {
    public static void main(String[] args) {
        AbstractApplicationContext applicationContext =  new AnnotationConfigApplicationContext(HibernateConfiguration.class);
        applicationContext.registerShutdownHook();
        EntityManagerFactory entityManagerFactory =  applicationContext.getBean(EntityManagerFactory.class,"entityManagerFactory");
        javax.persistence.EntityManager entityManager =  entityManagerFactory.createEntityManager();
        Session session = entityManager.unwrap(Session.class);
        Transaction transaction = session.beginTransaction();
        session.save(new Student("vivek pandey"));
        Student student = entityManager.find(Student.class,1L);
        transaction.commit();
        System.out.println(student);
    }
}
