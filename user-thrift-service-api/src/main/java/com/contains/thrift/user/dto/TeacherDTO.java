package com.contains.thrift.user.dto;

/**
 * @date: 2021-02-14 20:43
 **/
public class TeacherDTO extends UserDTO {

    private String intro;
    private int stars;

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}
