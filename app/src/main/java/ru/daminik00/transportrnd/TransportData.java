package ru.daminik00.transportrnd;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

class TransportData {

    private Map<String, ArrayList> hMap;
    private Map newMap;
    private GoogleMap googleMap;
    private ArrayList<String> arrayList;
    private ArrayList<String> newArrayList;

//    private ArrayList dataForAdd;

    TransportData(Map<String, ArrayList> hMap, ArrayList<String> array, GoogleMap map) {
        this.hMap = hMap;
        this.arrayList = array;
        this.googleMap = map;
    }

    void setData(Map map, ArrayList<String> array) {
        this.newMap = map;
        this.newArrayList = array;
        this.compareData();
    }


    private void compareData() {
        ArrayList<String> dataForDel = new ArrayList<>();
        ArrayList<String> dataForSave = new ArrayList<>();
//        this.dataForAdd = new ArrayList();

        for (int i = 0; i < this.arrayList.size(); i++) {
            if (this.newArrayList.indexOf(this.arrayList.get(i)) == -1) {
                dataForDel.add(this.arrayList.get(i));
            } else {
                dataForSave.add(this.arrayList.get(i));
            }
        }

        for (int i = 0; i < newArrayList.size() ; i++) {
             if (hMap.get(newArrayList.get(i)) == null) {
                 ArrayList<Object> arrayList = new ArrayList<>();
                 JSONObject jo = ((JSONObject)((ArrayList)this.newMap.get(newArrayList.get(i))).get(0));
                 Marker mk = this.googleMap.addMarker ((MarkerOptions) ((ArrayList) this.newMap.get(newArrayList.get(i))).get(1));
                 arrayList.add(jo);
                 arrayList.add(mk);
                 hMap.put(newArrayList.get(i), arrayList);
             }
        }
        this.arrayList = this.newArrayList;

        for (int i = 0; i < dataForDel.size(); i++) {
            ArrayList nA = this.hMap.get(dataForDel.get(i));
            if (nA != null) {
                delMarker((Marker)nA.get(1), dataForDel.get(i));
            }
        }

        for (int i = 0; i < dataForSave.size(); i++) {
            MarkerOptions mo = (MarkerOptions) (((ArrayList) this.newMap.get(dataForSave.get(i))).get(1));
            ((Marker)((hMap.get(dataForSave.get(i))).get(1))).setPosition(mo.getPosition());
            ((Marker)((hMap.get(dataForSave.get(i))).get(1))).setTitle(mo.getTitle());
            ((Marker)((hMap.get(dataForSave.get(i))).get(1))).setIcon(mo.getIcon());
        }

        for (int i = 0; i < dataForDel.size(); i++) {
            this.hMap.remove(dataForDel.get(i));
        }
    }

    private void delMarker(Marker marker, String number) {
        this.hMap.remove(number);
        marker.remove();
    }

}
