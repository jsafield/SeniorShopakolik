package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.controller.databasecontroller.UserType;
import com.shopakolik.seniorproject.controller.notificationcontroller.AlarmReceiver;
import com.shopakolik.seniorproject.controller.notificationcontroller.NotificationService;
import com.shopakolik.seniorproject.model.shopakolikelements.Customer;

import org.json.JSONException;
import org.json.JSONObject;


/** Login Button Fragment
 */
public class FaceBookFragment extends Fragment {
    private CallbackManager callbackManager;
    private FacebookCallback<LoginResult> facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile =  Profile.getCurrentProfile();
            if(profile != null)
            {
                Toast toast = Toast.makeText(getActivity().getApplicationContext() , profile.getName(), Toast.LENGTH_LONG);
                toast.show();
            }

            GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(final JSONObject jsonObject, GraphResponse graphResponse) {
                    Thread thr = new Thread(new Runnable() {
                        final  JSONObject jo = jsonObject;
                        @Override
                        public void run() {

                            String email = null;
                            try {
                                email = jo.getString("email");
                                UserType userType = UserType.NonUser;
                                userType = DatabaseManager.login(email, "123456789");
                                if (userType == UserType.Customer) {
                                    Intent getNameScreenIntent = new Intent(MainActivity.context, Wall.class);
                                    getNameScreenIntent.putExtra("user_email", email);
                                    getNameScreenIntent.putExtra("user_password", "123456789");
                                    getNameScreenIntent.putExtra("user_type", userType.toString());
                                    startActivity(getNameScreenIntent);
                                    DatabaseManager.isFBuser = true;

                                    final boolean notifchecked = false;
                                    final boolean gpschecked = false;

                                    if(notifchecked)
                                    {
                                        Intent alarmIntent = new Intent(getActivity().getApplicationContext(), AlarmReceiver.class);
                                        final PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, alarmIntent, 0);
                                        final AlarmManager manager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                                        final int interval = 20*60*1000;
                                        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
                                    }

                                    if(gpschecked)
                                    {
                                        final Intent gpsIntent = new Intent(getActivity().getApplicationContext(), NotificationService.class);
                                        getActivity().getApplicationContext().startService(gpsIntent);
                                    }
                                }else
                                {
                                    DatabaseManager.addCustomer(new Customer(email, "123456789", "", ""));
                                    Intent getNameScreenIntent = new Intent(MainActivity.context, Wall.class);
                                    getNameScreenIntent.putExtra("user_email", email);
                                    getNameScreenIntent.putExtra("user_password", "123456789");
                                    getNameScreenIntent.putExtra("user_type", "Customer");
                                    startActivity(getNameScreenIntent);
                                    DatabaseManager.isFBuser = true;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thr.start();
                }
            });

            Bundle bundle = new Bundle();
            bundle.putString("fields", "email");
            graphRequest.setParameters(bundle);
            graphRequest.executeAsync();
        }


        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    public static FaceBookFragment newInstance(String param1, String param2) {
        FaceBookFragment fragment = new FaceBookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FaceBookFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_face_book, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }*/

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton)view.findViewById(R.id.face_login_button);
        loginButton.setReadPermissions("email");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, facebookCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e("OOOOOOO",""+(data==null));
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
