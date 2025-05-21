package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty getFacultyById(Long id) {
        return facultyRepository.findById(id).get();
    }

    public Faculty editFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty deleteFaculty(Long id) {
        for (Student student : facultyRepository.findById(id).get().getStudents()) {
            student.setFaculty(null);
        }
        Faculty deletedFaculty = facultyRepository.findById(id).get();
        facultyRepository.deleteById(id);
        return deletedFaculty;
    }

    public List<Faculty> getFacultiesByColor(String color) {
        return facultyRepository.findByColor(color);
    }

    public List<Faculty> getFacultyByColorOrName(String name, String color) {
        return facultyRepository.findByNameOrColorIgnoreCase(name, color);
    }

    public List<Student> getStudentsFromFaculty(Long facultyId) {
        return facultyRepository.findById(facultyId).get().getStudents();
    }

}