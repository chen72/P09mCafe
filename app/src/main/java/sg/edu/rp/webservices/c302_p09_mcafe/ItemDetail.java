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

public class ItemDetail extends AppCompatActivity {


    private EditText etItemName, etItemPrice ;
    private Button btnUpdate, btnDelete;
    private String loginID,apikey,name,item_id;
    private double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        etItemName = findViewById(R.id.etItemName);
        etItemPrice = findViewById(R.id.etItemPrice);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);



        Intent i = getIntent();//get
        loginID = i.getStringExtra("loginId");
        apikey = i.getStringExtra("apikey");
        item_id = i.getStringExtra("id");
        name = i.getStringExtra("description");
        price = i.getDoubleExtra("price",0.0);


        etItemName.setText(name);
        etItemPrice.setText(price+"");



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Code for step 1 start
                String url = "http://10.0.2.2/C302_P09/updateMenuItemById.php";

                HttpRequest request = new HttpRequest(url);

                request.setOnHttpResponseListener(mHttpResponseListener);
                request.setMethod("POST");
                request.addData("loginId",loginID);
                request.addData("apikey",apikey);

                request.addData("item_id", item_id);
                request.addData("description", etItemName.getText().toString().trim());
                request.addData("price", etItemPrice.getText().toString().trim());

                request.execute();
                finish();
                // Code for step 1 end


            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Code for step 1 start
                String url = "http://10.0.2.2/C302_P09/deleteMenuItemById.php";

                HttpRequest request = new HttpRequest(url);

                request.setOnHttpResponseListener(mHttpResponseListener);
                request.setMethod("POST");
                request.addData("item_id",item_id);
                request.addData("loginId",loginID);
                request.addData("apikey",apikey);
                request.execute();
                finish();
                // Code for step 1 end


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
                        Toast.makeText(getApplicationContext(), jsonObj.getBoolean("authorized")+jsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            };
}
