package com.contains.course.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.contains.course.dto.CourseDTO;
import com.contains.course.service.ICourseService;
import com.contains.thrift.user.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.RequestWrapper;
import java.util.List;

/**
 * @date: 2021-02-14 21:33
 **/
@Controller
@RequestMapping("/course")
public class CourseController {

    @Reference
    private ICourseService courseService;

    @RequestMapping(value = "courseList",method = RequestMethod.GET)
    @ResponseBody
    public List<CourseDTO> courseList(HttpServletRequest request){
        UserDTO userDTO= (UserDTO) request.getAttribute("user");
        System.out.println(userDTO.toString());
        try {
            List<CourseDTO> courseDTOS=courseService.courseList();
            System.out.println("course-dubbo-service return :"+courseDTOS);
            return courseDTOS;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
