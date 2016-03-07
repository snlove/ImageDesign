package sn.imagedesign.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by acer on 2016/3/7.
 */
public class CloseUtils  {

    public static void closeQuietly(Closeable closeable) {
        if (null != closeable) {
            try {
                 closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
