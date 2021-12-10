/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.dao;

import hr.algebra.model.PersonCourse;
import hr.algebra.model.Course;
import hr.algebra.model.Person;
import hr.algebra.model.Position;
import java.util.List;

/**
 *
 * @author Nina
 */
public interface Repository {
    
    int addPerson(Person data) throws Exception;
    void updatePerson(Person data) throws Exception;
    void deletePerson(Person data) throws Exception;
    Person getPerson(int idPerson) throws Exception;
    List<Person> getPeople() throws Exception;
    
    int addCourse(Course data) throws Exception;
    void updateCourse(Course data) throws Exception;
    void deleteCourse(Course data) throws Exception;
    Course getCourse(int idCourse) throws Exception;
    List<Course> getCourses() throws Exception;
    
    int addPersonOnCourse(PersonCourse personCourse, Person person, Course course, Position position) throws Exception;
    void deletePersonOnCourse(PersonCourse personCourse) throws Exception;
    void updatePersonOnCourse(PersonCourse personCourse) throws Exception;
    List<PersonCourse> getPeopleOnCourse() throws Exception;
    
    int addPosition(Position data) throws Exception;
    void updatePosition(Position data) throws Exception;
    void deletePosition(Position data) throws Exception;
    Position getPosition(int idPosition) throws Exception;
    List<Position> getPositions() throws Exception;
    
    default void release() throws Exception{};
}
