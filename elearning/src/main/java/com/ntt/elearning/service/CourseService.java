package com.ntt.elearning.service;

import com.ntt.elearning.Repository.CourseRepository;
import com.ntt.elearning.dto.request.CourseRequest;
import com.ntt.elearning.entity.Course;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CourseService {

    @Autowired
    private  CourseRepository courseRepository;

    public Course createCourse(CourseRequest request) {
        Course course = new Course();
        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        int sequence_number = Integer.parseInt(String.valueOf(request.getSequence_number()));
        course.setSequence_number(request.getSequence_number());
        course.setStatus(request.getStatus());
        return courseRepository.save(course);
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }


    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }
    }
