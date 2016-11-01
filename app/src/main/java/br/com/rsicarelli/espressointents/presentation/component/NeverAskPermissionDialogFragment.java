package br.com.rsicarelli.espressointents.presentation.component;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import java.io.Serializable;

import br.com.rsicarelli.espressointents.R;

public class NeverAskPermissionDialogFragment extends AppCompatDialogFragment {
    public static final String ARG_TITLE = "title";
    public static final String ARG_MESSAGE = "message";
    private static final String ARG_LISTENER = "argListener";

    private CharSequence title;
    private CharSequence message;
    private OnNeverAskPermissionDialogListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        title = args.getCharSequence(ARG_TITLE);
        message = args.getCharSequence(ARG_MESSAGE);
        listener = (OnNeverAskPermissionDialogListener) args.getSerializable(ARG_LISTENER);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.go_to_settings,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                listener.onGoToSettingsClick();
                            }
                        }
                )
                .setNegativeButton(R.string.not_now, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                .create();

        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    public static class Builder {
        private CharSequence title;
        private CharSequence message;
        private OnNeverAskPermissionDialogListener listener;

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        public Builder setDialogListener(OnNeverAskPermissionDialogListener listener) {
            this.listener = listener;
            return this;
        }

        public NeverAskPermissionDialogFragment build() {
            NeverAskPermissionDialogFragment fragment = new NeverAskPermissionDialogFragment();
            Bundle args = new Bundle();
            args.putCharSequence(ARG_TITLE, title);
            args.putCharSequence(ARG_MESSAGE, message);
            args.putSerializable(ARG_LISTENER, listener);
            fragment.setArguments(args);
            return fragment;
        }
    }

    public interface OnNeverAskPermissionDialogListener extends Serializable {
        void onGoToSettingsClick();
    }
}
