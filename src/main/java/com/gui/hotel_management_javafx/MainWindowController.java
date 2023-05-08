package com.gui.hotel_management_javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainWindowController {

       public void manageClients(){

           try {
               FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("clients-window.fxml"));
               Scene scene = new Scene(fxmlLoader.load());
               Stage mainStage = new Stage();
               mainStage.setTitle("Client Management");
               mainStage.setScene(scene);
               mainStage.show();


           } catch (Exception e) {
               System.out.println("Cannot load Clients Window");
           }
    }

    public void manageRooms(){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("rooms-window.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage mainStage = new Stage();
            mainStage.setTitle("Room Management");
            mainStage.setScene(scene);
            mainStage.show();

        } catch (Exception e) {
            System.out.println("Cannot load Rooms Window");
            e.printStackTrace();
        }
    }

    public void manageReservations(){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("reservations-window.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage mainStage = new Stage();
            mainStage.setTitle("Reservation Management");
            mainStage.setScene(scene);
            mainStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Cannot load Reservations Window");
        }
    }
}
