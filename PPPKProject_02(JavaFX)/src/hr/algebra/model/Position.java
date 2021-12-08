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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Nina
 */
@Entity
@Table(name = "Position")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Position.findAll", query = "SELECT p FROM Position p")
    , @NamedQuery(name = "Position.findByIDPosition", query = "SELECT p FROM Position p WHERE p.iDPosition = :iDPosition")
    , @NamedQuery(name = "Position.findByTitle", query = "SELECT p FROM Position p WHERE p.title = :title")})
public class Position implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDPosition")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer iDPosition;
    @Column(name = "Title")
    private String title;

    public Position() {
    }
    
    public Position(Position position) {
        updateDetails(position);
    }

    public Position(Integer iDPosition, String title) {
        this.iDPosition = iDPosition;
        this.title = title;
    }

    public Position(Integer iDPosition) {
        this.iDPosition = iDPosition;
    }

    public Integer getIDPosition() {
        return iDPosition;
    }

    public void setIDPosition(Integer iDPosition) {
        this.iDPosition = iDPosition;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDPosition != null ? iDPosition.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Position)) {
            return false;
        }
        Position other = (Position) object;
        if ((this.iDPosition == null && other.iDPosition != null) || (this.iDPosition != null && !this.iDPosition.equals(other.iDPosition))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hr.algebra.model.Position[ iDPosition=" + iDPosition + " ]";
    }

    public void updateDetails(Position data) {
        this.title = data.getTitle();
    }
    
}
