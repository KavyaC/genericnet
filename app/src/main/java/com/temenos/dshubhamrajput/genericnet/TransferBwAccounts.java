package com.temenos.dshubhamrajput.genericnet;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;

import static com.temenos.dshubhamrajput.genericnet.AcctStmtActivity.context;

public class TransferBwAccounts extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    static public String RefNo="";
    public String intentData;
    public static String status;
    public Intent commit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_bw_accts);
        getSupportActionBar().setTitle("Account Transfer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EditText from = (EditText) findViewById(R.id.editText);
        EditText to = (EditText) findViewById(R.id.editText6);
        EditText desc = (EditText) findViewById(R.id.editText7);
        EditText amt = (EditText) findViewById(R.id.editText8);
        from.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        to.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        desc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        amt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        new FundTransfer().execute();

        Button fundsTransfer = (Button) findViewById(R.id.button);
        fundsTransfer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                EditText fromAcctNo = (EditText) findViewById(R.id.editText);
                String fromAccountNo = fromAcctNo.getText().toString();
                EditText toAcctNo = (EditText) findViewById(R.id.editText6);
                String toAccountNo = toAcctNo.getText().toString();
                EditText descr = (EditText) findViewById(R.id.editText7);
                String description = descr.getText().toString();
                EditText amt = (EditText) findViewById(R.id.editText8);
                String amount = amt.getText().toString();
                String transType = "AC";
                String Currency = "USD";

                new jsonResponse().execute(fromAccountNo,toAccountNo,description,amount,transType,Currency);
            }
        });
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

    public class FundTransfer extends AsyncTask<Void, Void, Boolean> {
        /**
         * Establishes connection with the url and authenticates the user name
         * and password.
         */
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                HttpHandler sh = new HttpHandler();
                // Making a request to url and getting response
                PropertiesReader property = new PropertiesReader();
                String url = property.getProperty("new_id_url", getApplicationContext());
                String jsonStr = sh.makeServiceCall(url);
                Log.e(TAG, "Response from url: " + jsonStr);
                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        RefNo = jsonObj.getString("RefNo");
                        System.out.println(RefNo);
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Json parsing error: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                } else {
                    Log.e(TAG, "Couldn't get json from server.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Couldn't get json from server. Check LogCat for possible errors!",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            catch(IOException e ){
                    e.printStackTrace();
                }
                return null;

        }
    }

    public class jsonResponse extends AsyncTask<String,Void,Boolean>
    {
        protected Boolean doInBackground(String... params) {
            InputStream inputStream = null;
            String result = "";
            String response = "";
            String url = "http://10.93.22.116:9089/Test-iris/Test.svc/GB0010001/verFundsTransfer_AcTranss(\'"+RefNo+"\')/validate";
            try {
                String json = "";

                // 3. build jsonObject
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("RefNo", RefNo);
                jsonObject.accumulate("TransactionType", params[4]);
                jsonObject.accumulate("DebitAcctNo", params[0]);
                jsonObject.accumulate("DebitCurrency", params[5]);
                jsonObject.accumulate("DebitAmount", params[3]);
                jsonObject.accumulate("CreditAcctNo", params[1]);
                jsonObject.accumulate("Description", params[2]);

                // 4. convert JSONObject to JSON to String
                json = jsonObject.toString();

                HttpHandler newObj = new HttpHandler();
                status = newObj.postfunc(url,json);
                if(status.equals("YES")) {
                    intentData = "account";
                    Bundle fundsTransferData = new Bundle();
                    fundsTransferData.putString("RefNo", RefNo);
                    fundsTransferData.putString("fromAccountNo", params[0]);
                    fundsTransferData.putString("toAccountNo", params[1]);
                    fundsTransferData.putString("description", params[2]);
                    fundsTransferData.putString("amount", params[3]);
                    fundsTransferData.putString("transType", params[4]);
                    fundsTransferData.putString("Currency", params[5]);
                    fundsTransferData.putString("getintent", intentData);

                    commit = new Intent(TransferBwAccounts.this, ConfirmPage.class);
                    commit.putExtras(fundsTransferData);
                    return true;
                }
                    return false;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return true;
            // 11. return result
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                startActivity(commit);
            }
            else{
                new AlertDialog.Builder(TransferBwAccounts.this)
                        .setTitle("Error")
                        .setMessage("The value you entered are wrong, Please Recheck?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                final Intent TransferBwAccounts = new Intent(TransferBwAccounts.this, TransferBwAccounts.class);
                                finish();
                                startActivity(TransferBwAccounts);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
    }

}
