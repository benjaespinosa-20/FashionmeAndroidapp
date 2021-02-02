package mx.app.fashionme.sticker;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import mx.app.fashionme.R;
import mx.app.fashionme.utils.Utils;

public class StickerTextInputDialog extends Dialog {
    private final String defaultStr;
    private EditText et_sticker_text_input;
    private TextView tv_show_count;
    private TextView tv_action_done;
    private static final int MAX_COUNT = 20; // Numero maximo de palabras
    private Context mContext;
    private StickerTextView stickerTextView;

    public StickerTextInputDialog(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
        defaultStr = context.getString(R.string.edit_text);
        initView();
    }

    public StickerTextInputDialog(Context context, StickerTextView view) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
        defaultStr = context.getString(R.string.edit_text);
        stickerTextView = view;
        initView();
    }

    public void setStickerTextView(StickerTextView stickerTextView) {
        this.stickerTextView = stickerTextView;
        if (defaultStr.equals(stickerTextView.getmStr())) {
            et_sticker_text_input.setText("");
        } else {
            et_sticker_text_input.setText(stickerTextView.getmStr());
            et_sticker_text_input.setSelection(stickerTextView.getmStr().length());
        }
    }

    private void initView() {
        setContentView(R.layout.view_input_dialog);
        tv_action_done = findViewById(R.id.tv_action_done);
        et_sticker_text_input = findViewById(R.id.et_sticker_text_input);
        tv_show_count = findViewById(R.id.tv_show_count);
        et_sticker_text_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                long textLength = Utils.calculateLength(s);
                tv_show_count.setText(String.valueOf(MAX_COUNT - textLength));
                if (textLength > MAX_COUNT) {
                    tv_show_count.setTextColor(mContext.getResources().getColor(R.color.red_e73a3d));
                } else  {
                    tv_show_count.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        et_sticker_text_input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    done();
                    return true;
                }
                return false;
            }
        });

        tv_action_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) et_sticker_text_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        }, 500);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        InputMethodManager imm = (InputMethodManager) et_sticker_text_input.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_sticker_text_input.getWindowToken(), 0);
    }

    public interface CompleteCallBack {
        void onComplete(View stickerTextView, String str);
    }

    private CompleteCallBack mCompleteCallBack;

    public void setCompleteCallBack(CompleteCallBack completeCallBack) {
        this.mCompleteCallBack = completeCallBack;
    }

    private void done() {
        if (Integer.valueOf(tv_show_count.getText().toString()) < 0) {
            Toast.makeText(mContext, mContext.getString(R.string.over_text_limit), Toast.LENGTH_SHORT).show();
            return;
        }
        dismiss();
        if (mCompleteCallBack != null) {
            String str;
            if (TextUtils.isEmpty(et_sticker_text_input.getText())) {
                str = "";
            } else {
                str = et_sticker_text_input.getText().toString();
            }
            mCompleteCallBack.onComplete(stickerTextView, str);
        }
    }
}
