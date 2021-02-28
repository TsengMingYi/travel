package com.example.travel;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class TravelManager {
    private static TravelManager instance;
    private ArrayList<TaiwanTravelData> TaiwanTravelDataList = new ArrayList<>();
    private ArrayList<TravelData> extraTravelDataList = new ArrayList<>();
    public void addExtraTravelData(TravelData data){
        TaiwanTravelDataList.get(0).InfoList.removeAll(extraTravelDataList);
        extraTravelDataList.add(data);
        TaiwanTravelDataList.get(0).InfoList.addAll(extraTravelDataList);
    }
    public static TravelManager getInstance(Context context) {
        if (instance == null) {
            instance = new TravelManager(context);
        }
        return instance;
    }
    public ArrayList<TaiwanTravelData> getTaiwanTravelDataList() {
        return TaiwanTravelDataList;
    }
    private TravelManager(Context context){

    }

    public void syncDataFromRemote(final Context context, final Runnable doSomething) {
        ApiHelper.requestApi("", null, new ApiHelper.Callback() {
            @Override
            public void success(String rawString) {
                TravelManager.getInstance(context).loadDataFromRawData(rawString);
                if (doSomething != null) {
                    doSomething.run();
                }
            }

            @Override
            public void fail(Exception e) {
                Log.e("test123", "error: " + e.getMessage());
            }
        });
    }
    public void loadDataFromRawData(String rawData) {
        TaiwanTravelDataList.clear();
        try{
            JSONObject jsonObject2 = new JSONObject(rawData);
                TaiwanTravelData taiwanTravelData = new TaiwanTravelData();
                JSONObject XML_Head = jsonObject2.getJSONObject("XML_Head");
                taiwanTravelData.Listname = XML_Head.getString("Listname");
                taiwanTravelData.Language = XML_Head.getString("Language");
                taiwanTravelData.Orgname = XML_Head.getString("Orgname");
                taiwanTravelData.Updatetime = XML_Head.getString("Updatetime");
                JSONObject Infos = XML_Head.getJSONObject("Infos");
                JSONArray infoArray = Infos.getJSONArray("Info");
                for(int j = 0;j<infoArray.length();j++){
                    TravelData travelData = new TravelData();
                    //TaiwanTravelData taiwanTravelData1 = new TaiwanTravelData();
                    JSONObject jsonObject1 =infoArray.getJSONObject(j);
                    //travelData.Id = jsonObject1.getString("Id");
                    travelData.Name = jsonObject1.getString("Name");
                    travelData.Add = jsonObject1.getString("Add");
                    travelData.Region = jsonObject1.getString("Region");
                    travelData.Town = jsonObject1.getString("Town");
                    travelData.Px = jsonObject1.getString("Px");
                    travelData.Py = jsonObject1.getString("Py");
                    taiwanTravelData.InfoList.add(travelData);
                }


            TravelData travelData = new TravelData();
            travelData.Name = "臺北101";
            travelData.Add = "臺北市信義區信義路五段台北101";
            travelData.Region ="臺北市";
            travelData.Town ="信義區";
            travelData.Px = "121.5623354";
            travelData.Py = "25.0338489";
            taiwanTravelData.InfoList.add(travelData);
//            TravelData travelData1 = new TravelData();
//            travelData1.Name = "象山親山步道";
//            travelData1.Add = "110台北市信義區信義路五段150巷342弄";
//            travelData1.Region ="臺北市";
//            travelData1.Town ="信義區";
//            travelData1.Px = "121.568634";
//            travelData1.Py = "25.0273924";
//            taiwanTravelData.InfoList.add(travelData1);

            taiwanTravelData.InfoList.addAll(extraTravelDataList);
            TaiwanTravelDataList.add(taiwanTravelData);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static class TaiwanTravelData {
        public String XML_Head;
        public String Listname;
        public String Region;
        public String Language;
        public String Orgname;
        public String Updatetime;
        public ArrayList<TravelData> InfoList = new ArrayList<>();

    }
    public static class TravelData implements Serializable {
        public String XML_Head;
        public String Listname;
        public String Language;
        public String Orgname;
        public String Updatetime;
        public String Infos;
        public String Id;
        public String Name;
        public String Zone;
        public String Toldescribe;
        public String Description;
        public String Tel;
        public String Add;
        public String Zipcode;
        public String Region;
        public String Town;
        public String Px;
        public String Py;
        public String Orgclass;

        public TravelData() {
        }
    }
}
