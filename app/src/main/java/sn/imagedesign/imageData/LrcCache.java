package sn.imagedesign.imageData;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.InputStream;

import sn.imagedesign.utils.BitmapHandle;

/**图片缓存类
 * Created by acer on 2016/3/6.
 */
public class LrcCache implements ImageCached{

//    利用Lru进行图片缓存
    LruCache<String ,Bitmap> mcached;


    public LrcCache() {
        initImageCache();
    }

    /**
     * 设置图片缓存的一些设置
     *
     * @author sn
     * 2016/3/6  16:34
     */
    private void initImageCache() {
//       get the running time memory
        int maxMem = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //set the cached size is the 0,24 maxMemory
        int cachesize = maxMem / 8;
        mcached =  new LruCache<String ,Bitmap>(cachesize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() /1024;
            }
//            必须重写该方法
        };
    }


    @Override
    public void put(String value, InputStream in) {
        if (value != null && in != null) {
            synchronized (LrcCache.this) {
                Bitmap bitmap = BitmapFactory.decodeStream(in);
                if (!bitmap.equals(mcached.get(value))) {
                    mcached.put(value, bitmap);
                }
            }
        }
    }

    @Override
    public synchronized Bitmap get(String url, ImageView view) {
        validateKey(url);
        Bitmap bitmap = mcached.get(url);
        if (bitmap != null) {
            return BitmapHandle.rejustSize(bitmap, view.getWidth(), view.getHeight());
        }
        return null;

    }
    private void validateKey(String key) {
        if (key.contains(" ") || key.contains("\n") || key.contains("\r")) {
            throw new IllegalArgumentException(
                    "keys must not contain spaces or newlines: \"" + key + "\"");
        }
    }


}
