package com.hackathon.dsce.amit.dosomething;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewTaskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewTaskFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView title, description, submit;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue requestQueue;
    String notifUrl = UrlStrings.taskUrl;
    private ImageButton attachButton, cameraFileButton;

    private OnFragmentInteractionListener mListener;

    public NewTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewTaskFragment newInstance(String param1, String param2) {
        NewTaskFragment fragment = new NewTaskFragment();
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
        View view = inflater.inflate(R.layout.fragment_new_task, container, false);

        cameraFileButton = (ImageButton) view.findViewById(R.id.cameraButton);
        title = (TextView) view.findViewById(R.id.newNotifTitle);
        description = (TextView) view.findViewById(R.id.newNotifDescription);
        submit = (TextView) view.findViewById(R.id.submitNotifTextView);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAndPrintResponse();
            }
        });

        return view;
    }

    private void sendAndPrintResponse()
    {
        //Showing a progress dialog
        requestQueue = VolleySingleton.getInstance(getContext()).getRequestQueue(getContext());
        JSONObject loginJsonObject = null;
        try {

            Log.d("ALERT !!", title.getText().toString());
            loginJsonObject = new JSONObject().put("Title", title.getText()).put("TaskBody", description.getText()).put("UserName", User.getUserName()).put("UserEmail", User.getEmail());  //----------HARDCODED VALUE !!!!!!--------------

        }catch (Exception e){e.printStackTrace();}

        Log.d("ALERT !!", notifUrl);
        Log.d("ALERT !!", loginJsonObject.toString());

        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, notifUrl, loginJsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("ALERT !!", response.toString());
                parseJsonResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("ALERT ERROR!!", error.toString());
                getActivity().finish();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    private void parseJsonResponse(JSONObject response)
    {
        String responseString = "";
        try{
            responseString = response.getString("messageTitle");
            //Toast.makeText(this, responseString, Toast.LENGTH_SHORT).show();
        }catch (Exception e){e.printStackTrace();}

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
