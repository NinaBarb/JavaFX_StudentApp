/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dao.RepositoryFactory;
import hr.algebra.viewmodel.PositionViewModel;
import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Nina
 */
public class PositionsController implements Initializable {
    
    private Map<TextField, Label> validationMap;
    
    private final ObservableList<PositionViewModel> list = FXCollections.observableArrayList();
    
    private PositionViewModel selectPositionViewModel;

    @FXML
    private TabPane tabListPeople;
    @FXML
    private Tab tabListPositions,tabAddPosition;
    @FXML
    private TableView<PositionViewModel> tvPositions;
    @FXML
    private TableColumn<PositionViewModel, String> tcPositionTitle;
    @FXML
    private TextField tfPositionTitle;
    @FXML
    private Label lbErrorPositionTitle;
    @FXML
    private StackPane stackPane;
    @FXML
    private AnchorPane anchorPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initValidation();
            initPositions();
            initTable();
            setListeners();
        } catch (Exception ex) {
            Logger.getLogger(PositionsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void editPosition() {
        if (tvPositions.getSelectionModel().getSelectedItem() != null) {
            bindPosition(tvPositions.getSelectionModel().getSelectedItem());
            tabListPeople.getSelectionModel().select(tabAddPosition);
        }
    }

    @FXML
    private void deletePosition() {
        if (tvPositions.getSelectionModel().getSelectedItem() != null) {
            list.remove(tvPositions.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void goToPeople(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/hr/algebra/view/People.fxml"));
            Scene scene = tvPositions.getScene();
            
            //Set X of second scene to Width of window
            root.translateXProperty().set(scene.getWidth());
            //Add second scene. Now both first and second scene is present
            stackPane.getChildren().add(root);
            
            //Create new TimeLine animation
            Timeline timeline = new Timeline();
            //Animate X property
            KeyValue kv = new KeyValue(root.translateXProperty(), 0, Interpolator.EASE_IN);
            KeyFrame kf = new KeyFrame(Duration.seconds(1), kv);
            timeline.getKeyFrames().add(kf);
            //After completing animation, remove first scene
            timeline.setOnFinished(t -> {
                stackPane.getChildren().remove(anchorPane);
            });
            timeline.play();
        } catch (IOException ex) {
            Logger.getLogger(PositionsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void commitPosition() {
        if (formValid()) {
            selectPositionViewModel.getPosition().setTitle(tfPositionTitle.getText().trim());
            
            if (selectPositionViewModel.getPosition().getIDPosition()== 0) {
                list.add(selectPositionViewModel);
            }else{
                try {
                    RepositoryFactory.getRepository().updatePosition(selectPositionViewModel.getPosition());
                    tvPositions.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectPositionViewModel = null;
            tabListPeople.getSelectionModel().select(tabListPositions);
            resetForm();
        }
    }

    private void initValidation() {
        validationMap = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfPositionTitle, lbErrorPositionTitle)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void initTable() {
        tcPositionTitle.setCellValueFactory(position -> position.getValue().getTitleProperty());
        
        tvPositions.setItems(list);
    }

    private void setListeners() {
        tabListPeople.setOnMouseClicked(event ->{
            if(tabListPeople.getSelectionModel().getSelectedItem().equals(tabAddPosition)){
                bindPosition(null);
            }
        });
        
        list.addListener((ListChangeListener.Change<? extends PositionViewModel> change) -> {
            if (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(pvm -> {
                        try {
                            RepositoryFactory.getRepository().deletePosition(pvm.getPosition());
                        } catch (Exception ex) {
                            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }else if(change.wasAdded()){
                    change.getAddedSubList().forEach(pvm -> {
                        try {
                            int idPosition = RepositoryFactory.getRepository().addPosition(pvm.getPosition());
                            pvm.getPosition().setIDPosition(idPosition);
                        } catch (Exception ex) {
                            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        });
    }

    private void initPositions() throws Exception {
        RepositoryFactory.getRepository().getPositions().forEach(position -> list.add(new PositionViewModel(position)));
    }
    
    private void bindPosition(PositionViewModel positionViewModel) {
        selectPositionViewModel = positionViewModel != null ? positionViewModel : new PositionViewModel(null);
        tfPositionTitle.setText(selectPositionViewModel.getTitleProperty().get());
    }

    private boolean formValid() {
        final AtomicBoolean ok = new AtomicBoolean(true);
        validationMap.keySet().forEach(tf -> {
            if (tf.getText().trim().isEmpty()) {
                ok.set(false);
                validationMap.get(tf).setVisible(true);
            }else{
                validationMap.get(tf).setVisible(false);
            }
        });
        return ok.get();
    }

    private void resetForm() {
        validationMap.values().forEach(l -> l.setVisible(false));
    }
}
