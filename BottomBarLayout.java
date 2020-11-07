package com.ihh.view.layout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ihh.R;
import com.ihh.constant.Bar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import butterknife.BindView;
public class BottomBarLayout extends BaseLayout implements OnClickListener {

    private static final int RESOURCE = R.layout.layout_bottom_bar;

    @BindView(R.id.ivHome) ImageView ivHome;
    @BindView(R.id.tvHome) TextView tvHome;
    @BindView(R.id.llHome) LinearLayout llHome;
    @BindView(R.id.ivDonations) ImageView ivDonations;
    @BindView(R.id.tvDonations) TextView tvDonations;
    @BindView(R.id.llDonations) LinearLayout llDonations;
    @BindView(R.id.ivCart) ImageView ivCart;
    @BindView(R.id.tvCart) TextView tvCart;
    @BindView(R.id.llCart) LinearLayout llCart;
    @BindView(R.id.ivAccount) ImageView ivAccount;
    @BindView(R.id.tvAccount) TextView tvAccount;
    @BindView(R.id.llAccount) LinearLayout llAccount;
    @BindView(R.id.ivIndicator) ImageView ivIndicator;
    @BindView(R.id.tvCartItemCount) TextView tvCartItemCount;

    private OnBarSelectListener mOnBarSelectListener;
    private Bar mCurrentlySelectedBar;

    public BottomBarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return RESOURCE;
    }

    @Override
    public void setListeners() {
        llHome.setOnClickListener(this);
        llDonations.setOnClickListener(this);
        llCart.setOnClickListener(this);
        llAccount.setOnClickListener(this);
    }

    @Override
    public void prepareUI() {
        selectBar(Bar.HOME);
    }

    @Override
    public void onClick(View v) {
        Bar bar = Bar.HOME;
        if (llHome == v) {
            bar = Bar.HOME;
        } else if (llDonations == v) {
            bar = Bar.DONATIONS;
        } else if (llCart == v) {
            bar = Bar.CART;
        } else if (llAccount == v) {
            bar = Bar.ACCOUNT;
        }
        if (bar == mCurrentlySelectedBar) {
            return;
        }
        deselectAll();
        selectBar(bar);
        mOnBarSelectListener.onBarSelect(bar);
        mCurrentlySelectedBar = bar;
    }

    public void selectFakeTabManually(Bar bar) {
        deselectAll();
        selectBar(bar);
        mCurrentlySelectedBar = bar;
    }

    public Bar getCurrentlySelectedBar() {
        return mCurrentlySelectedBar;
    }

    public void setBasketCount(int count) {
        tvCartItemCount.setText(String.valueOf(count));
        if (count == 0) {
            tvCartItemCount.setVisibility(GONE);
            ivIndicator.setVisibility(GONE);
        } else {
            tvCartItemCount.setVisibility(VISIBLE);
            ivIndicator.setVisibility(VISIBLE);
        }
    }

    public void setOnBarSelectedListener(OnBarSelectListener onBarSelectListener) {
        mOnBarSelectListener = onBarSelectListener;
    }

    private void deselectAll() {
        setBarDeselected(ivHome, tvHome);
        setBarDeselected(ivDonations, tvDonations);
        setBarDeselected(ivCart, tvCart);
        setBarDeselected(ivAccount, tvAccount);
    }

    private void selectBar(Bar bar) {
        if (bar == Bar.HOME) {
            setBarSelected(ivHome, tvHome);
        } else if (bar == Bar.DONATIONS) {
            setBarSelected(ivDonations, tvDonations);
        } else if (bar == Bar.CART) {
            setBarSelected(ivCart, tvCart);
        } else if (bar == Bar.ACCOUNT) {
            setBarSelected(ivAccount, tvAccount);
        }
    }

    private void setBarSelected(ImageView imageView, TextView textView) {
        makeIconSelected(imageView);
        makeTextSelected(textView);
    }

    private void setBarDeselected(ImageView imageView, TextView textView) {
        makeIconDeselected(imageView);
        makeTextDeselected(textView);
    }

    private void makeIconSelected(ImageView imageView) {
        int tint = ContextCompat.getColor(getContext(), R.color.bottom_tab_selected_menu);
        ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(tint));
    }

    private void makeIconDeselected(ImageView imageView) {
        int tint = ContextCompat.getColor(getContext(), R.color.bottom_tab_deselected_menu);
        ImageViewCompat.setImageTintList(imageView, ColorStateList.valueOf(tint));
    }

    private void makeTextDeselected(TextView textView) {
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.bottom_tab_deselected_menu));
    }

    private void makeTextSelected(TextView textView) {
        textView.setTextColor(ContextCompat.getColor(getContext(), R.color.bottom_tab_selected_menu));
    }

    public interface OnBarSelectListener {

        void onBarSelect(Bar bar);
    }
}
