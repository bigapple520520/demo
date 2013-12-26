package com.xuan.ahost;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import dalvik.system.DexClassLoader;

@SuppressLint("NewApi")
public class AhostMain extends Activity {
	private Object aplugInActivityClassInstance;
	private Class<?> aplugInActivityClass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ahost_main);

		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle paramBundle = new Bundle();
				paramBundle.putBoolean("KEY_START_FROM_OTHER_ACTIVITY", true);
				paramBundle.putString("str", "PlugActivity");
				String dexpath = Environment.getExternalStorageDirectory()
						+ "/ahost/aplugin.apk";
				String dexoutputpath = Environment
						.getExternalStorageDirectory() + "/ahost/";
				LoadApk(paramBundle, dexpath, dexoutputpath);
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		Method start;
		try {
			start = aplugInActivityClass.getMethod("onStart");
			start.invoke(aplugInActivityClassInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Method resume;
		try {
			resume = aplugInActivityClass.getMethod("onResume");
			resume.invoke(aplugInActivityClassInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Method pause;
		try {
			pause = aplugInActivityClass.getMethod("onPause");
			pause.invoke(aplugInActivityClassInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		try {
			Method stop = aplugInActivityClass.getMethod("onStop");
			stop.invoke(aplugInActivityClassInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			Method des = aplugInActivityClass.getMethod("onDestroy");
			des.invoke(aplugInActivityClassInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void LoadApk(Bundle paramBundle, String dexpath, String dexoutputpath) {
		ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
		DexClassLoader localDexClassLoader = new DexClassLoader(dexpath,
				dexoutputpath, null, localClassLoader);
		try {
			PackageInfo plocalObject = getPackageManager()
					.getPackageArchiveInfo(dexpath, 1);

			if ((plocalObject.activities != null)
					&& (plocalObject.activities.length > 0)) {
				String activityname = plocalObject.activities[0].name;
				Log.d("--------------------", "activityname = " + activityname);

				aplugInActivityClass = localDexClassLoader
						.loadClass(activityname);

				Constructor<?> localConstructor = aplugInActivityClass
						.getConstructor(new Class[] {});
				aplugInActivityClassInstance = localConstructor
						.newInstance(new Object[] {});
				Log.d("--------------------", "instance = "
						+ aplugInActivityClassInstance);

				Method des = aplugInActivityClass.getMethod("test");
				des.invoke(aplugInActivityClassInstance);

				Method localMethodSetActivity = aplugInActivityClass
						.getDeclaredMethod("setActivity",
								new Class[] { Activity.class });
				localMethodSetActivity.setAccessible(true);
				localMethodSetActivity.invoke(aplugInActivityClassInstance,
						new Object[] { this });

				Method methodonCreate = aplugInActivityClass.getDeclaredMethod(
						"onCreate", new Class[] { Bundle.class });
				methodonCreate.setAccessible(true);
				methodonCreate
						.invoke(aplugInActivityClassInstance, paramBundle);
			}
			return;
		} catch (Exception ex) {
			Log.d("----------", "", ex);
		}
	}

}
