package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method createFaculty");
        return facultyRepository.save(faculty);
    }

    public Faculty getFacultyById(Long id) {
        logger.info("Was invoked method getFacultyById");
        return facultyRepository.findById(id).get();
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method editFaculty");
        return facultyRepository.save(faculty);
    }

    public Faculty deleteFaculty(Long id) {
        logger.info("Was invoked method deleteFaculty");
        for (Student student : facultyRepository.findById(id).get().getStudents()) {
            student.setFaculty(null);
        }
        Faculty deletedFaculty = facultyRepository.findById(id).get();
        facultyRepository.deleteById(id);
        return deletedFaculty;
    }

    public List<Faculty> getFacultiesByColor(String color) {
        logger.info("Was invoked method getFacultiesByColor");
        return facultyRepository.findByColor(color);
    }

    public List<Faculty> getFacultyByColorOrName(String name, String color) {
        logger.info("Was invoked method getFacultyByColorOrName");
        return facultyRepository.findByNameOrColorIgnoreCase(name, color);
    }

    public List<Student> getStudentsFromFaculty(Long facultyId) {
        logger.info("Was invoked method getStudentsFromFaculty");
        return facultyRepository.findById(facultyId).get().getStudents();
    }

}