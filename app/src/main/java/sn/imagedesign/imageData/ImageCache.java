package sn.imagedesign.imageData;

import android.graphics.Bitmap;
import android.util.LruCache;

/**图片缓存类
 * Created by acer on 2016/3/6.
 */
public class ImageCache {

//    利用Lru进行图片缓存
    LruCache<String ,Bitmap> mcached;


    public ImageCache() {
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
        int cachesize = maxMem / 4;
        mcached =  new LruCache<String ,Bitmap>(cachesize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight() /1024;
            }
//            必须重写该方法
        };
    }

    public LruCache<String, Bitmap> getMcached() {
        return mcached;
    }

    /**
     * 将图片存储起来
     *
     * @author sn
     * 2016/3/6  16:26
     */
    public void put(String value, Bitmap element) {
        mcached.put(value, element);
    }

    /**
     * 得到图片
     *
     * @author sn
     * 2016/3/6  16:27
     */
    public Bitmap get(String value) {
        return mcached.get(value);
    }






}
