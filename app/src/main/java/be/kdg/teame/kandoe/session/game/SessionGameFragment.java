package be.kdg.teame.kandoe.session.game;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.activities.BaseActivity;
import be.kdg.teame.kandoe.core.activities.BaseToolbarActivity;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.session.game.picker.SessionGamePickerFragment;
import be.kdg.teame.kandoe.session.game.ranking.SessionGameRankingFragment;
import butterknife.Bind;
import butterknife.ButterKnife;

public class SessionGameFragment extends BaseFragment implements SessionGameContract.View {

    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    TabLayout mTabLayout;
    AppBarLayout mAppBarLayout;

    @Inject
    SessionGameContract.UserActionsListener mSessionGamePresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_session_game, container, false);
        ButterKnife.bind(this, root);

        View parentRoot = container.getRootView();

        mAppBarLayout = ButterKnife.findById(parentRoot, R.id.appbar);

        mTabLayout = new TabLayout(getContext());

        mTabLayout.setLayoutParams(
                new ActionBar.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
        );

        mTabLayout.setId(R.id.tab_layout);

        mAppBarLayout.addView(mTabLayout);

        // Setting ViewPager for each Tabs
        setupViewPager(mViewPager);

        // Set Tabs inside Toolbar
        mTabLayout.setupWithViewPager(mViewPager);

        return root;
    }

    @Override
    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }



    @Override
    public void showRanking() {

    }

    @Override
    public void showCardPicker() {

    }

    @Override
    public void showErrorConnectionFailure(String errorMessage) {
        DialogGenerator.showErrorDialog(getContext(), errorMessage);
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        SessionGamePickerFragment gamePickerFragment = new SessionGamePickerFragment();
        SessionGameRankingFragment gameRankingFragment = new SessionGameRankingFragment();

        mSessionGamePresenter.addDataListener(gamePickerFragment.getMSessionGamePickerPresenter());
        mSessionGamePresenter.addDataListener(gameRankingFragment.getMGameRankingContractPresenter());

        Adapter adapter = new Adapter(getFragmentManager());
        adapter.addFragment(gamePickerFragment, "Picker");
        adapter.addFragment(gameRankingFragment, "Ranking");
        viewPager.setAdapter(adapter);
    }

    private static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
