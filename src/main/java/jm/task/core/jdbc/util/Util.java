package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import java.util.Properties;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.service.ServiceRegistry;

public class Util {
    private static String url = "jdbc:mysql://localhost:3306/preproject113";
    private static String user = "root";
    private static String password = "132435";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) { // Для многопоточного приложения нужна синхронизация или eager init
            try {
                Configuration configuration = getConfiguration();
                configuration.addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
//                Нужно перехватить в DAO
                throw new RuntimeException("Failed to initialize Hibernate SessionFactory for DB URL: " + url, e);
            }
        }
        return sessionFactory;
    }

    private static Configuration getConfiguration() {
        Properties settings = getProperties();

        Configuration configuration = new Configuration();
        configuration.setProperties(settings);
        return configuration;
    }

    private static Properties getProperties() {
        Properties settings = new Properties();
        settings.put(AvailableSettings.DRIVER, "com.mysql.cj.jdbc.Driver");
        settings.put(AvailableSettings.URL, url);
        settings.put(AvailableSettings.USER, user);
        settings.put(AvailableSettings.PASS, password);
        settings.put(AvailableSettings.SHOW_SQL, "true"); // Hibernate печатает SQL-запросы, которые отправляет в БД
        settings.put(AvailableSettings.FORMAT_SQL, "true"); // Делает выводимые в консоль SQL-запросы более читаемыми
//        settings.put(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQLDialect");
//        settings.put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");
//        settings.put(AvailableSettings.HBM2DDL_AUTO, "create-drop");
        return settings;
    }
}
