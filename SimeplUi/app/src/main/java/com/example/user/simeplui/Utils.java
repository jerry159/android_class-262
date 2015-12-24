package com.example.user.simeplui;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by jerry on 2015/12/1.
 */
public class Utils {


    /**
     * 將字串寫入到檔案中
     * @param context
     * @param fileName
     * @param content
     */
    public static void writeFile(Context context , String fileName , String content)  {

        try {
            FileOutputStream fos =
                    context.openFileOutput(fileName,context.MODE_APPEND);
                    /* 開啟檔案並寫入，此檔案放置位址 手機本機端(DATA\DATA\APP 專案名稱\files)
                     * 第一參數 表示 要給定檔案名稱
                     * 第二參數 指定檔案寫入時可能操作模式，有四種模式
                     * Context.MODE_PRIVATE 為預設操作模式，代表該檔是私有資料，只能被應用本身訪問，在該模式下，寫入的內容會覆蓋原檔的內容，如果想把新寫入的內容追加到原檔中請使用Context.MODE_APPEND
                     * Context.MODE_APPEND 模式會檢查檔是否存在，存在就往檔追加內容，否則就創建新檔。
                     * Context.MODE_WORLD_READABLE 用來控制其他應用是否有許可權讀寫該檔。
                     * Context.MODE_WORLD_WRITEABLE  用來控制其他應用是否有許可權讀寫該檔。
                     */
            fos.write(content.getBytes());
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 從檔案中讀取字串
     * @param context
     * @param fileName
     * @return
     */
    public static String readFile(Context context , String fileName){

        try {

            byte[] buffer = new byte[1204];
            FileInputStream fis = context.openFileInput(fileName);
            fis.read(buffer, 0, buffer.length);
            fis.close();
            return new String(buffer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "";
    }


    //uri 識別證表示檔案 放置地方
    public static Uri getPhotoUri(){
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if(dir.exists()== false){
            dir.mkdir();
        }
        File file =new File(dir,"simpeeui_photo.png");
        return Uri.fromFile(file);
    }


    // 取得檔案對應的URL檔
    public  static byte[] urlToByte(Context context , Uri uri){

        try {
            InputStream is = context.getContentResolver().openInputStream(uri); // 取得檔案
            ByteArrayOutputStream baos = new ByteArrayOutputStream();// 輸出用字元陣列表示
            byte[] buffer = new byte[1024];
            int len = 0 ;
            while ((len = is.read()) != -1){
               baos.write(buffer,0,len);
            }

            return baos.toByteArray();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null ;

    }

    // 抓取GOOGLE MAP URL取得JSON的資訊
    public static byte[] urlToBytes(String urlString){
        try {
            URL url = new URL(urlString);
            URLConnection connection = url.openConnection();
            InputStream is = connection.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int len = 0;
            while((len = is.read(buffer)) != -1){
                baos.write(buffer, 0, len);
            }

            return baos.toByteArray();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getGeoCodingUrl(String address) {
        try {
            address = URLEncoder.encode(address, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url ="https://maps.googleapis.com/maps/api/geocode/json?address=" + address;

        return url;
    }

    public static double[] getLatLngFromJsonString(String jsonString) {
        try {
            JSONObject object = new JSONObject(jsonString);

            JSONObject locationObject = object.getJSONArray("results")
                    .getJSONObject(0)
                    .getJSONObject("geometry")
                    .getJSONObject("location");

            JSONObject formatted_address_Object = object.getJSONArray("results").getJSONObject(0);
            JSONObject address_components_Object = object.getJSONArray("results").getJSONObject(0).getJSONArray("address_components").getJSONObject(0);


            Log.d("debug", formatted_address_Object.get("formatted_address").toString());
                       Log.d("debug", address_components_Object.getString("long_name")+":"+address_components_Object.getString("short_name")+":"+address_components_Object.getString("types"));

            double lat = locationObject.getDouble("lat");
            double lng = locationObject.getDouble("lng");

            return new double[]{lat, lng};

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static double[] addressToLatLng(String address) {
        String url = Utils.getGeoCodingUrl(address);
        byte[] bytes = Utils.urlToBytes(url);
        String result = new String(bytes);
        return Utils.getLatLngFromJsonString(result);
    }

}
