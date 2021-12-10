/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewmodel;

import hr.algebra.model.Course;
import hr.algebra.model.Person;
import hr.algebra.model.PersonCourse;
import hr.algebra.model.Position;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author Nina
 */
public class PersonCourseViewModel {
    
    private final PersonCourse personCourse;

    public PersonCourseViewModel(PersonCourse personCourse, Person person, Course course, Position position) {
        if (personCourse == null) {
            personCourse = new PersonCourse(0, course, person, position);
        }
        this.personCourse = personCourse;
    }
    
    public PersonCourseViewModel(PersonCourse personCourse) {
        if (personCourse == null) {
            Person person = new Person(0, "", "", 0, "", "", "");
            Course course = new Course(0, "", 0);
            Position position = new Position(0, "");
            personCourse = new PersonCourse(0, course, person, position);
        }
        this.personCourse = personCourse;
    }

    public PersonCourse getPersonCourse() {
        return personCourse;
    }
    
    public ObjectProperty<Course> getCourseIDProperty() {
        return new SimpleObjectProperty<>(personCourse.getCourseID());
    }

    public ObjectProperty<Person> getPersonIDProperty() {
        return new SimpleObjectProperty<>(personCourse.getPersonID());
    }

    public ObjectProperty<Position> getPositionIDProperty() {
        return new SimpleObjectProperty<>(personCourse.getPositionID());
    }
}
