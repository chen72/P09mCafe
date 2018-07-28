package sg.edu.rp.webservices.c302_p09_mcafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {
    private ListView lvItem;
    private ArrayList<MenuItem> alItem;
    private ArrayAdapter<MenuItem> aaItem;
    private String loginID, apikey, category_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        lvItem = (ListView) findViewById(R.id.lvMenu);
        alItem = new ArrayList<MenuItem>();
        aaItem = new ArrayAdapter<MenuItem>(this, android.R.layout.simple_list_item_1, alItem);
        lvItem.setAdapter(aaItem);

        Intent i = getIntent();//get
        loginID = i.getStringExtra("id");
        apikey = i.getStringExtra("apikey");
        category_id = i.getStringExtra("id");


        if (loginID.equalsIgnoreCase("") || apikey.equalsIgnoreCase("")) {
            // redirect back to login screen
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        lvItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MenuItem selectedItem = alItem.get(position);

                // TODO (7) When a contact is selected, create an Intent to View Contact Details
                // Put the following into intent:- contact_id, loginId, apikey
                MenuItem c = alItem.get(position);
                String cID = c.getId();
                String cDescription = c.getItem_description();

                Intent i = new Intent(MenuActivity.this,
                        ItemDetail.class);
                i.putExtra("id", cID);
                i.putExtra("description", cDescription);
                i.putExtra("apikey", apikey);
                i.putExtra("loginId", loginID);
                i.putExtra("price",c.getItem_price());
                Log.i("info", cID + "");
                startActivity(i);

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        alItem.clear();


        // TODO (5) Refresh the main activity with the latest list of contacts by calling getListOfContacts.php
        // What is the web service URL?
        // What is the HTTP method?
        // What parameters need to be provided?

        // Code for step 1 start
        HttpRequest request = new HttpRequest
                ("http://10.0.2.2/C302_P09/getMenuItemsByCategory.php");
        request.setOnHttpResponseListener(mHttpResponseListener);
        request.setMethod("POST");
        request.addData("loginId", loginID);
        request.addData("apikey", apikey);
        request.addData("categoryId", category_id);
        request.execute();
        // Code for step 1 end

    }

    // Code for step 2 start
    private HttpRequest.OnHttpResponseListener mHttpResponseListener =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response) {

                    // process response here
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        Log.i("info", jsonArray + "");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObj = jsonArray.getJSONObject(i);

                            String id = jsonObj.getString("menu_item_id");
                            String menu_category_id = jsonObj.getString("menu_item_category_id");
                            String item_description = jsonObj.getString("menu_item_description");
                            double item_price = jsonObj.getDouble("menu_item_unit_price");
                            alItem.add(new MenuItem(id, menu_category_id, item_description, item_price));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    aaItem.notifyDataSetChanged();
                }
            };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_add) {

            // TODO (8) Create an Intent to Create Contact
            // Put the following into intent:- loginId, apikey

            Intent intent = new Intent(getBaseContext(), AddItem.class);
            intent.putExtra("apikey", apikey);
            intent.putExtra("loginId", loginID);
            intent.putExtra("category_id",category_id);
            startActivity(intent);
            return true;
        }
        if (id == R.id.logout) {

            // TODO (8) Create an Intent to Create Contact
            // Put the following into intent:- loginId, apikey
            finish();
            Intent intent = new Intent(getBaseContext(), LoginActivity.class);
            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}



