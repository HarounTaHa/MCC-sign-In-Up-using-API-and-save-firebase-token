package com.example.push_notification_assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String email,pass;
    String url;
    TextView textView;
    String token_reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        textView=findViewById(R.id.textView);
        url="https://mcc-users-api.herokuapp.com/add_reg_token";

        email = getIntent().getStringExtra("email");
        pass = getIntent().getStringExtra("pass");
        getFirebaseToken();

    }
    private void getFirebaseToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()){
                    Log.e("Token","Failed to get token: "+task.getException());
                    Toast.makeText(MainActivity.this, "Failed to get token", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MainActivity.this, ""+task.getResult(), Toast.LENGTH_SHORT).show();
                token_reg = task.getResult();
                saveToken(email,pass,token_reg);
            }
        });
    }
    private void saveToken(String email,String pass,String token) {
        RequestQueue queue= Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest=new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object=new JSONObject(response);
                    textView.setText(object.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error Save Token! "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("email",email);
                Log.e("email",email);
                param.put("password",pass);
                Log.e("pass",pass);
                param.put("reg_token",token);
                Log.e("token",token);
                return param;
            }

        };
        queue.add(stringRequest);
    }



}