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

import com.oopclass.breadapp.models.Employee;
import com.oopclass.breadapp.services.impl.EmployeeService;
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
public class EmployeeController implements Initializable {

@FXML
private Label employeeId;

@FXML
private TextField name;

@FXML
private TextField contactNumber;

@FXML
private DatePicker dob;

@FXML
private Timestamp createdAt;

@FXML
private Timestamp updatedAt;

@FXML
private ToggleGroup gender;

@FXML
private RadioButton rbFemale;

@FXML
private RadioButton rbMale;

@FXML
private Button reset;

@FXML
private Button saveEmployee;

@FXML
private Button deleteEmployees;

@FXML
private TableView<Employee> employeeTable;

@FXML
private TableColumn<Employee, Long> colEmployeeId;

@FXML
private TableColumn<Employee, String> colName;

@FXML
private TableColumn<Employee, String> colContactNumber;

@FXML
private TableColumn<Employee, LocalDate> colDOB;

@FXML
private TableColumn<Employee, Timestamp> colCreatedAt;

@FXML
private TableColumn<Employee, Timestamp> colUpdatedAt;

@FXML
private TableColumn<Employee, String> colGender;

@FXML
private TableColumn<Employee, Boolean> colEdit;

@Lazy
@Autowired
private StageManager stageManager;

@Autowired
private EmployeeService employeeService;

private ObservableList<Employee> employeeList = FXCollections.observableArrayList();

//    @FXML
//    private void exit(ActionEvent event) {
//        Platform.exit();
//    }

@FXML
void reset(ActionEvent event) {
    clearFields();
}

@FXML
private void saveEmployee(ActionEvent event) {

    if (validate("Name", getName(), "([a-zA-Z]{3,30}\\s*)+")
            && validate("Contact Number", getContactNumber(), "([0-9]{11}\\s*)+")
            && emptyValidation("DOB", dob.getEditor().getText().isEmpty())) {

        if (employeeId.getText() == null || "".equals(employeeId.getText())) {
            if (true) {

                Employee employee = new Employee();
                employee.setName(getName());
                employee.setContactNumber(getContactNumber());
                employee.setDob(getDob());
                employee.setUpdatedAt(getUpdatedAt());
                employee.setGender(getGender());

                Employee newEmployee = employeeService.save(employee);

                saveAlert(newEmployee);
            }

        } else {
            Employee employee = employeeService.find(Long.parseLong(employeeId.getText()));
            employee.setName(getName());
            employee.setContactNumber(getContactNumber());
            employee.setDob(getDob());
            employee.setGender(getGender());
            employee.setUpdatedAt(getUpdatedAt());
            Employee updatedEmployee = employeeService.update(employee);
            updateAlert(updatedEmployee);
        }

        clearFields();
        loadEmployeeDetails();
    }

}

@FXML
private void deleteEmployees(ActionEvent event) {
    List<Employee> employees = employeeTable.getSelectionModel().getSelectedItems();

    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmation Dialog");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to delete selected?");
    Optional<ButtonType> action = alert.showAndWait();

    if (action.get() == ButtonType.OK) {
        employeeService.deleteInBatch(employees);
    }

    loadEmployeeDetails();
}

@FXML
private void changeClient(ActionEvent event) {
    stageManager.switchScene(FxmlView.CLIENT);
};

@FXML
private void changeAppointment(ActionEvent event) {
    stageManager.switchScene(FxmlView.APPOINTMENT);
};

private void clearFields() {
    employeeId.setText(null);
    name.clear();
    contactNumber.clear();
    dob.getEditor().clear();
    rbMale.setSelected(true);
    rbFemale.setSelected(false);
}

private void saveAlert(Employee employee) {

    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Employee saved successfully.");
    alert.setHeaderText(null);
    alert.setContentText("");
    alert.showAndWait();
}

private void updateAlert(Employee employee) {

    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Employee updated successfully.");
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

    public String getContactNumber() {
        return contactNumber.getText();
    }

public LocalDate getDob() {
    return dob.getValue();
}

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

public String getGender() {
    return rbMale.isSelected() ? "Male" : "Female";
}


/*
     *  Set All employeeTable column properties
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

    colEmployeeId.setCellValueFactory(new PropertyValueFactory<>("id"));
    colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    colContactNumber.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
    colDOB.setCellValueFactory(new PropertyValueFactory<>("dob"));
    colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
    colUpdatedAt.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
    colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
    colEdit.setCellFactory(cellFactory);
}

Callback<TableColumn<Employee, Boolean>, TableCell<Employee, Boolean>> cellFactory
        = new Callback<TableColumn<Employee, Boolean>, TableCell<Employee, Boolean>>() {
    @Override
    public TableCell<Employee, Boolean> call(final TableColumn<Employee, Boolean> param) {
        final TableCell<Employee, Boolean> cell = new TableCell<Employee, Boolean>() {
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
                        Employee employee = getTableView().getItems().get(getIndex());
                        updateEmployee(employee);
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

            private void updateEmployee(Employee employee) {
                employeeId.setText(Long.toString(employee.getId()));
                name.setText(employee.getName());
                contactNumber.setText(employee.getContactNumber());
                dob.setValue(employee.getDob());
                if (employee.getGender().equals("Male")) {
                    rbMale.setSelected(true);
                } else {
                    rbFemale.setSelected(true);
                }
            }
        };
        return cell;
    }
};

/*
     *  Add All employees to observable list and update table
 */
private void loadEmployeeDetails() {
    employeeList.clear();
    employeeList.addAll(employeeService.findAll());

    employeeTable.setItems(employeeList);
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

        employeeTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();

        // Add all employees into table
        loadEmployeeDetails();
    }
}
