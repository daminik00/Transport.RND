package ru.daminik00.transportrnd;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity implements OnMapReadyCallback {
    public GoogleMap googleMap;
    private String MAP_ERROR = "MAP_ERROR";

    double lat = 47.222531;
    double lng = 39.718705;

    public int nowTr = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        createMapView();
        findViewById(R.id.Troll).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Log.i("Long", "Long");
                return false;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        new GetPoint(googleMap).execute();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        if (googleMap != null) {
            final double latitude = 47.222531;
            final double longitude = 39.718705;
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude));
            googleMap.moveCamera(center);
            googleMap.setMinZoomPreference(15);

            findViewById(R.id.traffic).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button btn = (Button) v;
                    googleTraffic(googleMap, btn);
                }
            });
//            new GetFile().execute();
            new GetPoint(googleMap).execute();
        } else {
            Log.d(MAP_ERROR, "Map is NULL");
        }
    }

    public void chengeTr(View v) {
        Button bt = (Button) v;
        if (bt.getText().toString().equals("Bus")) {
            this.nowTr = 0;
        } else if (bt.getText().toString().equals("Minibus")) {
            this.nowTr = 1;
        } else if (bt.getText().toString().equals("Tram")) {
            this.nowTr = 3;
        } else if (bt.getText().toString().equals("Troll")) {
            this.nowTr = 2;
        }
        Log.d("TR", String.valueOf(this.nowTr));
    }

    private void createMapView() {
        try {
            if (null == googleMap) {
                ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                CameraPosition googlePlex = CameraPosition.builder()
                        .target(new LatLng(lat, lng))
                        .zoom(15)
                        .bearing(0)
                        .tilt(45)
                        .build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(googlePlex));
                if (null == googleMap) {
                    Toast.makeText(getApplicationContext(), "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception) {
            Log.e(MAP_ERROR, exception.toString());
        }
    }

    public void googleTraffic(GoogleMap googleMap, Button btn) {
        if (googleMap.isTrafficEnabled()) {
            googleMap.setTrafficEnabled(false);
            btn.setBackgroundResource(R.drawable.traffic_off);
        } else {
            googleMap.setTrafficEnabled(true);
            btn.setBackgroundResource(R.drawable.traffic_on);
        }
    }

    public LatLng cornerleftBut(GoogleMap googleMap) {
        LatLng map;
        Point p = new Point(0, 0);
        map = googleMap.getProjection().fromScreenLocation(p);
        return map;
    }


    private class GetTransport extends AsyncTask<Void, Void, Void> {

        private LatLng center;
        private LatLng leftBut;
        private GoogleMap map;


        private HttpURLConnection urlConnection = null;
        private BufferedReader reader = null;
        private String resultJson = "";
        private boolean st = true;

        private boolean nc = false;
        private TransportData miniBusData;

        private JSONArray Minibus;
        private JSONArray Bus;
        private JSONArray Trolleybus;
        private JSONArray Tram;

        private GetTransport(LatLng center, LatLng leftBut, GoogleMap map) {
            this.center = center;
            this.leftBut = leftBut;
            this.map = map;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            try {
                this.leftBut = MainActivity.this.cornerleftBut(this.map);
                this.center = map.getCameraPosition().target;
                switch (MainActivity.this.nowTr) {
                    case 0:
                        if (this.nc) {
                            Map<String, ArrayList> minibusMap = this.getMap(this.Bus, false, 0);
                            ArrayList<String> minibusNumberArray = this.getArray(this.Bus);
                            this.miniBusData.setData(minibusMap, minibusNumberArray);
                        } else {
                            Map<String, ArrayList> minibusMap = this.getMap(this.Bus, true, 0);
                            ArrayList<String> minibusNumberArray = this.getArray(this.Bus);
                            this.miniBusData = new TransportData(minibusMap, minibusNumberArray, this.map);
                            this.nc = true;
                        }
                        break;
                    case 1:
                        if (this.nc) {
                            Map<String, ArrayList> minibusMap = this.getMap(this.Minibus, false, 1);
                            ArrayList<String> minibusNumberArray = this.getArray(this.Minibus);
                            this.miniBusData.setData(minibusMap, minibusNumberArray);
                        }
                        break;
                    case 2:
                        if (this.nc) {
                            Map<String, ArrayList> minibusMap = this.getMap(this.Trolleybus, false, 2);
                            ArrayList<String> minibusNumberArray = this.getArray(this.Trolleybus);
                            this.miniBusData.setData(minibusMap, minibusNumberArray);
                        }
                        break;
                    case 3:
                        if (this.nc) {
                            Map<String, ArrayList> minibusMap = this.getMap(this.Tram, false, 3);
                            ArrayList<String> minibusNumberArray = this.getArray(this.Tram);
                            this.miniBusData.setData(minibusMap, minibusNumberArray);
                        }
                        break;
                    default:
                        Map<String, ArrayList> minibusMap = this.getMap(this.Minibus, false, 1);
                        ArrayList<String> minibusNumberArray = this.getArray(this.Minibus);
                        this.miniBusData.setData(minibusMap, minibusNumberArray);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                while (this.st) {
                    String urlString = "https://datrrnd.herokuapp.com/getTransport?leftBut=" + this.leftBut.latitude + "::" + this.leftBut.longitude + "&center=" + this.center.latitude + "::" + this.center.longitude;
                    URL url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuilder buffer = new StringBuilder();

                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }

                    resultJson = buffer.toString();
                    Log.d("STRING", resultJson);
                    JSONObject json = new JSONObject(resultJson);
                    JSONArray Minibus = new JSONArray(json.get("Minibus").toString());
                    JSONArray Bus = new JSONArray(json.get("Bus").toString());
                    JSONArray Trolleybus = new JSONArray(json.get("Trolleybus").toString());
                    JSONArray Tram = new JSONArray(json.get("Tram").toString());

                    switch (MainActivity.this.nowTr) {
                        case 0:
                            this.Bus = Bus;
                            break;
                        case 1:
                            this.Minibus = Minibus;
                            break;
                        case 2:
                            this.Trolleybus = Trolleybus;
                            break;
                        case 3:
                            this.Tram = Tram;
                            break;
                        default:
                            this.Minibus = Minibus;
                            break;
                    }
                    urlConnection.disconnect();
                    Thread.sleep(100);
                    publishProgress();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(MAP_ERROR, e.getMessage());
            }
            return null;
        }


        private Map<String, ArrayList> getMap(JSONArray jsonArray, boolean tf, int type) throws JSONException {
            Map<String, ArrayList> map = new HashMap<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jo = ((JSONObject) (jsonArray.get(i)));
                ArrayList<Object> arrayList = new ArrayList<>();
                int inc = Integer.parseInt(String.valueOf(jo.get("incline")));
                int cc;

                String speed = String.valueOf(jo.get("speed"));
                String number = String.valueOf(jo.get("number"));
                String route = String.valueOf(jo.get("route"));
                double lat = Double.valueOf(String.valueOf(jo.get("lat")));
                double lng = Double.valueOf(String.valueOf(jo.get("lng")));
                switch (type) {
                    case 0:
                        if (speed.equals("-1") || speed.equals("0"))
                            cc = Color.argb(150, 206, 147, 216);
                        else cc = Color.rgb(255, 112, 67);
                        break;
                    case 1:
                        if (speed.equals("-1") || speed.equals("0"))
                            cc = Color.argb(150, 41, 182, 246);
                        else cc = Color.rgb(41, 182, 246);
                        break;
                    case 2:
                        if (speed.equals("-1") || speed.equals("0"))
                            cc = Color.argb(150, 0, 230, 118);
                        else cc = Color.rgb(0, 230, 118);
                        break;
                    case 3:
                        if (speed.equals("-1") || speed.equals("0"))
                            cc = Color.argb(150, 206, 147, 216);
                        else cc = Color.rgb(206, 147, 216);
                        break;
                    default:
                        cc = Color.MAGENTA;
                        break;
                }

                arrayList.add(jo);

                if (tf) {
                    arrayList.add(this.map.addMarker(this.getMarkerOptions(new LatLng(lat, lng), route + ", Number: " + number + ", Speed: " + speed, route, inc, cc)));
                } else {
                    arrayList.add(this.getMarkerOptions(new LatLng(lat, lng), route + ", Number: " + number + ", Speed: " + speed, route, inc, cc));
                }
                map.put((String) ((JSONObject) (jsonArray.get(i))).get("number"), arrayList);
            }
            return map;
        }

        private ArrayList<String> getArray(JSONArray jsonArray) throws JSONException {
            ArrayList<String> map = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                map.add((String) ((JSONObject) (jsonArray.get(i))).get("number"));
            }
            return map;
        }

        private MarkerOptions getMarkerOptions(LatLng cordination, String title, String number, int inc, int cc) {

            BitmapDescriptor icon = new MarkerIcon(cc, number, inc).getIcon();
            MarkerOptions marker = new MarkerOptions()
                    .position(cordination)
                    .title(title);
            marker.anchor(0.5f, 0.5f);
            marker.icon(icon);

            return marker;
        }
    }


    private class GetPoint extends AsyncTask<Void, Void, LatLng> {
        private GoogleMap map;
        private boolean stop = true;

        private GetPoint(GoogleMap map) {
            this.map = map;
        }

        @Override
        protected void onPostExecute(LatLng latLng) {
            super.onPostExecute(latLng);
        }

        @Override
        protected LatLng doInBackground(Void... params) {
            while (stop) {
                publishProgress();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            LatLng ll = MainActivity.this.cornerleftBut(this.map);
            if (ll.latitude > 43.0 && ll.latitude < 48.0) {
                this.stop = false;
                new GetTransport(this.map.getCameraPosition().target, ll, this.map).execute();
            }
        }
    }
}