package model;

/**
 * Created by Andrew1 on 5/27/17.
 */

public class EventData {
    private String birth;
    private String baptism;
    private String marriage;
    private String death;

    public EventData(String birth, String baptism, String marriage, String death) {
        this.birth = birth;
        this.baptism = baptism;
        this.marriage = marriage;
        this.death = death;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getBaptism() {
        return baptism;
    }

    public void setBaptism(String baptism) {
        this.baptism = baptism;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getDeath() {
        return death;
    }

    public void setDeath(String death) {
        this.death = death;
    }
}
