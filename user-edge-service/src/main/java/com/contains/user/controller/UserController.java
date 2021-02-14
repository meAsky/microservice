package com.contains.user.controller;

import com.contains.thrift.user.UserInfo;
import com.contains.thrift.user.dto.UserDTO;
import com.contains.user.redis.RedisClient;
import com.contains.user.response.LoginResponse;
import com.contains.user.response.Response;
import com.contains.user.thrift.ServiceProvider;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.util.locale.provider.LocaleServiceProviderPool;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @date: 2021-02-14 12:49
 **/
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ServiceProvider serviceProvider;
    @Autowired
    private RedisClient redisClient;

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(){
        return "/login";
    }

    @RequestMapping(value = "login" ,method = RequestMethod.POST)
    @ResponseBody
    public Response login(@RequestParam("username")String username,
                      @RequestParam("password")String password){
        //1.验证用户密码
        UserInfo userInfo=null;
        try {
            userInfo=serviceProvider.getUserService().getUserByName(username);
        } catch (TException e) {
            e.printStackTrace();
            return Response.USERNAME_PASSWORD_INVALID;
        }
        if(userInfo == null){
            return Response.USERNAME_PASSWORD_INVALID;
        }
        if(!userInfo.getPassword().equalsIgnoreCase(md5(password))){
            return Response.USERNAME_PASSWORD_INVALID;
        }
        //2.生成token
        String token = getToken();

        //3.缓存用户
        redisClient.set(token,toDTO(userInfo),3600);
        return new LoginResponse(token);
    }

    @RequestMapping(value="/sendVerifyCode",method = RequestMethod.POST)
    @ResponseBody
    public Response sendVerifyCode(@RequestParam(value = "mobile",required = false)String mobile,
                                   @RequestParam(value = "email",required = false)String email){
        String message="verify code is:";
        String code = randomCode("0123456789",6);
        try {
            boolean result= false;
            if(StringUtils.isNotBlank(mobile)){
                result=serviceProvider.getMessageService().sendMobileMessage(mobile,message+code);
                redisClient.set(mobile,code);
            }else if(StringUtils.isNotBlank(email)){
                result=serviceProvider.getMessageService().sendEmailMessage(email,message+code);
                redisClient.set(email,code);
            }else{
                return Response.MOBILE_OR_EMAIL_REQUIRED;
            }
            if(!result){
                return Response.SEND_VERIFYCODE_FAILED;
            }
            return Response.SUCCESS;
        } catch (TException e) {
            e.printStackTrace();
            return Response.exception(e);
        }
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public Response register(@RequestParam("username")String username,
                             @RequestParam("password")String password,
                             @RequestParam(value = "mobile",required = false)String mobile,
                             @RequestParam(value = "email",required = false)String email,
                             @RequestParam("verifyCode")String verifyCode){
        if(StringUtils.isBlank(mobile) && StringUtils.isBlank(email)){
            return Response.MOBILE_OR_EMAIL_REQUIRED;
        }
        if(StringUtils.isNotBlank(mobile)){
            String reidsCode=redisClient.get(mobile);
            if(!verifyCode.equals(reidsCode)){
                return Response.VERIFYCODE_INVALID;
            }
        }else{
            String redisCode=redisClient.get(email);
            if(!verifyCode.equals(redisCode)){
                return Response.VERIFYCODE_INVALID;
            }
        }
        UserInfo userInfo=new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(md5(password));
        userInfo.setMobile(mobile);
        userInfo.setEmail(email);
        try {
            serviceProvider.getUserService().regiserUser(userInfo);
        } catch (TException e) {
            e.printStackTrace();
            return Response.exception(e);
        }
        return Response.SUCCESS;
    }

    @RequestMapping(value = "/authentication",method = RequestMethod.POST)
    @ResponseBody
    public UserDTO authentication(@RequestHeader("token")String token){
        return redisClient.get(token);
    }

    private UserDTO toDTO(UserInfo userInfo) {
        UserDTO userDTO=new UserDTO();
        BeanUtils.copyProperties(userInfo,userDTO);
        return userDTO;
    }

    private String getToken() {
        return randomCode("0123456789abcdefghijklmnopqrstuvwxyz",32);
    }

    private String randomCode(String s, int size) {
        StringBuilder result=new StringBuilder();
        Random random =new Random();
        for(int i=0;i<size;i++){
            int loc=random.nextInt(s.length());
            result.append(s.charAt(loc));
        }
        return result.toString();
    }

    private String md5(String password) {
        try {
            MessageDigest md =MessageDigest.getInstance("MD5");
            byte[] md5Bytes=md.digest(password.getBytes("utf-8"));
            return HexUtils.toHexString(md5Bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
