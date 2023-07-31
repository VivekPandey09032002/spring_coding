package com.learn.main;

import com.learn.config.HibernateConfiguration;
import com.learn.service.StudentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;


@Log4j2
public class Main {
    public static void main(String[] args) {
        AbstractApplicationContext applicationContext =  new AnnotationConfigApplicationContext(HibernateConfiguration.class);
        applicationContext.registerShutdownHook();
        StudentService studentService = applicationContext.getBean(StudentService.class);
//        only one time select query will be run
        log.info(studentService.getAll()); ;
        log.info(studentService.getAll()); ;
        applicationContext.close();
    }
}
