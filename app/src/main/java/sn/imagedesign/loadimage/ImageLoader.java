package sn.imagedesign.loadimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sn.imagedesign.imageData.ImageCache;
import sn.imagedesign.utils.Constans;

/**图片加载类
 * Created by acer on 2016/3/6.
 */
public class ImageLoader {

    private ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private ImageCache imageCache;
    private LruCache<String,Bitmap> mCached;

    public ImageLoader(ImageCache mcached) {
        this.imageCache = mcached;
    }

    /**
     * ImageDisplay
     *
     * @author sn
     * 2016/3/6  16:29
     */
    public void display(final String url, final ImageView imageView) {
        imageView.setTag(url);
        service.submit(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = downloadImage(url);
                if (bitmap == null) {
                    Log.d(Constans.TAG, "run: the downImage is failue");
                    return;
                }
                if (imageView.getTag().equals(url)) {
                    imageView.setImageBitmap(bitmap);
                }
                imageCache.put(url,bitmap);
            }
        });

    }

    /**
     * 下载图片
     *一般使用第三库，需要针对网络的不同和网速的不同进行处理，后期进行这方面的迭代
     * @author sn
     * 2016/3/6  16:53
     */
    private Bitmap downloadImage(String url) {
        InputStream in = null;
        Bitmap bitmap = null;
        try {
            URL downUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) downUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(500);
             in = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (MalformedURLException e) {
            Log.d(Constans.TAG, "downloadImage: the url is wrong");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bitmap;

    }






}
