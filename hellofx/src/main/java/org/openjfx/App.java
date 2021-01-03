package org.openjfx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage primaryStage) throws IOException {
        //scene = new Scene(loadFXML("primary"), 640, 480);
        primaryStage.setTitle("NoteCrash");
        primaryStage.setMinWidth(500);
        primaryStage.setMinHeight(500);
        VBox vBox = new VBox();

        Menu menuFile = new Menu("File");
        List<String> menuNames = Arrays.asList("New", "Open", "Save", "SaveAs", "Exit");
        List<MenuItem> menuItems = new ArrayList<>();
        EventHandler<ActionEvent> action = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                MenuItem mItem = (MenuItem) actionEvent.getSource();
                String name = mItem.getText();
                menuBarAction(name);
            }
        };
        for(var name : menuNames){
            var menuItem = new MenuItem(name);
            menuItem.setOnAction(action);
            menuItems.add(menuItem);
            menuFile.getItems().add(menuItem);
        }

        Menu menuSearch = new Menu("Search");
        MenuItem menuItemSearch = new MenuItem("search");
        menuItemSearch.setOnAction(action);
        menuSearch.getItems().add(menuItemSearch);
        MenuBar menuBar = new MenuBar();

        menuBar.getMenus().addAll(menuFile, menuSearch);
        vBox.getChildren().add(menuBar);

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setHgap(5);
        grid.setVgap(5);
        Label labelAuthor = new Label("Author: ");
        TextField textFieldAuthor = new TextField();
        GridPane.setConstraints(labelAuthor, 0, 0);
        GridPane.setConstraints(textFieldAuthor, 1, 0);
        grid.getChildren().addAll(labelAuthor, textFieldAuthor);
        Label labelCategory = new Label("Category: ");
        TextField textFieldCategory = new TextField();
        GridPane.setConstraints(labelCategory, 0, 1);
        GridPane.setConstraints(textFieldCategory, 1, 1);
        grid.getChildren().addAll(labelCategory, textFieldCategory);

        GridPane grid2 = new GridPane();
        grid2.setPadding(new Insets(10, 10, 10, 10));
        TextArea textArea = new TextArea();
        //TextField textFieldArea = new TextField();
        GridPane.setConstraints(textArea, 0, 2);
        textArea.setPrefSize(500, 300);
        grid2.getChildren().add(textArea);

        VBox vb = new VBox();
        vb.getChildren().addAll(grid, grid2);
        BorderPane root = new BorderPane();
        root.setTop(vBox);
        root.setCenter(vb);

        Scene scene = new Scene(root, 960, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void menuBarAction(String name) {
        switch(name){
            case "New":
                menuBarActionNew();
                break;
            case "Load":
                menuBarActionLoad();
                break;
            case "Save":
                menuBarActionSave();
                break;
            case "SaveAs":
                menuBarActionSaveAs();
                break;
            case "Exit":
                menuBarActionExit();
                break;
            case "search":
                menuBarActionSearch();
                break;
        }
    }

    private void menuBarActionSearch() {
    }

    private void menuBarActionExit() {
    }

    private void menuBarActionSaveAs() {
    }

    private void menuBarActionSave() {
    }

    private void menuBarActionLoad() {
    }

    private void menuBarActionNew() {
    }


    public static void main(String[] args) {
        launch();
    }

}