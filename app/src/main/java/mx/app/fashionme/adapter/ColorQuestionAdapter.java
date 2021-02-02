package mx.app.fashionme.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mx.app.fashionme.R;
import mx.app.fashionme.pojo.ColorQuestionsViewModel;

public class ColorQuestionAdapter extends RecyclerView.Adapter<ColorQuestionAdapter.QuestionViewHolder> {

    private Context context;
    private List<ColorQuestionsViewModel> list;

    public ColorQuestionAdapter(Context context, List<ColorQuestionsViewModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.color_questions_list_item,
                        parent,
                        false
                );
        return new QuestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        final ColorQuestionsViewModel current = list.get(position);

        holder.title.setText(current.getQuestion());
        holder.options.removeAllViews();

        int id = (position + 1) * 100;

        for (int i = 0; i < current.getAnswers().size(); i++) {
            RadioButton radioButton = new RadioButton(context);
            radioButton.setId(id++);
            radioButton.setText(current.getAnswers().get(i).getAnswer());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ColorStateList colorStateList = new ColorStateList(
                        new int[][] {
                                new int[]{-android.R.attr.state_enabled},
                                new int[]{android.R.attr.state_enabled}
                                },
                        new int[] {
                                Color.DKGRAY,
                                ContextCompat.getColor(context, R.color.colorAccent)
                        });
                radioButton.setButtonTintList(colorStateList);
                radioButton.invalidate();
                radioButton.setTextColor(ContextCompat.getColor(context, R.color.gray_deep));
            }

            holder.options.addView(radioButton);
        }

        holder.options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId%10 == 0) {
                    current.setState(0);
                    current.setUserAnswer(current.getAnswers().get(0).getIndent());
                } else if ((checkedId - 1)%10 == 0) {
                    current.setState(1);
                    current.setUserAnswer(current.getAnswers().get(1).getIndent());
                }
            }
        });

        int holderPosition = holder.getAdapterPosition();

        if (current.getState() == 0) {
            holder.options.check((holderPosition + 1) *100);
        } else if (current.getState() == 1) {
            holder.options.check(((holderPosition + 1)*100)+1);
        } else {
            holder.options.clearCheck();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view_question)
        TextView title;
        @BindView(R.id.option_radio_group)
        RadioGroup options;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
