package main;

import com.sun.javafx.collections.UnmodifiableListSet;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.collections4.Unmodifiable;
import org.controlsfx.control.tableview2.filter.popupfilter.PopupNumberFilter;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class Main extends Application {
    private Stage primaryStage;
    private final ObservableList<TableRow> data = FXCollections.observableArrayList();
    private File file = null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;

        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Форма документа \"Ведомость учета движения посуды и приборов\"");
        primaryStage.setScene(new Scene(root));
        upgradeTable("#leftScroll");
        upgradeTable("#rightScroll");
        DatePicker dateField = (DatePicker) primaryStage.getScene().lookup("#date");
        dateField.setValue(LocalDate.now(ZoneId.of("GMT+07:00")));
        TextField numberField = (TextField) primaryStage.getScene().lookup("#number");
        int number = FileManager.getNumber();
        numberField.setText(Integer.toString(number++));
        FileManager.setNumber(number);
        setUpOrg();
        setUpAddFields();
        setUpTable();
        setUpPeopleFields();
        setUpTotalList();
        setUpSaving();
        primaryStage.show();



    }

    private void saveShortcut(){
        TextField numberField = (TextField) primaryStage.getScene().lookup("#number");
        DatePicker dateField = (DatePicker) primaryStage.getScene().lookup("#date");
        ComboBox organization = (ComboBox) primaryStage.getScene().lookup("#organization");
        ComboBox subdivision = (ComboBox) primaryStage.getScene().lookup("#subdivision");
        DatePicker periodFrom = (DatePicker) primaryStage.getScene().lookup("#periodFrom");
        DatePicker periodUntil = (DatePicker) primaryStage.getScene().lookup("#periodUntil");
        ComboBox matName = (ComboBox) primaryStage.getScene().lookup("#matName");
        ComboBox buhName = (ComboBox) primaryStage.getScene().lookup("#buhName");
        try {
            FileManager.saveExcel("src\\main\\temlate.xls", file, Integer.parseInt(numberField.getText()), dateField.getEditor().getText(),
                    (String) organization.getValue(), (String) subdivision.getValue(), periodFrom.getEditor().getText(), periodUntil.getEditor().getText(),
                    data, TableRow.getAverageValues(data), (String) matName.getValue(), (String) buhName.getValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUpSaving(){
        Button saveButton = (Button) primaryStage.getScene().lookup("#saveButton");
        saveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (file != null) {
                    saveShortcut();
                }
                else{
                    //Creating a dialog
                    Dialog<String> dialog = new Dialog<>();
                    //Setting the title
                    dialog.setTitle("Dialog");
                    ButtonType type = new ButtonType("ОК", ButtonBar.ButtonData.OK_DONE);
                    //Setting the content of the dialog
                    dialog.setContentText("Выберите файл!");
                    //Adding buttons to the dialog pane
                    dialog.getDialogPane().getButtonTypes().add(type);
                    dialog.showAndWait();
                }
            }
        });
        MenuBar menu = (MenuBar) primaryStage.getScene().lookup("#menu");
        Menu fileMenu = menu.getMenus().get(0);
        MenuItem menuSaveAs = fileMenu.getItems().get(1);
        MenuItem menuSave = fileMenu.getItems().get(0);

        menuSaveAs.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();

                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Excel files (*.xls)", "*.xls");
                fileChooser.getExtensionFilters().add(extensionFilter);

                file = fileChooser.showSaveDialog(primaryStage);

                if (file != null){
                    saveShortcut();
                }

            }
        });
        menuSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (file != null) {
                    saveShortcut();
                }
                else{
                    //Creating a dialog
                    Dialog<String> dialog = new Dialog<>();
                    //Setting the title
                    dialog.setTitle("Dialog");
                    ButtonType type = new ButtonType("ОК", ButtonBar.ButtonData.OK_DONE);
                    //Setting the content of the dialog
                    dialog.setContentText("Выберите файл!");
                    //Adding buttons to the dialog pane
                    dialog.getDialogPane().getButtonTypes().add(type);
                    dialog.showAndWait();
                }
            }
        });
    }

    private ListView setUpTotalList(){
        ListView list = (ListView) primaryStage.getScene().lookup("#list");
        ObservableList listData = FXCollections.observableArrayList();
        list.setItems(listData);
        list.setCellFactory(TextFieldListCell.forListView());
        populateListLeft();
        TabPane tabPane = (TabPane) primaryStage.getScene().lookup("#tabPane");
        tabPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                TabPane tabPane = (TabPane) primaryStage.getScene().lookup("#tabPane");
                if (tabPane.getSelectionModel().getSelectedIndex() == 0)
                    populateListLeft();
                else
                    populateListRight();
            }
        });
        return list;
    }

    private void populateListLeft(){
        ListView list = (ListView) primaryStage.getScene().lookup("#list");
        Set<Node> cells = list.lookupAll(".cell");
        ArrayList<Node> listCells = new ArrayList<>(cells);
        ObservableList listData = list.getItems();
        int amount = 0, income = 0, sent = 0;
        BigDecimal price = new BigDecimal(0), totalPrice = new BigDecimal(0);
        for (TableRow row : data){
            if (row.getAmount() != null)
                amount += Integer.parseInt(row.getAmount());
            if (row.getIncome() != null)
                income += Integer.parseInt(row.getIncome());
            if (row.getSentToStorageOrCompensatedByRecipient() != null)
                sent += Integer.parseInt(row.getSentToStorageOrCompensatedByRecipient());
            if (row.getPrice() != null)
                price = price.add(new BigDecimal(row.getPrice()));
            if (row.getTotalPrice() != null)
                totalPrice = totalPrice.add(new BigDecimal(row.getTotalPrice()));
        }
        price.setScale(2, RoundingMode.HALF_DOWN);
        totalPrice.setScale(2, RoundingMode.HALF_DOWN);
        listData.clear();
        listData.addAll(price.toString(), Integer.toString(amount), totalPrice.toString(),
                Integer.toString(income), Integer.toString(sent));
        for (Node listCell : listCells) {
            ListCell cell = (ListCell) listCell;
            cell.setPrefWidth(110);
        }
    }

    private void populateListRight(){
        ListView list = (ListView) primaryStage.getScene().lookup("#list");
        Set<Node> cells = list.lookupAll(".cell");
        ArrayList<Node> listCells = new ArrayList<>(cells);
        ObservableList listData = list.getItems();
        int brokenAmount = 0, lostAmount = 0, leftInTheEndAmount = 0;
        BigDecimal brokenTotalPrice = new BigDecimal(0), lostTotalPrice = new BigDecimal(0), leftInTheEndTotalPrice = new BigDecimal(0);
        for (TableRow row : data){
            if (row.getBrokenAmount() != null)
                brokenAmount += Integer.parseInt(row.getBrokenAmount());
            if (row.getLostAmount() != null)
                lostAmount += Integer.parseInt(row.getLostAmount());
            if (row.getLeftInTheEndAmount() != null)
                leftInTheEndAmount += Integer.parseInt(row.getLeftInTheEndAmount());
            if (row.getBrokenTotalPrice() != null)
                brokenTotalPrice = brokenTotalPrice.add(new BigDecimal(row.getBrokenTotalPrice()));
            if (row.getLostTotalPrice() != null)
                lostTotalPrice = lostTotalPrice.add(new BigDecimal(row.getLostTotalPrice()));
            if (row.getLeftInTheEndTotalPrice() != null)
                leftInTheEndTotalPrice = leftInTheEndTotalPrice.add(new BigDecimal(row.getLeftInTheEndTotalPrice()));
        }
        brokenTotalPrice.setScale(2, RoundingMode.HALF_DOWN);
        lostTotalPrice.setScale(2, RoundingMode.HALF_DOWN);
        leftInTheEndTotalPrice.setScale(2, RoundingMode.HALF_DOWN);
        listData.clear();
        listData.addAll(Integer.toString(brokenAmount), brokenTotalPrice.toString(),
                Integer.toString(lostAmount), lostTotalPrice.toString(),
                Integer.toString(leftInTheEndAmount), leftInTheEndTotalPrice.toString());
        for (Node listCell : listCells) {
            ListCell cell = (ListCell) listCell;
            cell.setPrefWidth(110);
        }
    }


    private void setUpPeopleFields() throws IOException {
        ComboBox matName = (ComboBox) primaryStage.getScene().lookup("#matName");
        ComboBox buhName = (ComboBox) primaryStage.getScene().lookup("#buhName");
        matName.setItems(FXCollections.observableList(FileManager.getReponsibleNames()));
        buhName.setItems(FXCollections.observableList(FileManager.getBuhNames()));
        FxUtilTest.autoCompleteComboBoxPlus(matName, (String typedText, String itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.equals(typedText));
        FxUtilTest.autoCompleteComboBoxPlus(buhName, (String typedText, String itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.equals(typedText));
    }

    private void setUpTable(){
        ScrollPane leftScroll = (ScrollPane) primaryStage.getScene().lookup("#leftScroll");
        TableView leftTable = (TableView) leftScroll.getContent();
        ObservableList<TableColumn> leftTableColumns = leftTable.getColumns();
        ScrollPane rightScroll = (ScrollPane) primaryStage.getScene().lookup("#rightScroll");
        TableView rightTable = (TableView) rightScroll.getContent();
        ObservableList<TableColumn> rightTableColumns = rightTable.getColumns();
        // link to model
        leftTableColumns.get(0).setCellValueFactory(new PropertyValueFactory<TableRow, String>("number"));
        leftTableColumns.get(1).setCellValueFactory(new PropertyValueFactory<TableRow, String>("title"));
        leftTableColumns.get(2).setCellValueFactory(new PropertyValueFactory<TableRow, String>("code"));
        leftTableColumns.get(3).setCellValueFactory(new PropertyValueFactory<TableRow, String>("price"));
        leftTableColumns.get(4).setCellValueFactory(new PropertyValueFactory<TableRow, String>("amount"));
        leftTableColumns.get(5).setCellValueFactory(new PropertyValueFactory<TableRow, String>("totalPrice"));
        leftTableColumns.get(6).setCellValueFactory(new PropertyValueFactory<TableRow, String>("income"));
        leftTableColumns.get(7).setCellValueFactory(new PropertyValueFactory<TableRow, String>("sentToStorageOrCompensatedByRecipient"));
        leftTable.setItems(data);

        rightTableColumns.get(0).setCellValueFactory(new PropertyValueFactory<TableRow, String>("number"));
        rightTableColumns.get(1).setCellValueFactory(new PropertyValueFactory<TableRow, String>("title"));
        rightTableColumns.get(2).setCellValueFactory(new PropertyValueFactory<TableRow, String>("code"));
        rightTableColumns.get(3).setCellValueFactory(new PropertyValueFactory<TableRow, String>("brokenAmount"));
        rightTableColumns.get(4).setCellValueFactory(new PropertyValueFactory<TableRow, String>("brokenTotalPrice"));
        rightTableColumns.get(5).setCellValueFactory(new PropertyValueFactory<TableRow, String>("lostAmount"));
        rightTableColumns.get(6).setCellValueFactory(new PropertyValueFactory<TableRow, String>("lostTotalPrice"));
        rightTableColumns.get(7).setCellValueFactory(new PropertyValueFactory<TableRow, String>("leftInTheEndAmount"));
        rightTableColumns.get(8).setCellValueFactory(new PropertyValueFactory<TableRow, String>("leftInTheEndTotalPrice"));
        rightTableColumns.get(9).setCellValueFactory(new PropertyValueFactory<TableRow, String>("comment"));
        rightTable.setItems(data);
        setUpColumnsFactory(leftTableColumns);
        setUpColumnsFactory(rightTableColumns);
        leftTableColumns.get(0).setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableRow, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableRow, String> t) {
                ((TableRow) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setNumber(t.getNewValue());
                populateListLeft();
            }
        });
        leftTableColumns.get(1).setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableRow, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableRow, String> t) {
                ((TableRow) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setTitle(t.getNewValue());
                populateListLeft();
            }
        });
        leftTableColumns.get(2).setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableRow, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableRow, String> t) {
                ((TableRow) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setCode(t.getNewValue());
                populateListLeft();
            }
        });
        leftTableColumns.get(3).setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableRow, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableRow, String> t) {
                ((TableRow) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setPrice(t.getNewValue());
                populateListLeft();
            }
        });
        leftTableColumns.get(4).setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableRow, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableRow, String> t) {
                ((TableRow) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setAmount(t.getNewValue());
                populateListLeft();
            }
        });
        leftTableColumns.get(5).setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableRow, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableRow, String> t) {
                ((TableRow) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setTotalPrice(t.getNewValue());
                populateListLeft();
            }
        });
        leftTableColumns.get(6).setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableRow, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableRow, String> t) {
                ((TableRow) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setIncome(t.getNewValue());
                populateListLeft();
            }
        });
        leftTableColumns.get(7).setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableRow, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableRow, String> t) {
                ((TableRow) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setSentToStorageOrCompensatedByRecipient(t.getNewValue());
                populateListLeft();
            }
        });
        rightTableColumns.get(0).setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableRow, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableRow, String> t) {
                ((TableRow) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setNumber(t.getNewValue());
                populateListRight();
            }
        });
        rightTableColumns.get(1).setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableRow, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableRow, String> t) {
                ((TableRow) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setTitle(t.getNewValue());
                populateListRight();
            }
        });
        rightTableColumns.get(2).setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableRow, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableRow, String> t) {
                ((TableRow) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setCode(t.getNewValue());
                populateListRight();
            }
        });
        rightTableColumns.get(3).setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableRow, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableRow, String> t) {
                ((TableRow) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setBrokenAmount(t.getNewValue());
                populateListRight();
            }
        });
        rightTableColumns.get(4).setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableRow, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableRow, String> t) {
                ((TableRow) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setBrokenTotalPrice(t.getNewValue());
                populateListRight();
            }
        });
        rightTableColumns.get(5).setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableRow, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableRow, String> t) {
                ((TableRow) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setLostAmount(t.getNewValue());
                populateListRight();
            }
        });
        rightTableColumns.get(6).setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableRow, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableRow, String> t) {
                ((TableRow) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setLostTotalPrice(t.getNewValue());
                populateListRight();
            }
        });
        rightTableColumns.get(7).setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableRow, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableRow, String> t) {
                ((TableRow) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setLeftInTheEndAmount(t.getNewValue());
                populateListRight();
            }
        });
        rightTableColumns.get(8).setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableRow, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableRow, String> t) {
                ((TableRow) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setLeftInTheEndTotalPrice(t.getNewValue());
                populateListRight();
            }
        });
        rightTableColumns.get(9).setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableRow, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableRow, String> t) {
                ((TableRow) t.getTableView().getItems().get(t.getTablePosition().getRow()))
                        .setComment(t.getNewValue());
            }
        });
        primaryStage.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().getName().equals("Delete")){
                    TabPane tabPane = (TabPane) primaryStage.getScene().lookup("#tabPane");
                    TableRow removedItem = new TableRow();
                    if (tabPane.getSelectionModel().getSelectedIndex() == 0){
                        removedItem = (TableRow) leftTable.getSelectionModel().getSelectedItem();
                    }
                    else if (tabPane.getSelectionModel().getSelectedIndex() == 1){
                        removedItem = (TableRow) rightTable.getSelectionModel().getSelectedItem();
                    }
                    data.remove(removedItem);
                    if (!removedItem.isEmpty()){
                        for (int i = 0; i < data.size(); i++) {
                            data.get(i).setNumber(Integer.toString(i + 1));
                            if (tabPane.getSelectionModel().getSelectedIndex() == 0)
                                populateListLeft();
                            else
                                populateListRight();
                        }
                    }
                }
            }
        });
    }

    private void setUpColumnsFactory(ObservableList<TableColumn> columns){
        for (TableColumn column : columns) {
            column.setCellFactory(TextFieldTableCell.forTableColumn());
        }
    }

    private void setUpAddFields() throws IOException {
        ComboBox dishName = (ComboBox) primaryStage.getScene().lookup("#dishName");
        ComboBox dishCode = (ComboBox) primaryStage.getScene().lookup("#dishCode");
        TextField dishPrice = (TextField) primaryStage.getScene().lookup("#dishPrice");
        dishName.setItems(FXCollections.observableList(FileManager.getDishNames()));
        dishCode.setItems(FXCollections.observableList(FileManager.getDishCodes()));
//        TextFields.bindAutoCompletion(dishName.getEditor(), dishName.getItems());
        FxUtilTest.autoCompleteComboBoxPlus(dishName, (String typedText, String itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.equals(typedText));
        FxUtilTest.autoCompleteComboBoxPlus(dishCode, (String typedText, String itemToCompare) -> itemToCompare.toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.equals(typedText));
        dishCode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String nameToSet = "";
                String priceToSet = "";
                if (((String) dishCode.getValue()) != null && !((String) dishCode.getValue()).equals(""))
                    try {
                        nameToSet = FileManager.getDishNameByCode(Integer.parseInt((String) dishCode.getValue()));
                        priceToSet = FileManager.getDishPriceByCode(Integer.parseInt((String) dishCode.getValue()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                if (!nameToSet.equals(""))
                    dishName.setValue(nameToSet);
                if (!priceToSet.equals(""))
                    dishPrice.setText(priceToSet);
            }
        });
        dishName.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String codeToSet = "";
                String priceToSet = "";
                try {
                    codeToSet = FileManager.getDishCodeByName((String) dishName.getValue());
                    if (!codeToSet.equals(""))
                        priceToSet = FileManager.getDishPriceByCode(Integer.parseInt(codeToSet));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!codeToSet.equals(""))
                    dishCode.setValue(codeToSet);
                if (!priceToSet.equals(""))
                    dishPrice.setText(priceToSet);
            }
        });
        Button addButton = (Button) primaryStage.getScene().lookup("#addButton");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                data.add(new TableRow(Integer.toString(data.size() + 1), ((String) dishName.getValue()), ((String) dishCode.getValue()), dishPrice.getText()));
                TabPane tabPane = (TabPane) primaryStage.getScene().lookup("#tabPane");
                if (tabPane.getSelectionModel().getSelectedIndex() == 0)
                    populateListLeft();
                else
                    populateListRight();
            }
        });
    }

    private void setUpOrg() throws IOException {
        ComboBox organization = (ComboBox) primaryStage.getScene().lookup("#organization");
        organization.setItems(FXCollections.observableList(FileManager.getOrganizations()));
        ComboBox subdivision = (ComboBox) primaryStage.getScene().lookup("#subdivision");
        organization.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (organization.getValue() == ""){
                    subdivision.setValue("");
                    subdivision.setDisable(true);
                }
                else{
                    subdivision.setDisable(false);
                    try {
                        subdivision.setItems(
                                FXCollections.observableList(
                                        FileManager.getSubdivisions((String) organization.getValue())
                                )
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private void upgradeTable(String id){
        ScrollPane scroll = (ScrollPane) primaryStage.getScene().lookup(id);
        TableView table = (TableView) scroll.getContent();
        ArrayList<TableColumn> columns = new ArrayList<>(table.getColumns());
        table.getColumns().addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change change) {
                change.next();
                if(change.wasReplaced()) {
                    table.getColumns().clear();
                    table.getColumns().addAll(columns);
                }
            }
        });
        for (Object column : table.getColumns()) {
            makeHeaderWrappable((TableColumn) column);
        }
    }

    private void makeHeaderWrappable(TableColumn col) {
        Label label = new Label(col.getText());
        label.setStyle("-fx-padding: 8px;");
        label.setWrapText(true);
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);

        StackPane stack = new StackPane();
        stack.getChildren().add(label);
        stack.prefWidthProperty().bind(col.widthProperty().subtract(5));
        label.prefWidthProperty().bind(stack.prefWidthProperty());
        col.setGraphic(stack);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
