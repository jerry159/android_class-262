package com.example.user.simeplui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DrinkMenuActivity extends AppCompatActivity {

    private Button buttonbacktopage ;
    private static final String TAG = "Bmi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);

        buttonbacktopage = (Button)findViewById(R.id.button3);
        buttonbacktopage.setText("done", TextView.BufferType.NORMAL);
        buttonbacktopage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toto(v);
            }
        });


    }

    public void onStart()
    {
        super.onStart();
        Log.v(TAG, "onStart_DrinkMenuActivity");
    }

    public void onResume()
    {
        super.onResume();
        Log.v(TAG,"onResume_DrinkMenuActivity");
    }

    public void onPause()
    {
        super.onPause();
        Log.v(TAG,"onPause_DrinkMenuActivity");
    }

    public void onStop()
    {
        super.onStop();
        Log.v(TAG,"onStop_DrinkMenuActivity");
    }

    public void onRestart()
    {
        super.onRestart();
        Log.v(TAG,"onReStart_DrinkMenuActivity");
    }

    public void onDestroy()
    {
        super.onDestroy();
        Log.v(TAG, "onDestroy_DrinkMenuActivity");
    }


    public void toto(View view){

        //setContentView(R.layout.activity_main);
        Bundle bundle = new Bundle();
        Intent intent = new Intent();
        intent.setClass(DrinkMenuActivity.this, MainActivity.class);
        bundle.putString("JSON", getdata().toString());
        intent.putExtras(bundle);
        DrinkMenuActivity.this.setResult(RESULT_OK, intent);
        DrinkMenuActivity.this.finish();
        //startActivity(intent);
        //Toast.makeText(this, "GOGOG", Toast.LENGTH_LONG).show();
    }


    /*
      view 類是點擊對象部分
     */
    public void add(View view){

        switch (view.getId()){

            case R.id.button2:
                Toast.makeText(this, "button2" ,Toast.LENGTH_SHORT).show();
                break;
            case R.id.button3:
                Toast.makeText(this, "button3" ,Toast.LENGTH_SHORT).show();
                break;
            case R.id.button4:
                Toast.makeText(this, "button4" ,Toast.LENGTH_SHORT).show();
                break;
            case R.id.button5:
                Toast.makeText(this, "button5" ,Toast.LENGTH_SHORT).show();
                break;
            case R.id.button6:
                Toast.makeText(this, "button6" ,Toast.LENGTH_SHORT).show();
                break;
            case R.id.button7:
                Toast.makeText(this, "button7" ,Toast.LENGTH_SHORT).show();
                break;
            case R.id.button8:
                Toast.makeText(this, "button8" ,Toast.LENGTH_SHORT).show();
                break;
            case R.id.button9:
                Toast.makeText(this, "button9" ,Toast.LENGTH_SHORT).show();
                break;
        }
        Button button = (Button)view ;
        int number = Integer.parseInt(button.getText().toString());
        number++;
        button.setText(String.valueOf(number));

    }


    /*
    * [{"name":"black  tea","l": "1","m":"2"},{"name":"mail tea","l": "1","m":"2"},{"name":"green tea","l": "1","m":"2"}]
    */
    public JSONArray getdata(){

        LinearLayout rootLinearLayout = (LinearLayout)findViewById(R.id.root);
        int count = rootLinearLayout.getChildCount(); // 取得LinearLayout全部的結點小孩

        JSONArray array = new JSONArray();// 建立最大JSON 陣列

        for(int i = 0 ; i < count -1 ; i++){
            LinearLayout ll = (LinearLayout)rootLinearLayout.getChildAt(i);
            TextView drinkNameTextView = (TextView)ll.getChildAt(0);
            Button lButton = (Button)ll.getChildAt(1);
            Button mButton = (Button)ll.getChildAt(2);

            String drinkName = drinkNameTextView.getText().toString();
            int lNumber = Integer.parseInt(lButton.getText().toString());
            int mNumber = Integer.parseInt(mButton.getText().toString());

            try {
                JSONObject  object = new JSONObject(); // 放置每個物件資料
                object.put("neme",drinkName);
                object.put("l",lNumber);
                object.put("m",mNumber);
                array.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        return array;
    }

}
