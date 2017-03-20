package com.temenos.dshubhamrajput.genericnet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

public class ConfirmPage extends AppCompatActivity {
    public Intent test;
    public String imp;
    public EditText e5;
    public TextView t1,t2,t3,t4,t5;
    static public String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button backbutton = (Button) findViewById(R.id.button);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        e5=(EditText) findViewById(R.id.editText8);
        t1=(TextView) findViewById(R.id.textView);
        t2=(TextView) findViewById(R.id.textView3);
        t3=(TextView) findViewById(R.id.textView5);
        t4=(TextView) findViewById(R.id.textView6);
        t5=(TextView) findViewById(R.id.textView11);
        test=getIntent();
        final Bundle extras = getIntent().getExtras();
//        imp=extras.getString("getintent");
//        imp=test.getExtras().getString("getintent");
        imp = extras.getString("getintent");
        if(imp.equals("internal"))
        {
            getSupportActionBar().setTitle("Confirm Beneficiary");
            e5.setVisibility(View.GONE);
            t1.setText("Account Number");
            t2.setText("Email");
            t3.setText("Nick Name");
            t4.setText("Customer Name");
        }
        else if(imp.equals("external"))
        {
            getSupportActionBar().setTitle("Confirm Beneficiary");
            t1.setText("Account Number");
            t2.setText("Email");
            t3.setText("Nick Name");
            t4.setText("IFSC code");
            t5.setText("Customer Name");
        }
        else if(imp.equals("account"))
        {
            EditText frmAcct = (EditText) findViewById(R.id.editText);
            EditText toAcct = (EditText) findViewById(R.id.editText6);
            EditText des = (EditText) findViewById(R.id.editText7);
            EditText amtFunds = (EditText) findViewById(R.id.editText10);

            getSupportActionBar().setTitle("Confirm Transfer");
            e5.setVisibility(View.GONE);
            t1.setText("From Account");
            t2.setText("To Account");
            t3.setText("Description");
            t4.setText("Amount");
            frmAcct.setText(extras.getString("fromAccountNo"));
            toAcct.setText(extras.getString("toAccountNo"));
            des.setText(extras.getString("description"));
            amtFunds.setText(extras.getString("amount"));
            final String url = "http://10.93.22.116:9089/Test-iris/Test.svc/GB0010001/verFundsTransfer_AcTranss()/input";

            Button confirmButton = (Button) findViewById(R.id.button2);
            confirmButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    new commitFunCall().execute(url,extras.getString("RefNo"),extras.getString("transType"),extras.getString("fromAccountNo"),extras.getString("Currency"),extras.getString("amount")
                            ,extras.getString("toAccountNo"),extras.getString("description"));
                }
            });
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class commitFunCall extends AsyncTask<String,Void,Boolean>
    {
        protected Boolean doInBackground(String... params)
        {
            try {
                String json = "";

                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("RefNo", params[1]);
                jsonObject.accumulate("TransactionType", params[2]);
                jsonObject.accumulate("DebitAcctNo", params[3]);
                jsonObject.accumulate("DebitCurrency", params[4]);
                jsonObject.accumulate("DebitAmount", params[5]);
                jsonObject.accumulate("CreditAcctNo", params[6]);
                jsonObject.accumulate("Description", params[7]);

                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();

                HttpHandler newObj = new HttpHandler();
                status = newObj.posCommit(params[0], json);
                if(status.equals("YES")) {
                    return true;
                }
                else
                {
                    return false;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                final Intent TransferBwAccounts = new Intent(ConfirmPage.this, SucessPage.class);
                startActivity(TransferBwAccounts);
            }
            else{
                final Intent TransferBwAccounts = new Intent(ConfirmPage.this, ConfirmPage.class);
                startActivity(TransferBwAccounts);
            }
        }
    }
}
