package be.kdg.teame.kandoe.core.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import be.kdg.teame.kandoe.App;
import be.kdg.teame.kandoe.R;
import be.kdg.teame.kandoe.core.DialogGenerator;
import be.kdg.teame.kandoe.core.contracts.AuthenticatedContract;
import be.kdg.teame.kandoe.di.Injector;
import be.kdg.teame.kandoe.di.components.AppComponent;

public abstract class BaseFragment extends Fragment implements AuthenticatedContract.View {
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        injectComponent(((App) getActivity().getApplication()).component());
    }

    @Override
    public void launchUnauthenticatedRedirectActivity() {
        Intent intent = new Intent(getContext(), Injector.getUnauthenticatedRedirectActivity());
        startActivity(intent, true);
    }

    @Override
    public void launchUnauthenticatedRedirectActivity(String reason) {
        DialogGenerator.showErrorDialog(getContext(), R.string.dialog_error_title_unauthenticated, R.string.dialog_error_message_unauthenticated, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                launchUnauthenticatedRedirectActivity();
            }
        });
    }

    public void startActivity(Intent intent, boolean clearStack) {
        if (clearStack)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    protected void injectComponent(AppComponent component) {
        component.inject(this);
    }

}
