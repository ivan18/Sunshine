package com.sunshine.ivan18.sunshine.fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sunshine.ivan18.sunshine.MainActivity;
import com.sunshine.ivan18.sunshine.R;
import com.sunshine.ivan18.sunshine.models.Forecasts;
import com.sunshine.ivan18.sunshine.models.TemperatureItem;
import com.sunshine.ivan18.sunshine.models.WeatherItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A placeholder fragment containing a single view.
 */
public class ForecastFragment extends Fragment {

    public  static  String ERROR="com.sunshine.ivan18.sunshine.forecastFragment";
    private ArrayAdapter<String> adapter;
    private ListView listView=null;

    public ForecastFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        listView=(ListView)this.getActivity().findViewById(R.id.listview_forecast);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_forecastfragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.action_refresh){
            FetchWeatherTask task= new FetchWeatherTask();
            task.execute("Roma,it");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        //Populate items forecasts
        List<String> itemsWeather = populateWeather();

        //Set adapter
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textView, itemsWeather);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);


        return rootView;
    }

    private List<String> populateWeather() {
        List<String> items = new ArrayList<String>();
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        String[] weathers = {"Sun", "Cloudy", "Rain", "Snow"};
        String itemContent = null;
        Random rnd = new Random();
        for (int i = 0; i < 20; i++) {
            int rand = (i) % 7;
            itemContent = days[rand] + " - ";
            itemContent += weathers[rnd.nextInt(3)];
            items.add(itemContent);
        }
        return items;
    }

    /**
     * This class provide to call OpenWeatherMap API to retrieve
     * the forecasts
     */
    public class FetchWeatherTask extends AsyncTask<String,String,String>{
        int count=0;


       // ArrayAdapter<String> adapter=null;
        ProgressDialog progress=null;
        @Override
        protected void onPreExecute() {

            //adapter= (ArrayAdapter<String>)listView.getAdapter();
            int adapterLength = adapter.getCount();
            for (int i=0; i < adapterLength; i++){
                adapter.remove(adapter.getItem(0));
            }
            progress = new ProgressDialog(getActivity());
            progress.setMessage("Downloading Forecasts . . . ");
            progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progress.setIndeterminate(false);
            progress.setMax(7);
            progress.show();
        }

        @Override
        protected void onPostExecute(String aVoid) {
            Toast.makeText(getActivity(),"Elementi caricati correttamente", Toast.LENGTH_SHORT).show();
            progress.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            adapter.add(values[0]);
            count++;
            progress.incrementProgressBy(count);
            //Toast.makeText(getActivity(),values[0], Toast.LENGTH_SHORT).show();
            }


        @Override
        protected String doInBackground(String... params) {

            if(params.length==0){
                return null;
            }

            //Declare Http connection variables
            HttpURLConnection connection=null;
            BufferedReader buffered=null;
            //responce
            String forecastsResponce=null;

            String format="json";
            String units="metric";
            int days=7;
            try {
                final String FORECAST_BASE_URL="http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAMS="q";
                final String FORMAT_PARAM="mode";
                final String UNITS_PARAM="units";
                final String DAYS_PARAM="cnt";
                //Set url api
               // URL url= new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=Roma,it&mode=json&units=metric&cnt=7");
                Uri buidUri=Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAMS,params[0])
                        .appendQueryParameter(FORMAT_PARAM,format)
                        .appendQueryParameter(UNITS_PARAM,units)
                        .appendQueryParameter(DAYS_PARAM,Integer.toString(days))
                        .build();
                URL url= new URL(buidUri.toString());
                Log.i(ERROR,url.toString());

                //Open connection
                connection= (HttpURLConnection)url.openConnection();
                //Set request type
                connection.setRequestMethod("GET");
                //conncet
                connection.connect();
                // Read the input stream into a String
                InputStream inputStream = connection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                buffered = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = buffered.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastsResponce = null;
                }
                forecastsResponce = buffer.toString();
                Log.v(ERROR,"JSON responce "+forecastsResponce);

            } catch (MalformedURLException e) {
                Log.e(ERROR,"Malformed URL");
            } catch (IOException e) {
                Log.e(ERROR, "IO Error");
                forecastsResponce=null;
            }finally {
                if(connection != null){
                    connection.disconnect();
                }
                if(buffered != null){
                    try {
                        buffered.close();
                    } catch (IOException e) {
                        Log.e(ERROR, "IO Error on Buffered close");
                    }
                }
            }
            if(forecastsResponce!= null && !(forecastsResponce.trim().equals(""))) {
                List<String> weatherItems = parseJsonResponce(forecastsResponce);
                if (weatherItems.isEmpty()) {
                    Log.e(ERROR, "Lista weatherItems vuota");
                    return null;
                }
                for (String items : weatherItems) {
                    publishProgress(items);
                    SystemClock.sleep(1000);
                }
            }
            return null;
        }
    }

    private List<String> parseJsonResponce(String forecastsResponce){
        List<WeatherItem> weatherItems =new ArrayList<WeatherItem>();
        List<String> itemsString=new ArrayList<String>();

        JSONObject obj= null;
        try {
            obj = new JSONObject(forecastsResponce);
            JSONArray jsonArray=null;
            obj.toJSONArray(jsonArray);

            List<Forecasts> forecastList=new ArrayList<Forecasts>();

            Log.i(ERROR,"JSON PARSED");

            //Get weather forecast list
            JSONArray list=obj.optJSONArray("list");
            for(int x=0; x< list.length(); x++){
                //Get json attributes
                JSONObject itemForecast= (JSONObject)list.get(x);
                JSONObject temp=(JSONObject)itemForecast.getJSONObject("temp");
                JSONArray weather=(JSONArray)itemForecast.getJSONArray("weather");
                //Log.i("JSON PARSE","STEP 1");

                // set new weather item
                WeatherItem wItem= new WeatherItem();
                //set temperature
                wItem.setMax(temp.getString("max"));
                wItem.setMin(temp.getString("min"));

                // set weather details
                wItem.setMain((weather.getJSONObject(0).getString("main")));
                wItem.setDescription((weather.getJSONObject(0).getString("description")));
                wItem.setIcon((weather.getJSONObject(0).getString("icon")));

                weatherItems.add(wItem);
                itemsString.add(wItem.toString());
                Log.i("Item "+x+" : ",wItem.toString());
            }

        } catch (JSONException e) {
            Log.e(ERROR, e.getMessage());
            return null;
        }

        return itemsString;
    }

}