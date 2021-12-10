/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nina
 */
@Entity
@Table(name = "PersonCourse")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PersonCourse.findAll", query = "SELECT p FROM PersonCourse p")
    , @NamedQuery(name = "PersonCourse.findByIDPersonCourse", query = "SELECT p FROM PersonCourse p WHERE p.iDPersonCourse = :iDPersonCourse")})
public class PersonCourse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDPersonCourse")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer iDPersonCourse;
    @JoinColumn(name = "CourseID", referencedColumnName = "IDCourse")
    @ManyToOne
    private Course courseID;
    @JoinColumn(name = "PersonID", referencedColumnName = "IDPerson")
    @ManyToOne
    private Person personID;
    @JoinColumn(name = "PositionID", referencedColumnName = "IDPosition")
    @ManyToOne
    private Position positionID;

    public PersonCourse() {
    }

    public PersonCourse(PersonCourse personCourse) {
        updateDetails(personCourse);
    }
    
    public PersonCourse(Integer iDPersonCourse, Course courseID, Person personID, Position positionID) {
        this.iDPersonCourse = iDPersonCourse;
        this.courseID = courseID;
        this.personID = personID;
        this.positionID = positionID;
    }
    

    public PersonCourse(Integer iDPersonCourse) {
        this.iDPersonCourse = iDPersonCourse;
    }

    public Integer getIDPersonCourse() {
        return iDPersonCourse;
    }

    public void setIDPersonCourse(Integer iDPersonCourse) {
        this.iDPersonCourse = iDPersonCourse;
    }

    public Course getCourseID() {
        return courseID;
    }

    public void setCourseID(Course courseID) {
        this.courseID = courseID;
    }

    public Person getPersonID() {
        return personID;
    }

    public void setPersonID(Person personID) {
        this.personID = personID;
    }

    public Position getPositionID() {
        return positionID;
    }

    public void setPositionID(Position positionID) {
        this.positionID = positionID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDPersonCourse != null ? iDPersonCourse.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonCourse)) {
            return false;
        }
        PersonCourse other = (PersonCourse) object;
        if ((this.iDPersonCourse == null && other.iDPersonCourse != null) || (this.iDPersonCourse != null && !this.iDPersonCourse.equals(other.iDPersonCourse))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hr.algebra.PersonCourse[ iDPersonCourse=" + iDPersonCourse + " ]";
    }

    public void updateDetails(PersonCourse personCourse) {
        this.personID = personCourse.getPersonID();
        this.courseID = personCourse.getCourseID();
        this.positionID = personCourse.getPositionID();
    }
    
}
