package com.example.microponenciaandroidmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private final int READEXTERNALSTORE_CODE = 1;
    RecyclerView re;
    ArrayList<MediaStoreItems> Items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(!TienePermisosLecutra())
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READEXTERNALSTORE_CODE);
        }

        else
        {
            re = findViewById(R.id.recyclerview);
            Items = GetListaImagenes();
            Toast.makeText(getApplicationContext(),LoadTMediashumbnails(Items)+" Errores.",Toast.LENGTH_SHORT).show();

            MediaStore_Adapter adaptador = new MediaStore_Adapter(Items);
            adaptador.setListener(ImageClickListener);
            re.setAdapter(adaptador);
            re.setLayoutManager( new GridLayoutManager(getApplicationContext(), calculateNoOfColumns(getApplicationContext(),120)));

        }


    }
    View.OnClickListener ImageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int num = re.getChildLayoutPosition(v);
            MediaStoreItems item = Items.get(num);
            LoadMediaUbication(item);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(item.getDisplayName());

            Spanned sp = Html.fromHtml(item.GetAlertFormat(), Html.FROM_HTML_MODE_COMPACT);
            builder.setMessage(sp);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    public ArrayList<MediaStoreItems> GetListaImagenes()
    {

        String[] projection = new String[]{
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.SIZE};


        String selection = MediaStore.Images.Media.DATE_ADDED + ">= ?";

        String[] selecionArgs = new String[]{GetLastYearDate().toString()};

        String sortOrder = MediaStore.Images.Media.DATE_ADDED+" DESC"+" LIMIT 100"; //ASC && DESC

        Cursor c = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selecionArgs, sortOrder);

        //Throws IllegalArgumentException
        int columna_id = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
        int columna_fecha = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED);
        int columna_nombre = c.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
        int columna_size = c.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE);
        Log.i("TAGs ", "Se han encontrado: " + c.getCount() + " Imagenes");

        ArrayList<MediaStoreItems> imagenes = new ArrayList<>();

        while (c.moveToNext()) {

            Long id = c.getLong(columna_id);
            String displayname = c.getString(columna_nombre);
            Date datemodif = new Date(TimeUnit.SECONDS.toMillis(c.getLong(columna_fecha)));
            Long size = c.getLong(columna_size);


            Uri contenturi = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            MediaStoreItems imagen = new MediaStoreItems(id, displayname, datemodif, contenturi, size);

            imagenes.add(imagen);
        }
        c.close();

        return imagenes;
    }
    private boolean LoadMediaUbication(MediaStoreItems item)
    {
        Boolean ret = false;
        try {
            InputStream in2 = getApplicationContext().getContentResolver().openInputStream(item.getLinkedUri());
            ExifInterface exif = new ExifInterface(in2);

            float[] latLong = new float[2];
            if(exif.getLatLong(latLong))
            {
                item.setLatLong(latLong);
            }
            ret = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
    private int LoadTMediashumbnails(ArrayList<MediaStoreItems> items)
    {
        int ErrorNumber = 0;

        for(MediaStoreItems item : items)
        {
            try
            {
                InputStream in = getApplicationContext().getContentResolver().openInputStream(item.getLinkedUri());
                Bitmap bit = BitmapFactory.decodeStream(in);
                item.setThumbMail(ThumbnailUtils.extractThumbnail(bit, 520,520));
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
                ErrorNumber++;
            }
        }
        return ErrorNumber;
    }

    private Long GetLastYearDate()
    {
        Date fecha = new Date();
        SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        String[] sfecha = formato.format(fecha).split("-");
        int newYear = Integer.parseInt(sfecha[2]);
        newYear--;
        try {fecha = formato.parse(sfecha[0]+"-"+sfecha[1]+"-"+newYear);}
        catch (ParseException e) {e.printStackTrace();}
        return TimeUnit.MICROSECONDS.toSeconds(fecha.getTime());
    }

    private boolean TienePermisosLecutra()
    {
        return ActivityCompat.checkSelfPermission(getApplicationContext(),  Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == READEXTERNALSTORE_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            Intent i = new Intent(MainActivity.this, MainActivity.class);
        }
    }

        public  int calculateNoOfColumns(Context context, float columnWidthDp) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
            int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5);
            return noOfColumns;
        }


}
