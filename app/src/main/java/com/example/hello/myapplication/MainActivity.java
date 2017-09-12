package com.example.hello.myapplication;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textView = (TextView) findViewById(R.id.textView);


        final Handler handler = new Handler(){
            @Override
            //主线程处理接收到的消息，即将消息显示在textview上
            public void handleMessage(Message msg) {
                textView.setText(msg.arg1+"");
            }
        };

        //利用runnable重写run()，在run()里进行耗时运算
        final Runnable myWorker = new Runnable(){
            @Override
            public void run() {
                int progress = 0;
                while(progress <= 100){
                    Message msg = new Message();
                    msg.arg1 = progress;
                    //将新线程的消息发给主线程
                    handler.sendMessage(msg);
                    progress += 10;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message msg = handler.obtainMessage();//同 new Message(),但避免了重复创建对象，节省内存
                msg.arg1 = -1;
                handler.sendMessage(msg);
            }
        };


        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //将runnable对象传递给thread对象
                Thread workThread = new Thread(null, myWorker, "WorkThread");
                //启动线程
                workThread.start();
            }
        });
    }
}

