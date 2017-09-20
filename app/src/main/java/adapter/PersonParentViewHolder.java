package adapter;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;
import com.example.andrew1.familymapserver.R;

/**
 * Created by Andrew1 on 6/14/17.
 */

public class PersonParentViewHolder extends ParentViewHolder {
    public ImageButton mParentDropDownArrow;
    public TextView mParentTextView;

    public PersonParentViewHolder(View itemView) {
        super(itemView);

        mParentDropDownArrow = (ImageButton) itemView.findViewById(R.id.parent_person_item_expand_arrow);
        mParentDropDownArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mParentTextView = (TextView) itemView.findViewById(R.id.parent_person_text_view);
    }

}
