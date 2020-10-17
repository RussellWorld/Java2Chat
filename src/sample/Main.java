package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    Controller c;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();

        Parent root = loader.load(getClass().getResourceAsStream("sample1.fxml"));
        primaryStage.setTitle("CatChat");
        c = loader.getController();

        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        //     primaryStage.getScene().getStylesheets().add("/css/Darkula.css");
        primaryStage.getIcons().add(new Image("img/icon.png"));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            c.Dispose();
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}