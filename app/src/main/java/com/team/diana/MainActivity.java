package com.team.diana;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {
    EditText txt;
    Button btnbyphone;
    CallbackManager callbackManager;
    ProfileTracker profileTracker;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    ImageView view;
    String url;
    LoginButton loginButton;
    private static final String EMAIL = "email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt=(EditText)findViewById(R.id.editText) ;
        btnbyphone=(Button)findViewById(R.id.btnphone);
        view=(ImageView) findViewById(R.id.image);
        btnbyphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, activity_chat_layout.class);
                startActivity(i);
            }
        });

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email","public_profile");
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        accessToken = AccessToken.getCurrentAccessToken();
        //  boolean isLoggedIn = accessToken != null && !accessToken.isExpired();


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, "You already signed by facebook", Toast.LENGTH_LONG);

                // String user_Id=loginResult.getAccessToken().getUserId();
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            Picasso.get()
                                    .load("https://graph.facebook.com/" + object.getString("id")+ "/picture?type=large")
                                    .resize(50, 50)
                                    .centerCrop()
                                    .into(view);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, last_name, email, id, gender, cover, picture");

                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();


                GraphRequestAsyncTask response = new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/",
                        parameters,  null).executeAsync();


            }

            @Override
            public void onCancel() {

            }



            @Override
            public void onError(FacebookException error) {
                txt.setText(error.toString());
                Toast.makeText(MainActivity.this,error.toString(), LENGTH_LONG).show();
            }

        });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        //isLoggedIn = accessToken != null && !accessToken.isExpired();
        // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        accessTokenTracker.stopTracking();
    }
    public void displayUserInfo(JSONObject object) throws Exception {
        String firstName, lastName,email,id;
        try{
            firstName=object.getString("first_name");
            lastName=object.getString("last_name");
            email=object.getString("email");
            id=object.getString("id");

            Toast.makeText(this,firstName +"\n"+lastName+"\n"+ email+"id"+"\n"+id,Toast.LENGTH_LONG).show();
            //   Bitmap bitmap = getFacebookProfilePicture(id);

        }
        catch (JSONException E){
            E.printStackTrace();
        }

    }
    public Bitmap getFacebookProfilePicture(String id) throws IOException {
        URL imageURL = new URL("https://graph.facebook.com/" + id + "/picture?type=large");
        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

        return bitmap;
    }






}
