package com.welyab.ithappens.tutorials.springboot.jaxrs.data.hibernate;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.scanner.ScanResult;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.persistence.Entity;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.context.spi.CurrentSessionContext;
import org.hibernate.engine.spi.SessionFactoryImplementor;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    private static void createSessionFactory() throws IOException {
        if (sessionFactory != null && sessionFactory.isOpen()) {
            sessionFactory.close();
        }
        Configuration configuration = new Configuration();
        configuration.setProperty(
                "connection.driver_class",
                "org.hsqldb.jdbc.JDBCDriver"
        );
        configuration.setProperty(
                "hibernate.connection.url",
                String.format("jdbc:hsqldb:%s:testdb", getDbDirectory().toString())
        );
        configuration.setProperty(
                "hibernate.connection.username",
                "db"
        );
        configuration.setProperty(
                "hibernate.connection.password",
                ""
        );
//        configuration.setProperty(
//                "hibernate.current_session_context_class",
//                "com.welyab.ithappens.tutorials.springboot.jaxrs.data.hibernate.HibernateUtil.CurrentSessionContextImpl"
//        );
        configuration.setProperty(
                "hibernate.hbm2ddl.auto",
                "update"
        );

        FastClasspathScanner scanner = new FastClasspathScanner(
                "com.welyab.ithappens.tutorials.springboot.jaxrs.data.model"
        );
        ScanResult scanResult = scanner
                .matchClassesWithAnnotation(Entity.class, e -> {
                })
                .scan();
        List<String> entitiesNames = scanResult.getNamesOfAllClasses();
        List<Class<?>> entitiesClasses = scanResult.classNamesToClassRefs(entitiesNames);
        entitiesClasses.forEach(configuration::addAnnotatedClass);

        sessionFactory = configuration.buildSessionFactory();
    }

    private static Path getDbDirectory() throws IOException {
        Path path = Paths.get(System.getProperty("user.home")).resolve("jaxrs-tutorial/db/database");
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        return path;
    }

    private static SessionFactory getSessionFactory() throws IOException {
        if (sessionFactory == null) {
            createSessionFactory();
        }

        return sessionFactory;
    }

    public static void beginTransaction() {
        if(getSession().getTransaction().isActive()) {
            return;
        }
        getSession().beginTransaction();
    }
    
    public static void commitTransaction() {
        if(getSession().getTransaction().isActive()) {
            getSession().getTransaction().commit();
        }
    }

    public static Session getSession() {
        if (CurrentSessionContextImpl.threadSession.get() == null) {
            try {
                Session session = getSessionFactory().openSession();
                CurrentSessionContextImpl.threadSession.set(session);
            } catch (Exception e) {
                throw new HibernateUtilException("Fail to create hibernate session", e);
            }
        }

        return CurrentSessionContextImpl.threadSession.get();
    }

    public final static class CurrentSessionContextImpl implements CurrentSessionContext {

        private static transient final ThreadLocal<Session> threadSession = new ThreadLocal<>();

        public CurrentSessionContextImpl(SessionFactoryImplementor sessionFactoryImplementor) {
        }

        @Override
        public Session currentSession() throws HibernateException {
            if (threadSession.get() == null) {
                try {
                    Session session = HibernateUtil.getSessionFactory().openSession();
                    threadSession.set(session);
                } catch (IOException ex) {
                    throw new HibernateUtilException(ex);
                }
            }

            return threadSession.get();
        }
    }
}
