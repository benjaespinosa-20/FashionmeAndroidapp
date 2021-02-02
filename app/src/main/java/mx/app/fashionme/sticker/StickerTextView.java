package mx.app.fashionme.sticker;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import mx.app.fashionme.R;
import mx.app.fashionme.sticker.StickerView;
import mx.app.fashionme.utils.AutoResizeTextView;
import mx.app.fashionme.view.interfaces.IMakeLookView;


public class StickerTextView extends StickerView {
    private AutoResizeTextView tv_main;

    private final String defaultStr;
    private String mStr = "";

    public StickerTextView(Context context, StickerTextInputDialog stickerTextInputDialog, IMakeLookView view) {
        super(context, stickerTextInputDialog, view);
        defaultStr = getContext().getString(R.string.edit_text);
        mStr = defaultStr;
    }

    public StickerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        defaultStr = getContext().getString(R.string.edit_text);
        mStr = defaultStr;
    }

    public StickerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        defaultStr = getContext().getString(R.string.edit_text);
        mStr = defaultStr;
    }

    @Override
    public View getMainView() {
        if(tv_main != null)
            return tv_main;

        tv_main = new AutoResizeTextView(getContext());
        //tv_main.setTextSize(22);
        tv_main.setTextColor(Color.WHITE);
        tv_main.setGravity(Gravity.CENTER);
        tv_main.setTextSize(400);
        tv_main.setShadowLayer(4, 0, 0, Color.BLACK);
        tv_main.setMaxLines(1);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.CENTER;
        tv_main.setLayoutParams(params);
        if(getImageViewFlip()!=null)
            getImageViewFlip().setVisibility(View.GONE);
        return tv_main;
    }

    public void setText(String text){
        if(tv_main!=null) {
            tv_main.setText(text);
        }
        mStr = text;
        invalidate();
    }

    public String getText(){
        if(tv_main!=null)
            return tv_main.getText().toString();

        return null;
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }

    @Override
    protected void onScaling(boolean scaleUp) {
        super.onScaling(scaleUp);
    }

    public String getmStr() {
        return mStr;
    }
}
