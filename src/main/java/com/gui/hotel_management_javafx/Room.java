package com.gui.hotel_management_javafx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Room {

    private IntegerProperty roomNumber;
    private IntegerProperty roomType;
    private StringProperty roomPhoneNumber;
    private StringProperty reserved;

    private ObservableList<Client> clientsList;

    public Room() {
        this.roomNumber = new SimpleIntegerProperty();
        this.roomType = new SimpleIntegerProperty();
        this.roomPhoneNumber = new SimpleStringProperty();
        this.reserved = new SimpleStringProperty();
    }

    MyConnection myConnection = new MyConnection();

    //Check if room number already exists
    public boolean checkRoomNumber(int roomNumber) {
        PreparedStatement preparedStatementCheck;
        ResultSet checkResultSet;
        String checkQuery = "SELECT * FROM rooms WHERE r_number =?";

        try {
            preparedStatementCheck = myConnection.createConnection().prepareStatement(checkQuery);
            preparedStatementCheck.setInt(1, roomNumber);

            checkResultSet = preparedStatementCheck.executeQuery();

           if(checkResultSet.next()){
               preparedStatementCheck.close();
               return true;
           }else {
               return false;
           }

        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //Create a new room
    public boolean addRoom(int roomNumber, int roomType, String phone) {

        PreparedStatement preparedStatement;
        String addQuery = "INSERT INTO rooms (r_number, type, phone, reserved) VALUES (?, ?, ?, ?)";

        try {
            preparedStatement = myConnection.createConnection().prepareStatement(addQuery);

            preparedStatement.setInt(1, roomNumber);
            preparedStatement.setInt(2, roomType);
            preparedStatement.setString(3, phone);
            //default Reservation value for newly added rooms is "No"
            preparedStatement.setString(4, "No");

            return (preparedStatement.executeUpdate() > 0);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);

        }
    }

    //Edit the selected room
    public boolean editRoom(int roomNumber, int roomType, String phone, String isReserved) {

        PreparedStatement preparedStatement;
        String editQuery = "UPDATE rooms SET type = ?, phone = ?, reserved = ? WHERE r_number = ?";

        try {
            preparedStatement = myConnection.createConnection().prepareStatement(editQuery);

            preparedStatement.setInt(1, roomType);
            preparedStatement.setString(2, phone);
            preparedStatement.setString(3, isReserved);
            preparedStatement.setInt(4, roomNumber);

            return (preparedStatement.executeUpdate() > 0);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Remove the selected room
    public boolean removeRoom(int roomNumber) {

        PreparedStatement preparedStatement;
        String deleteQuery = "DELETE FROM rooms WHERE r_number = ?";

        try {
            preparedStatement = myConnection.createConnection().prepareStatement(deleteQuery);

            preparedStatement.setInt(1, roomNumber);

            return (preparedStatement.executeUpdate() > 0);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static ObservableList<Room> fillRoomTable() throws ClassNotFoundException, SQLException {

        MyConnection myConnection = new MyConnection();
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String selectQuery = "SELECT * FROM rooms";

        try {
            preparedStatement = myConnection.createConnection().prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();
            ObservableList<Room> listOfRooms = getRoomObjects(resultSet);
            return listOfRooms;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static ObservableList<Room> getRoomObjects(ResultSet resultSet) throws ClassNotFoundException, SQLException{

        ObservableList<Room> roomList = FXCollections.observableArrayList();

        try {
            while (resultSet.next()) {
                Room room = new Room();
                room.setRoomNumber(resultSet.getInt("r_number"));
                room.setRoomType(resultSet.getInt("type"));
                room.setRoomPhoneNumber(resultSet.getString("phone"));
                room.setReserved(resultSet.getString("reserved"));
                roomList.add(room);
            }
            return roomList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //method to fill the comboBox with rooms id
    public void fillRoomsTypeComboBox(ComboBox comboBox){

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String selectQuery = "SELECT * FROM type";

        try  {
            preparedStatement = myConnection.createConnection().prepareStatement(selectQuery);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                comboBox.getItems().addAll(resultSet.getInt("id"));
            }
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Set Room reservation to Yes when new Reservation is made adn No when reservation is deleted
    public boolean setRoomReservation(int roomNumber, String isReserved) {

        PreparedStatement preparedStatement;
        String setReservationQuery = "UPDATE rooms SET reserved = ? WHERE r_number = ?";

        try {
            preparedStatement = myConnection.createConnection().prepareStatement(setReservationQuery);
            preparedStatement.setString(1, isReserved);
            preparedStatement.setInt(2, roomNumber);

            return (preparedStatement.executeUpdate() > 0);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Check if a room is already reserved before making a reservation
    public String checkRoomReservation(int roomNumber) {

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String isReserved;
        String selectQuery = "SELECT reserved FROM rooms WHERE r_number = ?";

        try {
            preparedStatement = myConnection.createConnection().prepareStatement(selectQuery);
            preparedStatement.setInt(1, roomNumber);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                isReserved = resultSet.getString(1);
                preparedStatement.close();
                return isReserved;
            }else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getRoomNumber() {
        return roomNumber.get();
    }

    public void setRoomNumber(int id) {
        this.roomNumber.set(id);
    }

    public IntegerProperty getRoomNumberProp() {
        return roomNumber;
    }

    public int getRoomType() {
        return roomType.get();
    }

    public IntegerProperty getRoomTypeProp() {
        return roomType;
    }

    public void setRoomType(int type) {
        this.roomType.set(type);
    }

    public String getRoomPhoneNumber() {
        return roomPhoneNumber.get();
    }

    public StringProperty getRoomPhoneNumberProp() {
        return roomPhoneNumber;
    }

    public void setRoomPhoneNumber(String roomPhoneNumber) {
        this.roomPhoneNumber.set(roomPhoneNumber);
    }

    public String getReserved() {
        return reserved.get();
    }

    public StringProperty reservedProp() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved.set(reserved);
    }
}
