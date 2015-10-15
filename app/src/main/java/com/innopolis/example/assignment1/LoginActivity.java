package com.innopolis.example.assignment1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.innopolis.example.assignment1.Const.Api;
import com.innopolis.example.assignment1.Const.Key;
import com.innopolis.example.assignment1.Dto.UserDto;
import com.innopolis.example.assignment1.Utils.JSONParser;
import com.innopolis.example.assignment1.Utils.ServerHelper;


public class LoginActivity extends Activity {
    private static final String DEBUG_TAG = "DEBUG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String currentUserName = sharedPref.getString(Key.USERNAME, null);
        if (currentUserName != null)
            gotoProjects();
    }

    private void gotoProjects(){
        final Intent intent = new Intent(this, ProjectsListActivity.class);
        startActivity(intent); //todo: seem like not proper way. One visible issue - back button would work;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void login(View view) {
        blockUI();

        final EditText loginUsername = (EditText)findViewById(R.id.login_username);
        final EditText loginPassword = (EditText)findViewById(R.id.login_password);

        new LoginTask().execute(loginUsername.getText().toString(), loginPassword.getText().toString());
    }

    private void blockUI(){
        final Button loginButton = (Button) findViewById(R.id.login_submit);
        loginButton.setVisibility(View.GONE);

        final ProgressBar loginProgress = (ProgressBar) findViewById(R.id.login_progress);
        loginProgress.setVisibility(View.VISIBLE);
    }

    private void unblockUI(){
        final ProgressBar loginProgress = (ProgressBar) findViewById(R.id.login_progress);
        loginProgress.setVisibility(View.GONE);

        final Button loginButton = (Button) findViewById(R.id.login_submit);
        loginButton.setVisibility(View.VISIBLE);
    }

    private class LoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            // params comes from the execute() call: params[0]
            return authorize(params[0], params[1]);
        }
        // onPostExecute displays the results of the AsyncTask.

        @Override
        protected void onPostExecute(Boolean authorized) {
            if (authorized)
                gotoProjects();
            else
                unblockUI();
        }
    }

    private boolean authorize(String userName, String userId)  {
        if (ServerHelper.connectionIsAvailable(this) == false) //inform user that phone has no internet connection available
        {
            Log.w("AUTH", "No connection");
            return false;
        }

        String userDataResponse = ServerHelper.makeGETRequest(Api.UsersUrl + userId);
        if (userDataResponse == null) //inform user that connection was not successfull
        {
            Log.w("AUTH", "No response");
            return false;
        }

        UserDto user = JSONParser.parseUser(userDataResponse);
        if (user == null) //inform user that password not right
        {
            Log.w("AUTH", "No user");
            return false;
        }

        if (!user.Name.toLowerCase().equals(userName.toLowerCase())) //inform user that password not right
        {
            Log.w("AUTH", "Wrong pass or username");
            return false;
        }

        //save user in preferences
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Key.USERNAME, user.Name);
        editor.commit();

        return true;
    }

    public void gotoSignup(View view) {
        final Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
