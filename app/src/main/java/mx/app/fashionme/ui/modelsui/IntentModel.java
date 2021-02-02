package mx.app.fashionme.ui.modelsui;

import android.app.Activity;
import android.util.Pair;
import android.view.View;

import androidx.fragment.app.Fragment;

import java.util.HashMap;

public class IntentModel {

    private Activity activity;
    private Class<?> _class;
    private int flags;
    private HashMap<String, String> params;
    private int code;
    private Fragment fragment;
    private Pair<View, String> shareElements;

    public IntentModel(Activity activity, Class<?> _class, int flags, HashMap<String, String> params, int code, Fragment fragment, Pair<View, String> shareElements) {
        this.activity = activity;
        this._class = _class;
        this.flags = flags;
        this.params = params;
        this.code = code;
        this.fragment = fragment;
        this.shareElements = shareElements;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Class<?> get_class() {
        return _class;
    }

    public void set_class(Class<?> _class) {
        this._class = _class;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public Pair<View, String> getShareElements() {
        return shareElements;
    }

    public void setShareElements(Pair<View, String> shareElements) {
        this.shareElements = shareElements;
    }
}
