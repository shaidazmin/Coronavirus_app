package net.onlinebuzz.coronavirus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import android.widget.ImageView;
import android.widget.LinearLayout;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;


import com.android.volley.toolbox.Volley;
import com.google.android.material.textview.MaterialTextView;


import net.onlinebuzz.coronavirus.Adapter.CoronaAdapter;
import net.onlinebuzz.coronavirus.model.DataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MaterialTextView totalCases, totalRecovered, effectedCountry, totalDeaths;

    private MaterialTextView one, two, three, four;

    private RecyclerView recyclerView;
    private List<DataModel> dataModelList ;
    private CoronaAdapter adapter;

    private LinearLayout loaderLayout;
    private   MenuItem searchCountry;
    private ImageView no_internet;


    @Override
    protected void onResume() {
        super.onResume();

    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                if(isOnline() == true){
//                    getAllData();
//                }
//            }
//        });
//        thread.start();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setLogo(R.drawable.app_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("Coronavirus");
        



        initializeAllData();

        getAllData();


    }

    private void initializeAllData() {

        dataModelList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycle_view);

        totalCases = findViewById(R.id.total_cases);
        totalDeaths = findViewById(R.id.total_deaths);
        effectedCountry = findViewById(R.id.effected_country);
        totalRecovered = findViewById(R.id.total_recovered);

        loaderLayout = findViewById(R.id.loader_layout);
        no_internet = findViewById(R.id.no_internet);


        one = findViewById(R.id.id_one);
        two = findViewById(R.id.id_two);
        three = findViewById(R.id.id_three);
        four = findViewById(R.id.id_four);



    }

    private void getAllData() {



        if (isOnline()!=true){
            no_internet.setVisibility(View.VISIBLE);
            loaderLayout.setVisibility(View.GONE);
        }else {
            no_internet.setVisibility(View.GONE);
        }

        final RequestQueue queue = Volley.newRequestQueue(this);


        String allDataURL = "https://azmin-coronavirus-restfull-api.herokuapp.com/atzdata";


        // add all data .......

        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, allDataURL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {



                        Log.d("alldata", response.toString());

                        searchCountry.setVisible(true);
                        loaderLayout.setVisibility(View.GONE);
                        // set Visibility ....
                        totalCases.setVisibility(View.VISIBLE);
                        totalRecovered.setVisibility(View.VISIBLE);
                        effectedCountry.setVisibility(View.VISIBLE);
                        totalDeaths.setVisibility(View.VISIBLE);

                        one.setVisibility(View.VISIBLE);
                        two.setVisibility(View.VISIBLE);
                        three.setVisibility(View.VISIBLE);
                        four.setVisibility(View.VISIBLE);

                        // add heading section......

                        try {
                            totalCases.setText(response.getJSONObject(0).getString("totalWorldCases"));
                            totalRecovered.setText(response.getJSONObject(0).getString("totalWorldRecoverd"));
                            effectedCountry.setText(response.getJSONObject(0).getString("totalEffectedCountry"));
                            totalDeaths.setText(response.getJSONObject(0).getString("totalWorldDeaths"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        // add all country ......

                        for (int i = 1; i < response.length()-1 ; i++) {

                            try {

                                JSONObject jsonObject = response.getJSONObject(i);

                                DataModel dataModel = new DataModel();

                                dataModel.setCountry(jsonObject.getString("country"));
                                dataModel.setTotalTestes(jsonObject.getString("totalTestes"));
                                dataModel.setTotalCases(jsonObject.getString("totalCases"));
                                dataModel.setTotalDeaths(jsonObject.getString("totalDeaths"));
                                dataModel.setTotalRecovered(jsonObject.getString("totalRecovered"));
                                dataModel.setActiveCases(jsonObject.getString("activeCases"));
                                dataModel.setNewCases(jsonObject.getString("newCases"));
                                dataModel.setCriticalCases(jsonObject.getString("criticalCases"));
                                dataModel.setNewDeaths(jsonObject.getString("newDeaths"));

                                dataModelList.add(dataModel);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        adapter = new CoronaAdapter(getApplicationContext(), dataModelList);
                        recyclerView.setAdapter(adapter);

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

// Adding the request to the queue along with a unique string tag

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                queue.add(jsonArrayRequest);
            }
        });
        thread.start();


    }
    
    //Call menu option .....

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        try {

            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu, menu);

            if (menu instanceof MenuBuilder) {

                MenuBuilder menuBuilder = (MenuBuilder) menu;
                menuBuilder.setOptionalIconsVisible(true);
            }


            searchCountry = menu.findItem(R.id.search_country);
            searchCountry.setVisible(false);
            SearchView searchView = (SearchView) searchCountry.getActionView();

            searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
            searchView.setQueryHint("Search Country...");

            searchView.setBackgroundColor(Color.parseColor("#ffffff"));

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return false;
                }
            });

        } catch (Exception e) {

        }

        return true;
    }

    // menu item action .....


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        MaterialTextView titleTxt, descriptionTxt;

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.aleart_dialog, viewGroup, false);
        titleTxt = dialogView.findViewById(R.id.title_txt);
        descriptionTxt = dialogView.findViewById(R.id.description_txt);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();


        try {

            switch (item.getItemId()) {

                case R.id.syn_data: {
                    loaderLayout.setVisibility(View.VISIBLE);
                    getAllData();
                    break;
                }
                case R.id.origin: {
                    titleTxt.setText("Origin of COVID - 19");
                    descriptionTxt.setText(R.string.origin);
                    alertDialog.show();
                    break;
                }
                case R.id.symptoms: {
                    titleTxt.setText("Symptoms");
                    descriptionTxt.setText(R.string.symptoms);
                    alertDialog.show();
                    break;

                }
                case R.id.prevention: {
                    titleTxt.setText("Prevention");
                    descriptionTxt.setText(R.string.prevention);
                    alertDialog.show();
                    break;
                }
                case R.id.treatments: {
                    titleTxt.setText("Treatments");
                    descriptionTxt.setText(R.string.treatments);
                    alertDialog.show();
                    break;
                }
                case R.id.about: {

                    break;
                }

            }


        } catch (Exception e) {

        }


        return super.onOptionsItemSelected(item);
    }

    boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }



}
