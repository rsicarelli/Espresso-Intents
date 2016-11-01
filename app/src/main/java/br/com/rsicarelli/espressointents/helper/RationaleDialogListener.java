package br.com.rsicarelli.espressointents.helper;

import android.support.annotation.NonNull;

import br.com.rsicarelli.espressointents.presentation.component.RationaleDialogFragment;
import permissions.dispatcher.PermissionRequest;

public class RationaleDialogListener {
    @NonNull
    public static RationaleDialogFragment.OnRationaleDialogListener getOnRationaleDialogListener(
            final PermissionRequest request) {
        return new RationaleDialogFragment.OnRationaleDialogListener() {
            @Override
            public void onAllowClick() {
                request.proceed();
            }

            @Override
            public void onDenyClick() {
                request.cancel();
            }
        };
    }
}
