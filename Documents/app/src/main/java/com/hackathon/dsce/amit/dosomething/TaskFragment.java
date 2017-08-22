package com.hackathon.dsce.amit.dosomething;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TaskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<Task> tasks;
    private JsonArrayRequest jsonArrayRequest;
    private RequestQueue requestQueue;
    String notificationURL = UrlStrings.taskUrl;
    SwipeRefreshLayout swipeLayout;
    private LinearLayout notifLinearLayout;

    private OnFragmentInteractionListener mListener;

    public TaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskFragment newInstance(String param1, String param2) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_task, container, false);
        setHasOptionsMenu(true);
        //Initializing Views
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                sendAndPrintResponse();
            }
        });
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this.getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        tasks = new ArrayList<>();
        sendAndPrintResponse();

        return view;
    }

    private void sendAndPrintResponse() {
        String modifiedUrl;

        if(User.getIsAdmin()){
            modifiedUrl = notificationURL + "?year=admin";
        }
        else {
            modifiedUrl = notificationURL + "?year=" + User.getYear();
        }
        //Showing a progress dialog
        final ProgressDialog loading = ProgressDialog.show(this.getContext(),"Loading Data", "Please wait...",false,false);
        requestQueue = VolleySingleton.getInstance(this.getContext()).getRequestQueue(this.getContext());

        jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, modifiedUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("ALERT !!", response.toString());
                loading.dismiss();
                parseJsonArrayResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                addRefreshGui();
                Log.i("ALERT ERROR!!", error.toString());
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    private void addRefreshGui()
    {
        notifLinearLayout = (LinearLayout) getView().findViewById(R.id.new_notif_linear_layout);
        Toast.makeText(getContext(), "Could not connect to Database.\nSwipe down to refresh.", Toast.LENGTH_SHORT).show();
        swipeLayout.setRefreshing(false);
    }

    private void parseJsonArrayResponse(JSONArray jsonArray)
    {
        tasks.clear();
        for (int i = 0; i < jsonArray.length(); i++)
        {
            Task task = new Task();
            JSONObject jsonObject = null;
            try{
                jsonObject = jsonArray.getJSONObject(i);
                task.setTitle(jsonObject.getString("Title"));
                task.setBody(jsonObject.getString("UserName"));
                task.setUpvotes(Integer.parseInt(jsonObject.getString("UserEmail")));
            }catch (Exception e){e.printStackTrace();}
            tasks.add(task);
        }

        /*
        Intent serviceIntent = new Intent(getActivity(), ServerHeartbeatService.class);
        serviceIntent.putExtra("num", tasks.size());
        serviceIntent.putExtra("year", User.getYear());
        SharedPreferences p = getActivity().getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed= p.edit();
        Ed.putInt("num", tasks.size());
        Ed.putString("year", User.getYear());
        Ed.commit();
        getActivity().startService(serviceIntent);
        */
        //Finally initializing our adapter
        adapter = new CardAdapter(tasks, this.getContext());
        //Adding adapter to recyclerView
        recyclerView.setAdapter(adapter);
        swipeLayout.setRefreshing(false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
                   // + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
