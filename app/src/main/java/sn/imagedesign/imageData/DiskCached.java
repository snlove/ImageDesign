package sn.imagedesign.imageData;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sn.imagedesign.AppModule;
import sn.imagedesign.utils.BitmapHandle;
import sn.imagedesign.MainActivity;

/**
 * Created by acer on 2016/3/7.
 */
public class DiskCached implements ImageCached {


    private static final String cachedDir = "sdcard/imagecache/";
    private static final int MAX_SIZE = 1024 * 1024 * 50;
    private static final int DISKE_CACHE_INDEX = 0; //节点数设置为1，这里就设置为零
    DiskLruCache diskLruCache;
    DiskLruCache.Editor editor;

    public DiskCached() {
        initDisk();
    }

    private void initDisk() {
        File driectoy = AppModule.getMain().getCacheDir();
        if (!driectoy.exists()) {
            driectoy.mkdirs();
        }
        try {
            if (AppModule.getMain().getFilesDir().getUsableSpace() > MAX_SIZE) {
                diskLruCache = DiskLruCache.open(driectoy, 1, 1, MAX_SIZE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 因为Url可能还有特殊字符，用MD5作为Key,
     *
     * @author sn
     * 2016/3/7  11:33
     */
    private String hadleFromUrl(String url) {
        String key;
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            key = byteHexToString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
//            无MD5值，就用hascode
            key = String.valueOf(url.hashCode());
        }
        return key;
    }

    /**
     * 将自己转化为字符串
     * @author sn
     * 2016/3/7  11:49
     */


    private String byteHexToString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (int i=0;i<bytes.length;i++) {
            String temp  = Integer.toHexString( 0xFF & bytes[i]);
            if (temp.length() == 1) {
                builder.append('0');
            }
            builder.append(temp);
        }
        return builder.toString();
    }


    @Override
    public void put(String value, InputStream in) {
//        FileOutputStream fos = null;
//        if (value != null && element != null) {
//            validateKey(value);
//            synchronized (DiskCached.class) {
//                try {
//                    if (!element.equals(get(value))) {
//                        fos = new FileOutputStream(cachedDir + value);
//                        element.compress(Bitmap.CompressFormat.PNG, 100, fos);
//                    }
//                } catch (FileNotFoundException fe) {
//                    Log.d(Constans.TAG, "put: " + fe.getLocalizedMessage());
//                } finally {
//                    CloseUtils.closeQuietly(fos);
//                }
//            }
//        }
        try {
            String key = hadleFromUrl(value);
            editor = diskLruCache.edit(key);
            if (editor != null) {
                OutputStream out = editor.newOutputStream(DISKE_CACHE_INDEX);
                int b = 0;
                while (( b = in.read()) != -1) {
                    out.write(b);
                }
                editor.commit();
                diskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public synchronized Bitmap get(String url, ImageView view) {
        String key = hadleFromUrl(url);
        Bitmap bitmap = null;
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if (snapshot != null) {
                //因为利用option进行缩放图片，对输入流进行了两次使用，由于fileinputstream是有序的，会是第
                //二次为空，处理如下
                FileInputStream is = (FileInputStream) snapshot.getInputStream(DISKE_CACHE_INDEX);
                //利用fileDescr得到描述
                 FileDescriptor fd = is.getFD();
                 bitmap = BitmapHandle.decordFromFD(fd,view.getWidth(),view.getHeight());

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  bitmap;
    }

    private void validateKey(String key) {
        if (key.contains(" ") || key.contains("\n") || key.contains("\r")) {
            throw new IllegalArgumentException(
                    "keys must not contain spaces or newlines: \"" + key + "\"");
        }
    }
}
