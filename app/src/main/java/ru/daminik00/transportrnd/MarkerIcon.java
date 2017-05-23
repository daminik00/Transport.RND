package ru.daminik00.transportrnd;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

class MarkerIcon {

    private Bitmap.Config conf = Bitmap.Config.ARGB_8888;
    private Bitmap bmp = Bitmap.createBitmap(100, 100, conf);
    private Canvas canvas1 = new Canvas(bmp);

    private int color;
    private String number;
    private int inc;

    MarkerIcon(int color, String number, int inc) {
        this.color = color;
        this.number = number;
        this.inc = inc;
        genIcon();
    }

    private Bitmap genIcon() {

        float x = 0;
        float y = 0;
//        float x1 = 0;
//        float y1 = 0;
//
//        float c = (float) (4/(Math.sin(45)));
        if (inc == 0) {
            x = 50;
            y = 50-34;
        } else if (inc == 90) {
            x = 50+34;
            y = 50;
        } else if (inc == 180) {
            x = 50+34;
            y = 50-34;
        } else if (inc == 270) {
            x = 50-34;
            y = 50;
        } else if (inc == -1) {
            x = -20;
            y = -20;
        } else if (inc > 0 && inc < 90) {
            inc = 90 - inc;
            inc = 90 - inc;
            x = Math.abs((float) (-1 * (Math.cos(inc) * 34)));
            y = Math.abs((float) (-1 * (Math.sin(inc) * 34)));
            x = 50 + (1 * x);
            y = 50 + (-1 * y);
//            x1 = x - c;
//            y1 = y + c;
        } else if (inc > 90 && inc < 180) {
            inc -= 90;
            x = Math.abs((float) (-1 * (Math.cos(inc) * 34)));
            y = Math.abs((float) (-1 * (Math.sin(inc) * 34)));
            x = 50 + (1 * x);
            y = 50 + (1 * y);
//            x1 = x - c;
//            y1 = y + c;
        } else if (inc > 180 && inc < 270) {
            inc -= 180;
            inc = 90 - inc;
            x = Math.abs((float) (-1 * (Math.cos(inc) * 34)));
            y = Math.abs((float) (-1 * (Math.sin(inc) * 34)));
            x = 50 + (-1 * x);
            y = 50 + (1 * y);
//            x1 = x - c;
//            y1 = y + c;
        } else if (inc > 270 && inc < 360) {
            inc -= 270;
            inc = 90 - inc;
            x = Math.abs((float) (-1 * (Math.cos(inc) * 34)));
            y = Math.abs((float) (-1 * (Math.sin(inc) * 34)));
            x = 50 + (-1 * x);
            y = 50 + (-1 * y);
//            x1 = x - c;
//            y1 = y + c;
        }

        Paint color = new Paint();
//
//        color.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//        color.setColor(Color.BLACK);
//
//        canvas1.drawCircle(10,70,10,color);
//        canvas1.drawCircle(70,70,10,color);
//
//        canvas1.drawCircle(20,20,20,color);

        color.setTextSize(50);
        color.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        color.setColor(Color.BLACK);
        canvas1.drawCircle(50, 50, 34, color);
        color.setColor(this.color);
        canvas1.drawCircle(50, 50, 30, color);
        color.setColor(Color.BLACK);
        canvas1.drawCircle(x, y, 12, color);
        if (number.length() == 1) {
            canvas1.drawText(number, 40 , 60, color);
        } else {
            canvas1.drawText(number, 20 , 60, color);
        }

//
//
//        Paint triangle = new Paint();
//        triangle.setColor(Color.RED);
//
//        Path path = new Path();
//        path.setFillType(Path.FillType.EVEN_ODD);
//        path.moveTo(y1, y); //добавьте эту строку
//        path.lineTo(x, y);
//        path.lineTo(x, x1);
//        path.lineTo(y1, y);
//        path.close();
//        canvas1.drawPath(path, triangle);

        return this.bmp;
    }

    BitmapDescriptor getIcon() {
        return BitmapDescriptorFactory.fromBitmap(bmp);
    }
}
