package sample.scenes;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.TextField;
import com.jfoenix.validation.RegexValidator;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.StringLengthValidator;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.database.MongoDB;
import sample.models.Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ViewRecords extends Application {
    private String matricNumber;
    private Student currentStudentRecord;
    private MongoDB mongoDB = new MongoDB();

    public ViewRecords(String matricNumber) {
        this.matricNumber = matricNumber;
    }

    @Override
    public void start(Stage primaryStage) {
        /* *******************************************************
         * Using arrays to store objects so they can be declared 'final'
         * and still be accessed/modified within methods
         * *******************************************************
         */

        final VBox[] imagePicker = {new VBox()};
        final GridPane[] userDetails = {new GridPane()};

        VBox rootLayout = new VBox(20);
        rootLayout.setAlignment(Pos.TOP_CENTER);

        JFXButton previousBtn = new JFXButton("<- Previous");
        previousBtn.setBackground(new Background(new BackgroundFill(Color.GREENYELLOW, new CornerRadii(5.0), null)));
        previousBtn.setMinSize(150.0, 50.0);
        previousBtn.setFont(Font.font(18.0));


        JFXButton nextBtn = new JFXButton("Next ->");
        nextBtn.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, new CornerRadii(5.0), null)));
        nextBtn.setMinSize(150.0, 50.0);
        nextBtn.setFont(Font.font(18.0));

        HBox buttonsLayout = new HBox(previousBtn, nextBtn);
        buttonsLayout.setAlignment(Pos.BASELINE_CENTER);
        buttonsLayout.setSpacing(30.0);


//        To show the current student's records on initialization of stage/window
        if (matricNumber != null) {
            currentStudentRecord = mongoDB.getStudent(matricNumber);
            imagePicker[0] = loadImagesView(currentStudentRecord);
            userDetails[0] = getUserDetails(currentStudentRecord);
            rootLayout.getChildren().addAll(imagePicker[0], userDetails[0], buttonsLayout);
        }

//        Get all student's record from database
        ArrayList<Student> studentArrayList = mongoDB.getAllStudents();

//        Iterate through each student's record to find currently
//        showing student's index in database
        for (int i = 0; i < studentArrayList.size(); i++) {

            if (studentArrayList.get(i).getMatricNumber().equals(matricNumber)) {
//
//                save the currently showing student's index to be
//                used to go to the previous or next record
//
                final int[] finalI = {i};

                previousBtn.setOnAction(actionEvent -> {
                    if (finalI[0] != 0) {
//                        get and display record

                        currentStudentRecord = studentArrayList.get(finalI[0] - 1);

                        imagePicker[0] = loadImagesView(currentStudentRecord);
                        userDetails[0] = getUserDetails(currentStudentRecord);

                        rootLayout.getChildren().removeAll(imagePicker[0], userDetails[0], buttonsLayout);

                        rootLayout.getChildren().add(0, imagePicker[0]);
                        rootLayout.getChildren().add(1, userDetails[0]);
                        rootLayout.getChildren().add(2, buttonsLayout);
                        rootLayout.getChildren().add(3, new VBox(30.0));

                        finalI[0]--;
                    }
                });

                nextBtn.setOnAction(actionEvent -> {
                    if (finalI[0] < studentArrayList.size() - 1) {
                        currentStudentRecord = studentArrayList.get(finalI[0] + 1);

                        imagePicker[0] = loadImagesView(currentStudentRecord);
                        userDetails[0] = getUserDetails(currentStudentRecord);

                        rootLayout.getChildren().removeAll(imagePicker[0], userDetails[0], buttonsLayout);

                        rootLayout.getChildren().add(0, imagePicker[0]);
                        rootLayout.getChildren().add(1, userDetails[0]);
                        rootLayout.getChildren().add(2, buttonsLayout);
                        rootLayout.getChildren().add(3, new VBox(30.0));


                        finalI[0]++;
                    }
                });
            }
        }


        Scene rootScene = new Scene(rootLayout, 700, 650);
        primaryStage.setScene(rootScene);
        primaryStage.show();
    }

    private VBox loadImagesView(Student student) {
//        Image View
        ImageView imageView = new ImageView();
        imageView.minHeight(200.0);
        imageView.maxHeight(200.0);
        imageView.minWidth(200.0);
        imageView.maxWidth(200.0);

//        adjust image size to fit layout
        imageView.setFitHeight(200.0);
        imageView.setFitWidth(200.0);

        HBox imageBox = new HBox(imageView);
        imageBox.setAlignment(Pos.TOP_RIGHT);
        imageBox.setMaxWidth(200.0);
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
        imageLayout.setAlignment(Pos.TOP_RIGHT);
        imageLayout.setPadding(new Insets(0.0, 50.0, 0.0, 0.0));

        return imageLayout;
    }

    private GridPane getUserDetails(Student student) {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0.0, 0.0, 0.0, 50.0));
        gridPane.setVgap(20.0);
        gridPane.setHgap(10.0);

//
//  ................First Name...................
//
        TextField firstNameInput = new TextField();

        firstNameInput.setMinSize(300, 40);
        firstNameInput.setFont(Font.font(15.0));
        firstNameInput.setDisable(true);

        firstNameInput.setStyle(
                "-fx-text-box-border: black;"
                        + "-fx-prompt-text-fill: black"

        );

        Label fNameLabel = new Label("First Name: ");
        fNameLabel.setFont(Font.font(18.0));
        fNameLabel.setLabelFor(firstNameInput);

        firstNameInput.setText(student.getFirstName());

//
//  ................Surname...................
//
        TextField surnameInput = new TextField();
        surnameInput.setMinSize(300, 40);
        surnameInput.setFont(Font.font(15.0));
        surnameInput.setDisable(true);

        surnameInput.setStyle(
                "-fx-text-box-border: black;"
                        + "-fx-prompt-text-fill: black"
        );

        Label lNameLabel = new Label("Last Name: ");
        lNameLabel.setFont(Font.font(18.0));
        lNameLabel.setLabelFor(surnameInput);

        surnameInput.setText(student.getLastName());

//
//  ................Matric Number...................
//
        TextField matricNumberInput = new TextField();
        matricNumberInput.setMinSize(300, 40);
        matricNumberInput.setFont(Font.font(15.0));
        matricNumberInput.setDisable(true);

        matricNumberInput.setStyle(
                "-fx-text-box-border: black;"
                        + "-fx-prompt-text-fill: black"

        );

        Label matricLabel = new Label("Matric Number: ");
        matricLabel.setFont(Font.font(18.0));
        matricLabel.setLabelFor(matricNumberInput);

        matricNumberInput.setText(student.getMatricNumber());

//
//  ................Department...................
//
        TextField departmentInput = new TextField();
        departmentInput.setMinSize(300, 40);
        departmentInput.setFont(Font.font(15.0));
        departmentInput.setDisable(true);

        departmentInput.setStyle(
                "-fx-text-box-border: black;"
                        + "-fx-prompt-text-fill: black"
        );

        Label departmentLabel = new Label("Department: ");
        departmentLabel.setFont(Font.font(18.0));
        departmentLabel.setLabelFor(departmentInput);

        departmentInput.setText(student.getDepartment());

//
//  ....................Get gender from User.......................
//
        TextField genderInput = new TextField();
        genderInput.setMinSize(300, 40);
        genderInput.setFont(Font.font(15.0));
        genderInput.setDisable(true);

        genderInput.setStyle(
                "-fx-text-box-border: black;"
                        + "-fx-prompt-text-fill: black"
        );

        Label genderLabel = new Label("Gender: ");
        genderLabel.setFont(Font.font(18.0));
        genderLabel.setLabelFor(genderInput);

        genderInput.setText(student.getGender());

//
//  .................Date picker for date of birth..............
//
        TextField dateOfBirthInput = new TextField();
        dateOfBirthInput.setMinSize(300, 40);
        dateOfBirthInput.setFont(Font.font(15.0));
        dateOfBirthInput.setDisable(true);

        dateOfBirthInput.setStyle(
                "-fx-text-box-border: black;"
                        + "-fx-prompt-text-fill: black"
        );

        Label dateOfBirthLabel = new Label("Date Of Birth: ");
        dateOfBirthLabel.setFont(Font.font(18.0));
        dateOfBirthLabel.setLabelFor(dateOfBirthInput);

        dateOfBirthInput.setText(student.getDateOfBirth());

//
//  ...............Arrange the layouts in rows...................
//
        gridPane.addRow(0, fNameLabel, firstNameInput);
        gridPane.addRow(1, lNameLabel, surnameInput);
        gridPane.addRow(2, matricLabel, matricNumberInput);
        gridPane.addRow(3, departmentLabel, departmentInput);
        gridPane.addRow(4, genderLabel, genderInput);
        gridPane.addRow(5, dateOfBirthLabel, dateOfBirthInput);

        return gridPane;
    }
}
