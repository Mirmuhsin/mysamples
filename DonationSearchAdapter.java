package com.ihh.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.ihh.model.entity.donate.Donation;
import com.ihh.utility.CustomDiffUtil;
import com.ihh.view.adapter.DonationSearchAdapter.DonationSearchItemViewHolder;
import com.ihh.view.layout.DonationSearchListItemLayout;
import com.ihh.view.layout.DonationSearchListItemLayout.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
public class DonationSearchAdapter extends RecyclerView.Adapter<DonationSearchItemViewHolder> implements Filterable {

    private Context mContext;
    private List<Donation> mList;
    private List<Donation> mOriginalList;
    private OnItemClickListener mListener;
    private OnSearchResultListener mSearchListener;

    public DonationSearchAdapter(Context context, List<Donation> list, OnItemClickListener listener, OnSearchResultListener searchResultListener) {
        mContext = context;
        mList = list;
        mOriginalList = list;
        mListener = listener;
        mSearchListener = searchResultListener;
    }

    @NonNull
    @Override
    public DonationSearchItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DonationSearchListItemLayout layout = new DonationSearchListItemLayout(mContext, mListener);
        return new DonationSearchItemViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationSearchItemViewHolder holder, int position) {
        Donation item = mList.get(position);

        boolean isFirst = position == 0;
        boolean isTitleShown = false;
        if (isFirst) {
            isTitleShown = true;
        } else {
            Donation prevItem = mList.get(position - 1);
            int prevItemTabID = prevItem.getTabID();
            int currentItemTabID = item.getTabID();
            if (prevItemTabID != currentItemTabID) {
                isTitleShown = true;
            }
        }
        holder.layout.fillContent(item, isTitleShown);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Donation> filteredResult;
                if (constraint.length() == 0) {
                    filteredResult = mOriginalList;
                } else {
                    filteredResult = getFilteredResults(constraint.toString().trim().toLowerCase());
                }
                FilterResults result = new FilterResults();
                result.values = filteredResult;
                return result;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<Donation> newList = (List<Donation>) results.values;
                updateList(newList);
                mList = newList;
                if (mList.isEmpty()) {
                    if (mSearchListener != null) {
                        mSearchListener.onEmptySearchResult();
                    }
                } else {
                    if (mSearchListener != null) {
                        mSearchListener.onNotEmptySearchResult();
                    }
                }
            }
        };
    }

    public void updateList(List<Donation> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CustomDiffUtil(mList, newList));
        diffResult.dispatchUpdatesTo(this);
    }

    private List<Donation> getFilteredResults(String constraint) {
        List<Donation> results = new ArrayList<>();
        for (Donation item : mOriginalList) {
            if (item.getContentName().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

    static class DonationSearchItemViewHolder extends RecyclerView.ViewHolder {

        private DonationSearchListItemLayout layout;

        public DonationSearchItemViewHolder(@NonNull View itemView) {
            super(itemView);
            layout = (DonationSearchListItemLayout) itemView;
        }
    }

    public interface OnSearchResultListener {

        void onEmptySearchResult();

        void onNotEmptySearchResult();
    }
}
