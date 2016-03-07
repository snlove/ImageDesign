package sn.imagedesign.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by acer on 2016/3/7.
 */
public class BitmapHandle {


    /**
     * 从资源中加载图片
     *
     * @author sn
     * 2016/3/7  10:31
     */
    public static Bitmap decordFrmResource(Resources rs,int resId, int resWid, int resHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false; //只得到大小而不下载真个图片
        options.inSampleSize = cacluateSampSize(options, resWid, resHeight);
        options.inJustDecodeBounds = true;
        return BitmapFactory.decodeResource(rs, resId, options);
    }

    public static Bitmap decordFromFD(FileDescriptor fd, int resWid, int resHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false; //只得到大小而不下载真个图片
        options.inSampleSize = cacluateSampSize(options, resWid, resHeight);
        options.inJustDecodeBounds = true;
        return BitmapFactory.decodeFileDescriptor(fd,null,options);
    }

    /**
    *根据需要加载不同大小的不同,压缩处理图片
    *@author sn
    * 2016/3/7  16:56
    *
    */



    public static Bitmap rejustSize(Bitmap bitmap,int resWid,int resHeight) {
        int samSize = cacluateSampSize(bitmap,resWid,resHeight);
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(1/samSize, 1/samSize);
        bitmap.recycle();
        return  Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }




    /**
     * 从流中加载图片
     *
     * @author sn
     * 2016/3/7  10:53
     */
    public static Bitmap decordFrmSteam(InputStream in,int resWid,int resHeight) {
        Bitmap bitmap = null;
        try {
            FileInputStream is = (FileInputStream) in;
            //利用fileDescr得到描述
            FileDescriptor fd = is.getFD();
            bitmap = decordFromFD(fd,resWid,resHeight);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }




    private static int cacluateSampSize(BitmapFactory.Options options, int resWid, int resHeight) {
        if (resHeight == 0 || resWid == 0) {
            return 1;
        }
        int realWid = options.outWidth;
        int relHeight = options.outHeight;
        int sampleSize = 1;


        if (realWid > resWid || relHeight > resHeight) {
            final  int halfWid = realWid / 2;
            final  int halfHeight = relHeight / 2;

            while (halfWid /sampleSize >= realWid && halfHeight /sampleSize >=relHeight) {
                sampleSize *= 2;
            }
        }



        return sampleSize;
    }

    private static int cacluateSampSize(Bitmap bitmap, int resWid, int resHeight) {
        if (resHeight == 0 || resWid == 0) {
            return 1;
        }
        int realWid = bitmap.getWidth();
        int relHeight = bitmap.getHeight();
        int sampleSize = 1;


        if (realWid > resWid || relHeight > resHeight) {
            final  int halfWid = realWid / 2;
            final  int halfHeight = relHeight / 2;

            while (halfWid /sampleSize >= realWid && halfHeight /sampleSize >=relHeight) {
                sampleSize *= 2;
            }
        }



        return sampleSize;
    }


}
