package adapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import com.example.andrew1.familymapserver.R;
import java.util.List;
import model.Model;

/**
 * Created by Andrew1 on 6/12/17.
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterHolder> {
    private List<String> mFilters;
    private static int mFiltersSize;

    public static class FilterHolder extends RecyclerView.ViewHolder {
        private TextView mFilter;
        private TextView mDescription;
        private Switch mSwitch;
        private static int curSwitch = 0; //lines the switch up to the ID/index in the list
        private static int curID = 0; //keeps track of the index of the current filter, to keep track of the switch being on or off
        private int numFilters = mFiltersSize;

        public FilterHolder(View v) {
            super(v);
            if(curID == numFilters)
                curID = 0;
            mFilter = (TextView) v.findViewById(R.id.filter_event);
            mDescription = (TextView) v.findViewById(R.id.filter_description);
            mSwitch = (Switch)v.findViewById(R.id.filter_switch);
            mSwitch.setId(curID);
            curID++;
            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Model.instance().changeFilterSwitch(mSwitch.getId(), isChecked);
                    if(isChecked)
                        Model.instance().removeFilter(getFilterType());
                    else{
                        Model.instance().addFilter(getFilterType());
                    }
                }
            });
        }

        public String getFilterType() {
            int id = mSwitch.getId();
            return Model.instance().getFilterTypes().get(id);
        }

        public void bindFilterType(String filter) {
            if(curSwitch == numFilters)
                curSwitch = 0;
            mFilter.setText(filter);
            mDescription.setText("FILTER BY " + filter.toUpperCase());
            List<Boolean> switches = Model.instance().getFilterSwitches();
            mSwitch.setChecked(switches.get(curSwitch));
            curSwitch++;
        }
    }


    public FilterAdapter() {
        mFilters = Model.instance().getFilters();
        mFiltersSize = mFilters.size();
    }

    @Override
    public FilterAdapter.FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_row, parent, false);
        return new FilterHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(FilterAdapter.FilterHolder holder, int position) {
        String filterType = mFilters.get(position);
        holder.bindFilterType(filterType);
    }

    @Override
    public int getItemCount() {
        return mFilters.size();
    }
}
