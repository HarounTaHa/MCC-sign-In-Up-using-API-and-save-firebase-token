package com.example.push_notification_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText edEmailLogin;
    private EditText edPassLogin;
    private Button btnLogin;
    private  Button btnRegister;
    private ProgressBar progressBar;
    private   String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        url = "https://mcc-users-api.herokuapp.com/login" ;
        edEmailLogin = findViewById(R.id.ed_email_login);
        edPassLogin = findViewById(R.id.pass_login_ed);
        btnLogin = findViewById(R.id.button_login);
        btnRegister = findViewById(R.id.button_register_login);
        progressBar = findViewById(R.id.progressBar_login);
//      -----------------------------------------------


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edEmailLogin.getText().toString().trim();
                String pass = edPassLogin.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    edEmailLogin.setError("should email");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    edPassLogin.setError("shold pass");
                    return;
                }
                if (pass.length() < 6) {
                    edPassLogin.setError("pass must be >=6 ");
                    return;
                }
                signInProcess(email,pass);

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    private void signInProcess(String email, String pass) {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue= Volley.newRequestQueue(LoginActivity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("email",email).putExtra("pass",pass));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Error in process sign in "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("email",email);
                param.put("password",pass);
                return param;
            }

        };
        queue.add(stringRequest);
    }
}