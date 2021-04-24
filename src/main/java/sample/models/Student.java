package sample.models;

import javafx.scene.control.Button;

public class Student {
    private String firstName, lastName, department, matricNumber, gender, dateOfBirth, displayPic;
    private int id;
    private Button edit, delete, view;

    public Student() {
    }

    public Student(String firstName, String lastName, String department, String matricNumber, String gender, String dateOfBirth, String displayPic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.matricNumber = matricNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.displayPic = displayPic;
    }

    public Button getEdit() {
        return edit;
    }

    public void setEdit(Button edit) {
        this.edit = edit;
    }

    public Button getView() {
        return view;
    }

    public void setView(Button view) {
        this.view = view;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    public String getDisplayPic() {
        return displayPic;
    }

    public void setDisplayPic(String displayPic) {
        this.displayPic = displayPic;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMatricNumber() {
        return matricNumber;
    }

    public void setMatricNumber(String matricNumber) {
        this.matricNumber = matricNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getFirstName() + "\n" + getLastName() + "\n" +
                getDepartment() + "\n" + getMatricNumber() + "\n" +
                getDateOfBirth() + "\n" +
                getGender() + "\n" + getDisplayPic() + "\n";
    }
}
