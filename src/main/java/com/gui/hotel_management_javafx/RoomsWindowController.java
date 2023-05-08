package com.gui.hotel_management_javafx;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;

public class RoomsWindowController {

    @FXML
    private TableColumn<Room, Integer> roomNumberCol;
    @FXML
    private TableColumn<Room, Integer> roomTypeCol;
    @FXML
    private TableColumn<Room, String> roomPhoneCol;
    @FXML
    private TableColumn<Room, String> reservation;
    @FXML
    private TableView<Room> roomTable;
    @FXML
    private TextField textFieldRoomNumber;
    @FXML
    private ComboBox comboBoxRoomType;
    @FXML
    private TextField textFieldRoomPhoneNumber;
    @FXML
    private RadioButton radioButtonYes;
    @FXML
    private RadioButton radioButtonNo;

    ToggleGroup buttonGroup = new ToggleGroup();

    Room room = new Room();

    public TableView<Room> getRoomTable() {
        return roomTable;
    }

    public TableColumn<Room, Integer> getRoomNumberCol() {
        return roomNumberCol;
    }

    public void setRoomNumberCol(TableColumn<Room, Integer> roomNumberCol) {
        this.roomNumberCol = roomNumberCol;
    }

    public TableColumn<Room, Integer> getRoomTypeCol() {
        return roomTypeCol;
    }

    public void setRoomTypeCol(TableColumn<Room, Integer> roomTypeCol) {
        this.roomTypeCol = roomTypeCol;
    }

    public TableColumn<Room, String> getRoomPhoneCol() {
        return roomPhoneCol;
    }

    public void setRoomPhoneCol(TableColumn<Room, String> roomPhoneCol) {
        this.roomPhoneCol = roomPhoneCol;
    }

    public TableColumn<Room, String> getReservation() {
        return reservation;
    }

    public void setReservation(TableColumn<Room, String> reservation) {
        this.reservation = reservation;
    }

    public void setRoomTable(TableView<Room> roomTable) {
        this.roomTable = roomTable;
    }

    public TextField getTextFieldRoomNumber() {
        return textFieldRoomNumber;
    }

    public void setTextFieldRoomNumber(TextField textFieldRoomNumber) {
        this.textFieldRoomNumber = textFieldRoomNumber;
    }

    public ComboBox getComboBoxRoomType() {
        return comboBoxRoomType;
    }

    public void setComboBoxRoomType(ComboBox comboBoxRoomType) {
        this.comboBoxRoomType = comboBoxRoomType;
    }

    public TextField getTextFieldRoomPhoneNumber() {
        return textFieldRoomPhoneNumber;
    }

    public void setTextFieldRoomPhoneNumber(TextField textFieldRoomPhoneNumber) {
        this.textFieldRoomPhoneNumber = textFieldRoomPhoneNumber;
    }

    //Populate the table with all existing clients in the database
    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {
        roomNumberCol.setCellValueFactory(cellData -> cellData.getValue().getRoomNumberProp().asObject());
        roomTypeCol.setCellValueFactory(cellData -> cellData.getValue().getRoomTypeProp().asObject());
        roomPhoneCol.setCellValueFactory(cellData -> cellData.getValue().getRoomPhoneNumberProp());
        reservation.setCellValueFactory(cellData -> cellData.getValue().reservedProp());

        ObservableList<Room> fullList = Room.fillRoomTable();
        populateTable(fullList);

        room.fillRoomsTypeComboBox(comboBoxRoomType);

        radioButtonYes.setToggleGroup(buttonGroup);
        radioButtonNo.setToggleGroup(buttonGroup);

    }
    public void populateTable(ObservableList<Room> fullList) {
        roomTable.setItems(fullList);
    }

    //Add new Room
    @FXML
    public void addNewRoomButtonAction() {

        try {
            int roomNumber = Integer.parseInt(textFieldRoomNumber.getText());
            int roomType = Integer.parseInt(comboBoxRoomType.getValue().toString());
            String phone = textFieldRoomPhoneNumber.getText();
            if (!room.checkRoomNumber(roomNumber)) {
                if (room.addRoom(roomNumber, roomType, phone)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("New Room Added Successfully");
                    alert.setContentText("Please Refresh the Table");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("New Room Not Added");
                    alert.setContentText("Add Room Error");
                    alert.showAndWait();
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("New Room Not Added");
                alert.setContentText("Room With that number already exists");
                alert.showAndWait();
            }
            } catch(NumberFormatException e){
                e.printStackTrace();
            }
    }

    //Display the selected row's data in the TextFields
    @FXML
    public void onSelect() {
        // check the table's selected item and get selected item
        if (roomTable.getSelectionModel().getSelectedItem() != null) {
            Room selectedRoom = roomTable.getSelectionModel().getSelectedItem();

            textFieldRoomNumber.setText(String.valueOf(selectedRoom.getRoomNumber()));
            comboBoxRoomType.setValue(String.valueOf(selectedRoom.getRoomType()));
            textFieldRoomPhoneNumber.setText(selectedRoom.getRoomPhoneNumber());

            String isReserved = roomTable.getSelectionModel().getSelectedItem().getReserved();
            if (isReserved.equals("Yes")){
                radioButtonYes.setSelected(true);
            }else {
                radioButtonNo.setSelected(true);
            }
        }
    }

    //Edit Room
    @FXML
    public void editRoomButtonAction() {
        //Collect data from the fields
        int roomNumber = 0;
        int roomType = Integer.parseInt(comboBoxRoomType.getValue().toString());
        String phone = textFieldRoomPhoneNumber.getText();
        String isReserved = "No";

        if (radioButtonYes.isSelected()){
            isReserved = "Yes";
        }
        if(phone.trim().equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Please enter all required fields");
            alert.setContentText("Room Phone number is required");
            alert.showAndWait();
        }else {

            try{
                roomNumber = Integer.parseInt(textFieldRoomNumber.getText());

                if (room.editRoom(roomNumber, roomType, phone, isReserved)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Room Updated Successfully");
                    alert.setContentText("Please Refresh the Table");
                    alert.showAndWait();
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("Room Not Updated");
                    alert.setContentText("Update Room Error");
                    alert.showAndWait();
                }
            }catch (NumberFormatException ex){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(ex.getMessage() + " - Please enter all required fields");
                alert.setContentText("Please check if Room Number is entered");
                alert.showAndWait();
            }
        }
    }

    //Remove Room
    @FXML
    public void removeRoomButtonAction() {

        try{
            int id = Integer.parseInt(textFieldRoomNumber.getText());

            if (room.removeRoom(id)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Room Deleted Successfully");
                alert.setContentText("Please Refresh the Table");
                alert.showAndWait();
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Room Not Deleted");
                alert.setContentText("Delete Room Error");
                alert.showAndWait();
            }

        }catch (NumberFormatException ex){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(ex.getMessage() + " - Please enter all required fields");
            alert.setContentText("Please check if Room Number is entered");
            alert.showAndWait();
        }
    }

    //Clear all text fields
    @FXML
    public void clearButtonAction() {
        textFieldRoomNumber.setText("");
        comboBoxRoomType.setValue(1);
        textFieldRoomPhoneNumber.setText("");
        radioButtonNo.setSelected(true);
    }

    //Refresh the table
    @FXML
    public void refreshButtonAction() throws SQLException, ClassNotFoundException {

        roomNumberCol.setCellValueFactory(cellData -> cellData.getValue().getRoomNumberProp().asObject());
        roomTypeCol.setCellValueFactory(cellData -> cellData.getValue().getRoomTypeProp().asObject());
        roomPhoneCol.setCellValueFactory(cellData -> cellData.getValue().getRoomPhoneNumberProp());
        reservation.setCellValueFactory(cellData -> cellData.getValue().reservedProp());

        ObservableList<Room> fullList = Room.fillRoomTable();
        populateTable(fullList);

    }

    @FXML
    public void showRoomTypesTable(){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("roomTypes-window.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage mainStage = new Stage();
            mainStage.setTitle("Room Types");
            mainStage.setScene(scene);
            mainStage.show();

        } catch (Exception e) {
            System.out.println("Cannot load Room Type Window");
            e.printStackTrace();
        }
    }
}
