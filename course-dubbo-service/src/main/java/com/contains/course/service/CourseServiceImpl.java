package com.contains.course.service;

import com.contains.course.dto.CourseDTO;
import com.contains.course.mapper.CourseMapper;
import com.contains.thrift.user.UserInfo;
import com.contains.thrift.user.dto.TeacherDTO;
import org.apache.thrift.TException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @date: 2021-02-14 20:52
 **/
@Service
public class CourseServiceImpl implements ICourseService{

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private ServiceProvider serviceProvider;

    public List<CourseDTO> courseList() {
        List<CourseDTO> courseDTOS=courseMapper.listCourse();
        if(courseDTOS!=null){
            for(CourseDTO courseDTO:courseDTOS){
                Integer teacherId=courseMapper.getCourseTeacher(courseDTO.getId());
                if(teacherId!=null){
                    try {
                        UserInfo userInfo=serviceProvider.getUserService().getTeacherById(teacherId);
                        courseDTO.setTeacher(trans2Teacher(userInfo));
                    } catch (TException e) {
                        e.printStackTrace();
                        return null;
                    }

                }
            }
        }
        return null;
    }

    private TeacherDTO trans2Teacher(UserInfo userInfo) {
        TeacherDTO teacherDTO = new TeacherDTO();
        BeanUtils.copyProperties(userInfo,teacherDTO);
        return teacherDTO;
    }
}
