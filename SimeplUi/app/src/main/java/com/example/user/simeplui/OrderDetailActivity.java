package com.example.user.simeplui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class OrderDetailActivity extends AppCompatActivity {


    private TextView addressTextView;
    //private String address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        addressTextView = (TextView) findViewById(R.id.address);

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
    }


    /**
     *  用繼承方式寫法
     *  AsyncTask 和 Thread 專注於操作 & 畫面的流暢呈現，其餘工作 (如網路資料傳輸、檔案/磁碟/資料存取)都在背景執行
     *  Thread 通常要搭配 Handler 使用，而 AsyncTask 用意在簡化背景執行 thread 程式碼的撰寫
     */
    class GeoCodingTask extends AsyncTask<String, Void, double[]> {

        @Override
        protected double[] doInBackground(String... params) {
            //return new double[0];
            String address = params[0];
            return Utils.addressToLatLng(address);
        }

        @Override
        protected void onPostExecute(double[] latLng) {
            //super.onPostExecute(doubles);
            addressTextView.setText(latLng[0] + "," + latLng[1]);
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
