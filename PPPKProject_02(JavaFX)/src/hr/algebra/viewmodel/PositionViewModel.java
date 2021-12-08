/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewmodel;

import hr.algebra.model.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Nina
 */
public class PositionViewModel {
    
    private final Position position;

    public PositionViewModel(Position position) {
        if (position == null) {
            position = new Position(0, "");
        }
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
    
    public StringProperty getTitleProperty(){
        return new SimpleStringProperty(position.getTitle());
    }
}
