package com.alpacalab.joel.animalrescue;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joel on 12/31/14.
 */
public class RescueFeedFragment extends Fragment implements AdapterView.OnItemClickListener {
    private EditText mTaskInput;
    private ListView mListView;
    private RescueAdapter mAdapter;
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
        mListView = (ListView) rootView.findViewById(R.id.rescue_list);

        mAdapter = new RescueAdapter(this.getActivity(), new ArrayList<Rescue>());
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Rescue rescue = mAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), RescueActivity.class);
                startActivity(intent);
            }
        });

        updateData();

        //Floating action button
        final FloatingActionsMenu mActionMenu = (FloatingActionsMenu) rootView.findViewById(R.id.multiple_actions);
        FloatingActionButton mAction = (FloatingActionButton) rootView.findViewById(R.id.action_a);
        mAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RescueRequestActivity.class);
                startActivity(intent);
                mActionMenu.collapse();
            }
        });


        return rootView;
    }


    public void updateData() {
        ParseQuery<Rescue> query = ParseQuery.getQuery(Rescue.class);
        query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.findInBackground(new FindCallback<Rescue>() {
            @Override
            public void done(List<Rescue> rescue, ParseException e) {
                if(rescue != null) {
                    mAdapter.clear();
                    mAdapter.addAll(rescue);
                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}