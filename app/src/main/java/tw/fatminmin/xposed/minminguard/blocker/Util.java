package tw.fatminmin.xposed.minminguard.blocker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import tw.fatminmin.xposed.minminguard.Common;
import tw.fatminmin.xposed.minminguard.Main;

public final class Util {
    
    public static XSharedPreferences pref;
    
    
    final static public String TAG = "MinMinGuard";
    final static public String PACKAGE = "tw.fatminmin.xposed.minminguard";

    private Util() throws InstantiationException {
        throw new InstantiationException("This class is not for instantiation");
    }

    static public Boolean xposedEnabled() {
        return false;
    }

    static public String getAppVersion(Context context) {
        String version = null;
        PackageManager pm = context.getPackageManager();
        try {
            version = pm.getPackageInfo(PACKAGE, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return version;
    }    

    static public void log(String packageName, String msg) {
        Log.d(TAG, packageName + ": " + msg);
    }
    
    static public Application getCurrentApplication() {
        try {
            return (Application)Class.forName("android.app.ActivityThread").
                    getMethod("currentApplication", new Class[0]).invoke(null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    static public void saveLog(final File dest, final Context context, final Handler handler) {
        
        handler.post(new Runnable() {

            @Override
            public void run() {
                try {


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    static public void notifyAdNetwork(final Context context, final String pkgName, final String adNetwork) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ContentResolver resolver = context.getContentResolver();
                    Uri uri = Uri.parse("content://tw.fatminmin.xposed.minminguard/");

                    ContentValues values = new ContentValues();
                    values.put(Common.KEY_PKG_NAME, pkgName);
                    values.put(Common.KEY_NETWORK, adNetwork);
                    resolver.update(uri, values, null, null);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    static public void notifyRemoveAdView(final Context context, final String pkgName, final int blockNum) {

        final Context mContext;
        if (context == null) {
            mContext = getCurrentApplication();
        }
        else {
            mContext = context;
        }

        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    ContentResolver resolver = mContext.getContentResolver();
                    Uri uri = Uri.parse("content://tw.fatminmin.xposed.minminguard/");

                    ContentValues values = new ContentValues();
                    values.put(Common.KEY_PKG_NAME, pkgName);
                    values.put(Common.KEY_BLOCK_NUM, blockNum);
                    resolver.update(uri, values, null, null);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Main.notifyWorker.submit(task);
    }
}
