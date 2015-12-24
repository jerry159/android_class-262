package com.example.user.simeplui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class OrderDetailActivity extends AppCompatActivity implements OnMapReadyCallback {


    private TextView addressTextView;
    private ImageView staticMapImage;
    private Switch mapswitch;
    private WebView staticMapweb;
    private GoogleMap mMap;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    //private String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        addressTextView = (TextView) findViewById(R.id.address);
        staticMapImage = (ImageView) findViewById(R.id.staticMapImage);
        staticMapweb = (WebView) findViewById(R.id.webView);
        staticMapweb.setVisibility(View.GONE);
        mapswitch = (Switch) findViewById(R.id.map_switch3);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        //監聽Switch 按扭動作進行判斷是否要消失MAPIMAGE
        mapswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    staticMapImage.setVisibility(View.GONE); // 消失
                    staticMapweb.setVisibility(View.VISIBLE);
                } else {
                    //staticMapImage.setVisibility(View.INVISIBLE); // 透明
                    staticMapweb.setVisibility(View.GONE);
                    staticMapImage.setVisibility(View.VISIBLE); // 顯示
                }

            }
        });


        String note = getIntent().getStringExtra("note");
        String storeInfo = getIntent().getStringExtra("storeInfo");
        Log.d("debug", note);
        Log.d("debug", storeInfo);
        String address = storeInfo.split(",")[1];
        addressTextView.setText(address);
        GeoCodingTask task = new GeoCodingTask();
        task.execute(address);

        /*
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //String url = "https://maps.googleapis.com/maps/api/geocode/json?address=taipei101";
                String url = Utils.getGeoCodingUrl(address);
                byte[] bytes = Utils.urlToBytes(url);
                String result = new String(bytes);
                double[] latLng = Utils.getLatLngFromJsonString(result);
                Log.d("debug", result);
                Log.d("debug", latLng[0] + "," + latLng[1]);
            }
        });
        thread.start();
        */

        /*
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                Utils.addressToLatLng(address);
                return null;
            }


            @Override
            protected void onPostExecute(Object o) {

            }
        };
        task.execute();*/
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "OrderDetail Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.user.simeplui/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "OrderDetail Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.example.user.simeplui/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    /**
     * 用繼承方式寫法
     * AsyncTask 和 Thread 專注於操作 & 畫面的流暢呈現，其餘工作 (如網路資料傳輸、檔案/磁碟/資料存取)都在背景執行
     * Thread 通常要搭配 Handler 使用，而 AsyncTask 用意在簡化背景執行 thread 程式碼的撰寫
     */
    // class GeoCodingTask extends AsyncTask<String, Void, double[]> {
    class GeoCodingTask extends AsyncTask<String, Void, byte[]> {

        private String url;

        @Override
        // protected double[] doInBackground(String... params) {
        /**
         *  連接網路最好統一進行  doInBackground 一起作業
         */
        protected byte[] doInBackground(String... params) {
            //return new double[0];
            // String address = params[0];
            // return Utils.addressToLatLng(address);
            String address = params[0];
            double[] latLng = Utils.addressToLatLng(address);
            url = Utils.getStaticMapUrl(latLng, 15); // 為了讓WEB抓取到URL
            return Utils.urlToBytes(url);
        }

        @Override
        // protected void onPostExecute(double[] latLng) {
        protected void onPostExecute(byte[] bytes) {
            //super.onPostExecute(doubles);
            //addressTextView.setText(latLng[0] + "," + latLng[1]);


            staticMapweb.loadUrl(url);
            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            staticMapImage.setImageBitmap(bm);

        }
    }

}


/**
 * 如果針對使用 AsyncTask，必定要建立一個繼承自 AsyncTask 的子類別，並傳入 3 項資料()
 * Params -- 要執行 doInBackground() 時傳入的參數，數量可以不止一個
 *  Progress -- doInBackground() 執行過程中回傳給 UI thread 的資料，數量可以不止一個
 *  Rsesult -- 傳回執行結果，
 *  若以上您沒有參數要傳入，則填入 Void (注意 V 為大寫)。
 *
 *
 *
 *  onPreExecute -- AsyncTask 執行前的準備工作，例如畫面上顯示進度表， doInBackground -- 實際要呼叫執行的程式碼就是寫在這裡，
    onProgressUpdate -- 用來顯示目前的進度，
    onPostExecute -- 執行完的結果 - Result 會傳入這裡。
 *
 */
