package adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.andrew1.familymapserver.PersonActivity;
import com.example.andrew1.familymapserver.R;

import java.util.List;

import model.Model;

/**
 * Created by Andrew1 on 6/15/17.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder>{
    private List<SearchRow> mSearchItems;

    public static class SearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private LinearLayout mSearchLayout;
        private ImageView mIcon;
        private TextView mTextTop;
        private TextView mTextBottom;
        private String mID;

        public SearchHolder(View v) {
            super(v);
            mIcon = (ImageView)v.findViewById(R.id.search_icon);
            mTextTop = (TextView) v.findViewById(R.id.search_text_top);
            mTextBottom = (TextView) v.findViewById(R.id.search_text_bottom);
            mSearchLayout = (LinearLayout) v.findViewById(R.id.search_layout);
            mSearchLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Holder holder = new Holder();
                    String bottomText = mTextBottom.getText().toString();
                    holder.goToNextPersonOrEvent(mID, bottomText, v);
                }
            });
        }

        public void bindSearchResult(SearchRow result) {
            mIcon.setImageResource(result.getIconResource());
            mTextTop.setText(result.getTextTop());
            mTextBottom.setText(result.getTextBottom());
            mID = result.getID();
        }

        @Override
        public void onClick(View v) {

        }
    }

    public SearchAdapter(){}

    @Override
    public SearchAdapter.SearchHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_row, parent, false);
        return new SearchAdapter.SearchHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(SearchAdapter.SearchHolder holder, int position) {
        SearchRow searchResult = Model.instance().getSearchResultRows().get(position);
        holder.bindSearchResult(searchResult);
    }

    @Override
    public int getItemCount() {
        return Model.instance().getSearchResultRows().size();
    }
}
