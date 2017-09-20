package adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.example.andrew1.familymapserver.MapActivity;
import com.example.andrew1.familymapserver.PersonActivity;
import com.example.andrew1.familymapserver.R;

import java.util.List;
import java.util.Map;

import model.Event;
import model.Model;
import model.Person;

/**
 * Created by Andrew1 on 6/14/17.
 */

public class PersonChildViewHolder extends ChildViewHolder {
    public ImageView mChildIcon;
    public TextView mChildTextTop;
    public TextView mChildTextBottom;
    public String mID;  //layout ID, to know which event or person it corresponds to.
    public LinearLayout mLayout;

    public PersonChildViewHolder(View itemView) {
        super(itemView);
        mChildIcon = (ImageView) itemView.findViewById(R.id.person_icon);
        mChildTextTop = (TextView) itemView.findViewById(R.id.person_text_top);
        mChildTextBottom = (TextView) itemView.findViewById(R.id.person_text_bottom);
        mLayout = (LinearLayout) itemView.findViewById(R.id.person_child_layout);
        mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            @SuppressWarnings("deprecation")
            public void onClick(View v) {
                Holder holder = new Holder();
                String bottomText = mChildTextBottom.getText().toString();
                holder.goToNextPersonOrEvent(mID, bottomText, v);
            }
        });
    }

}
