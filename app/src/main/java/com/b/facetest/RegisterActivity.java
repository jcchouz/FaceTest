package com.b.facetest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Handler;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private int ResultCode = 2;
    private final static int REGISTER_JUDGE = 2;
    private Button register,back;
    private EditText id,name,psw_1,psw_2,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = (Button) findViewById(R.id.register_do);
        register.setOnClickListener(this);
        id = (EditText) findViewById(R.id.id_edit);
        name = (EditText)findViewById(R.id.name_edit);
        psw_1 = (EditText) findViewById(R.id.password_edit);
        psw_2 = (EditText) findViewById(R.id.password_edit_1);
        email = (EditText) findViewById(R.id.email_edit);

        back = (Button)findViewById(R.id.back_edit);//zj  返回MainActivity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    //添加了SuppressLint("HandlerLeak")
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case REGISTER_JUDGE:{
                    Bundle bundle = new Bundle();
                    bundle = msg.getData();
                    String result = bundle.getString("result");
                    //Toast.makeText(MainActivity.this,result,Toast.LENGTH_SHORT).show();
                    try {
                        if (result.equals("success")) {
                            Intent intent = new Intent();
                            intent.putExtra("id",id.getText().toString());
                            intent.putExtra("password",psw_1.getText().toString());
                            setResult(ResultCode,intent);//向上一级发送数据
                            finish();
                        }
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.register_do:{
                if( ! psw_1.getText().toString().equals(psw_2.getText().toString())){
                    Toast.makeText(RegisterActivity.this,"两次密码不一致！",Toast.LENGTH_LONG).show();
                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String result = HttpLogin.RegisterByPost(id.getText().toString(),name.getText().toString(),
                                    psw_1.getText().toString(),email.getText().toString());
                            Bundle bundle = new Bundle();
                            bundle.putString("result",result);
                            Message msg = new Message();
                            msg.what = REGISTER_JUDGE;
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    }).start();
                }
            }
            break;
        }
    }
}


