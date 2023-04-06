package com.sangqle.sample.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Metamodel;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.stat.Statistics;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static java.lang.System.exit;

public class TestDb {
    public static final TestDb Instance = new TestDb();

    public void getAnimals() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "FROM Animal";
        List<Animal> animals = session.createQuery(hql, Animal.class).getResultList();
        System.err.println("Animals: " + animals);
    }

    public void getAnimal(long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Animal animal = session.get(Animal.class, id);
//        System.err.println("Animal: " + animal);
    }

    public long saveAnimal(int value) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            Animal animal = new Animal();
            animal.setName("Dog-" + value);
            animal.setValue(value);
            session.save(animal);
            transaction.commit();
            return animal.getId();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to save animal", e);
        } finally {
            session.close();
        }
    }

    public void randomFetchFromThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Session session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();

                Statistics statistics = session.getSessionFactory().getStatistics();
                statistics.setStatisticsEnabled(true);

                for (long i = 1000; i < 1100; i++) {
                    Animal animal = session.get(Animal.class, i);
                    // Print cache hit/miss count after each get operation
//                    System.out.println("Cache hits: " + statistics.getSecondLevelCacheHitCount());
//                    System.out.println("Cache misses: " + statistics.getSecondLevelCacheMissCount());
//                    System.out.println("Cache puts: "  + statistics.getSecondLevelCachePutCount() + "\n");
                }

                session.getTransaction().commit();
                session.close();
            }
        });

        thread.start();
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < 2000; i++) {
            Instance.randomFetchFromThread();
        }
        long numberConnection = HibernateUtil.getNumberConnection();
        System.err.println("Number connection: " + numberConnection);
        // Wait for all threads to finish
        long endTime = System.nanoTime();
        System.err.println("Time: " + (endTime - startTime)+ "ns");
    }
}
