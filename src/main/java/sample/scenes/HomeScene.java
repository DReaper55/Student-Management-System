package sample.scenes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.database.MongoDB;
import sample.utils.Utils;


public class HomeScene extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setMaximized(true);

//        Nodes/Objects placed in a vertical order
        VBox rootLayout = new VBox(20);
        rootLayout.setAlignment(Pos.TOP_CENTER);

//        Title of page
        Text pageTitle = new Text("Students Record");
        pageTitle.setFont(Font.font(50.0));
        pageTitle.setStyle("-fx-text-style: bold;");


        HBox buttons = showButtons();

        VBox studentTableLayout = new VBox();

//        Little tweak to enable refreshing of table
//        by first deleting existing table then replacing it with
//        new table and updated records

        final JFXTreeTableView[] studentTable = {new Utils().loadStudentsTable(new MongoDB().getAllStudents())}; // utility class to load the table
        studentTable[0].setFixedCellSize(40.0);
        studentTableLayout.getChildren().add(studentTable[0]);

//        refresh the student table
        JFXButton refreshButton = new JFXButton("Refresh");
        refreshButton.setButtonType(JFXButton.ButtonType.RAISED);
        refreshButton.setBackground(new Background(new BackgroundFill(Color.WHEAT, new CornerRadii(10.0), null)));
        refreshButton.setOnAction(actionEvent -> {
            studentTableLayout.getChildren().remove(0);
            studentTable[0] = new Utils().loadStudentsTable(new MongoDB().getAllStudents());
            studentTableLayout.getChildren().add(studentTable[0]);
        });

        rootLayout.getChildren().addAll(pageTitle, buttons, refreshButton, studentTableLayout);

        Scene rootScene = new Scene(rootLayout, 900, 500);
        primaryStage.setScene(rootScene);
        primaryStage.show();
    }


    private HBox showButtons() {
        JFXButton enterNewRecord = new JFXButton("New");
        enterNewRecord.setButtonType(JFXButton.ButtonType.RAISED);
        enterNewRecord.setBackground(new Background(new BackgroundFill(Color.AQUA, new CornerRadii(5.0), null)));
        enterNewRecord.setFont(Font.font(30.0));
        enterNewRecord.setMinHeight(50.0);
        enterNewRecord.setMinWidth(150.0);
        enterNewRecord.setOnAction(actionEvent -> new CreateNewRecord().start(new Stage(StageStyle.UNIFIED)));

        JFXButton searchRecords = new JFXButton("Search");
        searchRecords.setBackground(new Background(new BackgroundFill(Color.AQUA, new CornerRadii(5.0), null)));
        searchRecords.setButtonType(JFXButton.ButtonType.RAISED);
        searchRecords.setFont(Font.font(30.0));
        searchRecords.setMinHeight(50.0);
        searchRecords.setMinWidth(150.0);
        searchRecords.setOnAction(actionEvent -> new SearchRecord().start(new Stage(StageStyle.DECORATED)));

        HBox buttons = new HBox(enterNewRecord, searchRecords);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(50.0, 0.0, 0.0, 0.0)); // Add top padding
        buttons.setSpacing(223.0);

        return buttons;
    }
}
