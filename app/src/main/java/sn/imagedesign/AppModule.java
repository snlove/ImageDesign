package sn.imagedesign;

/**
 * Created by acer on 2016/3/7.
 */
public class AppModule {

    private static MainActivity main;

    public static MainActivity getMain() {
        return main;
    }

    public static void setMain(MainActivity main) {
        AppModule.main = main;
    }
}
