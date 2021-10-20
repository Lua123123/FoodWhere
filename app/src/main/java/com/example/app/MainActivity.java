package com.example.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView register, forgotPassword;                                      //Dang ky

    private EditText editTextEmail, editTextPassword;               //Dang nhap
    private Button signIn;                                          //Dang nhap

    private FirebaseAuth mAuth;                                     //Doi tuong xac thuc tren firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register = (TextView) findViewById(R.id.register);           //Dang ky
        register.setOnClickListener(this);                           //Dang ky

        signIn = (Button) findViewById(R.id.signIn);                 //Dang nhap
        signIn.setOnClickListener(this);                             //Dang nhap

        editTextEmail = (EditText) findViewById(R.id.email);         //Dang nhap
        editTextPassword = (EditText) findViewById(R.id.password);   //Dang nhap

        mAuth = FirebaseAuth.getInstance();                         //Khai bao thang nay de chuyen qua MainActivity2

                                                                     //De lay du lieu tren FireBase
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            //CHUYEN TOI RegisterUser
            case R.id.register:
                startActivity(new Intent(this, RegisterUser.class));
                break;

            //PHUONG THUC DANG NHAP
            case R.id.signIn:
                userLogin();
                break;

            //CHUYEN TOI ForgotPassword
            case R.id.forgotPassword:
                startActivity(new Intent(this, ForgotPassword.class));
                break;
        }

    }

    //PHUONG THUC DANG NHAP

    private void userLogin() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //BAO LOI

        if(email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email!");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextPassword.setError("Min password length is 6 characters!");
            editTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //TẠO ĐỐI TƯỢNG ĐỂ CHECK XEM EMAIL ĐÃ VERYFIED HAY CHƯA
                    //NẾU ĐÃ VERYFIED
                    if (user.isEmailVerified()){
                        startActivity(new Intent(MainActivity.this, MainActivity2.class));
                    }
                    //NẾU CHƯA VERYFIED
                    else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your email to veryfy your account!",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "Failed to login! Please check your credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}