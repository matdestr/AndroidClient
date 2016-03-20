package be.kdg.teame.kandoe.di.components;

import be.kdg.teame.kandoe.core.fragments.BaseFragment;

/*@FragmentScope
@Subcomponent(
        modules = {
                SessionModule.class
        }
)*/
public interface FragmentComponent {
    void inject(BaseFragment baseFragment);

    void inject(be.kdg.teame.kandoe.dashboard.sessionlist.SessionListFragment sessionListFragment);

}
