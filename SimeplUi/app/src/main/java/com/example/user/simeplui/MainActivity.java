package com.example.user.simeplui;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText edittext ; // 建立EditText 物件
    private CheckBox hideCheckBox ; // 建立CheckBox 物件
    private ListView historyListView ; //建立ListView 物件
    private Spinner  storeInfoSpinner ; //建立Spinner 物件


    String[] data ;

    //log
    private static final String TAG = "Bmi";

    //用途儲存私有的簡單資料在鍵-值配對 簡單存放到的編輯
    private SharedPreferences sharedPreferences; // 轻量级的存储类的物件
    private SharedPreferences.Editor editor; // 用來編輯轻量级的存储类的物件

    // 建立生命週期
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);// 指定layout檔

        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryClass();

        //
        edittext = (EditText)findViewById(R.id.input); //    物件轉換，透過viwe的findViewById，尋找Ｒ.的ID.的名稱(ＮＡＭＥ)，可能控制此物件
        hideCheckBox = (CheckBox)findViewById(R.id.hidecheckBox);
        historyListView = (ListView)findViewById(R.id.historyListView);
        storeInfoSpinner = (Spinner)findViewById(R.id.storeInofSpinner);


        checkModify();


        sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        /* 第一個參數 放置檔名 ,手機本機端(DATA\DATA\APP 專案名稱\Shared_Prefs)
         * 第二個參數 檔案分享模式
         * MODE_PRIVATE	該檔案是私有的，其它應用程式都無法存取（預設值）。
         * MODE_APPEND	當該檔案存在時，資料會從後面繼續寫入，否則就建立新檔案。
         * MODE_WORLD_READABLE	裝置內的其它應用程式都可以讀取。
         * MODE_WORLD_WRITEABLE	裝置內的應用程式都可以寫入。
         * MODE_MULTI_PROCESS	為一個旗標，在Android 2.3及以前，該旗標預設為開啟，允許多個行程同時存取同一個SharedPrecferences物件。而之後的Android版本，必須透過明確的將值設定後才能開啟多個行程的存取。
         */

        editor = sharedPreferences.edit(); // 將原本資料提交SharedPreferences的的編輯器用


        edittext.setText("GO input any word"); // 設定指定文字

        edittext.setText(sharedPreferences.getString("edittext", " "));//指定key從存储类載出資料
        /** 資料的取出動作
         *  第一個參數 取得放置key物件名稱並取出之前資料 第二參數 如果取出key物件沒有任何資料的話，必須指定預設資訊文字
        **/

        edittext.setOnKeyListener(new View.OnKeyListener() { //監聽鍵盤動作
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent keyEvent) {
                if (keyEvent.getAction() == keyEvent.ACTION_DOWN) { // 監聽鍵盤動作
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {  //按鈕ENTER
                        submit(v);
                        return true;
                    }
                }
                  return false;
              }
          });

          //hideCheckBox.setChecked(true);

          //sethistry();

        setStoreIofo();


        storeInfoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(MainActivity.this, "你選的是"+data[position], Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(MainActivity.this,"沒有動作", Toast.LENGTH_SHORT).show();

            }
        });

    }


    public void onStart() {
        super.onStart();
        Log.v(TAG,"onStart_MainActivity");
    }

    public void onResume() {
        super.onResume();
        Log.v(TAG,"onResume_MainActivity");
    }

    public void onPause()
    {
        super.onPause();
        Log.v(TAG,"onPause_MainActivity");
    }

    public void onStop() {
        super.onStop();
        Log.v(TAG,"onStop_MainActivity");
    }

    public void onRestart()
    {
        super.onRestart();
        Log.v(TAG,"onReStart");
    }

    public void onDestroy()
    {
        super.onDestroy();
        Log.v(TAG,"onDestroy_MainActivity");
    }

    // 定義顯示在ListView的列表上
    private void sethistry() {
        //String[] data = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        String[] data = Utils.readFile(this,"history.txt").split("\n"); //字尾有＼Ｎ就會分行
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, data);
        historyListView.setAdapter(adapter);
    }

    //
    private  void  setStoreIofo(){
        data = getResources().getStringArray(R.array.storeInfo); // 如果你的附屬檔案放置在(res)必須呼叫getResources()
        ArrayAdapter<String> stroeadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,data);
        storeInfoSpinner.setAdapter(stroeadapter);
    }

    // view可以跟你說 你會用到哪一個元件動作
    public void submit(View view){

        String test = edittext.getText().toString();
        editor.putString("edittext", test);
        /**
         * 資料的存入動作
         * 第一個參數 放置key物件名稱 第二參數 存放的資料
         **/
        editor.commit(); // 必須執行這行，資料才會真正存入到檔案裡面
        Utils.writeFile(this, "history.txt", test + "\n"); // 將字串寫入到檔案動作

        if(hideCheckBox.isChecked()){
            test = "**********" ;
            edittext.setText("**********");
        }else{
            edittext.setText("");
        }

        sethistry();

        //String fileContent = Utils.readFile(this,"history.txt");// 從檔案中讀取內容
        //Toast.makeText(this, fileContent, Toast.LENGTH_LONG).show();
        /* 氣泡訊息顯示
         * 參數一 指定是你的應用程式(Application)或活動(Activity)物件
         * 參數二 需要顯示文字(帶入可以是字串或是可以用到資源庫(RES)的Values的表示ex:R.string.go1)
         * 參數三 顯示時間長度
         * Toast.LENGTH_LONG  長時間顯示
         * Toast.LENGTH_SHORT 短時間顯示
         * PS 記得要下.SHOW()才能正常顯示
         */

        //Toast.makeText(this,test,Toast.LENGTH_LONG).show(); // 長期顯示_顯示在螢幕上的氣泡訊息
        /* 氣泡訊息顯示
         * 參數一 指定是你的應用程式(Application)或活動(Activity)物件
         * 參數二 需要顯示文字(帶入可以是字串或是可以用到資源庫(RES)的Values的表示ex:R.string.go1)
         * 參數三 顯示時間長度
         * Toast.LENGTH_LONG  長時間顯示
         * Toast.LENGTH_SHORT 短時間顯示
         * PS 記得要下.SHOW()才能正常顯示
         */

        //Toast.makeText(this,"132132132131321321213213",Toast.LENGTH_LONG).show(); // 短暫訊息_顯示在螢幕上的氣泡訊息
        /* 氣泡訊息顯示
         * 參數一 指定是你的應用程式(Application)或活動(Activity)物件
         * 參數二 需要顯示文字(帶入可以是字串或是可以用到資源庫(RES)的Values的表示ex:R.string.go1)
         * 參數三 顯示時間長度
         * Toast.LENGTH_LONG  長時間顯示
         * Toast.LENGTH_SHORT 短時間顯示
         * PS 記得要下.SHOW()才能正常顯示
         */

        //edittext.setText(""); // 設定指定文字

        /*
        //建立提示訊息框
        AlertDialog builder = new AlertDialog.Builder(MainActivity.this).create();
        builder.setTitle("訊息顯示");
        builder.setMessage(test);
        builder.setButton(AlertDialog.BUTTON_POSITIVE, "Button 1 Text", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                //...

            } });

        builder.setButton(AlertDialog.BUTTON_NEGATIVE, "Button 2 Text", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                //...

            }
        });

        builder.setButton(AlertDialog.BUTTON_NEUTRAL, "Button 3 Text", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                //...

            }
        });


        builder.show();
        //AlertDialog dialog = builder.create();
        */
/*
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("訊息顯示");
        builder.setMessage(test);

        builder.setNegativeButton(R.string.button_stop , new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNeutralButton(R.string.button_close , new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton(R.string.button_ok , new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        });

        builder.show();

*/
    }

    /*
     * 這是呼叫 checkbox 物件 動作
     */
    public void putcheckbok(View view){

        if(hideCheckBox.isChecked()){
            Toast.makeText(this,"Checked已打勾",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Checked打勾已取消",Toast.LENGTH_LONG).show();
        }

        editor.putBoolean("hideCheckBox",hideCheckBox.isChecked());
        editor.commit();

    }


    /*
    * 這是呼叫 物件進行另一個頁面活動
    */
    public void nexttopage(View view){

        //setContentView(R.layout.activity_drink_menu);
        /*
        *  表明的意圖 單向開啟動作
        */
        MainActivity.this.getApplicationContext();
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, DrinkMenuActivity.class); // 表明前往地方 CONtext(資源 權限) 前往的Activity
        //startActivity(intent);//進行單向開啟動作 DrinkMenuActivity
        startActivityForResult(intent, 0); //進行雙向動作 DrinkMenuActivity

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (resultCode) {
            case RESULT_OK:
                Bundle bunde = data.getExtras();
                String Json_String = bunde.getString("JSON");
                Toast.makeText(this, Json_String ,Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }




    public void tohidecheobjace(View view){
        int intest = storeInfoSpinner.getVisibility();
        String strin_int ;
        strin_int = String.valueOf(intest);
        Toast.makeText(this,strin_int,Toast.LENGTH_LONG).show();

        if(intest == 0 ){
        storeInfoSpinner.setVisibility(view.INVISIBLE);
        }else if(intest == 4){
            storeInfoSpinner.setVisibility(view.VISIBLE);
        }
        /*else if(intest == 8){
            storeInfoSpinner.setVisibility(view.GONE);
        }*/

    }

    private boolean checkModify()
    {
        ApplicationInfo localApplicationInfo = getApplicationInfo();
        Log.v("MainActivity", "manageSpaceActivityName: " + localApplicationInfo.manageSpaceActivityName);
        Log.v("MainActivity", "nativeLibraryDir: " + localApplicationInfo.nativeLibraryDir);
        Log.v("MainActivity", "processName: " + localApplicationInfo.processName);
        Log.v("MainActivity", "publicSourceDir: " + localApplicationInfo.publicSourceDir);
        Log.v("MainActivity", "sourceDir: " + localApplicationInfo.sourceDir);
        String str1 = getPackageCodePath();
        Log.v("MainActivity", "getPackageCodePath: " + str1);
        String str2 = "";
        String[] arrayOfString;
        return false;
    }


}
