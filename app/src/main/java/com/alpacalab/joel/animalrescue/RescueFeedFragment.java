package com.alpacalab.joel.animalrescue;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
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
        RelativeLayout rootView = (RelativeLayout) inflater.inflate(R.layout.fragment_rescue_feed, container, false);
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


        //Submit button
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

        //Submit button
        FloatingActionButton mAction = (FloatingActionButton) rootView.findViewById(R.id.action_a);
        mAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RescueRequest.class);
                startActivity(intent);
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
