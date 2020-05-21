package com.example.microponenciaandroidmedia;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MediaStore_Adapter extends RecyclerView.Adapter<MediaStore_Adapter.ViewHolderMedia>
{
    ArrayList<MediaStoreItems> Items;

    public MediaStore_Adapter(ArrayList<MediaStoreItems> items) {
        Items = items;
    }
    private View.OnClickListener Listener;

    public void setListener(View.OnClickListener listener) {
        Listener = listener;
    }

    @NonNull
    @Override
    public ViewHolderMedia onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_inflate, parent, false);
        v.setOnClickListener(Listener);

        return new ViewHolderMedia(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderMedia holder, int position) {
        holder.AsignarDatos(Items.get(position));
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

    public class ViewHolderMedia extends RecyclerView.ViewHolder {
        ImageView Imagen;
        //TextView Ubication;

        public ViewHolderMedia(@NonNull View itemView) {
            super(itemView);
            Imagen = itemView.findViewById(R.id.imageViewImagen);

            //Ubication = itemView.findViewById(R.id.textViewUbicacion);
        }

        public void AsignarDatos(MediaStoreItems mediaStoreItems) {





            Imagen.setImageBitmap(mediaStoreItems.getThumbMail());

        }
    }
}
