package br.com.rsicarelli.espressointents.presentation.component;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import java.io.Serializable;

import br.com.rsicarelli.espressointents.R;

public class RationaleDialogFragment extends AppCompatDialogFragment {
    public static final String ARG_TITLE = "title";
    public static final String ARG_MESSAGE = "message";
    public static final String ARG_LISTENER = "argListener";

    private CharSequence title;
    private CharSequence message;
    private OnRationaleDialogListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        title = args.getCharSequence(ARG_TITLE);
        message = args.getCharSequence(ARG_MESSAGE);
        listener = (OnRationaleDialogListener) args.getSerializable(ARG_LISTENER);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.allow,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                listener.onAllowClick();
                            }
                        }
                )
                .setNegativeButton(R.string.deny, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                listener.onDenyClick();
                            }
                        }
                )
                .create();

        dialog.setCanceledOnTouchOutside(true);

        return dialog;
    }

    public static class Builder {
        private CharSequence title;
        private CharSequence message;
        private OnRationaleDialogListener dialogListener;

        public Builder setTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder setMessage(CharSequence message) {
            this.message = message;
            return this;
        }

        public Builder setDialogListener(OnRationaleDialogListener dialogListener) {
            this.dialogListener = dialogListener;
            return this;
        }

        public RationaleDialogFragment build() {
            RationaleDialogFragment fragment = new RationaleDialogFragment();
            Bundle args = new Bundle();
            args.putCharSequence(ARG_TITLE, title);
            args.putCharSequence(ARG_MESSAGE, message);
            args.putSerializable(ARG_LISTENER, dialogListener);
            fragment.setArguments(args);
            return fragment;
        }
    }

    public interface OnRationaleDialogListener extends Serializable {
        void onAllowClick();

        void onDenyClick();
    }
}
