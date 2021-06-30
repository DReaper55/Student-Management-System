package sample.scenes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
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

public class CreateUpdateRecord extends Application {
    private Student student = new Student();

    private String matricNumber;
    private Student studentToBeUpdated;

    private Stage mPrimaryStage;

    CreateUpdateRecord() {

    }

    //    to assert that button clicked is to edit existing record
//    not to add new record
    public CreateUpdateRecord(String matricNumber) {
        this.matricNumber = matricNumber;
    }

    @Override
    public void start(Stage primaryStage) {
        mPrimaryStage = primaryStage; // get the current stage

//        get student if stage is loaded to edit existing record
        if (matricNumber != null) {
            MongoDB mongoDB = new MongoDB();
            studentToBeUpdated = mongoDB.getStudent(matricNumber);
        }

        VBox rootLayout = new VBox(20);
        rootLayout.setAlignment(Pos.TOP_CENTER);

//        The Image picker
        VBox imagePicker = loadImagesView(primaryStage);

//        Get student's details
        GridPane userDetails = getUserDetails();

//        Button to save user's data
        HBox actions = saveStudentButton();

        rootLayout.getChildren().addAll(imagePicker, userDetails, actions);

        Scene rootScene = new Scene(rootLayout, 700, 650);
        primaryStage.setScene(rootScene);
        primaryStage.show();
    }

    private HBox saveStudentButton() {
        JFXButton saveRecord = new JFXButton("Save");
        saveRecord.setButtonType(JFXButton.ButtonType.RAISED);
        saveRecord.setBackground(new Background(new BackgroundFill(Color.LAWNGREEN, new CornerRadii(5.0), null)));
        saveRecord.setMinSize(150.0, 50.0);
        saveRecord.setFont(Font.font(18.0));
        saveRecord.setOnAction(actionEvent -> {
            saveUserDetailsToDB(); // Save user's details to mongo database
        });

        HBox buttonLayout = new HBox(saveRecord);
        buttonLayout.setAlignment(Pos.BASELINE_RIGHT);
        buttonLayout.setPadding(new Insets(0.0, 20.0, 0.0, 0.0));

        return buttonLayout;
    }

    private void saveUserDetailsToDB() {
        MongoDB mongoDB = new MongoDB();

//        confirm user's intent to either update existing record
//        or insert new one to the database
        if (
            // Basic validation to make sure entries are not empty
                student.getMatricNumber() != null && student.getFullName() != null &&
                        student.getFaculty() != null && student.getGender() != null &&
                        student.getDateOfBirth() != null && student.getDisplayPic() != null &&
                        student.getDepartment() != null

        ) {
            if (studentToBeUpdated == null) {
                student.setGottenIDCard("False");
                mongoDB.insert(student);
                mPrimaryStage.close();
            } else {
                if(studentToBeUpdated.isGottenIDCard().equals("True")){
                    student.setGottenIDCard("True");
                } else {
                    student.setGottenIDCard("False");
                }
                mongoDB.updateStudent(student, matricNumber);
                mPrimaryStage.close();
            }
        }

    }

    private VBox loadImagesView(Stage primaryStage) {
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

//        File chooser button for the Image View
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll( // Filter file extensions for just images
                new FileChooser.ExtensionFilter("all", "*.jpeg", "*.jpg", ".png"),
                new FileChooser.ExtensionFilter(".jpeg", "*.jpeg"),
                new FileChooser.ExtensionFilter(".jpg", "*.jpg"),
                new FileChooser.ExtensionFilter(".png", "*.png")
        );

//        load current image to be updated
        if (studentToBeUpdated != null) {
            try {
                FileInputStream imageStream = new FileInputStream(studentToBeUpdated.getDisplayPic());
                Image image = new Image(imageStream);
                imageView.setImage(image);

                student.setDisplayPic(studentToBeUpdated.getDisplayPic());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

//        Pick new image and set path
        Button imageChooserButton = new Button("Choose image");
        imageChooserButton.setOnAction(actionEvent -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            try {
                InputStream imageStream = new FileInputStream(selectedFile);
                Image image = new Image(imageStream);
                imageView.setImage(image);

                student.setDisplayPic(selectedFile.getPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        VBox imageLayout = new VBox(imageBox, imageChooserButton);
        imageLayout.setAlignment(Pos.TOP_RIGHT);
        imageLayout.setPadding(new Insets(0.0, 50.0, 0.0, 0.0));

        return imageLayout;
    }

    private GridPane getUserDetails() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0.0, 0.0, 0.0, 50.0));
        gridPane.setVgap(20.0);
        gridPane.setHgap(10.0);

//
//  ................Full Name...................
//
        JFXTextField fullNameInput = new JFXTextField();

        fullNameInput.setLabelFloat(false);
        fullNameInput.setMinSize(300, 40);
        fullNameInput.setFont(Font.font(15.0));
        fullNameInput.setPromptText("Enter full name...");


        Label fNameLabel = new Label("Full Name: ");
        fNameLabel.setFont(Font.font(18.0));
        fNameLabel.setLabelFor(fullNameInput);


//
//  ....................Validators.........................
//
        RegexValidator validFirstName = new RegexValidator(); // to match special characters and numbers
        validFirstName.setRegexPattern("^[_A-Za-z- +]+([_A-Za-z- ]+)");
        validFirstName.setMessage("Numbers and special characters not allowed");

        fullNameInput.setValidators(
                new RequiredFieldValidator("No Input Given"), // when nothing is input
                validFirstName
        );

        fullNameInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                fullNameInput.validate();
            }
        });


//        To get the text_field input event and validate
        fullNameInput.setOnKeyReleased(keyEvent -> {
            if (fullNameInput.validate()) {
                student.setFullName(fullNameInput.getText());
            }

        });

//
//  ................Matric Number...................
//
        JFXTextField matricNumberInput = new JFXTextField();
        matricNumberInput.setLabelFloat(false);
        matricNumberInput.setMinSize(300, 40);
        matricNumberInput.setFont(Font.font(15.0));
        matricNumberInput.setPromptText("Enter Matric Number");

        Label matricLabel = new Label("Matric Number: ");
        matricLabel.setFont(Font.font(18.0));
        matricLabel.setLabelFor(matricNumberInput);

//
//        ................Validator....................
//
        RegexValidator validMatric = new RegexValidator(); // to match special characters and numbers
        validMatric.setRegexPattern("^[_0-9-+]+[_0-9-+]+/+([_0-9-]+)");
        validMatric.setMessage("Format: 12/345678900");


        matricNumberInput.setValidators(
                new RequiredFieldValidator("No Input Given"),
                new StringLengthValidator(12, "Value must not be more than "),
                validMatric
        );

        matricNumberInput.setOnKeyReleased(keyEvent -> {
                    if (matricNumberInput.validate())
                        student.setMatricNumber(matricNumberInput.getText());
                }
        );
//
//  ................Faculty...................
//
        JFXTextField facultyInput = new JFXTextField();
        facultyInput.setLabelFloat(false);
        facultyInput.setMinSize(300, 40);
        facultyInput.setFont(Font.font(15.0));
        facultyInput.setPromptText("Enter faculty...");

        Label facultyLabel = new Label("Faculty: ");
        facultyLabel.setFont(Font.font(18.0));
        facultyLabel.setLabelFor(facultyInput);

//
//  ....................Validators.........................
//
        RegexValidator validFaculty = new RegexValidator(); // to match special characters and numbers
        validFaculty.setRegexPattern("^[_A-Za-z- +]+([_A-Za-z- ]+)");
        validFaculty.setMessage("Numbers and special characters not allowed");

        facultyInput.setValidators(
                new RequiredFieldValidator("No Input Given"), // when nothing is input
                validFaculty
        );

        facultyInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                facultyInput.validate();
            }
        });


//        To get the text_field input event and validate
        facultyInput.setOnKeyReleased(keyEvent -> {
            if (facultyInput.validate())
                student.setFaculty(facultyInput.getText());
        });

//
//  ................Department...................
//
        JFXTextField departmentInput = new JFXTextField();
        departmentInput.setLabelFloat(false);
        departmentInput.setMinSize(300, 40);
        departmentInput.setFont(Font.font(15.0));
        departmentInput.setPromptText("Enter department...");

        Label departmentLabel = new Label("Department: ");
        departmentLabel.setFont(Font.font(18.0));
        departmentLabel.setLabelFor(departmentInput);

//
//  ....................Validators.........................
//
        RegexValidator validDepartment = new RegexValidator(); // to match special characters and numbers
        validDepartment.setRegexPattern("^[_A-Za-z- +]+([_A-Za-z- ]+)");
        validDepartment.setMessage("Numbers and special characters not allowed");

        departmentInput.setValidators(
                new RequiredFieldValidator("No Input Given"), // when nothing is input
                validDepartment
        );

        facultyInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                facultyInput.validate();
            }
        });

        departmentInput.setOnKeyReleased(keyEvent -> {
                    if (departmentInput.validate())
                        student.setDepartment(departmentInput.getText());
                }
        );

//
//  ....................Get gender from User.......................
//
        RadioButton maleGender = new RadioButton("Male");
        maleGender.setFont(Font.font(18.0));

        RadioButton femaleGender = new RadioButton("Female");
        femaleGender.setFont(Font.font(18.0));

        ToggleGroup toggleGenders = new ToggleGroup();
        maleGender.setToggleGroup(toggleGenders);
        femaleGender.setToggleGroup(toggleGenders);

        final RadioButton[] selectedGender = new RadioButton[1];
        maleGender.setOnMouseClicked(mouseEvent -> {
            selectedGender[0] = (RadioButton) toggleGenders.getSelectedToggle();
            student.setGender(selectedGender[0].getText());

        });

        femaleGender.setOnMouseClicked(mouseEvent -> {
            selectedGender[0] = (RadioButton) toggleGenders.getSelectedToggle();
            student.setGender(selectedGender[0].getText());
        });

//
//  .................Date picker for date of birth..............
//
        DatePicker dobPicker = new DatePicker();
        dobPicker.getEditor().setFont(Font.font(15.0));
        dobPicker.setMinWidth(300.0);
        dobPicker.setMinHeight(40);

        Label dobLabel = new Label("Date of Birth: ");
        dobLabel.setFont(Font.font(18.0));
        dobLabel.setLabelFor(dobPicker);

        LocalDate localDate = dobPicker.getValue();
        dobPicker.getEditor().setOnMouseClicked(mouseEvent -> {
            student.setDateOfBirth(dobPicker.getEditor().getText());
            System.out.println(localDate != null ? localDate.format(DateTimeFormatter.ISO_LOCAL_DATE) : "");
        });

//        load user's data to be updated
        if (studentToBeUpdated != null) {
            fullNameInput.setText(studentToBeUpdated.getFullName());
            facultyInput.setText(studentToBeUpdated.getFaculty());
            matricNumberInput.setText(studentToBeUpdated.getMatricNumber());
            departmentInput.setText(studentToBeUpdated.getDepartment());
            dobPicker.getEditor().setText(studentToBeUpdated.getDateOfBirth());

            if (studentToBeUpdated.getGender().equals("Male")) {
                maleGender.setSelected(true);
                student.setGender(studentToBeUpdated.getGender());
            } else {
                femaleGender.setSelected(true);
                student.setGender(studentToBeUpdated.getGender());
            }

            student.setFullName(studentToBeUpdated.getFullName());
            student.setFaculty(studentToBeUpdated.getFaculty());
            student.setMatricNumber(studentToBeUpdated.getMatricNumber());
            student.setDepartment(studentToBeUpdated.getDepartment());
            student.setDateOfBirth(studentToBeUpdated.getDateOfBirth());
        }

//
//  ...............Arrange the layouts in rows...................
//
        gridPane.addRow(0, fNameLabel, fullNameInput);
        gridPane.addRow(1, matricLabel, matricNumberInput);
        gridPane.addRow(2, facultyLabel, facultyInput);
        gridPane.addRow(3, departmentLabel, departmentInput);
        gridPane.addRow(4, maleGender, femaleGender);
        gridPane.addRow(5, dobLabel, dobPicker);

        return gridPane;
    }
}
