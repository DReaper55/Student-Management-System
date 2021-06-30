package sample.utils;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.beans.value.ObservableValueBase;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.database.MongoDB;
import sample.models.Student;
import sample.scenes.CreateUpdateRecord;
import sample.scenes.ViewRecords;

import java.util.ArrayList;

public class Utils {

//    utility class to be used by SearchRecord and HomeScene classes
//    to show student's records

    public JFXTreeTableView loadStudentsTable(ArrayList<Student> students) {
        JFXTreeTableView studentTable = new JFXTreeTableView();
        studentTable.setShowRoot(false);

//        Create table and columns
        JFXTreeTableColumn<Student, String> fullNameColumn = new JFXTreeTableColumn<>("Full Name");
        fullNameColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("fullName"));

        JFXTreeTableColumn<Student, String> facultyColumn = new JFXTreeTableColumn<>("Faculty");
        facultyColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("faculty"));

        JFXTreeTableColumn<Student, String> matricNumColumn = new JFXTreeTableColumn<>("Matric Number");
        matricNumColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("matricNumber"));

        JFXTreeTableColumn<Student, String> departmentColumn = new JFXTreeTableColumn<>("Department");
        departmentColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("department"));

        JFXTreeTableColumn<Student, String> genderColumn = new JFXTreeTableColumn<>("Gender");
        genderColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("gender"));

        JFXTreeTableColumn<Student, String> dateOfBirthColumn = new JFXTreeTableColumn<>("Date Of Birth");
        dateOfBirthColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("dateOfBirth"));

        JFXTreeTableColumn<Student, String> isGottenIDCardColumn = new JFXTreeTableColumn<>("IsGottenIDCard");
        isGottenIDCardColumn.setCellValueFactory(value -> new ObservableValueBase<String>() {
            @Override
            public String getValue() {
                return value.getValue().getValue().isGottenIDCard();
            }
        });

        JFXTreeTableColumn<Student, String> editButtonColumn = new JFXTreeTableColumn<>("Actions");
        editButtonColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("edit"));

        JFXTreeTableColumn<Student, String> deleteButtonColumn = new JFXTreeTableColumn<>("Actions");
        deleteButtonColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("delete"));

        JFXTreeTableColumn<Student, String> viewButtonColumn = new JFXTreeTableColumn<>("Actions");
        viewButtonColumn.setCellValueFactory(new TreeItemPropertyValueFactory<>("view"));

        studentTable.getColumns().addAll(
                fullNameColumn, matricNumColumn, facultyColumn,
                departmentColumn, genderColumn, dateOfBirthColumn, isGottenIDCardColumn,
                editButtonColumn, viewButtonColumn, deleteButtonColumn
        );

//        display error if student record does not exists in db
        try {
            if (students.get(0).getMatricNumber() == null) {
                // error for searched record not found in db
                studentTable.setPlaceholder(new Label("Record does not exist"));
                return studentTable;
            }
        } catch (IndexOutOfBoundsException e) {
            // error if database has no record
            studentTable.setPlaceholder(new Label("No record in database"));
        }

        TreeItem studentItem = new TreeItem();

        students.forEach(student -> {
//
//      ....................Edit Button......................
//
            JFXButton editBtn = new JFXButton("Edit");
            editBtn.setButtonType(JFXButton.ButtonType.FLAT);
            editBtn.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, new CornerRadii(3.0), null)));
            editBtn.setMinSize(50.0, 30.0);

            // edit button action to go to createNewRecord stage
            editBtn.setOnAction(actionEvent -> new CreateUpdateRecord(student.getMatricNumber()).start(new Stage(StageStyle.DECORATED)));
            student.setEdit(editBtn);
//
//      ....................View Button......................
//
            JFXButton viewBtn = new JFXButton("View");
            viewBtn.setButtonType(JFXButton.ButtonType.FLAT);
            viewBtn.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, new CornerRadii(3.0), null)));
            viewBtn.setMinSize(50.0, 30.0);

            // view button action to go to viewRecords stage
            viewBtn.setOnAction(actionEvent -> new ViewRecords(student.getMatricNumber()).start(new Stage(StageStyle.DECORATED)));
            student.setView(viewBtn);
//
//      ....................Delete Button......................
//
            // delete button to remove record from database
            JFXButton deleteBtn = new JFXButton("Delete");
            deleteBtn.setButtonType(JFXButton.ButtonType.FLAT);
            deleteBtn.setBackground(new Background(new BackgroundFill(Color.PALEVIOLETRED, new CornerRadii(3.0), null)));
            deleteBtn.setMinSize(50.0, 30.0);
            deleteBtn.setOnAction(actionEvent -> new MongoDB().deleteStudent(student.getMatricNumber()));
            student.setDelete(deleteBtn);

            studentItem.getChildren().add(new TreeItem(student));
            studentTable.setRoot(studentItem);
        });

        return studentTable;
    }

}
