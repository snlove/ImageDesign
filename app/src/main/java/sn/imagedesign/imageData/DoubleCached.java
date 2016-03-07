package sn.imagedesign.imageData;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.InputStream;

/**磁盘和内存缓存的双重策略实现
 * Created by acer on 2016/3/7.
 */
public class DoubleCached implements ImageCached {

    LrcCache  memoryCached;
    DiskCached diskCached;

    public DoubleCached() {
        initCached();
    }

    private void initCached() {
        memoryCached = new LrcCache();
        diskCached = new DiskCached();
    }

    @Override
    public void put(String url, InputStream in) {
            memoryCached.put(url,in);
            diskCached.put(url,in);
    }

    @Override
    public Bitmap get(String url, ImageView view) {
        Bitmap bitmap = memoryCached.get(url,view);
        if (bitmap == null) {
            bitmap = diskCached.get(url,view);
        }

        return  bitmap;
    }
}
