/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import hr.algebra.model.Person;
import hr.algebra.model.Position;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "PositionPerson")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PositionPerson.findAll", query = "SELECT p FROM PositionPerson p")
    , @NamedQuery(name = "PositionPerson.findByIDPositionPerson", query = "SELECT p FROM PositionPerson p WHERE p.iDPositionPerson = :iDPositionPerson")})
public class PositionPerson implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDPositionPerson")
    private Integer iDPositionPerson;
    @JoinColumn(name = "PersonID", referencedColumnName = "IDPerson")
    @ManyToOne
    private Person personID;
    @JoinColumn(name = "PositionID", referencedColumnName = "IDPosition")
    @ManyToOne
    private Position positionID;

    public PositionPerson() {
    }

    public PositionPerson(Integer iDPositionPerson) {
        this.iDPositionPerson = iDPositionPerson;
    }

    public Integer getIDPositionPerson() {
        return iDPositionPerson;
    }

    public void setIDPositionPerson(Integer iDPositionPerson) {
        this.iDPositionPerson = iDPositionPerson;
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
        hash += (iDPositionPerson != null ? iDPositionPerson.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PositionPerson)) {
            return false;
        }
        PositionPerson other = (PositionPerson) object;
        if ((this.iDPositionPerson == null && other.iDPositionPerson != null) || (this.iDPositionPerson != null && !this.iDPositionPerson.equals(other.iDPositionPerson))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hr.algebra.PositionPerson[ iDPositionPerson=" + iDPositionPerson + " ]";
    }
    
}
