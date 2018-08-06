package com.example.gaurang.key_mouse;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    EditText Text;
    InetAddress i;
Socket socket;
    private boolean isConnected=false;
    private boolean mouseMoved=false;
    private PrintWriter out;
    ConnectPhoneTask connectPhoneTask;
    DataOutputStream dout;
    Button b1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Text = findViewById(R.id.text);
        b1=findViewById(R.id.button);
        connectPhoneTask= new ConnectPhoneTask();
        connectPhoneTask.execute("192.168.0.106");



    }





    public class ConnectPhoneTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = true;
            try {
                InetAddress serverAddr = InetAddress.getByName("192.168.0.106");
                socket = new Socket(serverAddr, 3000);//Open socket on server IP and port
            } catch (IOException e) {
                Log.e("remotedroid", "Error 66 connecting", e);
                result = false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            isConnected = result;
           Toast.makeText(getApplicationContext(), isConnected ? "Connected to server!" : "Error while connecting", Toast.LENGTH_SHORT).show();
            //Log.e("exxecute","Connected To The Server!!!!");
            try {
                if (isConnected) {
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                     dout= new DataOutputStream(socket.getOutputStream());
                    /*while(true){
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                }

                            }
                        },6000);}*/
                    Text.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            try {
                                dout.writeUTF(Text.getText().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });


                    //dout.writeUTF(Text.getText().toString());
                    //create output stream to send data to server
                }
            } catch (IOException e) {
                Log.e("remotedroid", "Error while creating OutWriter--", e);
                Toast.makeText(getApplicationContext(), "Error while connecting++", Toast.LENGTH_LONG).show();
            }
        }
    }
}