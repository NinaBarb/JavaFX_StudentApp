/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewmodel;

import hr.algebra.model.Course;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Nina
 */
public class CourseViewModel {
    
    private final Course course;

    public CourseViewModel(Course course) {
        if (course == null) {
            course = new Course(0, "", 0);
        }
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }
    
    public StringProperty getTitleProperty(){
        return new SimpleStringProperty(course.getTitle());
    }
    
    public IntegerProperty getEctsProperty(){
        return new SimpleIntegerProperty(course.getEcts());
    }
}
