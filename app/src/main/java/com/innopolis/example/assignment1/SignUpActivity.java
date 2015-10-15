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
import android.widget.TextView;

import com.innopolis.example.assignment1.Const.Api;
import com.innopolis.example.assignment1.Const.Key;
import com.innopolis.example.assignment1.Dto.UserDto;
import com.innopolis.example.assignment1.Utils.JSONParser;
import com.innopolis.example.assignment1.Utils.ServerHelper;

public class SignUpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
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

    public void signUp(View view) {
        blockUI();

        final EditText signupUsername = (EditText)findViewById(R.id.signup_username);

        new SignupTask().execute(signupUsername.getText().toString());
    }

    public void gotoProjects(View view) {
        final Intent intent = new Intent(this, ProjectsListActivity.class);
        startActivity(intent); //todo: seem like not proper way. One visible issue - back button would work;
    }

    private class SignupTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            // params comes from the execute() call: params[0]
            return register(params[0]);
        }
        // onPostExecute displays the results of the AsyncTask.

        @Override
        protected void onPostExecute(String userId) {
            if (userId != null)
            {
                final ProgressBar signupProgress = (ProgressBar) findViewById(R.id.signup_progress);
                signupProgress.setVisibility(View.GONE);

                final Button signupButton = (Button) findViewById(R.id.signup_submit);
                signupButton.setVisibility(View.GONE);

                EditText password = (EditText) findViewById(R.id.signup_password);
                TextView passwordLabel = (TextView) findViewById(R.id.signup_password_label);

                password.setVisibility(View.VISIBLE);
                passwordLabel.setVisibility(View.VISIBLE);

                password.setText(userId);

                Button gotoProjects = (Button)findViewById(R.id.sigunp_projects_link);
                gotoProjects.setVisibility(View.VISIBLE);
            }
            else
                unblockUI();
        }
    }

    private String register(String userName)  {
        if (ServerHelper.connectionIsAvailable(this) == false) //inform user that phone has no internet connection available
            return null;

        UserDto newUser = new UserDto();
        newUser.Name = userName;

        String userDataResponse = ServerHelper.makePOSTRequest(Api.UsersUrl, JSONParser.serializeUser(newUser));
        if (userDataResponse == null) //inform user that connection was not successfull
            return null;

        Log.i("TAG", "resp: " + userDataResponse);

        UserDto user = JSONParser.parseUser(userDataResponse);
        if (user == null) //inform user that password not right
            return null;

        //save user in preferences
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Key.USERNAME, userName);
        editor.commit();

        return user.UserId;
    }

    private void blockUI(){
        final Button signupButton = (Button) findViewById(R.id.signup_submit);
        signupButton.setVisibility(View.GONE);

        final ProgressBar signupProgress = (ProgressBar) findViewById(R.id.signup_progress);
        signupProgress.setVisibility(View.VISIBLE);
    }

    private void unblockUI(){
        final ProgressBar signupProgress = (ProgressBar) findViewById(R.id.signup_progress);
        signupProgress.setVisibility(View.GONE);

        final Button signupButton = (Button) findViewById(R.id.signup_submit);
        signupButton.setVisibility(View.VISIBLE);
    }


}
