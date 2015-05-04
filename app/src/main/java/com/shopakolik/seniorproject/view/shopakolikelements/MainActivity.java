package com.shopakolik.seniorproject.view.shopakolikelements;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.shopakolik.seniorproject.R;
import com.shopakolik.seniorproject.controller.databasecontroller.DatabaseManager;
import com.shopakolik.seniorproject.controller.databasecontroller.UserType;
import com.shopakolik.seniorproject.controller.notificationcontroller.AlarmReceiver;
import com.shopakolik.seniorproject.controller.notificationcontroller.NotificationService;
import com.shopakolik.seniorproject.model.shopakolikelements.User;

import org.w3c.dom.Text;


public class MainActivity extends ActionBarActivity {

    private Button signInButton;
    private TextView forgetPassword, signUpCustomer, signUpShop, email, password;
    private String oldemail, oldpassword;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Email = "emailKey";
    public static final String Password = "passwordKey";
    public static String PACKAGE_NAME;
    private Context context;

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PACKAGE_NAME = getApplicationContext().getPackageName();


        Button signInButton = (Button) findViewById(R.id.signInButton);
        //TextView forgetPassword = (TextView) findViewById(R.storeID.forgetPassword);
        TextView signUpCustomer = (TextView) findViewById(R.id.SignUpAsCustomer);
        TextView signUpShop = (TextView) findViewById(R.id.SignUpAsShop);

        email = (TextView) findViewById(R.id.email);
        password = (TextView) findViewById(R.id.password);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        context = this;

        boolean emailFlag = false, passFlag = false;
        if (sharedpreferences.contains(Email)) {
            email.setText(sharedpreferences.getString(Email, ""));
            emailFlag = true;
        }
        if (sharedpreferences.contains(Password)) {
            password.setText(sharedpreferences.getString(Password, ""));
            passFlag = true;
        }

        if(emailFlag && passFlag)
        {
//            final ProgressDialog ringProgressDialog = ProgressDialog.show(MainActivity.this, "Please wait ...", "Logging in ...", true);
            //ringProgressDialog.setCancelable(true);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        userLogin();
                    } catch (Exception e) {

                    }
//                    ringProgressDialog.dismiss();
                }
            }).start();
        }

        oldemail = email.getText().toString();
        oldpassword = password.getText().toString();
    }


    public void forgetPasswordClick(View view) {
        Intent getNameScreenIntent = new Intent(this, ForgetPassword.class);
        startActivity(getNameScreenIntent);

    }



    public void userLogin(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                UserType userType = UserType.NonUser;

                try {

                    userType = DatabaseManager.login(email.getText().toString(), password.getText().toString());

                    if (userType == UserType.Customer) {
                        Log.e("user", userType.toString());
                        Intent getNameScreenIntent = new Intent(MainActivity.this, Wall.class);
                        getNameScreenIntent.putExtra("user_email", email.getText().toString());
                        getNameScreenIntent.putExtra("user_password", password.getText().toString());
                        getNameScreenIntent.putExtra("user_type", userType.toString());
                        startActivity(getNameScreenIntent);

                        final boolean notifchecked = DatabaseManager.getCustomer(sharedpreferences.getString(Email, ""), sharedpreferences.getString(Password, "")).isCampaignNotification();
                        final boolean gpschecked = DatabaseManager.getCustomer(sharedpreferences.getString(Email, ""), sharedpreferences.getString(Password, "")).isLocationNotification();

                        if(notifchecked)
                        {
                            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
                            final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
                            final AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                            final int interval = 15000;
                            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
                        }

                        if(gpschecked)
                        {
                            final Intent gpsIntent = new Intent(context, NotificationService.class);
                            context.startService(gpsIntent);
                        }


                    } else if (userType == UserType.Store) {
                        Intent getNameScreenIntent = new Intent(MainActivity.this, PageOfOwnerShop.class);
                        getNameScreenIntent.putExtra("user_email", email.getText().toString());
                        getNameScreenIntent.putExtra("user_password", password.getText().toString());
                        getNameScreenIntent.putExtra("user_type", userType.toString());
                        startActivity(getNameScreenIntent);
                    } else if (userType == UserType.NonUser) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Context context = getApplicationContext();
                                CharSequence text = "Check your email and password!";
                                int duration = Toast.LENGTH_SHORT;
                                Toast toast = Toast.makeText(context, text, duration);
                                toast.show();
                            }
                        });

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void signUpCustomerClick(View view) {
        Intent getNameScreenIntent = new Intent(this, SignUpForCustomer.class);
        startActivity(getNameScreenIntent);

    }

    public void signUpShopClick(View view) {
        Intent getNameScreenIntent = new Intent(this, SignUpForShop.class);
        startActivity(getNameScreenIntent);

    }


    public void signInButtonClick(View view) {


                if (!oldemail.equals(email.getText().toString())) {
                    Log.e("runOnUiThread", "runOnUiThread");
                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    Log.e("AlertDialog.Builder", "AlertDialog.Builder");

                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setMessage(R.string.updateUserInfo);
                    builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Email, email.getText().toString());
                            editor.putString(Password, password.getText().toString());
                            editor.commit();
                            userLogin();
                        }
                    });

                    builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userLogin();
                        }
                    });

                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else
                {
                    userLogin();
                }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
