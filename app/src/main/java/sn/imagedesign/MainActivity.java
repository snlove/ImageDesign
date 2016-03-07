package sn.imagedesign;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import sn.imagedesign.imageData.DoubleCached;
import sn.imagedesign.imageData.LrcCache;
import sn.imagedesign.loadimage.ImageLoader;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private ImageView imageView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageShow);
        initView();
    }

    private void initView() {
        ImageLoader load = new ImageLoader();
        load.setImageCached(new LrcCache());
        load.display("http://www.baidu.com/img/bdlogo.gif",imageView);
    }


}
