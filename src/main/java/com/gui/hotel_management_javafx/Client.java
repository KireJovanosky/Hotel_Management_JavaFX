package com.gui.hotel_management_javafx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Client {

    private IntegerProperty clientID;
    private StringProperty clientFirstName;
    private StringProperty clientLastName;
    private StringProperty clientPhoneNumber;
    private StringProperty clientEmail;

    private ObservableList<Client> clientsList;

    public Client() {
        this.clientID = new SimpleIntegerProperty();
        this.clientFirstName = new SimpleStringProperty();
        this.clientLastName = new SimpleStringProperty();
        this.clientPhoneNumber = new SimpleStringProperty();
        this.clientEmail = new SimpleStringProperty();
    }


    MyConnection myConnection = new MyConnection();

    //Create a new client
    public boolean addClient(String fName, String lName, String phone, String email) {

        PreparedStatement preparedStatement;
        String addQuery = "INSERT INTO clients (first_name, last_name, phone, email) VALUES (?, ?, ?, ?)";

        try {
            preparedStatement = myConnection.createConnection().prepareStatement(addQuery);

            preparedStatement.setString(1, fName);
            preparedStatement.setString(2, lName);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, email);

            return (preparedStatement.executeUpdate() > 0);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Edit the selected client
    public boolean editClient(int id, String fName, String lName, String phone, String email) {

        PreparedStatement preparedStatement;
        String editQuery = "UPDATE clients SET first_name = ?, last_name = ?, phone = ?, email = ? WHERE id = ?";

        try {
            preparedStatement = myConnection.createConnection().prepareStatement(editQuery);

            preparedStatement.setString(1, fName);
            preparedStatement.setString(2, lName);
            preparedStatement.setString(3, phone);
            preparedStatement.setString(4, email);
            preparedStatement.setInt(5, id);

            return (preparedStatement.executeUpdate() > 0);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Remove the selected client
    public boolean removeClient(int id) {

        PreparedStatement preparedStatement;
        String deleteQuery = "DELETE FROM clients WHERE id = ?";

        try {
            preparedStatement = myConnection.createConnection().prepareStatement(deleteQuery);

            preparedStatement.setInt(1, id);

            return (preparedStatement.executeUpdate() > 0);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ObservableList<Client> fillClientTable() throws ClassNotFoundException, SQLException {

        MyConnection myConnection = new MyConnection();
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String selectQuery = "SELECT * FROM clients";

        try {
            preparedStatement = myConnection.createConnection().prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();
            ObservableList<Client> listOfClients = getClientObjects(resultSet);
            return listOfClients;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static ObservableList<Client> getClientObjects(ResultSet resultSet) throws ClassNotFoundException, SQLException{

        ObservableList<Client> clientList = FXCollections.observableArrayList();

        try {
            while (resultSet.next()) {
                Client client = new Client();
                client.setClientID(resultSet.getInt("id"));
                client.setClientFirstName(resultSet.getString("first_name"));
                client.setClientLastName(resultSet.getString("last_name"));
                client.setClientPhoneNumber(resultSet.getString("phone"));
                client.setClientEmail(resultSet.getString("email"));
                clientList.add(client);
            }
            return clientList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getClientID() {
        return clientID.get();
    }

    public void setClientID(int id) {
        this.clientID.set(id);
    }

    public IntegerProperty getClientIDProp() {
        return clientID;
    }

    public String getClientFirstName() {
        return clientFirstName.get();
    }

    public void setClientFirstName(String firstName) {
        this.clientFirstName.set(firstName);
    }

    public StringProperty getClientFirstNameProp(){
        return clientFirstName;
    }

    public String getClientLastName() {
        return clientLastName.get();
    }

    public void setClientLastName(String lastName) {
        this.clientLastName.set(lastName);
    }

    public StringProperty getClientLastNameProp(){
        return clientLastName;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber.get();
    }

    public void setClientPhoneNumber(String phoneNumber) {
        this.clientPhoneNumber.set(phoneNumber);
    }

    public StringProperty getClientPhoneNumberProp(){
        return clientPhoneNumber;
    }

    public String getClientEmail() {
        return clientEmail.get();
    }

    public void setClientEmail(String email) {
        this.clientEmail.set(email);
    }

    public StringProperty getClientEmailProp(){
        return clientEmail;
    }

}
