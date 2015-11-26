package com.example.user.simeplui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private  EditText edittext ; // 建立EditText 物件
    private CheckBox hideCheckBox ; // 建立CheckBox 物件
    // 建立生命週期
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);// 指定layout檔
          edittext = (EditText)findViewById(R.id.input); // 物件轉換，透過findViewById方式尋找Ｒ.的ID.的名稱(ＮＡＭＥ)
          edittext.setText("GO input any word"); // 設定指定文字

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


          hideCheckBox = (CheckBox)findViewById(R.id.hidecheckBox);
          //hideCheckBox.setChecked(true);


    }

    // view可以跟你說 你會用到哪一個元件動作
    public void submit(View view){

        String test = edittext.getText().toString();
        if(hideCheckBox.isChecked()){
            test = "**********" ;
            edittext.setText("**********");
        }else{
            edittext.setText("");
        }

        Toast.makeText(this,test,Toast.LENGTH_LONG).show(); // 顯示在螢幕上的氣泡訊息

       // Toast.makeText(this,"132132132131321321213213",Toast.LENGTH_LONG).show(); // 短暫訊息_顯示在螢幕上的氣泡訊息

        //edittext.setText(""); // 設定指定文字
    }

}
