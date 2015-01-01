package com.alpacalab.joel.animalrescue;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 12/31/14.
 */
public class RescueFeedFragment extends Fragment implements AdapterView.OnItemClickListener{
    private EditText mTaskInput;
    private ListView mListView;
    private TaskAdapter mAdapter;
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static RescueFeedFragment newInstance(int sectionNumber) {
        RescueFeedFragment fragment = new RescueFeedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public RescueFeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.fragment_rescue_feed, container, false);

        // Parse intialisation .
        Parse.initialize(getActivity(), "H4ZKGFj7YJCYpy8UV1n0ZYGzEO4lMK5OMC5LXBIx", "tkrSkoYrZIYmVVzIuolxL8bV7N9iZDCFnkfCQqMm");
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

        //If user not logged in send to login Activity
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        }

        mTaskInput = (EditText) rootView.findViewById(R.id.task_input);
        mListView = (ListView) rootView.findViewById(R.id.task_list);

        mAdapter = new TaskAdapter(this.getActivity(), new ArrayList<Task>());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        updateData();

        Button mButton = (Button) rootView.findViewById(R.id.submit_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTaskInput.getText().length() > 0) {
                    Task t = new Task();
                    t.setDescription(mTaskInput.getText().toString());
                    t.setCompleted(false);
                    t.setACL(new ParseACL(ParseUser.getCurrentUser()));
                    t.setUser(ParseUser.getCurrentUser());
                    t.saveEventually();
                    mTaskInput.setText("");
                    mAdapter.insert(t, 0);
                }
            }
        });

        return rootView;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Task task = mAdapter.getItem(position);
        TextView taskDescription = (TextView) view.findViewById(R.id.task_description);

        task.setCompleted(!task.isCompleted());

        if(task.isCompleted()){
            taskDescription.setPaintFlags(taskDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            taskDescription.setPaintFlags(taskDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        task.saveEventually();
    }

    public void updateData() {
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findInBackground(new FindCallback<Task>() {
            @Override
            public void done(List<Task> tasks, ParseException e) {
                if(tasks != null) {
                    mAdapter.clear();
                    mAdapter.addAll(tasks);
                }
            }
        });
    }
}
