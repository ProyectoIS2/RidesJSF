package dataAccess;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import domain.Driver;
import domain.Ride;
import exceptions.RideAlreadyExistException;
import exceptions.RideMustBeLaterThanTodayException;

public class HibernateDataAccess {
    
    public HibernateDataAccess() {}

    public Ride createRide(String from, String to, Date date, int nPlaces, float price, String driverEmail) throws RideAlreadyExistException, RideMustBeLaterThanTodayException {
        EntityManager db = JPAUtil.createEntityManager();
        try {
            db.getTransaction().begin();
            
            Driver driver = db.find(Driver.class, driverEmail);
            
            if (driver.doesRideExists(from, to, date)) {
                db.getTransaction().rollback();
                throw new RideAlreadyExistException("Error: El conductor ya tiene un viaje creado con esos datos.");
            }

            if (new Date().compareTo(date) > 0) {
                db.getTransaction().rollback();
                throw new RideMustBeLaterThanTodayException("Error: El viaje debe ser en una fecha futura.");
            }


            Ride ride = driver.addRide(from, to, date, nPlaces, price);

            db.persist(ride); 
            db.merge(driver);
            
            db.getTransaction().commit();
            return ride;

        } catch (Exception e) {
            if (db.getTransaction().isActive()) {
                db.getTransaction().rollback();
            }
            throw e; 
        } finally {
            db.close();
        }
    }

    public List<Ride> getRides(String from, String to, Date date) {
        EntityManager db = JPAUtil.createEntityManager();
        try {
            String queryStr = "SELECT r FROM Ride r WHERE r.from = :from AND r.to = :to AND r.date = :date";
            TypedQuery<Ride> query = db.createQuery(queryStr, Ride.class);
            query.setParameter("from", from);
            query.setParameter("to", to);
            query.setParameter("date", date);
            return query.getResultList();
        } finally {
            db.close();
        }
    }

    public List<String> getDepartCities() {
        EntityManager db = JPAUtil.createEntityManager();
        try {
            String queryStr = "SELECT DISTINCT r.from FROM Ride r ORDER BY r.from";
            TypedQuery<String> query = db.createQuery(queryStr, String.class);
            return query.getResultList();
        } finally {
            db.close();
        }
    }

    public List<String> getArrivalCities(String from) {
        EntityManager db = JPAUtil.createEntityManager();
        try {
            String queryStr = "SELECT DISTINCT r.to FROM Ride r WHERE r.from = :from ORDER BY r.to";
            TypedQuery<String> query = db.createQuery(queryStr, String.class);
            query.setParameter("from", from);
            return query.getResultList();
        } finally {
            db.close();
        }
    }

    public List<Date> getThisMonthDatesWithRides(String from, String to, Date date) {
        EntityManager db = JPAUtil.createEntityManager();
        try {
            String queryStr = "SELECT DISTINCT r.date FROM Ride r WHERE r.from = :from AND r.to = :to";
            TypedQuery<Date> query = db.createQuery(queryStr, Date.class);
            query.setParameter("from", from);
            query.setParameter("to", to);
            return query.getResultList();
        } finally {
            db.close();
        }
    }
    
    public List<Ride> getAllRides() {
        EntityManager db = JPAUtil.createEntityManager();
        try {
            return db.createQuery("SELECT r FROM Ride r", Ride.class).getResultList();
        } finally {
            db.close();
        }
    }
    public Driver getDriver(String email) {
        EntityManager db = JPAUtil.createEntityManager();
        try {
            Driver driver = db.find(Driver.class, email);
            return driver;
        } finally {
            db.close();
        }
    }

    public void createDriver(String email, String name, String password) {
        EntityManager db = JPAUtil.createEntityManager();
        try {
            db.getTransaction().begin();
            Driver driver = new Driver(email, name, password);
            db.persist(driver);
            db.getTransaction().commit();
        } catch (Exception e) {
            if (db.getTransaction().isActive()) {
                db.getTransaction().rollback();
            }
            throw e;
        } finally {
            db.close();
        }
    }
}