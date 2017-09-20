package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.example.andrew1.familymapserver.R;

import java.util.List;

/**
 * Created by Andrew1 on 6/14/17.
 */

public class PersonExpandableAdapter extends ExpandableRecyclerAdapter<PersonParentViewHolder, PersonChildViewHolder>{
    LayoutInflater mInflater;

    public PersonExpandableAdapter(Context context, List<ParentObject> parentObjects){
        super(context, parentObjects);
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public PersonParentViewHolder onCreateParentViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.person_list_parent, viewGroup, false);
        return new PersonParentViewHolder(view);
    }

    @Override
    public PersonChildViewHolder onCreateChildViewHolder(ViewGroup viewGroup) {
        View view = mInflater.inflate(R.layout.person_list_child, viewGroup, false);
        return new PersonChildViewHolder(view);
    }

    @Override
    public void onBindParentViewHolder(PersonParentViewHolder personParentViewHolder, int i, Object o) {
        PersonParent parent = (PersonParent) o;
        personParentViewHolder.mParentTextView.setText(parent.getText());
    }

    @Override
    public void onBindChildViewHolder(PersonChildViewHolder personChildViewHolder, int i, Object o) {
        PersonChild personChild = (PersonChild) o;
        personChildViewHolder.mChildIcon.setImageResource(personChild.getIconResource());
        personChildViewHolder.mChildTextTop.setText(personChild.getTextTop());
        personChildViewHolder.mChildTextBottom.setText(personChild.getTextBottom());
        personChildViewHolder.mID = personChild.getID();
    }
}
