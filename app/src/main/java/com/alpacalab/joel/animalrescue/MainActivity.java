package com.alpacalab.joel.animalrescue;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;


public class MainActivity extends ActionBarActivity {
    private EditText mTaskInput;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enable Local Datastore.
        Parse.initialize(this, "H4ZKGFj7YJCYpy8UV1n0ZYGzEO4lMK5OMC5LXBIx", "tkrSkoYrZIYmVVzIuolxL8bV7N9iZDCFnkfCQqMm");
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Task.class);

        // Save the current Installation to Parse.
        ParseInstallation.getCurrentInstallation().saveInBackground();

        //Parse subsribe channel
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });

        mTaskInput = (EditText) findViewById(R.id.task_input);
        mListView = (ListView) findViewById(R.id.task_list);
        
    }

    public void createTask(View v) {
        if(mTaskInput.getText().length() > 0) {
            Task t = new Task();
            t.setDescription(mTaskInput.getText().toString());
            t.setCompleted(false);
            t.saveEventually();
            mTaskInput.setText("");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
