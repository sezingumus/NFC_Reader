package com.example.sezin.nfc_reader;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.nfc.NfcAdapter;
import android.nfc.Tag;

import android.os.Bundle;



import android.widget.TextView;
import android.widget.Toast;





public class MainActivity extends Activity {

    NfcAdapter adapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag nfcTag;
    Context context;
    TextView tw;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tw = (TextView) findViewById(R.id.nfc);

        context = this;


        adapter = NfcAdapter.getDefaultAdapter(this);
        if (adapter == null) {
            Toast.makeText(context, "Not supported by phone", Toast.LENGTH_SHORT).show();
        } else {

            pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            tagDetected.addCategory(Intent.CATEGORY_DEFAULT);

        }


    }


    @Override
    protected void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            nfcTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String id_nfctag = bytesToHexString(nfcTag.getId());
            tw.setText(id_nfctag.toString());


        }
    }


    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString();
    }


    public void onPause() {
        super.onPause();
        WriteModeOff();
    }

    public void onResume() {
        super.onResume();
        WriteModeOn();
    }


    private void WriteModeOn() {
        writeMode = true;
        adapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }


    private void WriteModeOff() {
        writeMode = false;
        adapter.disableForegroundDispatch(this);
    }
}