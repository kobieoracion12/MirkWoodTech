package com.oopclass.breadapp.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.oopclass.breadapp.config.StageManager;

import com.oopclass.breadapp.models.Appointment;
import com.oopclass.breadapp.services.impl.AppointmentService;
import com.oopclass.breadapp.views.FxmlView;

//import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * OOP Class 20-21
 *
 * @author Gerald Villaran
 */
@Controller
public class AppointmentController implements Initializable {

@FXML
private Label appointmentId;

@FXML
private TextField name;

@FXML
private TextField services;

@FXML
private TextField cost;

@FXML
private DatePicker sched;

@FXML
private Timestamp createdAt;

@FXML
private Timestamp updatedAt;

@FXML
private Button reset;

@FXML
private Button saveAppointment;

@FXML
private Button deleteAppointments;

@FXML
private TableView<Appointment> appointmentTable;

@FXML
private TableColumn<Appointment, Long> colAppointmentId;

@FXML
private TableColumn<Appointment, String> colName;

@FXML
private TableColumn<Appointment, String> colServices;

@FXML
private TableColumn<Appointment, LocalDate> colSched;

@FXML
private TableColumn<Appointment, LocalDate> colCost;

@FXML
private TableColumn<Appointment, Timestamp> colCreatedAt;

@FXML
private TableColumn<Appointment, Timestamp> colUpdatedAt;

@FXML
private TableColumn<Appointment, Boolean> colEdit;

@Lazy
@Autowired
private StageManager stageManager;

@Autowired
private AppointmentService appointmentService;

private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

//    @FXML
//    private void exit(ActionEvent event) {
//        Platform.exit();
//    }

@FXML
void reset(ActionEvent event) {
    clearFields();
}

@FXML
private void saveAppointment(ActionEvent event) {

    if (validate("Name", getName(), "([a-zA-Z]{3,30}\\s*)+")
            && validate("Services", getServices(), "([a-zA-Z]{3,30}\\s*)+")
            && validate("Cost", getCost(), "([0-9.,]\\s*)+")
            && emptyValidation("Schedule", sched.getEditor().getText().isEmpty())) {

        if (appointmentId.getText() == null || "".equals(appointmentId.getText())) {
            if (true) {

                Appointment appointment = new Appointment();
                appointment.setName(getName());
                appointment.setServices(getServices());
                appointment.setCost(getCost());
                appointment.setSched(getSched());
                appointment.setUpdatedAt(getUpdatedAt());

                Appointment newAppointment = appointmentService.save(appointment);

                saveAlert(newAppointment);
            }

        } else {
            Appointment appointment = appointmentService.find(Long.parseLong(appointmentId.getText()));
            appointment.setName(getName());
            appointment.setServices(getServices());
            appointment.setCost(getCost());
            appointment.setSched(getSched());
            appointment.setUpdatedAt(getUpdatedAt());
            Appointment updatedAppointment = appointmentService.update(appointment);
            updateAlert(updatedAppointment);
        }

        clearFields();
        loadAppointmentDetails();
    }

}

@FXML
private void deleteAppointments(ActionEvent event) {
    List<Appointment> appointments = appointmentTable.getSelectionModel().getSelectedItems();

    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmation Dialog");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to delete selected?");
    Optional<ButtonType> action = alert.showAndWait();

    if (action.get() == ButtonType.OK) {
        appointmentService.deleteInBatch(appointments);
    }

    loadAppointmentDetails();
}
@FXML
private void changeClient(ActionEvent event) {
    stageManager.switchScene(FxmlView.CLIENT);
};
@FXML
private void changeEmployee(ActionEvent event) {
    stageManager.switchScene(FxmlView.EMPLOYEE);
};

private void clearFields() {
    appointmentId.setText(null);
    name.clear();
    services.clear();
    cost.clear();
    sched.getEditor().clear();
}

private void saveAlert(Appointment appointment) {

    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Appointment saved successfully.");
    alert.setHeaderText(null);
    alert.setContentText("");
    alert.showAndWait();
}

private void updateAlert(Appointment appointment) {

    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Appointment updated successfully.");
    alert.setHeaderText(null);
    alert.setContentText("");
    alert.showAndWait();
}

private String getGenderTitle(String gender) {
    return (gender.equals("Male")) ? "his" : "her";
}

public String getName() {
    return name.getText();
}

public String getServices() {
    return services.getText();
}

public String getCost() {
    return cost.getText();
}

public LocalDate getSched() {
    return sched.getValue();
}

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }


/*
     *  Set All appointmentTable column properties
 */
private void setColumnProperties() {
    /* Override date format in table
             * colDOB.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDate>() {
                     String pattern = "dd/MM/yyyy";
                     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
                 @Override 
                 public String toString(LocalDate date) {
                     if (date != null) {
                         return dateFormatter.format(date);
                     } else {
                         return "";
                     }
                 }

                 @Override 
                 public LocalDate fromString(String string) {
                     if (string != null && !string.isEmpty()) {
                         return LocalDate.parse(string, dateFormatter);
                     } else {
                         return null;
                     }
                 }
             }));*/

    colAppointmentId.setCellValueFactory(new PropertyValueFactory<>("id"));
    colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    colServices.setCellValueFactory(new PropertyValueFactory<>("services"));
    colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
    colSched.setCellValueFactory(new PropertyValueFactory<>("sched"));
    colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
    colUpdatedAt.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
    colEdit.setCellFactory(cellFactory);
}

Callback<TableColumn<Appointment, Boolean>, TableCell<Appointment, Boolean>> cellFactory
        = new Callback<TableColumn<Appointment, Boolean>, TableCell<Appointment, Boolean>>() {
    @Override
    public TableCell<Appointment, Boolean> call(final TableColumn<Appointment, Boolean> param) {
        final TableCell<Appointment, Boolean> cell = new TableCell<Appointment, Boolean>() {
            Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
            final Button btnEdit = new Button();

            @Override
            public void updateItem(Boolean check, boolean empty) {
                super.updateItem(check, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    btnEdit.setOnAction(e -> {
                        Appointment appointment = getTableView().getItems().get(getIndex());
                        updateAppointment(appointment);
                    });

                    btnEdit.setStyle("-fx-background-color: transparent;");
                    ImageView iv = new ImageView();
                    iv.setImage(imgEdit);
                    iv.setPreserveRatio(true);
                    iv.setSmooth(true);
                    iv.setCache(true);
                    btnEdit.setGraphic(iv);

                    setGraphic(btnEdit);
                    setAlignment(Pos.CENTER);
                    setText(null);
                }
            }

            private void updateAppointment(Appointment appointment) {
                appointmentId.setText(Long.toString(appointment.getId()));
                name.setText(appointment.getName());
                services.setText(appointment.getServices());
                cost.setText(appointment.getCost());
                sched.setValue(appointment.getSched());
            }
        };
        return cell;
    }
};

/*
     *  Add All appointments to observable list and update table
 */
private void loadAppointmentDetails() {
    appointmentList.clear();
    appointmentList.addAll(appointmentService.findAll());

    appointmentTable.setItems(appointmentList);
}

/*
     * Validations
 */
private boolean validate(String field, String value, String pattern) {
    if (!value.isEmpty()) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(value);
        if (m.find() && m.group().equals(value)) {
            return true;
        } else {
            validationAlert(field, false);
            return false;
        }
    } else {
        validationAlert(field, true);
        return false;
    }
}

private boolean emptyValidation(String field, boolean empty) {
    if (!empty) {
        return true;
    } else {
        validationAlert(field, true);
        return false;
    }
}

private void validationAlert(String field, boolean empty) {
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle("Validation Error");
    alert.setHeaderText(null);
    if (field.equals("Role")) {
        alert.setContentText("Please Select " + field);
    } else {
        if (empty) {
            alert.setContentText("Please Enter " + field);
        } else {
            alert.setContentText("Please Enter Valid " + field);
        }
    }
    alert.showAndWait();
}
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        appointmentTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();

        // Add all appointments into table
        loadAppointmentDetails();
    }
}
