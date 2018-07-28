package sg.edu.rp.webservices.c302_p09_mcafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";

    private ListView lvCategories;
    private ArrayList<MenuCategory> alCategories;
    private ArrayAdapter<MenuCategory> aaCategories;
    private String loginID,apikey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lvCategories = (ListView)findViewById(R.id.lv);
        alCategories = new ArrayList<MenuCategory>();
        aaCategories = new ArrayAdapter<MenuCategory>(this, android.R.layout.simple_list_item_1, alCategories);
        lvCategories.setAdapter(aaCategories);

        Intent i = getIntent();//get
        loginID = i.getStringExtra("id");
        apikey = i.getStringExtra("apikey");


        if (loginID.equalsIgnoreCase("") || apikey.equalsIgnoreCase("")) {
            // redirect back to login screen
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }

        lvCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MenuCategory selectedCategory = alCategories.get(position);

                // TODO (7) When a contact is selected, create an Intent to View Contact Details
                // Put the following into intent:- contact_id, loginId, apikey
                MenuCategory c = alCategories.get(position);
                String cID = c.getCategoryId();
                String cDescription = c.getDescription();

                Intent i = new Intent(MainActivity.this,
                        MenuActivity.class);
                i.putExtra("id", cID);
                i.putExtra("description", cDescription);
                i.putExtra("apikey",apikey);
                i.putExtra("loginId",loginID);
                Log.i("info", cID+"");
                startActivity(i);

            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();

        alCategories.clear();



        // TODO (5) Refresh the main activity with the latest list of contacts by calling getListOfContacts.php
        // What is the web service URL?
        // What is the HTTP method?
        // What parameters need to be provided?

        // Code for step 1 start
        HttpRequest request = new HttpRequest
                ("http://10.0.2.2/C302_P09/getMenuCategories.php");
        request.setOnHttpResponseListener(mHttpResponseListener);
        request.setMethod("POST");
        request.addData("loginId",loginID);
        request.addData("apikey",apikey);
        request.execute();
        // Code for step 1 end

    }
    // Code for step 2 start
    private HttpRequest.OnHttpResponseListener mHttpResponseListener =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response){

                    // process response here
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i=0; i<jsonArray.length(); i++){
                            JSONObject jsonObj = jsonArray.getJSONObject(i);

                            String id = jsonObj.getString("menu_item_category_id");
                            String description = jsonObj.getString("menu_item_category_description");
                            alCategories.add(new MenuCategory(id,description));
                        }
                    }
                    catch(Exception e){
                        e.printStackTrace();
                    }

                    aaCategories.notifyDataSetChanged();
                }
            };



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
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
