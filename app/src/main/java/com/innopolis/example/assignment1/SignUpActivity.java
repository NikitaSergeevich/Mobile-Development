package com.innopolis.example.assignment1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.accounts.AccountManager;
import android.widget.TextView;
import android.widget.Toast;
import static com.innopolis.example.assignment1.AccountGeneral.sServerAuthenticate;

public class SignUpActivity extends Activity {

    private String TAG = getClass().getSimpleName();
    private String mAccountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        findViewById(R.id.alreadySignedUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });
    }

    private void createAccount() {

        new AsyncTask<String, Void, Intent>() {

            String userName = ((TextView) findViewById(R.id.name)).getText().toString().trim();
            String userPassword = ((TextView) findViewById(R.id.accountPassword)).getText().toString().trim();
            boolean res = false;

            @Override
            protected Intent doInBackground(String... params) {
                Bundle data = new Bundle();
                try {
                    res = sServerAuthenticate.userSignUp(userName, userPassword);
                    data.putString(AccountManager.KEY_ACCOUNT_NAME, userName);

                } catch (Exception e) {
                    data.putString("error_msg", e.getMessage());
                }

                final Intent res = new Intent();
                res.putExtras(data);
                return res;
            }

            @Override
            protected void onPostExecute(Intent intent) {
                if (intent.hasExtra("error_msg")) {
                    Toast.makeText(getBaseContext(), intent.getStringExtra("error_msg"), Toast.LENGTH_SHORT).show();
                } else {
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }.execute();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}