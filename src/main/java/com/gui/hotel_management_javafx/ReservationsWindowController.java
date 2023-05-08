package com.gui.hotel_management_javafx;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.time.LocalDate;

public class ReservationsWindowController {

    @FXML
    private TextField textFieldReservationID;
    @FXML
    private TextField textFieldClientID;
    @FXML
    private TextField textFieldRoomNumber;
    @FXML
    private DatePicker datePickerDateIn;
    @FXML
    private DatePicker datePickerDateOut;
    @FXML
    private TableColumn<Reservation, Integer> reservationIdCol;
    @FXML
    private TableColumn<Reservation, Integer> resClientIdCol;
    @FXML
    private TableColumn<Reservation, Integer> resRoomNumberCol;
    @FXML
    private TableColumn<Reservation, String> dateInCol;
    @FXML
    private TableColumn<Reservation, String> dateOutCol;
    @FXML
    private TableView<Reservation> reservationsTable;

    Reservation reservation = new Reservation();

    Room room = new Room();

    //Populate the table with all existing reservations in the database
    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {

        reservationIdCol.setCellValueFactory(cellData -> cellData.getValue().reservationIdProperty().asObject());
        resClientIdCol.setCellValueFactory(cellData -> cellData.getValue().clientIdProperty().asObject());
        resRoomNumberCol.setCellValueFactory(cellData -> cellData.getValue().roomNumberProperty().asObject());
        dateInCol.setCellValueFactory(cellData -> cellData.getValue().dateInProperty());
        dateOutCol.setCellValueFactory(cellData -> cellData.getValue().dateOutProperty());

        ObservableList<Reservation> fullList = Reservation.fillReservationTable();
        populateTable(fullList);
    }
    public void populateTable(ObservableList<Reservation> fullList) {
        reservationsTable.setItems(fullList);
    }

    //Add new reservation
    @FXML
    public void addReservationButtonAction() {

        try {
        if((datePickerDateIn.getValue() != null) && (datePickerDateOut.getValue() != null) && (!textFieldClientID.getText().equals("")) && (!textFieldRoomNumber.getText().equals(""))){

            //Collect data from the fields
            int clientId = Integer.parseInt(textFieldClientID.getText());
            int roomNumber = Integer.parseInt(textFieldRoomNumber.getText());
            String dateIn = datePickerDateIn.getValue().toString();
            String dateOut = datePickerDateOut.getValue().toString();

            if ((datePickerDateIn.getValue().isAfter(LocalDate.now()) || datePickerDateIn.getValue().isEqual(LocalDate.now())) && (datePickerDateOut.getValue().isAfter(datePickerDateIn.getValue()))){
                if (reservation.addReservation(clientId, roomNumber, dateIn, dateOut)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("New Reservation Added Successfully");
                    alert.setContentText("Please Refresh the Table");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("New Reservation Not Added");
                    alert.setContentText("Add Reservation Error");
                    alert.showAndWait();
                    }
                }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Date In - Date Out error");
                alert.setContentText("Please check Date In and Date Out");
                alert.showAndWait();
                    }
            }else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("New Reservation Not Added");
            alert.setContentText("Please enter all fields correctly");
            alert.showAndWait();
        }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(e.getMessage() + " - Please enter all required fields");
            alert.setContentText("Please check and try again");
            alert.showAndWait();
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //Display the selected row's data in the TextFields
    @FXML
    public void onSelect() {
        // check the table's selected item and get selected item
        if (reservationsTable.getSelectionModel().getSelectedItem() != null) {
            Reservation selectedReservation = reservationsTable.getSelectionModel().getSelectedItem();

            textFieldReservationID.setText(String.valueOf(selectedReservation.getReservationId()));
            textFieldClientID.setText(String.valueOf(selectedReservation.getClientId()));
            textFieldRoomNumber.setText(String.valueOf(selectedReservation.getRoomNumber()));
            datePickerDateIn.setValue(LocalDate.parse(selectedReservation.getDateIn()));
            datePickerDateOut.setValue(LocalDate.parse(selectedReservation.getDateOut()));
        }
    }

    //Edit client
    @FXML
    public void editReservationButtonAction() {
        //Collect data from the fields
        try{
        int id = 0;
        int reservationId = Integer.parseInt(textFieldReservationID.getText());
        int clientId = Integer.parseInt(textFieldClientID.getText());
        int roomNumber = Integer.parseInt(textFieldRoomNumber.getText());
        String dateIn = datePickerDateIn.getValue().toString();
        String dateOut = datePickerDateOut.getValue().toString();

        if(reservationId == 0 || (textFieldReservationID.getText() == null) || (textFieldClientID.getText() == null) || dateIn.trim().equals("") || dateOut.trim().equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Please enter all required fields");
            alert.setContentText("Reservation Number, Client ID, Room Number and Date In/Out are required");
            alert.showAndWait();
        }else {
            id = Integer.parseInt(textFieldReservationID.getText());
            if ((datePickerDateIn.getValue().isAfter(LocalDate.now()) || datePickerDateIn.getValue().isEqual(LocalDate.now())) && (datePickerDateOut.getValue().isAfter(datePickerDateIn.getValue()))) {
                if (reservation.editReservation(id, clientId, roomNumber, dateIn, dateOut)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Reservation Updated Successfully");
                    alert.setContentText("Please Refresh the Table");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("Reservation Not Updated");
                    alert.setContentText("Update Reservation Error");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Date In - Date Out error");
                alert.setContentText("Please check Date In and Date Out");
                alert.showAndWait();
            }
        }
            }catch (NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(ex.getMessage() + " - Please enter all required fields");
                alert.setContentText("Please check and try again");
                alert.showAndWait();
            }
        }

    //Remove Reservation
    @FXML
    public void removeReservationButtonAction() {
        try{
            int id = Integer.parseInt(textFieldReservationID.getText());

            if (reservation.removeReservation(id)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Reservation Deleted Successfully");
                alert.setContentText("Please Refresh the Table");
                alert.showAndWait();
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Reservation Not Deleted");
                alert.setContentText("Delete Reservation Error");
                alert.showAndWait();
            }

        }catch (NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(ex.getMessage() + " - Please enter all required fields");
            alert.setContentText("Please check if Reservation ID is entered");
            alert.showAndWait();
        }
    }

    //Clear all text fields
    @FXML
    public void clearReservationAction() {
        textFieldReservationID.setText("");
        textFieldClientID.setText("");
        textFieldRoomNumber.setText("");
        datePickerDateIn.setValue(null);
        datePickerDateOut.setValue(null);
    }

    //Refresh the table
    @FXML
    public void refreshButtonAction() throws SQLException, ClassNotFoundException {
        initialize();

    }
}
