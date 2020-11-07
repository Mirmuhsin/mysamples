package com.ihh.view.layout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ihh.R;
import com.ihh.constant.DonationTabEnum;
import com.ihh.constant.ProjectSettings;
import com.ihh.controller.activity.SponsorshipExploreActivity;
import com.ihh.controller.base.BaseActivity;
import com.ihh.controller.fragment.popup.DonatePopupSheetFragment;
import com.ihh.controller.fragment.popup.QurbanPopupSheetFragment;
import com.ihh.model.entity.donate.Donation;
import com.squareup.picasso.Picasso;

import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
public class CardPagerItemLayout extends RelativeLayout implements OnClickListener {

    private static final int RESOURCE = R.layout.layout_card_pager_item;

    @BindView(R.id.ivBg) ImageView ivBg;
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvDescription) TextView tvDescription;
    @BindView(R.id.btnDonate) Button btnDonate;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.tvPeopleCount) TextView tvPeopleCount;
    @BindView(R.id.tvProgress) TextView tvProgress;
    @BindView(R.id.llProgress) LinearLayout llProgress;
    @BindView(R.id.llInfoContainer) LinearLayout llInfoContainer;
    @BindView(R.id.flContainer) FrameLayout flContainer;

    private BaseActivity mActivity;
    private OnCardClickListener mListener;
    private Donation mDonation;

    public CardPagerItemLayout(Context context, OnCardClickListener listener) {
        super(context);
        LayoutInflater.from(context).inflate(RESOURCE, this);
        ButterKnife.bind(this);
        // assignObjects
        mActivity = (BaseActivity) context;
        mListener = listener;
        // setListeners
        flContainer.setOnClickListener(this);
        btnDonate.setOnClickListener(this);
        // prepareUI
        LayoutParams params = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        setLayoutParams(params);
    }

    @Override
    public void onClick(View v) {
        if (flContainer == v) {
            mListener.onCardClick(mDonation, llInfoContainer, tvTitle, ivBg);
        } else if (btnDonate == v) {
            if (mDonation.isSponsorship()) {
                SponsorshipExploreActivity.start(mActivity);
            } else {
                if (DonationTabEnum.valueOf(mDonation.getTabID()) == DonationTabEnum.QURBAN) {
                    QurbanPopupSheetFragment.showDialog(mActivity, mDonation);
                } else {
                    DonatePopupSheetFragment.showDialog(mActivity, mDonation);
                }
            }
        }
    }

    public void fillContent(Donation donation, int fakeIndex) {
        mDonation = donation;
        DonationTabEnum donationTabEnum = DonationTabEnum.valueOf(donation.getTabID());
        checkProgress(donationTabEnum);
        checkButtonText(donationTabEnum);

        DonationTabEnum menu = DonationTabEnum.valueOf(donation.getTabID());
        int tint = ContextCompat.getColor(getContext(), menu.getTextColorResId());
        ViewCompat.setBackgroundTintList(btnDonate, ColorStateList.valueOf(tint));
        tvTitle.setText(donation.getContentName());
        tvDescription.setText(donation.getContentSpot());
        Picasso.get().load(ProjectSettings.IMAGE_SERVER + donation.getMobileImageLink()).placeholder(R.color.white).into(ivBg);
    }

    private void checkProgress(DonationTabEnum donationTabEnum) {
        if (donationTabEnum == DonationTabEnum.PROJECT) {
            llProgress.setVisibility(VISIBLE);
            progressBar.setVisibility(VISIBLE);
        } else {
            llProgress.setVisibility(GONE);
            progressBar.setVisibility(GONE);
        }
    }

    private void checkButtonText(DonationTabEnum donationTabEnum) {
        if (donationTabEnum == DonationTabEnum.ORPHAN) {
            btnDonate.setText(R.string.donation_card_sponsor_button);
        } else if (donationTabEnum == DonationTabEnum.QURBAN) {
            btnDonate.setText(R.string.donation_card_qurban_button);
        } else {
            btnDonate.setText(R.string.donation_card_donate_button);
        }
    }

    public interface OnCardClickListener {

        void onCardClick(Donation donation, LinearLayout layout, TextView textView, ImageView imageView);
    }
}
