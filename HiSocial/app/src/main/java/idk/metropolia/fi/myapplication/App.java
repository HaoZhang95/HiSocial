package idk.metropolia.fi.myapplication;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;


import skin.support.SkinCompatManager;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;
import skin.support.utils.Slog;

/**
 * Created by ximsfei on 2017/1/10.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Slog.DEBUG = true;
        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinMaterialViewInflater())    // material design
                .addInflater(new SkinConstraintViewInflater())  // ConstraintLayout
                .addInflater(new SkinCardViewInflater())        // CardView v7
                .setSkinStatusBarColorEnable(false)              // 关闭状态栏换肤
//                .setSkinWindowBackgroundEnable(false)           // 关闭windowBackground换肤
//                .setSkinAllActivityEnable(false)                // true: 默认所有的Activity都换肤; false: 只有实现SkinCompatSupportable接口的Activity换肤
                .loadSkin();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
