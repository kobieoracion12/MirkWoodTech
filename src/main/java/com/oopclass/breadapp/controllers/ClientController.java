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

import com.oopclass.breadapp.models.Client;
import com.oopclass.breadapp.services.impl.ClientService;
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
public class ClientController implements Initializable {

@FXML
private Label clientId;

@FXML
private TextField firstName;

@FXML
private TextField lastName;

@FXML
private TextField services;

@FXML
private TextField cost;


@FXML
private Timestamp createdAt;

@FXML
private Timestamp updatedAt;

@FXML
private Button reset;

@FXML
private Button saveClient;

@FXML
private Button deleteClients;

@FXML
private TableView<Client> clientTable;

@FXML
private TableColumn<Client, Long> colClientId;

@FXML
private TableColumn<Client, String> colFirstName;

@FXML
private TableColumn<Client, String> colServices;

@FXML
private TableColumn<Client, String> colCost;

@FXML
private TableColumn<Client, String> colLastName;

@FXML
private TableColumn<Client, Timestamp> colCreatedAt;

@FXML
private TableColumn<Client, Timestamp> colUpdatedAt;

@FXML
private TableColumn<Client, Boolean> colEdit;

@Lazy
@Autowired
private StageManager stageManager;

@Autowired
private ClientService clientService;

private ObservableList<Client> clientList = FXCollections.observableArrayList();

//    @FXML
//    private void exit(ActionEvent event) {
//        Platform.exit();
//    }

@FXML
void reset(ActionEvent event) {
    clearFields();
}

@FXML
private void saveClient(ActionEvent event) {

    if (validate("First Name", getFirstName(), "([a-zA-Z]{3,30}\\s*)+")
            && validate("Last Name", getLastName(), "([a-zA-Z]{3,30}\\s*)+")
            && validate("Services", getServices(), "([a-zA-Z]{3,30}\\s*)+")
            && validate("Cost", getCost(), "([0-9.,]\\s*)+")) {

        if (clientId.getText() == null || "".equals(clientId.getText())) {
            if (true) {

                Client client = new Client();
                client.setFirstName(getFirstName());
                client.setLastName(getLastName());
                client.setServices(getServices());
                client.setCost(getCost());
                client.setUpdatedAt(getUpdatedAt());

                Client newClient = clientService.save(client);

                saveAlert(newClient);
            }

        } else {
            Client client = clientService.find(Long.parseLong(clientId.getText()));
            client.setFirstName(getFirstName());
            client.setLastName(getLastName());
            client.setServices(getServices());
            client.setCost(getCost());
            client.setUpdatedAt(getUpdatedAt());
            Client updatedClient = clientService.update(client);
            updateAlert(updatedClient);
        }

        clearFields();
        loadClientDetails();
    }

}

@FXML
private void deleteClients(ActionEvent event) {
    List<Client> clients = clientTable.getSelectionModel().getSelectedItems();

    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Confirmation Dialog");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure you want to delete selected?");
    Optional<ButtonType> action = alert.showAndWait();

    if (action.get() == ButtonType.OK) {
        clientService.deleteInBatch(clients);
    }

    loadClientDetails();
}
@FXML
private void changeAppointment(ActionEvent event) {
    stageManager.switchScene(FxmlView.APPOINTMENT);
};

@FXML
private void changeEmployee(ActionEvent event) {
    stageManager.switchScene(FxmlView.EMPLOYEE);
};

private void clearFields() {
    clientId.setText(null);
    firstName.clear();
    lastName.clear();
    services.clear();
    cost.clear();
}

private void saveAlert(Client client) {

    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Client saved successfully.");
    alert.setHeaderText(null);
    alert.setContentText("The client " + client.getFirstName() + " " + client.getLastName() + " has been created and \n" + client.getId() + ".");
    alert.showAndWait();
}

private void updateAlert(Client client) {

    Alert alert = new Alert(AlertType.INFORMATION);
    alert.setTitle("Client updated successfully.");
    alert.setHeaderText(null);
    alert.setContentText("The client " + client.getFirstName() + " " + client.getLastName() + " has been updated.");
    alert.showAndWait();
}

    public String getFirstName() {
        return firstName.getText();
    }

    public String getLastName() {
        return lastName.getText();
    }

    public String getServices() {
        return services.getText();
    }

    public String getCost() {
        return cost.getText();
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

/*
     *  Set All clientTable column properties
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

    colClientId.setCellValueFactory(new PropertyValueFactory<>("id"));
    colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    colServices.setCellValueFactory(new PropertyValueFactory<>("services"));
    colCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
    colCreatedAt.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
    colUpdatedAt.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
    colEdit.setCellFactory(cellFactory);
}

Callback<TableColumn<Client, Boolean>, TableCell<Client, Boolean>> cellFactory
        = new Callback<TableColumn<Client, Boolean>, TableCell<Client, Boolean>>() {
    @Override
    public TableCell<Client, Boolean> call(final TableColumn<Client, Boolean> param) {
        final TableCell<Client, Boolean> cell = new TableCell<Client, Boolean>() {
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
                        Client client = getTableView().getItems().get(getIndex());
                        updateClient(client);
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

            private void updateClient(Client client) {
                clientId.setText(Long.toString(client.getId()));
                firstName.setText(client.getFirstName());
                lastName.setText(client.getLastName());
                services.setText(client.getServices());
                cost.setText(client.getCost());
            }
        };
        return cell;
    }
};

/*
     *  Add All clients to observable list and update table
 */
private void loadClientDetails() {
    clientList.clear();
    clientList.addAll(clientService.findAll());

    clientTable.setItems(clientList);
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

        clientTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        setColumnProperties();

        // Add all clients into table
        loadClientDetails();
    }
}
