package com.ihh.view.layout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ihh.R;
import com.ihh.constant.ActionType;
import com.ihh.controller.base.BaseActivity;
import com.ihh.model.entity.ActionItem;
import com.ihh.view.interfaces.OnListItemClickListener;

import java.time.LocalDate;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.ImageViewCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
public class DonateListItemLayout extends RelativeLayout implements OnClickListener {

    private static final int RESOURCE = R.layout.layout_donate_list_item;

    @BindView(R.id.llRow) LinearLayout llRow;
    @BindView(R.id.tvDate) TextView tvDate;
    @BindView(R.id.ivIcon) ImageView ivIcon;
    @BindView(R.id.tvType) TextView tvType;
    @BindView(R.id.tvTypeCount) TextView tvTypeCount;
    @BindView(R.id.llHeader) LinearLayout llHeader;
    @BindView(R.id.vLineBottom) View vLineBottom;
    @BindView(R.id.vLineHeader) View vLineHeader;
    @BindView(R.id.vLineTop) View vLineTop;
    @BindView(R.id.ivCircle) ImageView ivCircle;
    @BindView(R.id.llActionRow) LinearLayout llActionRow;
    @BindView(R.id.tvActivityName) TextView tvActivityName;
    @BindView(R.id.tvDonor) TextView tvDonor;
    @BindView(R.id.tvAmount) TextView tvAmount;

    private BaseActivity mActivity;
    private OnListItemClickListener mClickListener;
    private ActionItem mActionItem;

    public DonateListItemLayout(Context context, OnListItemClickListener listener) {
        super(context);
        LayoutInflater.from(context).inflate(RESOURCE, this);
        ButterKnife.bind(this);
        //assignObjects
        mActivity = (BaseActivity) context;
        mClickListener = listener;
        //setListeners
        llRow.setOnClickListener(this);
        llActionRow.setOnClickListener(this);
    }

    public void fillContent(ActionItem donateItem, ViewType viewType) {
        mActionItem = donateItem;
        if (viewType == ViewType.DATE) {
            tvDate.setVisibility(VISIBLE);
            llHeader.setVisibility(VISIBLE);
        } else if (viewType == ViewType.HEADER) {
            tvDate.setVisibility(GONE);
            llHeader.setVisibility(VISIBLE);
        } else if (viewType == ViewType.NORMAL) {
            tvDate.setVisibility(GONE);
            llHeader.setVisibility(GONE);
        }

        boolean isToday = LocalDate.now().equals(donateItem.getDate());
        String dateString = isToday ? mActivity.getString(R.string.donations_fragment_header_today) : donateItem.getDate().toString();
        tvDate.setText(dateString);
        tvType.setText(donateItem.getType());
        tvActivityName.setText(donateItem.getActivity());
        tvDonor.setText(donateItem.getDonor());
        tvAmount.setText(donateItem.getExchange() + " " + donateItem.getAmount());
        tvTypeCount.setText(viewType.subItemCount + "");

        boolean isLastViewType = viewType.isLast;
        vLineBottom.setVisibility(isLastViewType ? INVISIBLE : VISIBLE);

        ActionType actionType = donateItem.getTypeEnum();

        updateHeaderColor(actionType);
        updateCircleColor(actionType);
        updateLineColor(actionType);
    }

    @Override
    public void onClick(View view) {
        if (llRow == view) {

        } else if (llActionRow == view) {
            mClickListener.onListItemClick(mActionItem);
        }
    }

    private void updateHeaderColor(ActionType actionType) {
        ivIcon.setImageResource(actionType.getIcon());
        int color = ContextCompat.getColor(getContext(), actionType.getColor());
        ImageViewCompat.setImageTintList(ivIcon, ColorStateList.valueOf(color));
        tvType.setTextColor(color);
    }

    private void updateCircleColor(ActionType actionType) {
        LayerDrawable layerDrawable = (LayerDrawable) ivCircle.getDrawable();
        GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.shapeItemBg);
        gradientDrawable.setColor(ContextCompat.getColor(mActivity, actionType.getColor()));
    }

    private void updateLineColor(ActionType actionType) {
        int color = ContextCompat.getColor(getContext(), actionType.getColor());
        ViewCompat.setBackgroundTintList(vLineTop, ColorStateList.valueOf(color));
        ViewCompat.setBackgroundTintList(vLineHeader, ColorStateList.valueOf(color));
        ViewCompat.setBackgroundTintList(vLineBottom, ColorStateList.valueOf(color));
    }

    public enum ViewType {
        DATE,
        HEADER,
        NORMAL;

        boolean isLast;
        int subItemCount;

        public ViewType setLast(boolean last) {
            isLast = last;
            return this;
        }

        public ViewType setSubItemCount(int subItemCount) {
            this.subItemCount = subItemCount;
            return this;
        }
    }
}
