package com.gui.hotel_management_javafx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Reservation {

    private IntegerProperty reservationId;
    private IntegerProperty clientId;
    private IntegerProperty roomNumber;
    private StringProperty dateIn;
    private StringProperty dateOut;

    private ObservableList<Reservation> reservationsList;

    public Reservation() {
        this.reservationId = new SimpleIntegerProperty();
        this.clientId = new SimpleIntegerProperty();
        this.roomNumber = new SimpleIntegerProperty();
        this.dateIn = new SimpleStringProperty();
        this.dateOut = new SimpleStringProperty();

    }

    MyConnection myConnection = new MyConnection();
    Room room = new Room();

    //Create a new reservation
    public boolean addReservation(int clientId, int roomNumber, String dateIn, String dateOut) {
        PreparedStatement preparedStatement;
        String addQuery = "INSERT INTO reservations (client_id, room_number, date_in, date_out) VALUES (?, ?, ?,?)";

        try {
            preparedStatement = myConnection.createConnection().prepareStatement(addQuery);

            preparedStatement.setInt(1, clientId);
            preparedStatement.setInt(2, roomNumber);
            preparedStatement.setString(3, dateIn);
            preparedStatement.setString(4, dateOut);

            if (!isDateReserved(roomNumber, dateIn)) {
                if (preparedStatement.executeUpdate() > 0) {
                    room.setRoomReservation(roomNumber, "Yes");
                    return true;
                } else {
                    return false;
                }
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Room already reserved");
                alert.setContentText("Please check and try again");
                alert.showAndWait();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    //Edit the selected reservation
    public boolean editReservation(int reservationId, int clientId, int roomNumber, String dateIn, String dateOut){
        PreparedStatement preparedStatement;
        String editQuery = "UPDATE reservations SET client_id = ?, room_number = ?, date_in = ?, date_out = ? WHERE id = ?";

        try{
            preparedStatement = myConnection.createConnection().prepareStatement(editQuery);

            preparedStatement.setInt(1, clientId);
            preparedStatement.setInt(2, roomNumber);
            preparedStatement.setString(3, dateIn);
            preparedStatement.setString(4, dateOut);
            preparedStatement.setInt(5, reservationId);

            return (preparedStatement.executeUpdate() > 0);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Remove the selected reservation
    public boolean removeReservation(int reservationId){
        PreparedStatement preparedStatement;
        String deleteQuery = "DELETE FROM reservations WHERE id = ?";

        try{
            preparedStatement = myConnection.createConnection().prepareStatement(deleteQuery);
            preparedStatement.setInt(1, reservationId);
            int roomNumber = getRoomNumberFromReservation(reservationId);

            if (preparedStatement.executeUpdate() > 0) {
                room.setRoomReservation(roomNumber, "No");
                return true;
            }else {
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObservableList<Reservation> fillReservationTable() throws ClassNotFoundException, SQLException {

        MyConnection myConnection = new MyConnection();
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String selectQuery = "SELECT * FROM reservations";

        try {
            preparedStatement = myConnection.createConnection().prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();
            ObservableList<Reservation> listOfReservations = getReservationObjects(resultSet);
            return listOfReservations;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObservableList<Reservation> getReservationObjects(ResultSet resultSet) throws ClassNotFoundException, SQLException{

        ObservableList<Reservation> reservationList = FXCollections.observableArrayList();

        try {
            while (resultSet.next()) {
                Reservation reservation = new Reservation();
                reservation.setReservationId(resultSet.getInt("id"));
                reservation.setClientId(resultSet.getInt("client_id"));
                reservation.setRoomNumber(resultSet.getInt("room_number"));
                reservation.setDateIn(resultSet.getString("date_in"));
                reservation.setDateOut(resultSet.getString("date_out"));
                reservationList.add(reservation);
            }
            return reservationList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Get the roomNumber from a reservation
    public int getRoomNumberFromReservation(int reservationID) {

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String selectQuery = "SELECT room_number FROM reservations WHERE id =?";
        int roomNumber;

        try {
            preparedStatement = myConnection.createConnection().prepareStatement(selectQuery);
            preparedStatement.setInt(1, reservationID);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                roomNumber = resultSet.getInt(1);
                preparedStatement.close();
                return roomNumber;
            }else {
                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //Check if the room is reserved
    public boolean isDateReserved(int reservationForRoomNumber, String dateIn){

        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String selectQuery = "SELECT * FROM reservations WHERE room_number = ? AND date_out >= ?";
        boolean roomReserved;

        try {
            preparedStatement = myConnection.createConnection().prepareStatement(selectQuery);
            preparedStatement.setInt(1, reservationForRoomNumber);
            preparedStatement.setString(2, dateIn);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                roomReserved = true;
                preparedStatement.close();
                return roomReserved;
            }else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int getReservationId() {
        return reservationId.get();
    }

    public IntegerProperty reservationIdProperty() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId.set(reservationId);
    }

    public int getClientId() {
        return clientId.get();
    }

    public IntegerProperty clientIdProperty() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId.set(clientId);
    }

    public int getRoomNumber() {
        return roomNumber.get();
    }

    public IntegerProperty roomNumberProperty() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber.set(roomNumber);
    }

    public String getDateIn() {
        return dateIn.get();
    }

    public StringProperty dateInProperty() {
        return dateIn;
    }

    public void setDateIn(String dateIn) {
        this.dateIn.set(dateIn);
    }

    public String getDateOut() {
        return dateOut.get();
    }

    public StringProperty dateOutProperty() {
        return dateOut;
    }

    public void setDateOut(String dateOut) {
        this.dateOut.set(dateOut);
    }
}
