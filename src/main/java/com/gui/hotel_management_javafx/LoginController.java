package com.gui.hotel_management_javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private TextField textFieldUsername;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void onLoginButtonClick() {
        PreparedStatement ps;
        ResultSet rs;

        //Returning username and password values
        String username = textFieldUsername.getText();
        String password = String.valueOf(passwordField.getText());

        if (username.trim().equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Please enter Username to login");
            alert.setContentText("Username required");
            alert.showAndWait();
        } else if (password.trim().equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Please enter Password to login");
            alert.setContentText("Password required");
            alert.showAndWait();

        }else {
            MyConnection myConnection = new MyConnection();

            //Query creation and check if username and password match
            String selectQuery = "SELECT * FROM users WHERE user = ? AND password = ?";
            try {
                ps = myConnection.createConnection().prepareStatement(selectQuery);
                ps.setString(1, username);
                ps.setString(2, password);
                rs = ps.executeQuery();

                //If they match - open the main form and exit the login form
                if (rs.next()){
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-window.fxml"));
                        Scene scene = new Scene(fxmlLoader.load());
                        Stage mainStage = new Stage();
                        mainStage.setTitle("Welcome to Hotel Management");
                        mainStage.setScene(scene);
                        mainStage.setMaximized(true);
                        mainStage.show();

                        loginClose();

                    } catch (Exception e) {
                        System.out.println("Cannot load Main Window");
                        e.printStackTrace();
                    }
                    ps.close();
                }else {
                    //If wrong information is entered
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Wrong Username or Password");
                    alert.setContentText("Username and Password must be correct");
                    alert.showAndWait();
                }
            }catch (SQLException ex) {
                System.out.println(ex.getMessage());
                throw new RuntimeException(ex);
            }
        }
    }

    public void loginClose(){
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }
}