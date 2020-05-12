package com.acceptance.suraj.chatapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {
    EditText username, password;
    Button registerButton;
    String user, pass;
    TextView login;

    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        registerButton = (Button) findViewById(R.id.registerButton);
        login = (TextView) findViewById(R.id.login);

        Firebase.setAndroidContext(this);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Register.this,
                    Users.class);
            startActivity(intent);
            finish();
        }

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Register.this, Login.class));
                }
            });

            registerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user = username.getText().toString();
                    pass = password.getText().toString();


                    if (user.equals("")) {
                        username.setError("can't be blank");
                    } else if (pass.equals("")) {
                        password.setError("can't be blank");
                    } else if (!user.matches("[A-Za-z0-9]+")) {
                        username.setError("only alphabet or number allowed");
                    } else if (user.length() < 1) {
                        username.setError("at least 1 characters long");
                    } else if (pass.length() < 3) {
                        password.setError("at least 3 characters long");
                    } else {
                        final ProgressDialog pd = new ProgressDialog(Register.this);
                        pd.setMessage("Loading...");
                        pd.show();

                        String url = "https://chatapp-56220.firebaseio.com/user.json";

                        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                Firebase reference = new Firebase("https://chatapp-56220.firebaseio.com/user");

                                if (s.equals("null")) {
                                    reference.child(user).child("password").setValue(pass);
                                    Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                                } else {
                                    try {
                                        JSONObject obj = new JSONObject(s);

                                        if (!obj.has(user)) {
                                            reference.child(user).child("password").setValue(pass);
                                            Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();


                                         //   String uid = obj.getString("uid");

                                           // JSONObject user = obj.getJSONObject("user");
                                            String name = user.toString();
                                            String email = pass.toString();

                                            Log.e("sss",name);


                                            // Inserting row in users table
                                            db.addUser(name, email);


                                            startActivity(new Intent(Register.this, Login.class));
                                        } else {
                                            Toast.makeText(Register.this, "username already exists", Toast.LENGTH_LONG).show();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                pd.dismiss();
                            }

                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                System.out.println("" + volleyError);
                                pd.dismiss();
                            }
                        });

                        RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                        rQueue.add(request);
                    }
                }
            });


    }
}