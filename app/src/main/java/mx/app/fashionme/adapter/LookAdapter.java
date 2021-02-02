package mx.app.fashionme.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import mx.app.fashionme.R;
import mx.app.fashionme.pojo.Look;
import mx.app.fashionme.view.interfaces.ILooksView;

public class LookAdapter extends RecyclerView.Adapter<LookAdapter.ViewHolder> {

    private static final String TAG = LookAdapter.class.getSimpleName();

    private ArrayList<Look> looks;
    private Activity activity;
    private ILooksView view;

    public LookAdapter(ArrayList<Look> looks, Activity activity, ILooksView view) {
        this.looks = looks;
        this.activity = activity;
        this.view = view;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_look, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Look look = looks.get(position);

        final File file = new File(look.getUri());

        if (file.exists()) {
            Picasso.get()
                    .load(file)
                    .into(holder.imgLook);

            holder.cvLook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog fullScreenDialog = new Dialog(activity, R.style.DialogFullscreen);
                    fullScreenDialog.setContentView(R.layout.dialog_fullscreen);
                    ImageView img_full_screen_dialog = fullScreenDialog.findViewById(R.id.img_full_screen_dialog);
                    Picasso.get().load(file).into(img_full_screen_dialog);
                    ImageView img_dialog_fullscreen_close = fullScreenDialog.findViewById(R.id.img_dialog_fullscreen_close);
                    img_dialog_fullscreen_close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fullScreenDialog.dismiss();
                        }
                    });
                    fullScreenDialog.show();
                }
            });

            holder.tvHead.setText(look.getDate());

            holder.cvLook.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                    menu.setHeaderTitle("Selecciona una opción");

                    MenuItem actionItem = menu.add(0, 100, 0, "Agregar look al calendario");
                    MenuItem actionShare = menu.add(0, 101, 0, "Compartir");

                    actionItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case 100:
                                    LookAdapter.this.view.openCalendarDialog(look);
                                    break;
                                case 101:
                                    Toast.makeText(activity, "Uri: " + look.getUri(), Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                        Toast.makeText(activity, "Id: " + menuItem.getItemId(), Toast.LENGTH_SHORT).show();
                                        break;
                            }
                            return true;
                        }
                    });

                    actionShare.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case 100:
                                    LookAdapter.this.view.openCalendarDialog(look);
                                    break;
                                case 101:
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_TEXT, "Mi look en Fashion Me");
                                    String path = null;
                                    try {
                                        path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), look.getUri(), "", null);
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    Uri screenshotUri = Uri.parse(path);

                                    intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                                    intent.setType("image/*");
                                    activity.startActivity(Intent.createChooser(intent, "Share image via..."));
                                    break;
                                default:
                                    Toast.makeText(activity, "Id: " + menuItem.getItemId(), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            return true;
                        }
                    });
                }
            });


        } else {
            Picasso.get()
                    .load(R.drawable.ic_sentiment_dissatisfied)
                    .into(holder.imgLook);
        }
    }

    @Override
    public int getItemCount() {
        return looks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvHead;
        private ImageView imgLook;
        private LinearLayout cvLook;

        public ViewHolder(View itemView) {
            super(itemView);

            tvHead      = itemView.findViewById(R.id.tvHead);
            imgLook     = itemView.findViewById(R.id.imgLook);
            cvLook      = itemView.findViewById(R.id.cvLook);
        }
    }


}
