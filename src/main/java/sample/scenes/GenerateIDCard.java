package sample.scenes;

import com.jfoenix.controls.JFXButton;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.database.MongoDB;
import sample.models.Student;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GenerateIDCard extends Application {
    private String matricNumber;
    private Student student;

    public GenerateIDCard(String matricNumber){this.matricNumber = matricNumber;};

    @Override
    public void start(Stage primaryStage) {
        student = new MongoDB().getStudent(matricNumber);

        VBox generatedCardLayout = generateCard(student);

        VBox rootLayout = new VBox();

        JFXButton printCard = new JFXButton("[Print]");
        printCard.setBackground(new Background(new BackgroundFill(Color.GREENYELLOW, new CornerRadii(5.0), null)));
        printCard.setMinSize(50.0, 30.0);
        printCard.setFont(Font.font(15.0));
        printCard.setAlignment(Pos.CENTER);

        rootLayout.getChildren().addAll(generatedCardLayout, printCard);
        primaryStage.setScene(new Scene(rootLayout));
        primaryStage.setMaxHeight(400.0);
        primaryStage.setMaxWidth(250.0);
        primaryStage.setMinHeight(400.0);
        primaryStage.setMinWidth(250.0);
        primaryStage.show();

        printCard.setOnAction(actionEvent -> printIDCard(generatedCardLayout, primaryStage));
    }

    private void printIDCard(VBox rootLayout, Stage primaryStage) {
        PrinterJob job = PrinterJob.createPrinterJob();

        boolean proceed = job.showPrintDialog(primaryStage);
        if (proceed) {
            boolean printed = job.printPage(rootLayout);

            if(printed){
                job.endJob();

                student.setGottenIDCard("True");
                new MongoDB().updateStudent(student, matricNumber);
                primaryStage.close();
            }
        }
    }

    private VBox generateCard(Student student) {
        VBox cardLayout = new VBox();
        cardLayout.setAlignment(Pos.CENTER);
        cardLayout.setSpacing(5.0);
        cardLayout.setStyle("-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: grey;");

        Label university = new Label("UNIVERSITY OF CALABAR");
        university.setFont(Font.font(15.0));
        university.setStyle("-fx-text-style: bold;");

        Label locale = new Label("CALABAR");
        locale.setFont(Font.font(15.0));
        locale.setStyle("-fx-text-style: bold;");

        VBox studentImage = loadImagesView(student);

        Label studentName = new Label(student.getFullName());
        studentName.setFont(Font.font(15.0));
        studentName.setStyle("-fx-text-style: bold;");

        Label studentFaculty = new Label(student.getFaculty());
        studentFaculty.setFont(Font.font(15.0));
        studentFaculty.setStyle("-fx-text-style: bold;");

        Label studentDepartment = new Label(student.getDepartment());
        studentDepartment.setFont(Font.font(15.0));
        studentDepartment.setStyle("-fx-text-style: bold;");

        Label studentMatric = new Label(student.getMatricNumber());
        studentMatric.setFont(Font.font(15.0));
        studentMatric.setStyle("-fx-text-style: bold;");

        Label studentdob = new Label(student.getDateOfBirth());
        studentdob.setFont(Font.font(15.0));
        studentdob.setStyle("-fx-text-style: bold;");

        cardLayout.getChildren().addAll(university, locale, studentImage, studentName,
                studentFaculty, studentDepartment, studentMatric, studentdob);

        return cardLayout;
    }

    private VBox loadImagesView(Student student) {
//        Image View
        ImageView imageView = new ImageView();
        imageView.minHeight(100.0);
        imageView.maxHeight(100.0);
        imageView.minWidth(100.0);
        imageView.maxWidth(100.0);

//        adjust image size to fit layout
        imageView.setFitHeight(100.0);
        imageView.setFitWidth(100.0);

        HBox imageBox = new HBox(imageView);
        imageBox.setAlignment(Pos.CENTER);
        imageBox.setMaxWidth(100.0);
        imageBox.setStyle("-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: grey;");


//        load current image
        if (student != null) {
            try {
                FileInputStream imageStream = new FileInputStream(student.getDisplayPic());
                Image image = new Image(imageStream);
                imageView.setImage(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


        VBox imageLayout = new VBox(imageBox);
        imageLayout.setAlignment(Pos.CENTER);
        imageLayout.setPadding(new Insets(0.0, 50.0, 0.0, 0.0));

        return imageLayout;
    }

}
