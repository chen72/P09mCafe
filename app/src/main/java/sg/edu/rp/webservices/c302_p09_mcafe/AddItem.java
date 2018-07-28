package sg.edu.rp.webservices.c302_p09_mcafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

public class AddItem extends AppCompatActivity {


    private EditText etName, etPrice;
    private Button btnAdd;
    private String loginID,apikey,category_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);


        etName = findViewById(R.id.etName);
        etPrice = findViewById(R.id.etPrice);
        btnAdd = findViewById(R.id.btnAdd);


        Intent i = getIntent();//get
        loginID = i.getStringExtra("loginId");
        apikey = i.getStringExtra("apikey");
        category_id = i.getStringExtra("category_id");

        Toast.makeText(getApplicationContext(),loginID, Toast.LENGTH_SHORT).show();



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String url = "http://10.0.2.2/C302_P09/addMenuItem.php";
                HttpRequest request = new HttpRequest(url);
                request.setOnHttpResponseListener(mHttpResponseListener);
                request.setMethod("POST");
                request.addData("loginId",loginID);
                request.addData("apikey",apikey);
                request.addData("category_id", category_id);
                request.addData("description",etName.getText().toString().trim());
                request.addData("price",etPrice.getText().toString().trim()+"");
                request.execute();
                finish();

            }
        });

    }

    // Code for step 2 start
    private HttpRequest.OnHttpResponseListener mHttpResponseListener =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response) {

                    // process response here
                    try {
                        Log.i("JSON Results: ", response);

                        JSONObject jsonObj = new JSONObject(response);
                        String au = jsonObj.getBoolean("authorized")+"";

                        if(au.equalsIgnoreCase("true")){
                            Toast.makeText(getApplicationContext(), jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(), jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };
}
