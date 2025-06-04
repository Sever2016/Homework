package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.*;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {

    private final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student {}", student.getName());
        return studentRepository.save(student);
    }

    public Student getStudentById(Long id) {
        logger.info("Was invoked method getStudentById");
        return studentRepository.findById(id).get();
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student}");
        return studentRepository.save(student);
    }

    public Student deleteStudent(Long id) {
        logger.info("Was invoked method for delete student");
        Student deletedStudent = studentRepository.findById(id).get();
        studentRepository.deleteById(id);
        return deletedStudent;
    }

    public List<Student> getStudentsByAge(int age) {
        logger.info("Was invoked method getStudentByAge");
        return studentRepository.findByAge(age);
    }

    public List<Student> getStudentsByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method getStudentsByAgeBetween");
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty getStudentFaculty(Long studentId) {
        logger.info("Was invoked method getStudentFaculty");
        return studentRepository.findById(studentId).get().getFaculty();
    }

    public NumberOfStudents getNumberOfStudents() {
        logger.info("Was invoked method getNumberOfStudents");
        return studentRepository.getNumberOfStudents();
    }

    public AverageAgeOfStudents getAverageOfStudents() {
        logger.info("Was invoked method getAverageOfStudents");
        return studentRepository.getAverageAgeOfStudents();
    }

    public List<LastFiveStudents> getLastFiveStudents() {
        logger.info("Was invoked method getLastFiveStudents");
        return studentRepository.getLastFiveStudents();
    }

    public List<String> getStudentsStartsWithA() {
        return studentRepository.findAll()
                .stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .filter(i -> i.startsWith("A"))
                .sorted()
                .toList();
    }

    public Double getAverageAgeOfStudentByStreamAPI() {
        return studentRepository.findAll()
                .stream()
                .mapToInt(Student::getAge)
                .average()
                .getAsDouble();
    }

    public void printParallel() {
        StudentService studentService = new StudentService(studentRepository);

        Thread thread_1 = new Thread (() -> {
            studentService.printParallelStudent(0);
            studentService.printParallelStudent(1);
        });

        Thread thread_2 = new Thread (() -> {
            studentService.printParallelStudent(2);
            studentService.printParallelStudent(3);
        });

        Thread thread_3 = new Thread (() -> {
            studentService.printParallelStudent(4);
            studentService.printParallelStudent(5);
        });

        thread_1.start();
        thread_2.start();
        thread_3.start();
    }

    public void printParallelStudent(int index) {
        System.out.println("Студент " + (1 + index) + ": " + studentRepository.findAll().get(index).getName());
    }

    public void printSynchronized() {
        StudentService studentService = new StudentService(studentRepository);

        Thread thread_1 = new Thread (() -> {
            studentService.printSynchronizedStudent();
            studentService.printSynchronizedStudent();
        });

        Thread thread_2 = new Thread (() -> {
            studentService.printSynchronizedStudent();
            studentService.printSynchronizedStudent();
        });

        Thread thread_3 = new Thread (() -> {
            studentService.printSynchronizedStudent();
            studentService.printSynchronizedStudent();
        });

        thread_1.start();
        thread_2.start();
        thread_3.start();
    }

    int index = -1;

    public void printSynchronizedStudent() {
        synchronized (StudentService.class) {
            index++;
            System.out.println("Студент " + (1 + index) + ": " + studentRepository.findAll().get(index).getName());
        }
    }
}