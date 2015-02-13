package ais.co.th.apicaller.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import ais.co.th.apicaller.R;
import ais.co.th.apicaller.Util.AppController;


public class MainActivity extends Activity {
    EditText inputUrl;
    TextView txtHeader;
    TextView txtData;
    String header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                requestDataFormUrl();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initView(){
        inputUrl = (EditText) findViewById(R.id.input_url);
        txtData = (TextView) findViewById(R.id.txt_data);
        txtHeader = (TextView) findViewById(R.id.txt_header);
    }

    private void requestDataFormUrl(){
        String url = inputUrl.getEditableText().toString();
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        txtHeader.setText("Header: "+header);
                        txtData.setText(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                    }
                }
        ) {
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
// we must override this to get headers. and with the fix, we should get all headers including duplicate names
// in an array of apache headers called apacheHeaders. everything else about volley is the same
                header = "\n";
                for ( String key : response.headers.keySet() ) {

                    header += key+": "+response.headers.get(key)+"\n";
                }
                return super.parseNetworkResponse(response);
            }
        };
        AppController.getInstance().addToRequestQueue(postRequest);
    }
}
