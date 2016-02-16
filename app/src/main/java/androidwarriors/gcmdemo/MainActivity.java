package androidwarriors.gcmdemo;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import androidwarriors.gcmdemo.utils.JSONParser;
import androidwarriors.gcmdemo.utils.TAG;
import androidwarriors.gcmdemo.utils.Utils;

public class MainActivity extends AppCompatActivity implements TAG {
    String PROJECT_NUMBER = "26701829862";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GCMClientManager pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
        pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
            @Override
            public void onSuccess(String registrationId, boolean isNewRegistration) {
                Log.d("Registration id", registrationId);
                if (isNewRegistration) {
                    onRegisterGcm(registrationId);
                }
            }

            @Override
            public void onFailure(String ex) {
                super.onFailure(ex);
            }
        });
    }

    public void onRegisterGcm(final String regid) {

        if (Utils.onlineStatus2(MainActivity.this)) {
            new RegisterGcm(regid).execute();
        } else {
            Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    class RegisterGcm extends AsyncTask<Void, Void, String> {
        JSONObject response = null;
        String regid, code;

        public RegisterGcm(String regid) {
            this.regid = regid;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            try {
                response = JSONParser.REG_GCM(REGISTER_URL, regid);
                Log.d("REFER", response.toString());

                if (response.has(CODE)) {
                    code = response.getString(CODE);

                }


            } catch (Exception e) {
                Log.d("REFER", e.getMessage());
            }
            return code;
        }

        @Override
        protected void onPostExecute(String data) {

            if (data != null) {
                Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
            }


        }


    }
}
