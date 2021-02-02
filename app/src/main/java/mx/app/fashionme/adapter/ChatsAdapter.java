package mx.app.fashionme.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.pojo.ChatAssessorClient;
import mx.app.fashionme.presenter.interfaces.IChatsPresenter;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {

    private ArrayList<ChatAssessorClient> chatAssessorClients;
    private IChatsPresenter presenter;

    public ChatsAdapter(IChatsPresenter presenter) {
        this.presenter              = presenter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending_chat, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ChatAssessorClient chat = chatAssessorClients.get(position);
        holder.tvClientEmail.setText(chat.getClient());
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.acceptChat(chat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatAssessorClients.size();
    }

    public void setData(ArrayList<ChatAssessorClient> chatAssessorClients) {
        this.chatAssessorClients = chatAssessorClients;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvClientEmail;
        private Button btnAccept;

        public ViewHolder(View itemView) {
            super(itemView);
            tvClientEmail   = itemView.findViewById(R.id.tvClientEmail);
            btnAccept       = itemView.findViewById(R.id.btnAcceptChat);
        }
    }
}
