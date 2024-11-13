package com.ntt.elearning.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.ntt.elearning.dto.request.CourseCreationRequest;
import com.ntt.elearning.dto.response.CourseResponse;
import com.ntt.elearning.entity.Course;
import com.ntt.elearning.entity.Lesson;
import com.ntt.elearning.exception.AppException;
import com.ntt.elearning.exception.ErrorCode;
import com.ntt.elearning.mapper.CourseMapper;
import com.ntt.elearning.repository.CourseRepository;
import com.ntt.elearning.repository.LessonRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CourseService {
    CourseRepository courseRepository;
    CourseMapper courseMapper;
    LessonRepository lessonRepository;
    CloudinaryService cloudinaryService;

    @PreAuthorize("hasRole('ADMIN')")
    public CourseResponse createCourse(CourseCreationRequest request) {
        Course course = Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .thumbnailUrl(cloudinaryService
                        .upload(request.getFile(), "course_folder")
                        .get("secure_url")
                        .toString())
                .build();
        return courseMapper.toCourseResponse(courseRepository.save(course));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CourseResponse getCourseById(String id) {
        return courseMapper.toCourseResponse(courseRepository.findById(id).get());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toCourseResponse)
                .toList();
    }

    public void deleteCourse(String id) {
        courseRepository.deleteById(id);
    }

    public void addLessonToCourse(String courseId, String lessonId) {
        var course = courseRepository.findById(courseId);
        if (!course.isPresent()) {
            throw new AppException(ErrorCode.COURSE_NOT_FOUND);
        }
        Optional<Lesson> lesson = lessonRepository.findById(lessonId);
        if (!lesson.isPresent()) {
            throw new AppException(ErrorCode.LESSON_NOT_FOUND);
        }
        course.get().getLessons().add(lesson.get());
        courseRepository.save(course.get());
    }
}
