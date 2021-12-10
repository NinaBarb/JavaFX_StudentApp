/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Nina
 */
@Entity
@Table(name = "Course")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Course.findAll", query = "SELECT c FROM Course c")
    , @NamedQuery(name = "Course.findByIDCourse", query = "SELECT c FROM Course c WHERE c.iDCourse = :iDCourse")
    , @NamedQuery(name = "Course.findByTitle", query = "SELECT c FROM Course c WHERE c.title = :title")
    , @NamedQuery(name = "Course.findByEcts", query = "SELECT c FROM Course c WHERE c.ects = :ects")})
public class Course implements Serializable {

    @OneToMany(mappedBy = "courseID")
    private Collection<PersonCourse> personCourseCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDCourse")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer iDCourse;
    @Column(name = "Title")
    private String title;
    @Column(name = "ECTS")
    private Integer ects;

    public Course() {
    }
    
    public Course(Course data) {
        updateDetails(data);
    }

    public Course(Integer iDCourse) {
        this.iDCourse = iDCourse;
    }

    public Course(Integer iDCourse, String title, Integer ects) {
        this.iDCourse = iDCourse;
        this.title = title;
        this.ects = ects;
    }
    
    public Integer getIDCourse() {
        return iDCourse;
    }

    public void setIDCourse(Integer iDCourse) {
        this.iDCourse = iDCourse;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getEcts() {
        return ects;
    }

    public void setEcts(Integer ects) {
        this.ects = ects;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDCourse != null ? iDCourse.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Course)) {
            return false;
        }
        Course other = (Course) object;
        if ((this.iDCourse == null && other.iDCourse != null) || (this.iDCourse != null && !this.iDCourse.equals(other.iDCourse))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return title;
    }

    public void updateDetails(Course data) {
        this.title = data.getTitle();
        this.ects = data.getEcts();
    }

    @XmlTransient
    public Collection<PersonCourse> getPersonCourseCollection() {
        return personCourseCollection;
    }

    public void setPersonCourseCollection(Collection<PersonCourse> personCourseCollection) {
        this.personCourseCollection = personCourseCollection;
    }
    
}
