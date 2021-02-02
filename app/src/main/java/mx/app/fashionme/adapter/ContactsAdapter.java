package mx.app.fashionme.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.pojo.ChatAssessorClient;
import mx.app.fashionme.presenter.interfaces.IContactsPresenter;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private ArrayList<ChatAssessorClient> chatAssessorClients;
    private IContactsPresenter presenter;

    public ContactsAdapter(IContactsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_contacts, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvEmail.setText(chatAssessorClients.get(position).getClient());
        holder.llBtnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.openChat(chatAssessorClients.get(position));
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

        private TextView tvEmail;
        private LinearLayout llBtnContact;

        public ViewHolder(View itemView) {
            super(itemView);
            tvEmail         = itemView.findViewById(R.id.tvContactEmail);
            llBtnContact    = itemView.findViewById(R.id.llBtnContact);
        }
    }
}
