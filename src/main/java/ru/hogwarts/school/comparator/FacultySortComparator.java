package ru.hogwarts.school.comparator;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Comparator;

public class FacultySortComparator implements Comparator<String> {

    @Override
    public int compare(String o1, String o2) {
        return (o2.length() - o1.length());
    }
}
