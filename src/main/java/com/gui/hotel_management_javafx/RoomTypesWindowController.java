package com.gui.hotel_management_javafx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomTypesWindowController {

    @FXML
    private TableColumn<RoomTypesWindowController, Integer> typeIdCol;
    @FXML
    private TableColumn<RoomTypesWindowController, String> labelCol;
    @FXML
    private TableColumn<RoomTypesWindowController, Integer> priceCol;
    @FXML
    private TableView<RoomTypesWindowController> roomTypesTable;

    private IntegerProperty typeId;
    private StringProperty label;
    private IntegerProperty price;

    private ObservableList<RoomTypesWindowController> roomTypesList;

    public RoomTypesWindowController(){
        this.typeId = new SimpleIntegerProperty();
        this.label = new SimpleStringProperty();
        this.price = new SimpleIntegerProperty();
    }

    public static ObservableList<RoomTypesWindowController> fillRoomTypesTable() throws ClassNotFoundException, SQLException {

        MyConnection myConnection = new MyConnection();
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        String selectQuery = "SELECT * FROM type";

        try {
            preparedStatement = myConnection.createConnection().prepareStatement(selectQuery);
            resultSet = preparedStatement.executeQuery();
            ObservableList<RoomTypesWindowController> listOfRoomTypes = getRoomTypeObjects(resultSet);
            return listOfRoomTypes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObservableList<RoomTypesWindowController> getRoomTypeObjects(ResultSet resultSet) throws ClassNotFoundException, SQLException{

        ObservableList<RoomTypesWindowController> roomTypesList = FXCollections.observableArrayList();

        try {
            while (resultSet.next()) {
                RoomTypesWindowController roomTypes = new RoomTypesWindowController();
                roomTypes.setTypeId(resultSet.getInt("id"));
                roomTypes.setLabel(resultSet.getString("label"));
                roomTypes.setPrice(resultSet.getInt("price"));
                roomTypesList.add(roomTypes);
            }
            return roomTypesList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void initialize() throws SQLException, ClassNotFoundException {

        typeIdCol.setCellValueFactory(cellData -> cellData.getValue().getTypeIdProperty().asObject());
        labelCol.setCellValueFactory(cellData -> cellData.getValue().getLabelProperty());
        priceCol.setCellValueFactory(cellData -> cellData.getValue().getPriceProperty().asObject());

        ObservableList<RoomTypesWindowController> fullList = RoomTypesWindowController.fillRoomTypesTable();
        populateTable(fullList);
    }

    public void populateTable(ObservableList<RoomTypesWindowController> fullList) {
        roomTypesTable.setItems(fullList);
    }

    public TableColumn<RoomTypesWindowController, Integer> getTypeIdCol() {
        return typeIdCol;
    }

    public void setTypeIdCol(TableColumn<RoomTypesWindowController, Integer> typeIdCol) {
        this.typeIdCol = typeIdCol;
    }

    public TableColumn<RoomTypesWindowController, String> getLabelCol() {
        return labelCol;
    }

    public void setLabelCol(TableColumn<RoomTypesWindowController, String> labelCol) {
        this.labelCol = labelCol;
    }

    public TableColumn<RoomTypesWindowController, Integer> getPriceCol() {
        return priceCol;
    }

    public void setPriceCol(TableColumn<RoomTypesWindowController, Integer> priceCol) {
        this.priceCol = priceCol;
    }

    public int getTypeId() {
        return typeId.get();
    }

    public IntegerProperty getTypeIdProperty() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId.set(typeId);
    }

    public String getLabel() {
        return label.get();
    }

    public StringProperty getLabelProperty() {
        return label;
    }

    public void setLabel(String label) {
        this.label.set(label);
    }

    public double getPrice() {
        return price.get();
    }

    public IntegerProperty getPriceProperty() {
        return price;
    }

    public void setPrice(int price) {
        this.price.set(price);
    }
}
