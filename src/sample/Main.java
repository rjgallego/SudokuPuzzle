package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import sample.Controllers.Controller;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{ ;
        Controller controller = new Controller();
        BorderPane borderPane = controller.buildBoard();
        primaryStage.setTitle("Sudoku Game");
        Scene scene = new Scene(borderPane, 370, 312);

        //adding stylesheet to scene
        scene.getStylesheets().add(this.getClass().getResource("Resources/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        //adding icon to scene
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("Resources/sudokuicon.png")));
        //disable users from resizing window
        primaryStage.setResizable(false);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
