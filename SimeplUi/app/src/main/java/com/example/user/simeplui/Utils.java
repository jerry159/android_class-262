package com.example.user.simeplui;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
            fis.read(buffer,0,buffer.length);
            fis.close();
            return new String(buffer);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "";
    }

}
