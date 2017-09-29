package com.example.simrandeep.invoicemaker;



import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class NetworkResponse{

    static private String username="shivanibh";
    static private String API_Key="5229354a-66c9-11e7-94da-0200cd936042";
    public static URL buildUrlCountry() {
        URL url = null;
        try {
            url = new URL("http://api.geonames.org/countryInfoJSON?formatted=true&lang=en&style=full&username="+username);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrlState(Integer id) {
        URL url = null;
        try {
            url = new URL("http://api.geonames.org/childrenJSON?geonameId="+id.toString()+"&username="+username);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrl(String phonenumber) {
        URL url = null;
        try {
            url = new URL("http://2factor.in/API/V1/"+API_Key+"/SMS/"+phonenumber+"/AUTOGEN");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            int code=urlConnection.getResponseCode();
            if (code < HttpURLConnection.HTTP_BAD_REQUEST) {
                InputStream in = urlConnection.getInputStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            } else {
                InputStream in = urlConnection.getErrorStream();

                Scanner scanner = new Scanner(in);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    return scanner.next();
                } else {
                    return null;
                }
            }

        } finally {
            urlConnection.disconnect();
        }
    }
    public static ObjectCountry parseJSON(String  Results) throws Exception
    {       HashMap<String,Integer> hash=new HashMap<>();
        ArrayList<String> s=new ArrayList<>();
        String name=null;Integer id;
        JSONObject json = new JSONObject(Results);
        JSONArray jsonMainArr = json.getJSONArray("geonames");
        for (int i = 0; i < jsonMainArr.length(); i++) {
            JSONObject childJSONObject = jsonMainArr.getJSONObject(i);
            name=childJSONObject.getString("countryName");
            s.add(name);
            hash.put(childJSONObject.getString("countryName"),Integer.parseInt(childJSONObject.getString("geonameId")));


        }
        return new ObjectCountry(hash,s);
    }
    public static ArrayList<String> parseJSONStates(String  Results) throws Exception
    {   ArrayList<String> s=new ArrayList<>();
        String name=null;
        JSONObject json = new JSONObject(Results);
        JSONArray jsonMainArr = json.getJSONArray("geonames");
        for (int i = 0; i < jsonMainArr.length(); i++) {
            JSONObject childJSONObject = jsonMainArr.getJSONObject(i);
            name=childJSONObject.getString("name");
            if(name.equals("NCT"))
            {name="New Delhi";}
            s.add(name);
        }
        return s;

    }
    public static class ObjectCountry{
        public HashMap<String,Integer> hash;
        public ArrayList<String> string;
        public ObjectCountry(HashMap<String, Integer> h, ArrayList<String> s)
        {
            hash=h;
            string=s;
        }
    }
}

