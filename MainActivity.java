package com.ihh.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.ihh.R;
import com.ihh.constant.ApiMethod;
import com.ihh.constant.Bar;
import com.ihh.constant.DonationTabEnum;
import com.ihh.controller.base.BaseActivity;
import com.ihh.controller.fragment.AccountFragment;
import com.ihh.controller.fragment.AccountGuestFragment;
import com.ihh.controller.fragment.CartFragment;
import com.ihh.controller.fragment.DonateCategoryDetailFragment;
import com.ihh.controller.fragment.DonateCategoryFragment;
import com.ihh.controller.fragment.HomeFragment;
import com.ihh.controller.fragment.popup.QurbanPopupSheetFragment;
import com.ihh.model.request.GetBasketSummaryRequest;
import com.ihh.model.response.BaseResponse;
import com.ihh.model.response.GetBasketSummaryResponse;
import com.ihh.view.layout.BottomBarLayout;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    public static final int REQUEST_CODE_IS_QURBAN_ADD_CLICKED = getNewRequestCode();
    public static final int REQUEST_CODE_IS_DONATE_ADD_CLICKED = getNewRequestCode();
    public static final int REQUEST_IS_BASKET_UPDATE_NEEDED = getNewRequestCode();

    @BindView(R.id.bottomBarLayout) BottomBarLayout bottomBarLayout;

    private HomeFragment frHome;
    private DonateCategoryFragment frDonate;
    private AccountGuestFragment frAccountGuest;
    private CartFragment frCart;
    private AccountFragment frAccount;

    // todo: check if user logged
    boolean mIsUserLoggedIn = false;

    public static void startWithClearTask(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void createViews() {
        frHome = new HomeFragment();
        frDonate = new DonateCategoryFragment();
        frAccountGuest = new AccountGuestFragment();
        frCart = new CartFragment();
        frAccount = new AccountFragment();
    }

    @Override
    public void assignObjects() {
        mIsUserLoggedIn = !TextUtils.isEmpty(getPreference().getToken());
    }

    @Override
    public void setListeners() {
        bottomBarLayout.setOnBarSelectedListener(this::openMenuFragment);
    }

    @Override
    public void prepareUI() {
        openMenuFragment(Bar.HOME);
    }

    @Override
    public void onLayoutReady() {
        super.onLayoutReady();
        sendGetBasketSummaryRequest();
    }

    @Override
    public void onApiResponseReceive(ApiMethod method, BaseResponse baseResponse, boolean isSuccess) {
        if (ApiMethod.BASKET_SUMMARY == method && isSuccess) {
            handleGetBasketSummaryResponse(baseResponse);
        }
    }

    @Override
    public void onEventReceive(int event, Object... datas) {
        if (event == QurbanPopupSheetFragment.EVENT_QURBAN_ADDED_TO_BASKET) {
            sendGetBasketSummaryRequest();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IS_QURBAN_ADD_CLICKED && resultCode == RESULT_OK) {
            bottomBarLayout.selectFakeTabManually(Bar.DONATIONS);
            DonateCategoryDetailFragment.open((BaseActivity) activity, DonationTabEnum.QURBAN);
        } else if (requestCode == REQUEST_CODE_IS_DONATE_ADD_CLICKED && resultCode == RESULT_OK) {
            bottomBarLayout.selectFakeTabManually(Bar.DONATIONS);
            openMenuFragment(Bar.DONATIONS);
        } else if (requestCode == REQUEST_IS_BASKET_UPDATE_NEEDED && resultCode == RESULT_OK) {
            sendGetBasketSummaryRequest();
        }
    }

    @Override
    public void onBackPressed() {
        Bar currentMenuBar = bottomBarLayout.getCurrentlySelectedBar();
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (currentMenuBar != Bar.HOME && count == 0) {
            bottomBarLayout.selectFakeTabManually(Bar.HOME);
            openFragmentAsRoot(frHome);
        } else {
            super.onBackPressed();
        }
    }

    private void sendGetBasketSummaryRequest() {
        GetBasketSummaryRequest request = new GetBasketSummaryRequest();
        request.setAccountId(getPreference().getAccountId());
        request.setToken(getPreference().getToken());
        request.setBasketkey(getPreference().getBasketKey());
        sendRequest(request, false);
    }

    private void handleGetBasketSummaryResponse(BaseResponse baseResponse) {
        GetBasketSummaryResponse response = (GetBasketSummaryResponse) baseResponse;
        bottomBarLayout.setBasketCount(response.getBasketLineCount());
    }

    private void openMenuFragment(Bar bar) {
        if (bar == Bar.HOME) {
            openFragmentAsRoot(frHome);
        } else if (bar == Bar.DONATIONS) {
            openFragmentAsRoot(frDonate);
        } else if (bar == Bar.ACCOUNT) {
            if (mIsUserLoggedIn) {
                openFragmentAsRoot(frAccount);
            } else {
                openFragmentAsRoot(frAccountGuest);
            }
        } else if (bar == Bar.CART) {
            openFragmentAsRoot(frCart);
        }
    }
}
