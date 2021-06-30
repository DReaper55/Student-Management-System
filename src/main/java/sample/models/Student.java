package sample.models;

import javafx.scene.control.Button;

public class Student {
    private String fullName, faculty, department, matricNumber, gender, dateOfBirth, displayPic, isGottenIDCard;
    private int id;

    private Button edit, delete, view;

    public Student() {
    }

    public Student(String fullName, String faculty, String department, String matricNumber, String gender, String dateOfBirth, String displayPic, String isGottenIDCard) {
        this.fullName = fullName;
        this.faculty = faculty;
        this.department = department;
        this.matricNumber = matricNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.displayPic = displayPic;
        this.isGottenIDCard = isGottenIDCard;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
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

    public String isGottenIDCard() {
        return isGottenIDCard;
    }

    public void setGottenIDCard(String gottenIDCard) {
        isGottenIDCard = gottenIDCard;
    }

    @Override
    public String toString() {
        return getFullName() + "\n" + getFaculty() + "\n" +
                getDepartment() + "\n" + getMatricNumber() + "\n" +
                getDateOfBirth() + "\n" +
                getGender() + "\n" + getDisplayPic() + "\n";
    }
}
