package sample.database;

import com.mongodb.*;
import sample.models.Student;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class MongoDB {
    private static DBCollection studentsCollection;

    private static final String MATRIC = "Matric";
    private static final String FULLNAME = "Fullname";
    private static final String FACULTY = "Faculty";
    private static final String DEPARTMENT = "Department";
    private static final String GENDER = "Gender";
    private static final String DATEOFBIRTH = "DateOfBirth";
    private static final String DISPLAYPICTURE = "DisplayPicture";
    private static final String ISGOTTENIDCARD = "IsGottenIDCard";

    //    initialize mongo db
    private static void initialize() {
        try {
            MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
            DB database = mongoClient.getDB("StudentRecord");
            studentsCollection = database.getCollection("students");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

//    Insert new record
    public void insert(Student student) {
        initialize();

        BasicDBObject query = new BasicDBObject(MATRIC, student.getMatricNumber());
        if (!query.isEmpty()) { // assert that user does not exist in the database
            studentsCollection.insert(convertToDBObject(student));
        }
    }

//    Get a student record
    public Student getStudent(String matricNumber) {
        initialize();

        DBObject query = new BasicDBObject(MATRIC, matricNumber);
        DBCursor cursor = studentsCollection.find(query);
        return cursor.one() != null ? convertToStudent(cursor.one()) : new Student(); // confirm record exists
    }

//    Get all student records
    public ArrayList<Student> getAllStudents() {
        initialize();

        ArrayList<Student> students = new ArrayList<>();

        DBObject query = new BasicDBObject();
        DBCursor cursor = studentsCollection.find(query);
        while (cursor.hasNext()) {
            DBObject studentDbObject = cursor.next();
            students.add(convertToStudent(studentDbObject));
        }

//        students.forEach(student -> System.out.println(student.toString()));

        return students;
    }

//    Delete student's record
    public void deleteStudent(String matricNumber){
        initialize();

        BasicDBObject student = new BasicDBObject(MATRIC, matricNumber);
        studentsCollection.remove(student);
    }

//    Update student's record
    public void updateStudent(Student newStudent, String matricNumber) {
        initialize();

        BasicDBObject oldStudent = new BasicDBObject(MATRIC, matricNumber);
        studentsCollection.remove(oldStudent);
        studentsCollection.insert(convertToDBObject(newStudent));

    }

    //    to convert a class to a mongo database object
    private static DBObject convertToDBObject(Student student) {
        return new BasicDBObject(
                "Matric", student.getMatricNumber()
        ).append("Fullname", student.getFullName())
                .append("Faculty", student.getFaculty())
                .append("Department", student.getDepartment())
                .append("Gender", student.getGender())
                .append("DateOfBirth", student.getDateOfBirth())
                .append("DisplayPicture", student.getDisplayPic())
                .append("IsGottenIDCard", student.isGottenIDCard());
    }

    //    to convert db object back to student object
    private static Student convertToStudent(DBObject dbObject) {
        return new Student(
                dbObject.get(FULLNAME).toString(),
                dbObject.get(FACULTY).toString(),
                dbObject.get(DEPARTMENT).toString(),
                dbObject.get(MATRIC).toString(),
                dbObject.get(GENDER).toString(),
                dbObject.get(DATEOFBIRTH).toString(),
                dbObject.get(DISPLAYPICTURE).toString(),
                dbObject.get(ISGOTTENIDCARD).toString());
    }
}
