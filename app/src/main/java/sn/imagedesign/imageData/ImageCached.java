package sn.imagedesign.imageData;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.InputStream;

/**图片缓存处理接口
 * Created by acer on 2016/3/7.
 */
public interface ImageCached {
    abstract void put(String url, InputStream inputStream);
    abstract Bitmap get(String url, ImageView view);
}
