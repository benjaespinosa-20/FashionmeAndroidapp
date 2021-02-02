package mx.app.fashionme.ui.utils;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.util.Pair;
import android.view.View;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

import mx.app.fashionme.ui.modelsui.IntentModel;

/**
 * NavigationUtils
 */
public class NavigationUtils {
    
    private IntentModel mIntentModel;

    /**
     * Public default constructor
     * @param intentModel intent model object
     */
    public NavigationUtils(IntentModel intentModel) {
        this.mIntentModel = intentModel;
        launchIntent();
    }

    /**
     * Method to build intent
     */
    private void launchIntent() {
        Intent intent = new Intent(mIntentModel.getActivity(), mIntentModel.get_class());
        if (mIntentModel.getFlags() > 0) {
            intent.addFlags(mIntentModel.getFlags());
        }
        
        if (mIntentModel.getParams() != null) {
            for (Map.Entry<String, String> entry : mIntentModel.getParams().entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }
        if (mIntentModel.getShareElements() == null) {
            startActivity(intent);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                startActivityTransition(intent);
            } else {
                startActivity(intent);
            }
        }
    }

    /**
     * Method to start activity
     * @param intent Intent object
     */
    private void startActivity(Intent intent) {
        if (mIntentModel.getCode() > 0) {
            if (mIntentModel.getFragment() == null) {
                mIntentModel.getActivity().startActivityForResult(intent, mIntentModel.getCode());
            } else {
                mIntentModel.getFragment().startActivityForResult(intent, mIntentModel.getCode());
            }
        } else if (mIntentModel.getCode() == 0) {
            mIntentModel.getActivity().startActivity(intent);
        }
        setTransition();
    }

    /**
     * Method to start activity with transition
     * @param intent intent object
     */
    private void startActivityTransition(Intent intent) {
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mIntentModel.getActivity(), mIntentModel.getShareElements());
        mIntentModel.getActivity().startActivity(intent, options.toBundle());
    }

    private void setTransition() {
        mIntentModel.getActivity().overridePendingTransition(0, 0);
    }

    /**
     * Builder class
     */
    public static class Builder {

        private Activity activity;
        private Class<?> _class;
        private int mFlags = 0;
        private HashMap<String, String> mParams = null;
        private int mCode = 0;
        private Fragment mFragment = null;
        private Pair<View, String> mShareElements = null;

        /**
         * Default constructor
         */
        public Builder(Activity activity, Class<?> _class) {
            this.activity = activity;
            this._class = _class;
        }

        /**
         * Method to config flags
         *
         * @param flags flags to intent
         * @return this builder
         */
        public Builder setFlags(int flags) {
            this.mFlags = flags;
            return this;
        }

        /**
         * Method to set params
         * @param params params to send to next view
         * @return this Builder
         */
        public Builder setParams(HashMap<String, String> params) {
            this.mParams = params;
            return this;
        }

        /**
         * Method to set code
         * @param code code to intent
         * @return this Builder
         */
        public Builder setCode(int code) {
            this.mCode = code;
            return this;
        }

        /**
         * Method to set fragment
         * @param fragment fragment instance
         * @return this Builder
         */
        public Builder setFragment(Fragment fragment) {
            this.mFragment = fragment;
            return this;
        }

        /**
         * Method to set params in transition
         * @param  shareElements share elements for transition
         * @return this Builder
         */
        public Builder setParamsTransitionAnimation(Pair<View, String> shareElements) {
            this.mShareElements = shareElements;
            return this;
        }

        /**
         * Method to start intent
         */
        public void start() {
            IntentModel intentModel = new IntentModel(activity, _class, mFlags, mParams, mCode, mFragment, mShareElements);
            new NavigationUtils(intentModel);
        }

    }
}
