package com.gui.hotel_management_javafx;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class ClientsWindowController {
    @FXML
    private TableColumn<Client, Integer> clientIDCol;
    @FXML
    private TableColumn<Client, String> clientFirstNameCol;
    @FXML
    private TableColumn<Client, String> clientLastNameCol;
    @FXML
    private TableColumn<Client, String> clientPhoneCol;
    @FXML
    private TableColumn <Client, String> clientEmailCol;
    @FXML
    private TableView<Client> clientTable;
    @FXML
    private TextField textFieldID;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFieldPhoneNumber;
    @FXML
    private TextField textFieldEmail;

    Client client = new Client();

    public TableView<Client> getClientTable() {
        return clientTable;
    }

    public TableColumn<Client, Integer> getClientIDCol() {
        return clientIDCol;
    }

    public void setClientIDCol(TableColumn<Client, Integer> clientIDCol) {
        this.clientIDCol = clientIDCol;
    }

    public TableColumn<Client, String> getClientFirstNameCol() {
        return clientFirstNameCol;
    }

    public void setClientFirstNameCol(TableColumn<Client, String> clientFirstNameCol) {
        this.clientFirstNameCol = clientFirstNameCol;
    }

    public TableColumn<Client, String> getClientLastNameCol() {
        return clientLastNameCol;
    }

    public void setClientLastNameCol(TableColumn<Client, String> clientLastNameCol) {
        this.clientLastNameCol = clientLastNameCol;
    }

    public TableColumn<Client, String> getClientPhoneCol() {
        return clientPhoneCol;
    }

    public void setClientPhoneCol(TableColumn<Client, String> clientPhoneCol) {
        this.clientPhoneCol = clientPhoneCol;
    }

    public TableColumn<Client, String> getClientEmailCol() {
        return clientEmailCol;
    }

    public void setClientEmailCol(TableColumn<Client, String> clientEmailCol) {
        this.clientEmailCol = clientEmailCol;
    }

    public void setClientTable(TableView<Client> clientTable) {
        this.clientTable = clientTable;
    }

    public TextField getTextFieldID() {
        return textFieldID;
    }

    public void setTextFieldID(TextField textFieldID) {
        this.textFieldID = textFieldID;
    }

    public TextField getTextFieldFirstName() {
        return textFieldFirstName;
    }

    public void setTextFieldFirstName(TextField textFieldFirstName) {
        this.textFieldFirstName = textFieldFirstName;
    }

    public TextField getTextFieldLastName() {
        return textFieldLastName;
    }

    public void setTextFieldLastName(TextField textFieldLastName) {
        this.textFieldLastName = textFieldLastName;
    }

    public TextField getTextFieldPhoneNumber() {
        return textFieldPhoneNumber;
    }

    public void setTextFieldPhoneNumber(TextField textFieldPhoneNumber) {
        this.textFieldPhoneNumber = textFieldPhoneNumber;
    }

    public TextField getTextFieldEmail() {
        return textFieldEmail;
    }

    public void setTextFieldEmail(TextField textFieldEmail) {
        this.textFieldEmail = textFieldEmail;
    }

    //Populate the table with all existing clients in the database
    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {

        clientIDCol.setCellValueFactory(cellData -> cellData.getValue().getClientIDProp().asObject());
        clientFirstNameCol.setCellValueFactory(cellData -> cellData.getValue().getClientFirstNameProp());
        clientLastNameCol.setCellValueFactory(cellData -> cellData.getValue().getClientLastNameProp());
        clientPhoneCol.setCellValueFactory(cellData -> cellData.getValue().getClientPhoneNumberProp());
        clientEmailCol.setCellValueFactory(cellData -> cellData.getValue().getClientEmailProp());

        ObservableList<Client> fullList = Client.fillClientTable();
        populateTable(fullList);
    }
    public void populateTable(ObservableList<Client> fullList) {
        clientTable.setItems(fullList);
    }

    //Add new client
    @FXML
    public void addNewClientButtonAction() {

            //Collect data from the fields
            String fName = textFieldFirstName.getText();
            String lName = textFieldLastName.getText();
            String phone = textFieldPhoneNumber.getText();
            String email = textFieldEmail.getText();

            if(fName.trim().equals("") || lName.trim().equals("") || phone.trim().equals("")){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Please enter all required fields");
                alert.setContentText("Client's First/Last name and Phone number are required");
                alert.showAndWait();
            }else {
                if (client.addClient(fName, lName, phone, email)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("New Client Added Successfully");
                    alert.setContentText("Please Refresh the Table");
                    alert.showAndWait();
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("New Client Not Added");
                    alert.setContentText("Add Client Error");
                    alert.showAndWait();
                }
            }
    }

    //Display the selected row's data in the TextFields
        @FXML
        public void onSelect() {
        // check the table's selected item and get selected item
        if (clientTable.getSelectionModel().getSelectedItem() != null) {
            Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
            textFieldID.setText(String.valueOf(selectedClient.getClientID()));
            textFieldFirstName.setText(selectedClient.getClientFirstName());
            textFieldLastName.setText(selectedClient.getClientLastName());
            textFieldPhoneNumber.setText(selectedClient.getClientPhoneNumber());
            textFieldEmail.setText(selectedClient.getClientEmail());
        }
    }

    //Edit client
    @FXML
    public void editClientButtonAction() {
        //Collect data from the fields
        int id = 0;
        String fName = textFieldFirstName.getText();
        String lName = textFieldLastName.getText();
        String phone = textFieldPhoneNumber.getText();
        String email = textFieldEmail.getText();

        if(fName.trim().equals("") || lName.trim().equals("") || phone.trim().equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Please enter all required fields");
            alert.setContentText("Client's First/Last name and Phone number are required");
            alert.showAndWait();
        }else {

            try{
                id = Integer.parseInt(textFieldID.getText());

                if (client.editClient(id, fName, lName, phone, email)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Client Updated Successfully");
                    alert.setContentText("Please Refresh the Table");
                    alert.showAndWait();
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("Client Not Updated");
                    alert.setContentText("Update Client Error");
                    alert.showAndWait();
                }
            }catch (NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(ex.getMessage() + " - Please enter all required fields");
                alert.setContentText("Please check if client ID is entered");
                alert.showAndWait();
            }
        }
    }

    //Remove Client
    @FXML
    public void removeClientButtonAction() {

        try{
            int id = Integer.parseInt(textFieldID.getText());

            if (client.removeClient(id)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Client Deleted Successfully");
                alert.setContentText("Please Refresh the Table");
                alert.showAndWait();
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Client Not Deleted");
                alert.setContentText("Delete Client Error");
                alert.showAndWait();
            }
        }catch (NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(ex.getMessage() + " - Please enter all required fields");
            alert.setContentText("Please check if client ID is entered");
            alert.showAndWait();
        }
    }

    //Clear all text fields
    @FXML
    public void clearButtonAction() {
        textFieldID.setText("");
        textFieldFirstName.setText("");
        textFieldLastName.setText("");
        textFieldPhoneNumber.setText("");
        textFieldEmail.setText("");
    }

    //Refresh the table
    @FXML
    public void refreshButtonAction() throws SQLException, ClassNotFoundException {
        initialize();

    }
}
