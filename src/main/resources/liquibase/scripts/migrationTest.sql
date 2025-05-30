-- liquibase formatted sql

-- changeset aandrew:1
CREATE INDEX student_name_index ON student (name)

-- changeset aandrew:2
CREATE INDEX faculty_name_and_color_index ON faculty (name, color)