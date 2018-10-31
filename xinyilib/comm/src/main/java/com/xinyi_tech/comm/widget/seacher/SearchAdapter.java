package com.xinyi_tech.comm.widget.seacher;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xinyi_tech.comm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Suggestions Adapter.
 *
 * @author Miguel Catalan Bañuls
 */
public class SearchAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements Filterable {

    private String[] suggestions;
    private Drawable suggestionIcon;
    private boolean ellipsize;

    public SearchAdapter(String[] suggestions) {
        this(suggestions, null, false);
    }


    public SearchAdapter(String[] suggestions, Drawable suggestionIcon, boolean ellipsize) {
        super(R.layout.comm_suggest_item);
        this.suggestions = suggestions;
        this.suggestionIcon = suggestionIcon;
        this.ellipsize = ellipsize;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (!TextUtils.isEmpty(constraint)) {

                    // Retrieve the autocomplete results.
                    List<String> searchData = new ArrayList<>();

                    for (String string : suggestions) {
                        if (string.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            searchData.add(string);
                        }
                    }

                    // Assign the data to the FilterResults
                    filterResults.values = searchData;
                    filterResults.count = searchData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    setNewData((List<String>) results.values);
                    notifyDataSetChanged();
                }
            }
        };
        return filter;
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        final TextView suggestion_text = helper.getView(R.id.suggestion_text);
        suggestion_text.setText(item);
        if (ellipsize) {
            suggestion_text.setSingleLine();
            suggestion_text.setEllipsize(TextUtils.TruncateAt.END);
        }
        if (suggestionIcon != null) {
            helper.setImageDrawable(R.id.suggestion_icon, suggestionIcon);
        }
    }

}