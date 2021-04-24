package sample.scenes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.database.MongoDB;
import sample.models.Student;
import sample.utils.Utils;

import java.util.ArrayList;

public class SearchRecord extends Application {

    @Override
    public void start(Stage primaryStage) {
        Text pageTitle = new Text("Search Record");
        pageTitle.setFont(Font.font(30.0));

        TextField matricNumber = new TextField();
        matricNumber.setMinSize(500, 40);
        matricNumber.setFont(Font.font(15.0));
        matricNumber.setPromptText("Enter matric number without '/'");
        matricNumber.setStyle(
                "-fx-text-box-border: black;"
                        + "-fx-prompt-text-fill: black"

        );

        JFXButton searchMatricBtn = new JFXButton("Search");
        searchMatricBtn.setButtonType(JFXButton.ButtonType.RAISED);
        searchMatricBtn.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(5.0), null)));


        HBox searchLayout = new HBox(matricNumber, searchMatricBtn);
        searchLayout.setAlignment(Pos.TOP_CENTER);

        VBox studentTableLayout = new VBox(); // layout for only the table

        searchMatricBtn.setFont(Font.font(18.0));
        searchMatricBtn.setOnAction(actionEvent -> {
            if (!matricNumber.getText().isEmpty()){
                JFXTreeTableView studentRecord = showResult(matricNumber.getText());
                try{
//                check if a table exists then remove and replace with new searched data
                    studentTableLayout.getChildren().remove(0);
                } catch (Exception e){
                    System.out.println(e.getMessage());
                } finally {
                    studentTableLayout.getChildren().add(studentRecord);
                }
            }
        });

        VBox rootLayout = new VBox(pageTitle, searchLayout, studentTableLayout);
        rootLayout.setAlignment(Pos.TOP_CENTER);
        rootLayout.setSpacing(20.0);

        Scene rootScene = new Scene(rootLayout, 650, 400);

        primaryStage.setScene(rootScene);
        primaryStage.show();
    }

    private JFXTreeTableView showResult(String matricNumber) {
        MongoDB mongoDB = new MongoDB();
        Student student = mongoDB.getStudent(matricNumber);

        ArrayList<Student> students = new ArrayList<>();
        students.add(student);

        return new Utils().loadStudentsTable(students);

    }
}
