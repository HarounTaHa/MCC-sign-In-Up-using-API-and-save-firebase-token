package com.example.push_notification_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class RegisterActivity extends AppCompatActivity {
    EditText firstName_rg , email_rg ,pass_rg ,lastName_rg;
    Button buttonRegister,buttonLogin;
    ProgressBar progressBar;
    String url;
//    FirebaseAuth firebaseAuth;
//    FirebaseFirestore firebaseFirestore;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        url = "https://mcc-users-api.herokuapp.com/add_new_user" ;
        firstName_rg=findViewById(R.id.firstName_editText_rg);
        lastName_rg=findViewById(R.id.lastName_editText_rg);
        email_rg=findViewById(R.id.email_editText_rg);
        pass_rg=findViewById(R.id.pass_rg_ed);
        progressBar=findViewById(R.id.progressBar_rg);
        buttonRegister=findViewById(R.id.button_rg);
        buttonLogin=findViewById(R.id.button_rg_login);

//        ------------------------------------------------
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=email_rg.getText().toString().trim();
                String pass=pass_rg.getText().toString().trim();
                String firstName=firstName_rg.getText().toString();
                String lastName=lastName_rg.getText().toString();
                if (TextUtils.isEmpty(email)){
                    email_rg.setError("should email");
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    pass_rg.setError("should password");
                    return;
                }
                if (pass.length()<6){
                    pass_rg.setError("pass must be >=6 ");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

//           ----------------------------------------
                signUpProcess(firstName,lastName,email,pass);


            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

    }

    private void signUpProcess(String firstName, String lastName, String email, String pass) {
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue= Volley.newRequestQueue(RegisterActivity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class).putExtra("email",email).putExtra("pass",pass));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, "Error in process sign up "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("firstName",firstName);
                param.put("secondName",lastName);
                param.put("email",email);
                param.put("password",pass);
                Log.e("dddddddddddd",firstName+lastName+email+pass);
                return param;
            }
        };
        queue.add(stringRequest);
    }

}