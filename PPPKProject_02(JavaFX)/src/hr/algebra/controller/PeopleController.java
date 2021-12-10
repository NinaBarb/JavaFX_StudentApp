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
import hr.algebra.viewmodel.PersonViewModel;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author Nina
 */
public class PeopleController implements Initializable {

    private Map<TextField, Label> validationMap;
    
    private final ObservableList<PersonViewModel> list = FXCollections.observableArrayList();
    
    private PersonViewModel selectPersonViewModel;
    private final String DEFAULT_IMAGE_PATH = "src/assets/no_image.png";
    
    @FXML
    private TabPane tabListPeople;
    @FXML
    private Tab tabList,tabAddPerson;
    @FXML
    private TableView<PersonViewModel> tvPeople;
    @FXML
    private TableColumn<PersonViewModel, String> tcFirstName,tcLastName,tcEmail,tcJMBAG,tcGender;
    @FXML
    private TableColumn<PersonViewModel, Integer> tcAge;
    @FXML
    private TextField tfFirstName,tfLastName, tfAge, tfJMBAG, tfEmail;
    @FXML
    private ToggleGroup rbtns;
    @FXML
    private ImageView ivPicture;
    @FXML
    private Label lbErrorFName,lbErrorLName, lbErrorAge,lbErrorJmbag,lbErrorEmail,lbErrorPicture;
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
            initPeople();
            initTable();
            MaskUtils.addIntegerMask(tfAge);
            MaskUtils.addIntegerMask(tfJMBAG);
            MaskUtils.addTextLimiter(tfJMBAG, 10);
            setListeners();
        } catch (Exception ex) {
            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            list.remove(tvPeople.getSelectionModel().getSelectedItem());
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
                list.add(selectPersonViewModel);
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
        validationMap = Stream.of(
                new AbstractMap.SimpleImmutableEntry<>(tfFirstName, lbErrorFName),
                new AbstractMap.SimpleImmutableEntry<>(tfLastName, lbErrorLName),
                new AbstractMap.SimpleImmutableEntry<>(tfAge, lbErrorAge),
                new AbstractMap.SimpleImmutableEntry<>(tfEmail, lbErrorEmail),
                new AbstractMap.SimpleImmutableEntry<>(tfJMBAG, lbErrorJmbag)
        ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private void initPeople() throws Exception {
        RepositoryFactory.getRepository().getPeople().forEach(person -> list.add(new PersonViewModel(person)));
    }

    private void initTable() {
        tcFirstName.setCellValueFactory(person -> person.getValue(). getFirstNameProperty());
        tcLastName.setCellValueFactory(person -> person.getValue(). getLastNameProperty());
        tcAge.setCellValueFactory(person -> person.getValue(). getAgeProperty().asObject());
        tcEmail.setCellValueFactory(person -> person.getValue(). getEmailProperty());
        tcGender.setCellValueFactory(person -> person.getValue(). getGenderProperty());
        tcJMBAG.setCellValueFactory(person -> person.getValue(). getJmbagProperty());
        
        tvPeople.setItems(list);
    }
  
    private void setListeners() {
        tabListPeople.setOnMouseClicked(event ->{
            if (tabListPeople.getSelectionModel().getSelectedItem().equals(tabAddPerson)) {
                bindPerson(null);
            }
        });
        
        list.addListener((ListChangeListener.Change<? extends PersonViewModel> change) -> {
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
        validationMap.values().forEach(l -> l.setVisible(false));
        lbErrorPicture.setVisible(false);
    }

    private boolean formValid() {
        final AtomicBoolean ok = new AtomicBoolean(true);
        validationMap.keySet().forEach(tf -> {
            if (tf.getText().trim().isEmpty()
                    || tf.getId().contains("Email") && !ValidationUtils.isValidEmail(tf.getText().trim())) {
                ok.set(false);
                validationMap.get(tf).setVisible(true);
            }else{
                validationMap.get(tf).setVisible(false);
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
    private void goToCourses(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/hr/algebra/view/Courses.fxml"));
            Scene scene = tvPeople.getScene();
            
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
            Logger.getLogger(PeopleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
