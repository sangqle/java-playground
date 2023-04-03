package com.sangqle.sample.hibernate;

import org.hibernate.Metamodel;
import org.hibernate.Session;
import org.hibernate.boot.Metadata;
import org.hibernate.mapping.PersistentClass;

import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.EntityType;
import java.util.List;
import java.util.Set;

public class TestDb {
    public static final TestDb Instance = new TestDb();

    public void getAnimals() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "FROM Animal";
        List<Animal> animals = session.createQuery(hql, Animal.class).getResultList();
        System.err.println("Animals: " + animals);


    }
    public static void main(String[] args) {
        Instance.getAnimals();
        System.out.println("Test DB");
    }
}
