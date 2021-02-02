package mx.app.fashionme.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.app.fashionme.R;
import mx.app.fashionme.pojo.FashionMessage;

public class ChatFirebaseAdapter extends FirebaseRecyclerAdapter<FashionMessage, ChatFirebaseAdapter.ChatViewHolder> {

    private Context context;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ChatFirebaseAdapter(FirebaseRecyclerOptions<FashionMessage> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(ChatViewHolder viewHolder, int position, FashionMessage fashionMessage) {
        if (fashionMessage.getText() != null) {
            viewHolder.messageTextView.setText(fashionMessage.getText());
            viewHolder.messageTextView.setVisibility(TextView.VISIBLE);
            viewHolder.messageImageView.setVisibility(ImageView.GONE);
        } else if (fashionMessage.getImageUrl() != null) {
            String imageUrl = fashionMessage.getImageUrl();

            if (imageUrl.startsWith("http://") || imageUrl.startsWith("https://")) {
                Picasso.get().load(imageUrl).into(viewHolder.messageImageView);
            } else {
                Picasso.get().load(R.drawable.logo_fme_black).into(viewHolder.messageImageView);

                viewHolder.messageImageView.setVisibility(View.VISIBLE);
                viewHolder.messageTextView.setVisibility(View.GONE);
            }

            viewHolder.messageImageView.setVisibility(View.VISIBLE);
            viewHolder.messageTextView.setVisibility(View.GONE);
        }

        viewHolder.messengerTextView.setText(fashionMessage.getName());

        if (fashionMessage.getPhotoUrl() == null) {
            viewHolder.messengerImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_person_pin_black_24dp));
        } else {
            Picasso.get()
                    .load(fashionMessage.getPhotoUrl())
                    .into(viewHolder.messengerImageView);
        }

    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ChatViewHolder(inflater.inflate(R.layout.item_message, parent, false));
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        private TextView messageTextView;
        private ImageView messageImageView;
        private TextView messengerTextView;
        private CircleImageView messengerImageView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            messageTextView     = itemView.findViewById(R.id.messageTextView);
            messageImageView    = itemView.findViewById(R.id.messageImageView);
            messengerTextView   = itemView.findViewById(R.id.messengerTextView);
            messengerImageView  = itemView.findViewById(R.id.messengerImageView);
        }
    }
}
