/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dao.RepositoryFactory;
import hr.algebra.utils.MaskUtils;
import hr.algebra.viewmodel.CombinedViewModel;
import hr.algebra.viewmodel.CourseViewModel;
import hr.algebra.viewmodel.PersonViewModel;
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
import javafx.scene.control.ComboBox;
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
public class CoursesController implements Initializable {
    
    private Map<TextField, Label> validationMap;
    
    private final ObservableList<CourseViewModel> list = FXCollections.observableArrayList();
    private final ObservableList<PersonViewModel> people = FXCollections.observableArrayList();
    private final ObservableList<PositionViewModel> positions = FXCollections.observableArrayList();
    private final ObservableList<CombinedViewModel> combined = FXCollections.observableArrayList();
    
    private CourseViewModel selectCourseViewModel;
    private CombinedViewModel selectCombinedViewModel;

    @FXML
    private TabPane tabListPeople;
    @FXML
    private Tab tabListCourses,tabAddCourse;
    @FXML
    private TableView<CourseViewModel> tvCourses;
    @FXML
    private TableColumn<CourseViewModel, String> tcTourseTitle;
    @FXML
    private TableColumn<CourseViewModel, Integer> tcEcts;
    @FXML
    private TextField tfCourseTitle,tfEcts;
    @FXML
    private Label lbErrorCourseTitle,lbErrorEcts;
    @FXML
    private StackPane stackPane;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Tab tabAddPerson;
    @FXML
    private TextField tfSelectedCourse;
    @FXML
    private ComboBox<PersonViewModel> cbPerson;
    @FXML
    private ComboBox<PositionViewModel> cbPosition;
    @FXML
    private TableView<CombinedViewModel> tvPeopleOnCourse;
    @FXML
    private TableColumn<CombinedViewModel, String> tcPersonOnCourse;
    @FXML
    private TableColumn<CombinedViewModel, String> tcPosition;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initValidation();
            initCourses();
            initTable();
            MaskUtils.addIntegerMask(tfEcts);
            setListeners();
        } catch (Exception ex) {
            Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void editCourse() {
        if (tvCourses.getSelectionModel().getSelectedItem() != null) {
            bindCourse(tvCourses.getSelectionModel().getSelectedItem());
            tabListPeople.getSelectionModel().select(tabAddCourse);
        }
    }

    @FXML
    private void deleteCourse() {
        if (tvCourses.getSelectionModel().getSelectedItem() != null) {
            list.remove(tvCourses.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void goToPositions() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/hr/algebra/view/Positions.fxml"));
            Scene scene = tvCourses.getScene();
            
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
            Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void commitCourse() {
        if (formValid()) {
            selectCourseViewModel.getCourse().setTitle(tfCourseTitle.getText().trim());
            selectCourseViewModel.getCourse().setEcts( Integer.valueOf(tfEcts.getText().trim()));
            
            if (selectCourseViewModel.getCourse().getIDCourse()== 0) {
                list.add(selectCourseViewModel);
            }else{
                try {
                    RepositoryFactory.getRepository().updateCourse(selectCourseViewModel.getCourse());
                    tvCourses.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectCourseViewModel = null;
            tabListPeople.getSelectionModel().select(tabListCourses);
            resetForm();
        }
    }

    private void initValidation() {
        validationMap = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfCourseTitle, lbErrorCourseTitle),
                new AbstractMap.SimpleImmutableEntry<>(tfEcts, lbErrorEcts)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void initCourses() throws Exception {
        RepositoryFactory.getRepository().getCourses().forEach(course -> list.add(new CourseViewModel(course)));
    }

    private void initTable() {
        tcTourseTitle.setCellValueFactory(course -> course.getValue().getTitleProperty());
        tcEcts.setCellValueFactory(course -> course.getValue(). getEctsProperty().asObject());
        
        tvCourses.setItems(list);
    }

    private void setListeners() {
        tabListPeople.setOnMouseClicked(event ->{
            if(tabListPeople.getSelectionModel().getSelectedItem().equals(tabAddCourse)){
                bindCourse(null);
            }
        });
        
        list.addListener((ListChangeListener.Change<? extends CourseViewModel> change) -> {
            if (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(pvm -> {
                        try {
                            RepositoryFactory.getRepository().deleteCourse(pvm.getCourse());
                        } catch (Exception ex) {
                            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }else if(change.wasAdded()){
                    change.getAddedSubList().forEach(pvm -> {
                        try {
                            int idCourse = RepositoryFactory.getRepository().addCourse(pvm.getCourse());
                            pvm.getCourse().setIDCourse(idCourse);
                        } catch (Exception ex) {
                            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        });
    }
    
    private void bindCourse(CourseViewModel courseViewModel) {
        selectCourseViewModel = courseViewModel != null ? courseViewModel : new CourseViewModel(null);
        tfCourseTitle.setText(selectCourseViewModel.getTitleProperty().get());
        tfEcts.setText(String.valueOf(selectCourseViewModel.getEctsProperty().get()));
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

    @FXML
    private void addPersonOnCourse(ActionEvent event) {
        if (tvCourses.getSelectionModel().getSelectedItem() == null) return;
        bindCourse(tvCourses.getSelectionModel().getSelectedItem());
        tabListPeople.getSelectionModel().select(tabAddCourse);
        try {
            tfSelectedCourse.setText(selectCourseViewModel.getCourse().toString());
            
            RepositoryFactory.getRepository().getPeople().forEach(person -> people.add(new PersonViewModel(person)));
            cbPerson.getItems().addAll(people);
            cbPerson.getSelectionModel().select(0);
            
            RepositoryFactory.getRepository().getPositions().forEach(position -> positions.add(new PositionViewModel(position)));
            cbPosition.getItems().addAll(positions);
            cbPosition.getSelectionModel().select(0);
            
        } catch (Exception ex) {
            Logger.getLogger(CoursesController.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        tabAddPerson.setDisable(false);
        tabListPeople.getSelectionModel().select(tabAddPerson);
        selectCourseViewModel = null;
    }

    @FXML
    private void commitPerson(ActionEvent event) {
        selectCombinedViewModel.getPerson().setFirstName(cbPerson.getValue().toString());
        selectCombinedViewModel.getPosition().setTitle(cbPosition.getValue().toString());
            
            if (selectCombinedViewModel.getPerson().getIDPerson()== 0) {
                combined.add(selectCombinedViewModel);
            }else{
                try {
                    RepositoryFactory.getRepository().update(selectCourseViewModel.getCourse());
                    tvCourses.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectCourseViewModel = null;
            tabListPeople.getSelectionModel().select(tabListCourses);
            resetForm();
    }

    @FXML
    private void personChanged(ActionEvent event) {
    }

    @FXML
    private void positionChanged(ActionEvent event) {
    }
}
