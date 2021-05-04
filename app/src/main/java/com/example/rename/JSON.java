package com.example.rename;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class JSON { //is URL istraukiamas JSON ir jis atspausdinamas

    private static String readAll(Reader rd) throws IOException { //metodas
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    //Visas ilgas tekstas is API nuskaitomas i stringa, o is stringo jis konvertuojamas i JSON objekta
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException { //metodas. Kreipsimes i URL ir mums is jo grazins JSON
        InputStream is = new URL(url).openStream();
        try { // i try talpinamas kodas, kuriame gali kilti kokia nors isimtis - klaida
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8"))); //nuskaitoma is URL linko
            String jsonText = readAll(rd); //Grazina stringa, padaromas stringas
            JSONObject json = new JSONObject(jsonText); //formuoja JSON objekta
            return json; //grazina JSON
        } finally { //Ivyks ar neivyks klaida, galiausiai viskas bus uzdaryta. Siuo atveju uzdaromi ivedimo srautai
            is.close();
        }
    }

    //metodas, kuris paims JSON objekta ir grazins masyva. Issitraukiame is JSON tik tai, kas mus domina. Nereikalingos info reikia atsikratyti. Isimsim pacia pradzia (meta duomenis) ir pabaigos simbolius
    public static ArrayList<Drinks> getList(JSONArray jsonArray) throws JSONException {//metodo antraste. Grazins sarasa, Corona klases objektu (ArrayList, o ne visa JSON). JSONArray yra klase ir jsonArray yra objektas. ArrayList - vienas is sarasu programavime
        ArrayList<Drinks> drinksList = new ArrayList<Drinks>();
        for (int i = 0; i < jsonArray.length(); i++) { //ciklas, rodo 3 salygas: pradinis iteratorius i
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            //public Drinks(String nameId, String name, String category, String glass, String instructions
            Drinks drinks = new Drinks( // konstruktorius, susideda elemntus. 1 elementas - drinks, todel trauksime drinks
                    jsonObject.getString("idDrink"), //country is mazosios, nes JSON duomenyse irgi is mazosios. Traukiant raktus is JSON, jie turi buti IDENTISKI
                    jsonObject.getString("strDrink"),
                    jsonObject.getString("strCategory"),
                    jsonObject.getString("strGlass"), //visi raktai yra String tipo, eilutes, nors ir grazins int skaiciu
                    jsonObject.getString("strInstructions")
            );
            drinksList.add(drinks); //eis per visus JSON sarase esancius objektus, paims objektus, issitrauks reiksmes, konstruosime corona klases objekta ir prideime i corona sarasa
        }

        return drinksList;
    }

    public static JSONArray getJSONArray(JSONObject jsonObject) throws JSONException {//metodas
        //pasalinama is JSON visa nereikalinga info (meta duomenys), paliekant
        //int jsonLength = jsonObject.toString().length(); //mums reikia tik . Gauname viso JSON ilgi (apie 80 tukst simboliu), grazina ilgio skaiciu
        //String drinksStats = "{" + jsonObject.toString().substring(96, jsonLength) + "}"; //jsonObject konvertuojame i eilute (String), substring - iskerpa dali simboliu is stringo. Iskirps nuo 96-to iki pacio galo

        //String konvertacija i JSON objekta
        //JSONObject jsonObject1 = new JSONObject(drinksStats); //perduodame covid19Stats stringa

        //JSONObject i JSONArray. Sukonstarvome is objekto sarasa, masyva
        JSONArray jsonArray = jsonObject.getJSONArray("drinks");
        return jsonArray; //jsonArray paims getList
    }

    //pagal visa sarasa suformuos tik tra valstybe, kurios mumsreikia
    public static ArrayList<Drinks> getDrinksListByName(ArrayList<Drinks> drinksArrayList, String drinksname) { //metodas, noresim gauti sarasa pagal valstybe. F-ja paims du parametrus - ArrayList, valstybes pav. Grazins arrayList
        ArrayList<Drinks> drinksArrayListbyName = new ArrayList<Drinks>();
        for (Drinks drinks : drinksArrayList) { //desineje puseje bus sukuriamas tos klases objektas, per kurios sarasa iteruojame. Iteruojame per klases objektus
            if (drinks.getName().contains(drinksname)) { //contains metodas (vienas is String metodu) - iesko zodzio dalies. Pradejus rasyti zodi, pradeda ieskoti.
                drinksArrayListbyName.add(drinks);
                System.out.println(drinks);
            }
        }
        return drinksArrayListbyName;
    }
}
