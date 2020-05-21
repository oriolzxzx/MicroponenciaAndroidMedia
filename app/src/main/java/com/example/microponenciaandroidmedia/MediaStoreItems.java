package com.example.microponenciaandroidmedia;

import android.graphics.Bitmap;
import android.net.Uri;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MediaStoreItems {
    private long Id;
    private String DisplayName;
    private Date DateAdded;
    private float[] LatLong;
    private long Size;
    private Bitmap ThumbMail;
    private Uri LinkedUri;

    public MediaStoreItems(long id, String displayName, Date dateAdded, Uri linkedUri, long size) {
        Id = id;
        DisplayName = displayName;
        DateAdded = dateAdded;
        LinkedUri = linkedUri;
        Size =  size;
    }

    public void setThumbMail(Bitmap thumbMail) {
        ThumbMail = thumbMail;
    }

    public void setLatLong(float[] latLong) {
        LatLong = latLong;
    }
    public String GetAlertFormat()
    {
        DecimalFormat format = new DecimalFormat("##.00");
        StringBuilder str = new StringBuilder();

        str.append("<Strong>");
        str.append("Tamaño: ");
        str.append("</Strong>");
        str.append(format.format(Size/1024f));
        str.append("Kb");
        str.append("<br>");
        str.append("<Strong>");
        str.append("Fecha: ");
        str.append("</Strong>");
        SimpleDateFormat formato = new SimpleDateFormat("dd MM YYYY");
        str.append(formato.format(DateAdded));
        str.append("<br>");
        str.append("<Strong>");
        str.append("Hora: ");
        str.append("</Strong>");
        formato = new SimpleDateFormat("HH:mm");
        str.append(formato.format(DateAdded));
        if(LatLong != null)
        {
            str.append("<br>");
            str.append("<br>");
            str.append("<Strong>");
            str.append("Longitud: ");
            str.append("</Strong>");
            str.append(this.LatLong[0]);
            str.append("<br>");
            str.append("<Strong>");
            str.append("Latitud: ");
            str.append("</Strong>");
            str.append(this.LatLong[1]);
        }

        return str.toString();

    }

    public long getId() {
        return Id;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public Date getDateAdded() {
        return DateAdded;
    }

    public float[] getLatLong() {
        return LatLong;
    }

    public long getSize() {
        return Size;
    }

    public Bitmap getThumbMail() {
        return ThumbMail;
    }

    public Uri getLinkedUri() {
        return LinkedUri;
    }
}
