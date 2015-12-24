package com.example.user.simeplui;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText edittext ; // 建立EditText 物件
    private CheckBox hideCheckBox ; // 建立CheckBox 物件
    private ListView historyListView ; //建立ListView 物件
    private Spinner  storeInfoSpinner ; //建立Spinner 物件
    private ImageView photoimageview ;  //建立ImageView 物件
    private ProgressBar progressBar;    // 建立ProgressBar 物件

    String[] data ;

    //log
    private static final String TAG = "Bmi";

    //用途儲存私有的簡單資料在鍵-值配對 簡單存放到的編輯
    private SharedPreferences sharedPreferences; // 轻量级的存储类的物件
    private SharedPreferences.Editor editor; // 用來編輯轻量级的存储类的物件
    private static final int Resul＿code_MENU_Activity = 1;
    private static final int REQUEST_TAKE_PHOTO = 2;

    private String muneResul ;

    private  ParseObject testObject ;
    private  boolean hasphoto = false;
    private  ProgressDialog progressDialog; // 取的提示訊息列表
    private  List<ParseObject> queryResult;



    // 建立生命週期
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 加入API　 紀錄輸入的資料動作
        // [Optional] Power your app with Local Datastore. For more info, go to
        // https://parse.com/docs/android/guide#local-datastore
        //Parse.enableLocalDatastore(this);
        //Parse.initialize(this);
        testObject = new ParseObject("People");
        testObject.put("name", "TOM");
        testObject.put("age", "13");
        testObject.saveInBackground();


        //查詢 People 的資料 某一筆的資料 用 ID指定
        ParseQuery<ParseObject> query = ParseQuery.getQuery("People");
        query.getInBackground("c35I5r9Pa7", new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your game score
                    String name = (String)object.get("name");
                    Log.d("Data for Parse",name);

                } else {
                    // something went wrong
                    Log.e("ERROR for Parse",e.getMessage());
                }
            }
        });

        setContentView(R.layout.activity_main);// 指定layout檔

        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryClass();

        //
        edittext = (EditText)findViewById(R.id.input); //    物件轉換，透過viwe的findViewById，尋找Ｒ.的ID.的名稱(ＮＡＭＥ)，可能控制此物件
        hideCheckBox = (CheckBox)findViewById(R.id.hidecheckBox);
        historyListView = (ListView)findViewById(R.id.historyListView);
        storeInfoSpinner = (Spinner)findViewById(R.id.storeInofSpinner);
        photoimageview = (ImageView)findViewById(R.id.photo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

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

          setHistory(); // 讀取訂單資訊

          setStoreInfo(); //　取得店家資訊
          progressDialog = new ProgressDialog(this);


        storeInfoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(MainActivity.this, "你選的是"+data[position], Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                //Toast.makeText(MainActivity.this,"沒有動作", Toast.LENGTH_SHORT).show();

            }
        });


        historyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // position 第幾筆
                Toast.makeText(MainActivity.this,"編號"+position, Toast.LENGTH_SHORT).show();

                goToOrderDetail(position);
            }
        });

    }

    private void goToOrderDetail(int position) {

        Intent intent = new Intent();
        intent.setClass(this, OrderDetailActivity.class);
        ParseObject object = queryResult.get(position);
        intent.putExtra("storeInfo", object.getString("storeInfo"));
        intent.putExtra("note", object.getString("note"));
        startActivity(intent);


    }

    /**
     * 選項功能開啟
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "onStart_MainActivity");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "onResume_MainActivity");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        Log.v(TAG,"onPause_MainActivity");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "onStop_MainActivity");
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
        Log.v(TAG, "onReStart");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        Log.v(TAG, "onDestroy_MainActivity");
    }

    // 定義顯示在ListView的列表上
    private void setHistory() {

        //String[] data = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        //String[] rawdata = Utils.readFile(this,"history.txt").split("\n"); //字尾有＼Ｎ就會分行

        //　透過網路方式查詢Parse資料庫
        ParseQuery<ParseObject> query = new ParseQuery<>("Order");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                queryResult = objects;
                List<Map<String, String>> data = new ArrayList<>();
                for (int i = 0; i < objects.size(); i++) {
                    ParseObject object = objects.get(i);
                    String note = object.getString("note");
                    JSONArray array = object.getJSONArray("menu");
                    Map<String, String> item = new HashMap<>();
                    item.put("note", note);
                    item.put("drinkNum", "15");
                    item.put("storeInfo", "NTU Store");
                    data.add(item);
                 }

                String[] from = {"note", "drinkNum", "storeInfo"};
                int[] to = {R.id.note, R.id.drinkNum, R.id.storeInfo};
                SimpleAdapter adapter  = new SimpleAdapter(MainActivity.this, data, R.layout.listview_item, from, to); //
                historyListView.setAdapter(adapter);
                historyListView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);

            }
        });


        // Map 用來對應型態
        /*
        *  "P1" --> "foo"
        *  "P2"  --> "marry"
        *
        *  "NAME"  --> "TOM"
        *  "Birth" -->  "19991010"
        *  "age"  --->  "19"
        * */
        /*
        List<Map<String ,String>> data = new ArrayList<>();
        for (int i = 0 ; i < rawdata.length; i++){ //rawdata有幾行
            try {
                JSONObject object = new JSONObject(rawdata[i]) ;

                String note = object.getString("note");
                JSONArray array = object.getJSONArray("menu");
                Map<String ,String>item = new HashMap<>();//
                item.put("noet",note);
                item.put("drinkNum","15");
                item.put("storeInof"," not");

                data.add(item); // 要新增
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String[] from = {"noet","drinkNum","storeInof"};  // 對應關係
        int[] to = {R.id.note,R.id.drinkNum,R.id.storeInfo} ; // 對應關係

        SimpleAdapter adapter  = new SimpleAdapter(this, data, R.layout.listview_item, from, to); //
        historyListView.setAdapter(adapter);
        historyListView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        */
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, data);
        //historyListView.setAdapter(adapter);
    }

    //
    private  void  setStoreInfo(){
        //data = getResources().getStringArray(R.array.storeInfo); // 如果你的附屬檔案放置在(res)必須呼叫getResources()
        //ArrayAdapter<String> stroeadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,data);
        //storeInfoSpinner.setAdapter(stroeadapter);
        ParseQuery<ParseObject> query =  new ParseQuery<>("StoreInfo");
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                String[] stores = new String[objects.size()];
                Log.v(TAG, "StoreInfo" + stores.length);
                for (int i = 0; i < stores.length; i++) {
                    ParseObject object = objects.get(i);
                    stores[i] = object.getString("name") + "," +
                            object.getString("address");
                }
                ArrayAdapter<String> storeAdapter =
                        new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, stores);
                storeInfoSpinner.setAdapter(storeAdapter);
            }
        });



    }

    // view可以跟你說 你會用到哪一個元件動作
    public void submit(View view){

        progressDialog.setTitle("Loading...");
        progressDialog.show();
        String test = edittext.getText().toString();

        //if(checkInput(test)) {


            editor.putString("edittext", test);
            /**
             * 資料的存入動作
             * 第一個參數 放置key物件名稱 第二參數 存放的資料
             **/
            editor.commit(); // 必須執行這行，資料才會真正存入到檔案裡面

        try {
            JSONObject orderData = new JSONObject();
            if(muneResul == null) muneResul="口";
            JSONArray array = new JSONArray(muneResul);
            orderData.put("note",test);
            orderData.put("menu", array);
            Utils.writeFile(this, "history.txt", orderData.toString() + "\n"); // 將字串寫入到檔案動作


            testObject = new ParseObject("Order");
            testObject.put("note",test );
            testObject.put("storeInfo", storeInfoSpinner.getSelectedItem());
            testObject.put("menu", array);
            //testObject.saveInBackground();

            if(hasphoto==true){
                Toast.makeText(MainActivity.this, "[getphoto] ok", Toast.LENGTH_SHORT).show();
                Uri uri = Utils.getPhotoUri();
                ParseFile parseFile = new ParseFile("photo.png", Utils.urlToByte(this, uri));
                testObject.put("photo", parseFile );
            }


            // 檢查是否有上傳動作是否成功
            testObject.saveInBackground(new SaveCallback() {

                @Override
                public void done(ParseException e) {
                    progressDialog.dismiss();
                    if (e == null) {
                        Toast.makeText(MainActivity.this, "[SaveCallback] ok", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(MainActivity.this, "[SaveCallback] fail", Toast.LENGTH_SHORT).show();

                    }
                }
            });



        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Utils.writeFile(this, "history.txt", test + "\n"); // 將字串寫入到檔案動作

            if (hideCheckBox.isChecked()) {
                test = "**********";
                edittext.setText("**********");
            } else {
                edittext.setText("");
            }
        setHistory();
    //}


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



    public boolean onOptionsItemSelected(MenuItem menuItem){
        int item_id = menuItem.getItemId();

        switch (item_id){
            case R.id.new_message:
                Toast.makeText(this,"new_message",Toast.LENGTH_LONG).show();
                break;
            case R.id.action_take_photo:
                Toast.makeText(this,"take photo",Toast.LENGTH_LONG).show();
                //相機功能出來
                goToCamera();

                break;
            case R.id.quit:
                Toast.makeText(this,"quit",Toast.LENGTH_LONG).show();
                break;
            default: return false;
        }
        return true;
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

        editor.putBoolean("hideCheckBox", hideCheckBox.isChecked());
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
        //startActivity(intent);//進行單向開啟動作 DrinkMenuActivity 不會回傳結果出來
        startActivityForResult(intent, Resul＿code_MENU_Activity); //進行雙向動作 DrinkMenuActivity  告訴他前往哪一個Activity 再回來 / 最後在定義回傳Resul是多少

    }

    /**
     * requestCode 判斷回傳的CODE
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Resul＿code_MENU_Activity) {
            if (resultCode == RESULT_OK) {
                muneResul = data.getStringExtra("JSON");
                //Log.d("debug",muneResul);
                /*
                testObject = new ParseObject("TestObject");
                testObject.put("JSON",muneResul );
                testObject.saveInBackground();*/

            }

        }else if(requestCode==REQUEST_TAKE_PHOTO){
            if(resultCode==RESULT_OK){
                //Bitmap im = data.getParcelableExtra("data");
                //photoimageview.setImageBitmap(im);
                Uri uri = Utils.getPhotoUri();
                photoimageview.setImageURI(uri);
                hasphoto = true ;
            }
        }

        /*
        switch (resultCode) {
            case RESULT_OK:
                Bundle bunde = data.getExtras();
                String Json_String = bunde.getString("JSON");
                Toast.makeText(this, Json_String ,Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
        * */



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


    public boolean checkInput(String inputtext){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        boolean aaa = true ;

        if(inputtext.isEmpty()){



            builder.setTitle("錯誤訊息顯示");
            builder.setMessage("未輸入任何文字");

            builder.setNegativeButton(R.string.button_stop, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            aaa = false ;
        }else if(inputtext.equals("")){

            builder.setTitle("錯誤訊息顯示");
            builder.setMessage("輸入是空值");

            builder.setNegativeButton(R.string.button_stop, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            aaa = false ;
        }else if(inputtext.length()<8){

            builder.setTitle("錯誤訊息顯示");
            builder.setMessage("8碼以上");

            builder.setNegativeButton(R.string.button_stop, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            aaa = false ;
        }



            return aaa ;
    }




    private void goToCamera(){

        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE); // MediaStore  http://blog.csdn.net/zqiang_55/article/details/7061882
        intent.putExtra(MediaStore.EXTRA_OUTPUT,Utils.getPhotoUri()); //
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);


    }

}
