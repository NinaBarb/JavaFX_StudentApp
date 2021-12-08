/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.controller;

import hr.algebra.dao.RepositoryFactory;
import hr.algebra.utils.FileUtils;
import hr.algebra.utils.ImageUtils;
import hr.algebra.utils.MaskUtils;
import hr.algebra.utils.ValidationUtils;
import hr.algebra.viewmodel.CourseViewModel;
import hr.algebra.viewmodel.PersonViewModel;
import hr.algebra.viewmodel.PositionViewModel;
import java.io.ByteArrayInputStream;
import java.io.File;
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
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Nina
 */
public class PeopleController implements Initializable {

    private Map<TextField, Label> validationMapPerson;
    private Map<TextField, Label> validationMapCourse;
    private Map<TextField, Label> validationMapPosition;
    
    private final ObservableList<PersonViewModel> listOfPeople = FXCollections.observableArrayList();
    private final ObservableList<CourseViewModel> listOfCourses = FXCollections.observableArrayList();
    private final ObservableList<PositionViewModel> listOfPositions = FXCollections.observableArrayList();
    
    private PersonViewModel selectPersonViewModel;
    private CourseViewModel selectCourseViewModel;
    private PositionViewModel selectPositionViewModel;
    private final String DEFAULT_IMAGE_PATH = "src/assets/no_image.png";
    
    @FXML
    private TabPane tabListPeople;
    @FXML
    private Tab tabList, tabAddPerson;
    @FXML
    private TableView<PersonViewModel> tvPeople;
    @FXML
    private TableColumn<PersonViewModel, String> tcFirstName,tcLastName,tcEmail,tcJMBAG,tcGender;
    @FXML
    private TableColumn<PersonViewModel, Integer> tcAge;
    @FXML
    private TextField tfFirstName, tfLastName, tfAge, tfJMBAG, tfEmail;
    @FXML
    private ToggleGroup rbtns;
    @FXML
    private ImageView ivPicture;
    @FXML
    private Label lbErrorFName, lbErrorLName,lbErrorAge,lbErrorJmbag,lbErrorEmail,lbErrorPicture;
    @FXML
    private Tab tabListCourses, tabAddCourse;
    @FXML
    private TableView<CourseViewModel> tvCourses;
    @FXML
    private TableColumn<CourseViewModel, String> tcTourseTitle;
    @FXML
    private TableColumn<CourseViewModel, Integer> tcEcts;
    @FXML
    private TextField tfCourseTitle, tfEcts;
    @FXML
    private Label lbErrorCourseTitle, lbErrorEcts;
    @FXML
    private TableView<PositionViewModel> tvPositions;
    @FXML
    private TableColumn<PositionViewModel, String> tcPositionTitle;
    @FXML
    private TextField tfPositionTitle;
    @FXML
    private Label lbErrorPositionTitle;
    @FXML
    private Tab tabListPositions, tabAddPosition;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            initValidation();
            initPeople();
            initCourses();
            initPositions();
            initTables();
            initMasks();
            setListeners();
        } catch (Exception ex) {
            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    private void initMasks() {
        MaskUtils.addIntegerMask(tfAge);
        MaskUtils.addIntegerMask(tfJMBAG);
        MaskUtils.addIntegerMask(tfEcts);
    }

    @FXML
    private void editPerson() {
        if (tvPeople.getSelectionModel().getSelectedItem() != null) {
            bindPerson(tvPeople.getSelectionModel().getSelectedItem());
            tabListPeople.getSelectionModel().select(tabAddPerson);
        }
    }

    @FXML
    private void deletePerson() {
        if (tvPeople.getSelectionModel().getSelectedItem() != null) {
            listOfPeople.remove(tvPeople.getSelectionModel().getSelectedItem());
        }
    }
    
    @FXML
    private void commitPerson() {
        if (formValid()) {
            selectPersonViewModel.getPerson().setFirstName(tfFirstName.getText().trim());
            selectPersonViewModel.getPerson().setLastName(tfLastName.getText().trim());
            selectPersonViewModel.getPerson().setAge(Integer.valueOf(tfAge.getText().trim()));
            selectPersonViewModel.getPerson().setEmail(tfEmail.getText().trim());
            selectPersonViewModel.getPerson().setJmbag(tfJMBAG.getText().trim());
            selectPersonViewModel.getPerson().setGender(((RadioButton)rbtns.getSelectedToggle()).getText());
            
            if (selectPersonViewModel.getPerson().getIDPerson() == 0) {
                listOfPeople.add(selectPersonViewModel);
            }else{
                try {
                    RepositoryFactory.getRepository().updatePerson(selectPersonViewModel.getPerson());
                    tvPeople.refresh();
                } catch (Exception ex) {
                    Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            selectPersonViewModel = null;
            tabListPeople.getSelectionModel().select(tabList);
            resetForm();
        }
    }

    @FXML
    private void upload() {
        File file = FileUtils.uploadFileDialog(tfAge.getScene().getWindow(),"jpg", "png", "jpeg");
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
            try {
                selectPersonViewModel.getPerson().setPicture(ImageUtils.imageToByteArray(image, ext));
                ivPicture.setImage(image);
            } catch (IOException ex) {
                Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void initValidation() {
        initValidationPerson();
        initValidationCourse();
        initValidationPosition();
    }

    private void initValidationPosition() {
        validationMapPosition = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfPositionTitle, lbErrorPositionTitle)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
    
    private void initValidationCourse() {
        validationMapCourse = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfCourseTitle, lbErrorCourseTitle),
                new AbstractMap.SimpleImmutableEntry<>(tfEcts, lbErrorEcts)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void initValidationPerson() {
        validationMapPerson = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfFirstName, lbErrorFName),
                new AbstractMap.SimpleImmutableEntry<>(tfLastName, lbErrorLName),
                new AbstractMap.SimpleImmutableEntry<>(tfAge, lbErrorAge),
                new AbstractMap.SimpleImmutableEntry<>(tfEmail, lbErrorEmail),
                new AbstractMap.SimpleImmutableEntry<>(tfJMBAG, lbErrorJmbag)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void initPeople() throws Exception {
        RepositoryFactory.getRepository().getPeople().forEach(person -> listOfPeople.add(new PersonViewModel(person)));
    }
    
    private void initCourses() throws Exception {
        RepositoryFactory.getRepository().getCourses().forEach(course -> listOfCourses.add(new CourseViewModel(course)));
    }
    
    private void initPositions() throws Exception {
        RepositoryFactory.getRepository().getPositions().forEach(position -> listOfPositions.add(new PositionViewModel(position)));
    }

    private void initTables() {
        initTablePeople();
        initTableCourses();
        initTablePositions();
    }
    
    private void initTablePositions() {
        tcPositionTitle.setCellValueFactory(position -> position.getValue().getTitleProperty());
        
        tvPositions.setItems(listOfPositions);
    }

    private void initTableCourses() {
        tcTourseTitle.setCellValueFactory(course -> course.getValue().getTitleProperty());
        tcEcts.setCellValueFactory(course -> course.getValue(). getEctsProperty().asObject());
        
        tvCourses.setItems(listOfCourses);
    }

    private void initTablePeople() {
        tcFirstName.setCellValueFactory(person -> person.getValue(). getFirstNameProperty());
        tcLastName.setCellValueFactory(person -> person.getValue(). getLastNameProperty());
        tcAge.setCellValueFactory(person -> person.getValue(). getAgeProperty().asObject());
        tcEmail.setCellValueFactory(person -> person.getValue(). getEmailProperty());
        tcGender.setCellValueFactory(person -> person.getValue(). getGenderProperty());
        tcJMBAG.setCellValueFactory(person -> person.getValue(). getJmbagProperty());
        
        tvPeople.setItems(listOfPeople);
    }

    

    private void setListeners() {
        tabListPeople.setOnMouseClicked(event ->{
            if (tabListPeople.getSelectionModel().getSelectedItem().equals(tabAddPerson)) {
                bindPerson(null);
            }else if(tabListPeople.getSelectionModel().getSelectedItem().equals(tabAddCourse)){
                bindCourse(null);
            }else if(tabListPeople.getSelectionModel().getSelectedItem().equals(tabAddPosition)){
                bindPosition(null);
            }
        });
        
        listOfPeople.addListener((ListChangeListener.Change<? extends PersonViewModel> change) -> {
            if (change.next()) {
                if (change.wasRemoved()) {
                    change.getRemoved().forEach(pvm -> {
                        try {
                            RepositoryFactory.getRepository().deletePerson(pvm.getPerson());
                        } catch (Exception ex) {
                            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }else if(change.wasAdded()){
                    change.getAddedSubList().forEach(pvm -> {
                        try {
                            int idPerson = RepositoryFactory.getRepository().addPerson(pvm.getPerson());
                            pvm.getPerson().setIDPerson(idPerson);
                        } catch (Exception ex) {
                            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                }
            }
        });
        
        listOfCourses.addListener((ListChangeListener.Change<? extends CourseViewModel> change) -> {
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
        
        listOfPositions.addListener((ListChangeListener.Change<? extends PositionViewModel> change) -> {
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

    private void bindPerson(PersonViewModel personViewModel) {
        selectPersonViewModel = personViewModel != null ? personViewModel : new PersonViewModel(null);
        tfFirstName.setText(selectPersonViewModel.getFirstNameProperty().get());
        tfLastName.setText(selectPersonViewModel.getLastNameProperty().get());
        tfAge.setText(String.valueOf(selectPersonViewModel.getAgeProperty().get()));
        tfJMBAG.setText(selectPersonViewModel.getJmbagProperty().get());
        tfLastName.setText(selectPersonViewModel.getLastNameProperty().get());
        tfEmail.setText(selectPersonViewModel.getEmailProperty().get());
        if (selectPersonViewModel.getIdProperty().get() != 0) {
            rbtns.getToggles().forEach(btn -> btn.setSelected(((RadioButton)btn).getText().equals(selectPersonViewModel.getGenderProperty().get())));
        }
    
        ivPicture.setImage(
            selectPersonViewModel.getPictureProperty().get() != null
                ? new Image(new ByteArrayInputStream(selectPersonViewModel.getPictureProperty().get()))
                : new Image(new File(DEFAULT_IMAGE_PATH).toURI().toString())
        );
    }
    
    private void resetForm(){
        validationMapPerson.values().forEach(l -> l.setVisible(false));
        lbErrorPicture.setVisible(false);
    }

    private boolean formValid() {
        final AtomicBoolean ok = new AtomicBoolean(true);
        validationMapPerson.keySet().forEach(tf -> {
            if (tf.getText().trim().isEmpty()
                    || tf.getId().contains("Email") && !ValidationUtils.isValidEmail(tf.getText().trim())) {
                ok.set(false);
                validationMapPerson.get(tf).setVisible(true);
            }else{
                validationMapPerson.get(tf).setVisible(false);
            }
        });
        if (selectPersonViewModel.getPictureProperty().get() == null) {
            lbErrorPicture.setVisible(true);
            ok.set(false);
        }else{
            lbErrorPicture.setVisible(false);
        }
        
        return ok.get();
    }

    @FXML
    private void editCourse(ActionEvent event) {
        if (tvCourses.getSelectionModel().getSelectedItem() != null) {
            bindCourse(tvCourses.getSelectionModel().getSelectedItem());
            tabListPeople.getSelectionModel().select(tabAddCourse);
        }
    }

    @FXML
    private void deleteCourse(ActionEvent event) {
        if (tvCourses.getSelectionModel().getSelectedItem() != null) {
            listOfCourses.remove(tvCourses.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void commitCourse(ActionEvent event) {
        if (formValid2()) {
            selectCourseViewModel.getCourse().setTitle(tfCourseTitle.getText().trim());
            selectCourseViewModel.getCourse().setEcts( Integer.valueOf(tfEcts.getText().trim()));
            
            if (selectCourseViewModel.getCourse().getIDCourse()== 0) {
                listOfCourses.add(selectCourseViewModel);
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
            resetForm2();
        }
    }

    private void bindCourse(CourseViewModel courseViewModel) {
        selectCourseViewModel = courseViewModel != null ? courseViewModel : new CourseViewModel(null);
        tfCourseTitle.setText(selectCourseViewModel.getTitleProperty().get());
        tfEcts.setText(String.valueOf(selectCourseViewModel.getEctsProperty().get()));
    }

    private boolean formValid2() {
        final AtomicBoolean ok = new AtomicBoolean(true);
        validationMapCourse.keySet().forEach(tf -> {
            if (tf.getText().trim().isEmpty()) {
                ok.set(false);
                validationMapCourse.get(tf).setVisible(true);
            }else{
                validationMapCourse.get(tf).setVisible(false);
            }
        });
        
        return ok.get();
    }

    private void resetForm2() {
        validationMapCourse.values().forEach(l -> l.setVisible(false));
    }

    @FXML
    private void editPosition(ActionEvent event) {
         if (tvPositions.getSelectionModel().getSelectedItem() != null) {
            bindPosition(tvPositions.getSelectionModel().getSelectedItem());
            tabListPeople.getSelectionModel().select(tabAddPosition);
        }
    }

    @FXML
    private void deletePosition(ActionEvent event) {
        if (tvPositions.getSelectionModel().getSelectedItem() != null) {
            listOfPositions.remove(tvPositions.getSelectionModel().getSelectedItem());
        }
    }

    @FXML
    private void commitPosition(ActionEvent event) {
        if (formValid3()) {
            selectPositionViewModel.getPosition().setTitle(tfPositionTitle.getText().trim());
            
            if (selectPositionViewModel.getPosition().getIDPosition()== 0) {
                listOfPositions.add(selectPositionViewModel);
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
            resetForm3();
        }
    }
    
    private void bindPosition(PositionViewModel positionViewModel) {
        selectPositionViewModel = positionViewModel != null ? positionViewModel : new PositionViewModel(null);
        tfPositionTitle.setText(selectPositionViewModel.getTitleProperty().get());
    }

    private boolean formValid3() {
        final AtomicBoolean ok = new AtomicBoolean(true);
        validationMapPosition.keySet().forEach(tf -> {
            if (tf.getText().trim().isEmpty()) {
                ok.set(false);
                validationMapPosition.get(tf).setVisible(true);
            }else{
                validationMapPosition.get(tf).setVisible(false);
            }
        });
        return ok.get();
    }

    private void resetForm3() {
        validationMapPosition.values().forEach(l -> l.setVisible(false));
    }

    @FXML
    private void addPersonToCourse(ActionEvent event) {
    }
}
