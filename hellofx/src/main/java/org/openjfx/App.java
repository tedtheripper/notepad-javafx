package org.openjfx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.stage.Window;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage mainStage;
    private TextField textFieldAuthor;
    private TextField textFieldCategory;
    private TextArea textArea;
    private String currOpenedPath = "";
    private boolean cleared;


    @Override
    public void start(Stage primaryStage) throws IOException {
        mainStage = primaryStage;
        mainStage.setTitle("NoteCrash");
        mainStage.setMinWidth(500);
        mainStage.setMinHeight(500);
        VBox vBox = new VBox();

        Menu menuFile = new Menu("File");
        List<String> menuNames = Arrays.asList("New", "Open", "Save", "SaveAs", "Exit");
        List<MenuItem> menuItems = new ArrayList<>();
        EventHandler<ActionEvent> action = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                MenuItem mItem = (MenuItem) actionEvent.getSource();
                String name = mItem.getText();
                menuBarAction(actionEvent, name);
            }
        };
        for(var name : menuNames){
            var menuItem = new MenuItem(name);
            menuItem.setOnAction(action);
            menuItems.add(menuItem);
            menuFile.getItems().add(menuItem);
        }
        Menu menuAbout = new Menu("About");
        menuAbout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
//        Menu menuSearch = new Menu("Search");
//        MenuItem menuItemSearch = new MenuItem("search");
//        menuItems.add(menuItemSearch);
//        menuItemSearch.setOnAction(action);
//        menuSearch.getItems().add(menuItemSearch);
        MenuBar menuBar = new MenuBar();

        menuBar.getMenus().addAll(menuFile);
        vBox.getChildren().add(menuBar);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(5);
        grid.setVgap(5);
        Label labelAuthor = new Label("Author: ");
        textFieldAuthor = new TextField();
        textFieldAuthor.setPrefColumnCount(20);
        GridPane.setConstraints(labelAuthor, 0, 0);
        GridPane.setConstraints(textFieldAuthor, 1, 0);
        grid.getChildren().addAll(labelAuthor, textFieldAuthor);
        Label labelCategory = new Label("Category: ");
        textFieldCategory = new TextField();
        textFieldCategory.setPrefColumnCount(20);
        GridPane.setConstraints(labelCategory, 0, 1);
        GridPane.setConstraints(textFieldCategory, 1, 1);
        grid.getChildren().addAll(labelCategory, textFieldCategory);

        GridPane grid2 = new GridPane();
        grid2.setPadding(new Insets(10, 10, 10, 10));
        textArea = new TextArea();
        //TextField textFieldArea = new TextField();
        GridPane.setConstraints(textArea, 0, 2);
        textArea.setPrefSize(500, 300);
        textArea.setWrapText(true);
        grid2.getChildren().add(textArea);

        VBox vb = new VBox();
        vb.getChildren().addAll(grid, grid2);
        BorderPane root = new BorderPane();
        root.setTop(vBox);
        root.setCenter(vb);

        Scene scene = new Scene(root, 960, 600);
        mainStage.setScene(scene);
        mainStage.show();
    }

    private void menuBarAction(ActionEvent actionEvent, String name) {
        switch(name){
            case "New":
                menuBarActionNew(actionEvent);
                break;
            case "Open":
                menuBarActionLoad(actionEvent);
                break;
            case "Save":
                menuBarActionSave(actionEvent);
                break;
            case "SaveAs":
                menuBarActionSaveAs(actionEvent);
                break;
            case "Exit":
                menuBarActionExit(actionEvent);
                break;
//            case "search":
//                menuBarActionSearch(actionEvent);
//                break;
        }
    }

    private boolean textFieldsNotEmpty(){
        return !textFieldAuthor.getText().isEmpty() || !textFieldCategory.getText().isEmpty() || !textArea.getText().isEmpty();
    }

//    private void menuBarActionSearch(ActionEvent actionEvent) {
//    }

    private void menuBarActionExit(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure? Unsaved changes will be lost.");
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                mainStage.close();
            }
        });
    }

    private void menuBarActionSaveAs(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(mainStage);
        if (!file.isFile() && file.getName().split("\\.")[1].equals("txt") && !file.isDirectory()){
            try {
                if(!file.createNewFile()){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "There was an error while saving the file.");
                    alert.setHeaderText(null);
                    alert.showAndWait();
                    return;
                }
            } catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        currOpenedPath = file.getPath();
        menuBarActionSave(actionEvent);
    }

    private void menuBarActionSave(ActionEvent actionEvent) {
        if(!currOpenedPath.equals("")){
            try{
                FileWriter fileWriter = new FileWriter(currOpenedPath);
                fileWriter.write(textFieldAuthor.getText() + "\n");
                fileWriter.write(textFieldCategory.getText() + "\n");
                fileWriter.write(textArea.getText());
                fileWriter.close();
                File file = new File(currOpenedPath);
                mainStage.setTitle("NoteCrash - " + file.getName());
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "File saved.");
                alert.setHeaderText(null);
                alert.showAndWait();
            } catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "No currently opened file.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    private void menuBarActionLoad(ActionEvent actionEvent) {
        if(clearTextFields()){
            loadFileDataIntoApp();
        }
    }

    private void loadFileDataIntoApp() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open text file");
        File file = fileChooser.showOpenDialog(mainStage);
        if(file != null && file.canRead() && file.canRead() && file.getName().matches(".\\.txt")){
            try {
                mainStage.setTitle("NoteCrash - " + file.getName());
                Scanner scanner = new Scanner(file);
                String author = scanner.nextLine();
                String category = scanner.nextLine();
                StringBuilder data = new StringBuilder();
                while(scanner.hasNextLine()){
                    data.append(scanner.nextLine()).append("\n");
                }
                scanner.close();
                textFieldAuthor.setText(author);
                textFieldCategory.setText(category);
                textArea.setText(data.toString());
                currOpenedPath = file.getPath();
            }catch (IOException ex){
                System.out.println(ex.getMessage());
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "The was an error loading the file.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    private void menuBarActionNew(ActionEvent actionEvent) {
        clearTextFields();
        currOpenedPath = "";
    }

    private boolean clearTextFields() {
        AtomicBoolean clrd = new AtomicBoolean(false);
        if(textFieldsNotEmpty()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure? Unsaved changes will be lost.");
            alert.setHeaderText(null);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    textFieldAuthor.clear();
                    textFieldCategory.clear();
                    textArea.clear();
                    mainStage.setTitle("NoteCrash");
                    clrd.set(true);
                } else {
                    clrd.set(false);
                }
            });
        }
        return clrd.get();
    }


    public static void main(String[] args) {
        launch();
    }

}