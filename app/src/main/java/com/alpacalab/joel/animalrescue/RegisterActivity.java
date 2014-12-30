package com.alpacalab.joel.animalrescue;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class RegisterActivity extends ActionBarActivity {
    private EditText mUsernameField;
    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Parse.initialize(this, "H4ZKGFj7YJCYpy8UV1n0ZYGzEO4lMK5OMC5LXBIx", "tkrSkoYrZIYmVVzIuolxL8bV7N9iZDCFnkfCQqMm");

        mUsernameField = (EditText) findViewById(R.id.register_username);
        mPasswordField = (EditText) findViewById(R.id.register_password);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    public void register(final View v) {
        if(mUsernameField.getText().length() == 0 || mPasswordField.getText().length() == 0)
            return;
        v.setEnabled(false);
        ParseUser user = new ParseUser();
        user.setUsername(mUsernameField.getText().toString());
        user.setPassword(mPasswordField.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //Sign up not succeed
                    v.setEnabled(true);
                }
            }
        });
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
}
