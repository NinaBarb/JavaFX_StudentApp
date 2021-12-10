/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao.sql;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Nina
 */
public class HibernateFactory {
    
    private static final String PERSISTENCE_UNIT = "PPPKProject_02_JavaFX_PU";
    public static final String SELECT_ALL_PEOPLE = "Person.findAll";
    public static final String SELECT_ALL_COURSES = "Course.findAll";
    public static final String SELECT_ALL_POSITIONS = "Position.findAll";
    public static final String SELECT_ALL_PERSON_COURSE = "PersonCourse.findAll";
    
    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
    
    public static EntityManagerWrapper getEntityManager(){
        return new EntityManagerWrapper(EMF.createEntityManager());
    }
    
    public static void release(){
        EMF.close();
    }
}
