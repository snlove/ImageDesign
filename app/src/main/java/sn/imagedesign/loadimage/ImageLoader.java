package sn.imagedesign.loadimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sn.imagedesign.imageData.ImageCached;
import sn.imagedesign.imageData.LrcCache;
import sn.imagedesign.utils.BitmapHandle;
import sn.imagedesign.utils.CloseUtils;
import sn.imagedesign.utils.Constans;

/**图片加载类
 * Created by acer on 2016/3/6.
 */
public class ImageLoader {

    private ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private ImageCached imageCached;

    public ImageLoader() {

    }
    public ImageLoader(LrcCache mcached) {
        this.imageCached = mcached;
    }

    /**
     * ImageDisplay
     *
     * @author sn
     * 2016/3/6  16:29
     */
    public void display(final String url, final ImageView imageView) {
        Bitmap bitmap = imageCached.get(url,imageView);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            Log.d(Constans.TAG, "run: sucess");
            return;
        } else {
            Log.d(Constans.TAG, "run: failue");
        }
        imageView.setTag(url);
        service.submit(new Runnable() {

            @Override
            public void run() {
                Log.d(Constans.TAG, "run: the downImage is start");
                InputStream in = downloadImage(url);
                Log.d(Constans.TAG, "run: the downImage is finish");
                if (in  == null) {
                    Log.d(Constans.TAG, "run: the downImage is failue");
                    return;
                }
                //Bitmap bitmap = BitmapHandle.decordFrmSteam(in,imageView.getWidth(),imageView.getHeight());
               Bitmap bitmap = BitmapFactory.decodeStream(in);
                if (imageView.getTag().equals(url)) {
                    imageView.setImageBitmap(bitmap);
                    Log.d(Constans.TAG, "run: the downImage is set" + bitmap.getHeight());
                }
                checkImageCached();
//                imageCached.put(url,in);
                CloseUtils.closeQuietly(in);

            }
        });

    }

    private void checkImageCached() {
        if (imageCached == null) {
            throw new NullPointerException("the imagecahced is null, you must set it");
        }
    }

    /**
     * 下载图片
     *一般使用第三库，需要针对网络的不同和网速的不同进行处理，后期进行这方面的迭代
     * @author sn
     * 2016/3/6  16:53
     */
    public static InputStream downloadImage(String url) {
        Log.d(Constans.TAG, "downloadImage:  the image is downloading" );
        InputStream in = null;
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        int response = -1;
        try {
            URL downUrl = new URL(url);
            conn = (HttpURLConnection) downUrl.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            conn.connect();
            response = conn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = conn.getInputStream();
                Log.d(Constans.TAG, "downloadImage: the stream is get");
            }
            if (response == -1) {
                Log.d(Constans.TAG, "downloadImage: the connect is wrong");
            }

        } catch (MalformedURLException e) {
            Log.d(Constans.TAG, "downloadImage: the url is wrong");
        } catch (IOException e) {
            Log.d(Constans.TAG, "The conninect is time out   " + e.getLocalizedMessage());
        } finally {

            if (conn != null) {
                conn.disconnect();
            }
        }

        return in;

    }

    public void setImageCached(ImageCached imageCached) {
        this.imageCached = imageCached;
    }
}
