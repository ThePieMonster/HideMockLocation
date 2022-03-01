package com.github.thepiemonster.hidemocklocation;

import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import butterknife.BindString;
//import butterknife.BindView;
//import butterknife.ButterKnife;


interface OnAppCheckChangedListener {
    void appsItemCheckChanged(AppItem item);
}


public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.ViewHolder> {

    private static final String TAG = AppsAdapter.class.getName();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //@BindView(R.id.item_icon)
        ImageView itemIcon;
        //@BindView(R.id.item_title)
        TextView itemTitle;
        //@BindView(R.id.item_checkBox)
        CheckBox itemCheckBox;

        public ViewHolder(View v) {
            super(v);
            //ButterKnife.bind(this, v);
        }

        public void bind(AppItem app) {
            itemTitle.setText(app.getName());
            itemIcon.setImageDrawable(app.getIcon());
            itemCheckBox.setChecked(app.isChecked());
            itemCheckBox.setTag(app);
        }
    }

    //@BindString(R.string.whitelist)
    String whitelist;
    //@BindString(R.string.blacklist)
    String blacklist;

    private List<AppItem> apps;
    private List<AppItem> allApps;

    private List<OnAppCheckChangedListener> listeners = new ArrayList<>();

    public void setOnCheckChangedListener(OnAppCheckChangedListener toAdd) {
        listeners.add(toAdd);
    }

    public AppsAdapter(List<AppItem> apps) {
        this.apps = apps;
        this.allApps = new ArrayList<>(apps);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item_view, parent, false);
        //return new ViewHolder(v);
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AppItem app = apps.get(position);
        holder.bind(app);

        holder.itemCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                AppItem item = (AppItem) cb.getTag();
                item.setChecked(cb.isChecked());

                // Setting checks in all apps
                allApps.get(allApps.indexOf(item)).setChecked(cb.isChecked());

                for (OnAppCheckChangedListener l : listeners)
                    l.appsItemCheckChanged(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public List<AppItem> getApps() {
        return apps;
    }

    public List<AppItem> getAllApps() {
        return allApps;
    }

    public Set<String> getCheckedAppsPackageNames() {
        Set<String> checkedApps = new HashSet<>();

        for (AppItem item : allApps) {
            if (item.isChecked())
                checkedApps.add(item.getPackageName());
        }
        return checkedApps;
    }

    public void filter(String text) {
        if(text.isEmpty()){
            apps.clear();
            apps.addAll(allApps);
        }
        else {
            ArrayList<AppItem> result = new ArrayList<>();
            text = text.toLowerCase();
            for (AppItem item : allApps) {
                if (item.getName().toString().toLowerCase().contains(text))
                    result.add(item);
            }
            apps.clear();
            apps.addAll(result);
        }
        notifyDataSetChanged();
    }
}

