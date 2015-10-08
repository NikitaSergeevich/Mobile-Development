package com.innopolis.example.assignment1;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.accounts.AccountManager;
import android.widget.TextView;
import android.widget.Toast;
import static com.innopolis.example.assignment1.AccountGeneral.sServerAuthenticate;

public class LoginActivity extends AccountAuthenticatorActivity {
    private AccountManager mAccountManager;
    private DialogFragment dialog;
    private final int REQ_SIGNUP = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        dialog = new NetworkDialog();
        dialog.setCancelable(false);
        if (!networkcheck(this))
        {
            dialog.show(getFragmentManager(), "Connecting to Network");
        }

        mAccountManager = AccountManager.get(this);
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(getBaseContext(), SignUpActivity.class);
                //signup.putExtras(getIntent().getExtras());
                startActivityForResult(signup, REQ_SIGNUP);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // The sign up activity returned that the user has successfully created an account
        if (requestCode == REQ_SIGNUP && resultCode == RESULT_OK) {
            finishLogin(data);
        } else
            super.onActivityResult(requestCode, resultCode, data);
    }

    private boolean networkcheck(Context context)
    {
        ConnectivityManager connMgr	= (ConnectivityManager)
                getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo	=
                connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean	isWifiConn = networkInfo.isConnected();
        networkInfo	= connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean	isMobileConn = networkInfo.isConnected();

        if (isWifiConn == true | isMobileConn == true)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void submit() {

        final String userName = ((TextView) findViewById(R.id.accountName)).getText().toString();
        final String userPassword = ((TextView) findViewById(R.id.accountPassword)).getText().toString();

        new AsyncTask<String, Void, Intent>() {

            @Override
            protected Intent doInBackground(String... params) {
                Bundle data = new Bundle();
                boolean result = false;
                try {
                    result = sServerAuthenticate.userSignIn(userName, userPassword);
                    data.putString(AccountManager.KEY_ACCOUNT_NAME, userName);
                    data.putBoolean("result", result);

                } catch (Exception e) {
                    data.putString("error_msg", e.getMessage());
                    data.putBoolean("result", false);
                }

                final Intent res = new Intent();
                res.putExtras(data);
                return res;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                if (intent.hasExtra("error_msg")) {
                    Toast.makeText(getBaseContext(), intent.getStringExtra("error_msg"), Toast.LENGTH_SHORT).show();
                } else if (intent.getBooleanExtra("result", false) == true) {
                    finishLogin(intent);
                } else {
                    Toast.makeText(getBaseContext(), "wrong password or name", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void finishLogin(Intent intent) {
        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        Account newAccount = new Account(accountName, "example.com");
        mAccountManager.addAccountExplicitly(newAccount, "com", null);
        setResult(RESULT_OK, intent);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
