package com.team.diana;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Activities extends AppCompatActivity {
    FirebaseAuth auth;
    EditText txtphone, txtverify;
    Button afterVerify;
    Button send, verify;
    String verification_code;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        afterVerify=(Button)findViewById(R.id.btnafterverify) ;
        txtphone=(EditText)findViewById(R.id.txtphone);
        txtverify=(EditText)findViewById(R.id.txtverify);
        send=(Button) findViewById(R.id.btnsend);
        afterVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  afterver=new Intent(Activities.this,activity_chat_layout.class );
                startActivity(afterver);


            }
        });
        verify=(Button) findViewById(R.id.btnverify);
        mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(Activities.this, "Completed",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(Activities.this, "Failed",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verification_code=s;
                Toast.makeText(Activities.this, "Code sent......", Toast.LENGTH_LONG).show();
            }
        };
    }

    public void send_sms(View v){
        try{
            if(txtphone.getText().equals("")){

                Toast.makeText(Activities.this, "Textphne is null",Toast.LENGTH_LONG).show();
            }

            String number=txtphone.getText().toString();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(number,60, TimeUnit.SECONDS,this, mCallBack);
        }

        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void signInwithPhone(PhoneAuthCredential credential){
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){


                    Toast.makeText(Activities.this,"Success",Toast.LENGTH_LONG).show();
                }
            }
        });



    }
    public void verify(View v){

        String inputCode=txtverify.getText().toString();
        if(!inputCode.equals("")){

            verifyPhoneNumber(verification_code,inputCode);
            Toast.makeText(Activities.this,"verification-code "+ verification_code ,Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(Activities.this,"Please enter verification Text" ,Toast.LENGTH_LONG).show();

        }



    }
    public void verifyPhoneNumber(String verificationcode,String inputCode){

        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationcode,inputCode);
        signInwithPhone(credential);

        if(verificationcode.equals(inputCode)){

            Toast.makeText(Activities.this,"PERFECT!!!!!!!!!" ,Toast.LENGTH_LONG).show();


        }

    }


}