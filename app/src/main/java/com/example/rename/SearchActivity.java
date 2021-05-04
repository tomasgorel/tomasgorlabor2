package com.example.rename;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity { //cia yra globalus kintamieji, kurie aprasomi klases virsuje

    public static final String Drinks_API = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=Marga";
    private ArrayList<Drinks> drinksList = new ArrayList<Drinks>();
    private Adapter adapter; //tarpininkas tarp xml ir searchactivitysearch_no_results
    SearchView searchView = null;
    private RecyclerView recyclerView; //korteliu vaizdas

    @Override
    //The Override-Annotation is just a hint for the compiler that you want to overwrite a certain function. The compiler will then check parent-classes and interfaces if the function exists there. If not, you will get a compile-error
    protected void onCreate(Bundle savedInstanceState) {  //Bundle savedInstanceState - isaugoti veiklos busenai, kur naudojam vel atnaujinus, pradzioj null, pvz.: lango slinkimo pozicija
        super.onCreate(savedInstanceState); //tuscio lango sukurimas
        setContentView(R.layout.activity_search); //suteikti langui si vaizda

        AsyncFetch asyncFetch = new AsyncFetch(); //paleidziama nauja gija - nuskaitymui JSON is API
        asyncFetch.execute();  //vykdo kas toje klaseje numatyta. Butina execute

        // ProgressDialog progressDialog = new ProgressDialog(SearchActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { // adds item to action bar
        getMenuInflater().inflate(R.menu.search, menu); // Get Search item from action bar and Get Search service
        MenuItem searchItem = menu.findItem(R.id.action_search); //vartotojas irasys duomenis
        SearchManager searchManager = (SearchManager) SearchActivity.this.getSystemService(Context.SEARCH_SERVICE);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(SearchActivity.this.getComponentName()));
            searchView.setIconified(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //meniu juostoj lupa
        return super.onOptionsItemSelected(item);
    } // Every time when you press search button on keypad an Activity is recreated which in turn calls this function

    @Override
    protected void onNewIntent(Intent intent) { // Get search query
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);  //vartotojas ives pvz Margarita
            if (searchView != null) {
                searchView.clearFocus(); //isvalo kursoriu
            }
            //is visu kokteiliu saraso sukuriamas sarasas pagal ieskoma kokteilio pavadinima
            ArrayList<Drinks> drinksListByName = JSON.getDrinksListByName(drinksList, query);
            if (drinksListByName.size()==0){
                Toast.makeText(this, getResources().getString(R.string.seach_no_results)+query, Toast.LENGTH_LONG).show();
            }
            //duomenu perdavimas Adapteriui ir RecyclerView sukurimas
            recyclerView = (RecyclerView) findViewById(R.id.drinks_List);
            adapter = new Adapter(SearchActivity.this, drinksListByName);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        }
    }

    private class AsyncFetch extends AsyncTask<String, String, ArrayList<Drinks>> { //lygiag uzd. apdorot //AsyncTask perduoda parametrus i doInBackground

        //parametrai eina i doInBackground // Params, Progress and Result (per result grazins rezultata)
        ProgressDialog progressDialog = new ProgressDialog(SearchActivity.this);

        @Override
        protected void onPreExecute() { //metodas vykdomas pries doInBackground metoda. Paprasysime palaukti vartotojo, kol gausim duomenis
            super.onPreExecute(); //paleidzia gija pries doInBackground
            progressDialog.setMessage(getResources().getString(R.string.search_loading_data));
            progressDialog.setCancelable(false); //negales atsaukti ir tures islaukti
            progressDialog.show(); //rodymas vaizdas kol laukia/igalinti
        }
        @Override
        protected ArrayList<Drinks> doInBackground(String... params) { //jis bus vykdomas tuo metu kai vartotojas matys progress dialoga
            //skirtas duomenu paemimui is API
            try {
                JSONObject jsonObject = JSON.readJsonFromUrl(Drinks_API); //perduodamas adresas

                JSONArray jsonArray = null;
                drinksList = new ArrayList<Drinks>();
                try {
                    jsonArray = JSON.getJSONArray(jsonObject);
                    drinksList = JSON.getList(jsonArray);
                }
                catch (JSONException e){
                    Toast.makeText(SearchActivity.this, getResources().getText(R.string.search_error_reading_data)+ e.getMessage(), Toast.LENGTH_LONG).show();
                }
                return drinksList;
            } catch (IOException e) {  //isimtys, apdoros duomenisir isvesvartotojui
                Toast.makeText(SearchActivity.this, getResources().getText(R.string.search_error_reading_data)+ e.getMessage(), Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                Toast.makeText(SearchActivity.this, getResources().getText(R.string.search_error_reading_data) + e.getMessage(), Toast.LENGTH_LONG).show();
            } //baigiasi jsone
            return null;  //jei bus problemu grazinsim null
        }

        protected void onPostExecute(ArrayList<Drinks> drinksList) { //po
            progressDialog.dismiss(); //duomenys gauti, naikinam laukimo vaizda

            //  if (coctailList != null) {
            //       Toast.makeText(SearchActivity.this, coctailList., Toast.LENGTH_LONG).show();
            //   }
        }// baigiasi onPostExecute
    }// baigiasi AsyncFetch
}// baigiasi SearchActivity
