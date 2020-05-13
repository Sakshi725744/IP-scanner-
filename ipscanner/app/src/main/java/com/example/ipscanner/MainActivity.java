package com.example.ipscanner;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnScan;
    private ListView listViewIp;
    private EditText e;
    int k=0;


    ArrayList<String> ipList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnScan = (Button)findViewById(R.id.button);
        listViewIp = (ListView)findViewById(R.id.Listview);
        e=(EditText)findViewById(R.id.editText);


        ipList = new ArrayList();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, ipList);
        listViewIp.setAdapter(adapter);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ScanIpTask().execute();
            }
        });

    }

    private class ScanIpTask extends AsyncTask<Void, String, Void>{

        /*
        Scan IP 192.168.1.100~192.168.1.110
        you should try different timeout for your network/devices
         */
        String subnet=e.getText().toString();
        /*static final int lower = 0;
        static final int upper = 20;
        static final int timeout = 5000;*/

        @Override
        protected void onPreExecute() {
            ipList.clear();
            adapter.notifyDataSetInvalidated();
            Toast.makeText(MainActivity.this, "Scan IP...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            Thread myThreads[]=new Thread[256];
            for(int j=0; j<256;j++){
            myThreads[j] = new Thread(new Runnable() {
                @Override
                public void run() {
                        String host = subnet + k++;

                        try {
                            InetAddress inetAddress = InetAddress.getByName(host);
                            if (inetAddress.isReachable(5000)){
                                publishProgress(inetAddress.getHostName());
                            }

                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });
            myThreads[j].start();}
            for(int l=0;l<256;l++)
            {
                try {
                    myThreads[l].join();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            return null;}

        //myThreads[j].start();
                //k+=8;}

          /*  for (int i = lower; i <= upper; i++) {
                String host = subnet + i;

                try {
                    InetAddress inetAddress = InetAddress.getByName(host);
                    if (inetAddress.isReachable(timeout)){
                        publishProgress(inetAddress.toString());
                    }

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }*

           */

        @Override
        protected void onProgressUpdate(String... values) {
            ipList.add(values[0]);
            adapter.notifyDataSetInvalidated();
            Toast.makeText(MainActivity.this, values[0], Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(MainActivity.this, "Done", Toast.LENGTH_LONG).show();
        }
    }

}


