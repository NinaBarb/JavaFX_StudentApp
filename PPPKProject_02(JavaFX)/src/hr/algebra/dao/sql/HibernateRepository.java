/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao.sql;

import hr.algebra.dao.Repository;
import hr.algebra.model.Course;
import hr.algebra.model.Person;
import hr.algebra.model.PersonCourse;
import hr.algebra.model.Position;
import java.util.List;
import javax.persistence.EntityManager;


public class HibernateRepository implements Repository {

    @Override
    public int addPerson(Person data) throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            Person person = new Person(data);
            em.persist(person);
            em.getTransaction().commit();
            return person.getIDPerson();
        }
    }

    @Override
    public void updatePerson(Person person) throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            em.find(Person.class, person.getIDPerson()).updateDetails(person);
            em.getTransaction().commit();
        }
    }

    @Override
    public void deletePerson(Person person) throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            em.remove(em.contains(person) ? person : em.merge(person));
            em.getTransaction().commit();
        }
    }

    @Override
    public Person getPerson(int idPerson) throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            return em.find(Person.class, idPerson);
        }
    }

    @Override
    public List<Person> getPeople() throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            return em.createNamedQuery(HibernateFactory.SELECT_ALL_PEOPLE).getResultList();
        }
    }

    @Override
    public void release(){
        HibernateFactory.release();
    }

    @Override
    public int addCourse(Course data) throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            Course course = new Course(data);
            em.persist(course);
            em.getTransaction().commit();
            return course.getIDCourse();
        }
    }

    @Override
    public void updateCourse(Course course) throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            em.find(Course.class, course.getIDCourse()).updateDetails(course);
            em.getTransaction().commit();
        }
    }

    @Override
    public void deleteCourse(Course course) throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            em.remove(em.contains(course) ? course : em.merge(course));
            em.getTransaction().commit();
        }
    }

    @Override
    public Course getCourse(int idCourse) throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            return em.find(Course.class, idCourse);
        }
    }

    @Override
    public List<Course> getCourses() throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            return em.createNamedQuery(HibernateFactory.SELECT_ALL_COURSES).getResultList();
        }
    }

    @Override
    public int addPosition(Position data) throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            Position position = new Position(data);
            em.persist(position);
            em.getTransaction().commit();
            return position.getIDPosition();
        }
    }

    @Override
    public void updatePosition(Position position) throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            em.find(Position.class, position.getIDPosition()).updateDetails(position);
            em.getTransaction().commit();
        }
    }

    @Override
    public void deletePosition(Position position) throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            em.remove(em.contains(position) ? position : em.merge(position));
            em.getTransaction().commit();
        }
    }

    @Override
    public Position getPosition(int idPosition) throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            return em.find(Position.class, idPosition);
        }
    }

    @Override
    public List<Position> getPositions() throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            return em.createNamedQuery(HibernateFactory.SELECT_ALL_POSITIONS).getResultList();
        }
    }

    @Override
    public List<PersonCourse> getPeopleOnCourse() throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            return em.createNamedQuery(HibernateFactory.SELECT_ALL_PERSON_COURSE).getResultList();
        }
        /*try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
        EntityManager em = wrapper.get();
        Query query = em.createNativeQuery("{call GetPeopleCourses(?)}",
        PersonCourse.class)
        .setParameter(1, course.getIDCourse());
        
        return query.getResultList();
        }*/
    }

    @Override
    public int addPersonOnCourse(PersonCourse data, Person person, Course course, Position position) throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            PersonCourse personCourse = new PersonCourse(data.getIDPersonCourse(), course, person, position);
            em.merge(personCourse);
            em.getTransaction().commit();
            return personCourse.getIDPersonCourse();
        }
    }

    @Override
    public void deletePersonOnCourse(PersonCourse personCourse) throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            em.remove(em.contains(personCourse) ? personCourse : em.merge(personCourse));
            em.getTransaction().commit();
        }
    }

    @Override
    public void updatePersonOnCourse(PersonCourse personCourse) throws Exception {
        try(EntityManagerWrapper wrapper = HibernateFactory.getEntityManager()){
            EntityManager em = wrapper.get();
            em.getTransaction().begin();
            em.find(PersonCourse.class, personCourse.getIDPersonCourse()).updateDetails(personCourse);
            em.getTransaction().commit();
        }
    }
    
}
