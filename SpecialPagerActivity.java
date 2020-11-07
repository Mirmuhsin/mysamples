package com.ihh.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ihh.R;
import com.ihh.controller.base.BaseActivity;
import com.ihh.controller.fragment.popup.RegisteredCardsSheetFragment;
import com.ihh.model.entity.data.GoodDeed;
import com.ihh.model.response.GetDonateSpecialResponse.Price;
import com.ihh.utility.UiUtil;
import com.ihh.view.adapter.ShareBreadPagerAdapter;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2.PageTransformer;
import butterknife.BindView;

public class SpecialPagerActivity extends BaseActivity implements OnClickListener {

    private static final String EXTRA_SPECIAL_DEED = getNewExtraId();

    @BindView(R.id.viewPager2) ViewPager2 viewPager2;
    @BindView(R.id.btnCancel) Button btnCancel;
    @BindView(R.id.btnStartShare) Button btnStartShare;
    @BindView(R.id.dotsIndicator) DotsIndicator dotsIndicator;

    private ShareBreadPagerAdapter mAdapter;
    private int mItemMarginAndOffset;
    private List<Price> mPrices;

    public static void start(Activity activity, GoodDeed specialDeed) {
        Intent intent = new Intent(activity, SpecialPagerActivity.class);
        intent.putExtra(EXTRA_SPECIAL_DEED, specialDeed);
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_special_pager;
    }

    @Override
    public void assignObjects() {
        GoodDeed specialDonation = (GoodDeed) getIntent().getSerializableExtra(EXTRA_SPECIAL_DEED);
        mItemMarginAndOffset = UiUtil.dpToPx(context, 64 + 32);

        mPrices = new ArrayList<>(specialDonation.getPrices());
        mAdapter = new ShareBreadPagerAdapter(context, mPrices);
    }

    @Override
    public void setListeners() {
        btnStartShare.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void prepareUI() {
        viewPager2.setPageTransformer(mDonationsPagerTransform);
        viewPager2.setAdapter(mAdapter);
        viewPager2.setOffscreenPageLimit(5);
        dotsIndicator.setViewPager2(viewPager2);
        UiUtil.setStatusBarAndNavigationColor(activity, R.color.share_bread_bg);
    }

    @Override
    public void onClick(View v) {
        if (btnStartShare == v) {
            RegisteredCardsSheetFragment.showDialog(activity, 0);
        } else if (btnCancel == v) {
            finish();
        }
    }

    private PageTransformer mDonationsPagerTransform = new PageTransformer() {
        @Override
        public void transformPage(@NonNull View page, float position) {
            int offset = (int) (position * -(mItemMarginAndOffset));
            if (ViewCompat.getLayoutDirection(viewPager2) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                page.setTranslationX(-offset);
            } else {
                page.setTranslationX(offset);
            }

            float scaleFactor;
            if (position >= -1 && position <= 1) {
                scaleFactor = -0.2f * position * position + 1f;
            } else {
                scaleFactor = 0.8f;
            }

            page.setScaleY(scaleFactor);
        }
    };
}