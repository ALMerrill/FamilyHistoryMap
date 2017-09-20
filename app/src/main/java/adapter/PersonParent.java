package adapter;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;

import model.Person;

/**
 * Created by Andrew1 on 6/14/17.
 */

public class PersonParent implements ParentObject {

    private List<Object> mChildrenList;
    private String text;

    public PersonParent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public List<Object> getChildObjectList() {
        return mChildrenList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        mChildrenList = list;
    }
}
