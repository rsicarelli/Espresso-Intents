package br.com.rsicarelli.espressointents.model;

public class Permission {
    private final CharSequence title;
    private final CharSequence rationaleMessage;
    private final CharSequence neverAskAgainMessage;

    public Permission(CharSequence title, CharSequence rationaleMessage, CharSequence neverAskAgainMessage) {
        this.title = title;
        this.rationaleMessage = rationaleMessage;
        this.neverAskAgainMessage = neverAskAgainMessage;
    }

    public CharSequence getTitle() {
        return title;
    }

    public CharSequence getRationaleMessage() {
        return rationaleMessage;
    }

    public CharSequence getNeverAskAgainMessage() {
        return neverAskAgainMessage;
    }

    public static class Builder {
        private CharSequence title;
        private CharSequence rationaleMessage;
        private CharSequence neverAskAgainMessage;

        public Builder withTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder withRationaleMessage(CharSequence rationaleMessage) {
            this.rationaleMessage = rationaleMessage;
            return this;
        }

        public Builder withNeverAskAgainMessage(CharSequence neverAskAgainMessage) {
            this.neverAskAgainMessage = neverAskAgainMessage;
            return this;
        }

        public Permission build() {
            return new Permission(title, rationaleMessage, neverAskAgainMessage);
        }
    }
}
