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

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.fragments.BaseFragment;
import be.kdg.teame.kandoe.di.components.AppComponent;
import be.kdg.teame.kandoe.models.cards.CardPosition;
import be.kdg.teame.kandoe.session.SessionActivity;
import be.kdg.teame.kandoe.session.game.picker.SessionGamePickerFragment;
import be.kdg.teame.kandoe.session.game.ranking.SessionGameRankingFragment;
import butterknife.Bind;
import butterknife.ButterKnife;

public class SessionGameFragment extends BaseFragment implements SessionGameContract.View, ChildFragmentReadyListener {

    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    private TabLayout mTabLayout;
    private AppBarLayout mAppBarLayout;
    private PagerAdapter mPagerAdapter;
    private SessionGamePickerFragment mSessionGamePickerFragment;
    private SessionGameRankingFragment mSessionGameRankingFragment;
    private int mSessionId;

    public static final String CARD_POSITIONS = "session_game_fragment_card_positions";

    @Inject
    SessionGameContract.UserActionsListener mSessionGamePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSessionGamePresenter.setView(this);

        Bundle arguments = getArguments();

        mSessionId = arguments.getInt(SessionActivity.SESSION_ID);
        final boolean isOrganizer = arguments.getBoolean(SessionActivity.SESSION_IS_ORGANIZER);

        Bundle gamePickerArgs = new Bundle();
        gamePickerArgs.putInt(SessionActivity.SESSION_ID, mSessionId);

        mSessionGamePickerFragment = new SessionGamePickerFragment();
        mSessionGamePickerFragment.setArguments(gamePickerArgs);

        Bundle gameRankingArgs = new Bundle();
        gameRankingArgs.putInt(SessionActivity.SESSION_ID, arguments.getInt(SessionActivity.SESSION_ID));
        gameRankingArgs.putBoolean(SessionActivity.SESSION_IS_ORGANIZER, isOrganizer);

        mSessionGameRankingFragment = new SessionGameRankingFragment();
        mSessionGameRankingFragment.setArguments(gameRankingArgs);

        mSessionGamePickerFragment.setFragmentReadyListener(this);
        mSessionGameRankingFragment.setFragmentReadyListener(this);


        mSessionGamePresenter.loadCardPositions(mSessionId, true);
    }

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
    public void onResume() {
        super.onResume();
        // nice to know: get current item viewpager
        // Fragment baseFragment = mPagerAdapter.getItem(mViewPager.getCurrentItem());
        mSessionGamePresenter.openCurrentParticipantListener(mSessionId);
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
        mPagerAdapter = new PagerAdapter(getFragmentManager());
        mPagerAdapter.addFragment(mSessionGamePickerFragment, "Picker");
        mPagerAdapter.addFragment(mSessionGameRankingFragment, "Ranking");
        viewPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onReadyToListen(DataListener listener) {
        mSessionGamePresenter.addDataListener(listener);
    }

    private static class PagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public PagerAdapter(FragmentManager manager) {
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
