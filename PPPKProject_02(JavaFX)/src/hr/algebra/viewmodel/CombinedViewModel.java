/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewmodel;

import hr.algebra.model.Person;
import hr.algebra.model.Position;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Nina
 */
public class CombinedViewModel {
    
    private final Person person;
    private final Position position;

    public CombinedViewModel(Person person, Position position) {
        if (person == null || position == null) {
            person = new Person(0, "", "", 0, "", "", "");
            position = new Position(0, "");
        }
        this.person = person;
        this.position = position;
    }

    public Person getPerson() {
        return person;
    }
    
    public StringProperty getFirstNameProperty(){
        return new SimpleStringProperty(person.getFirstName());
    }
    
    public StringProperty getLastNameProperty(){
        return new SimpleStringProperty(person.getLastName());
    }
    
    public StringProperty getEmailProperty(){
        return new SimpleStringProperty(person.getEmail());
    }
    
    public StringProperty getGenderProperty(){
        return new SimpleStringProperty(person.getGender());
    }
    
    public StringProperty getJmbagProperty(){
        return new SimpleStringProperty(person.getJmbag());
    }
    
    public IntegerProperty getAgeProperty(){
        return new SimpleIntegerProperty(person.getAge());
    }
    
    public IntegerProperty getIdProperty(){
        return new SimpleIntegerProperty(person.getIDPerson());
    }
    
    public ObjectProperty<byte[]> getPictureProperty(){
        return new SimpleObjectProperty<>(person.getPicture());
    }
    
    public Position getPosition() {
        return position;
    }
    
    public StringProperty getTitleProperty(){
        return new SimpleStringProperty(position.getTitle());
    }
}
