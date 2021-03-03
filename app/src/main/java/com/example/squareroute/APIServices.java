package com.example.squareroute;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APIServices {

    public List<String> getLinesFromTransport(final String transport){

        OkHttpClient okHttpClient = new OkHttpClient();

        final List<String> list = new ArrayList<String>();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        List<String> newList = new ArrayList<String>();
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                countDownLatch.countDown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    String myResponse = response.body().string();

                    if (transport.equals("metros")) {

                        try {
                            JSONObject object = new JSONObject(myResponse);
                            JSONObject result = object.getJSONObject("result");
                            JSONArray array = result.getJSONArray("metros");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject row = array.getJSONObject(i);
                                String numeroLigne = row.getString("code");
                                list.add(numeroLigne);
                            }
                            countDownLatch.countDown();
                        } catch (JSONException e) {
                            countDownLatch.countDown();
                            e.printStackTrace();
                            countDownLatch.countDown();
                        }


                    } else if (transport.equals("rers")) {
                        try {

                            JSONObject object = new JSONObject(myResponse.toString());
                            JSONObject result = object.getJSONObject("result");
                            JSONArray array = result.getJSONArray("rers");

                            for(int i=0;i<array.length();i++){
                                JSONObject row = array.getJSONObject(i);
                                String codeLigne = row.getString("code");
                                if(codeLigne.equals("A") | codeLigne.equals("B") | codeLigne.equals("E")){
                                    list.add(codeLigne);
                                }

                            }
                            countDownLatch.countDown();
                        } catch (JSONException e) {
                            countDownLatch.countDown();
                            e.printStackTrace();
                            countDownLatch.countDown();
                        }



                    } else if (transport.equals("buses")){
                        try {
                            JSONObject object = new JSONObject(myResponse);
                            JSONObject result = object.getJSONObject("result");
                            JSONArray array = result.getJSONArray("buses");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject row = array.getJSONObject(i);
                                String numeroLigne = row.getString("code");
                                list.add(numeroLigne);
                            }
                            countDownLatch.countDown();
                        } catch (JSONException e) {
                            countDownLatch.countDown();
                            e.printStackTrace();
                            countDownLatch.countDown();
                        }


                    } else if (transport.equals("tramways")){
                        try {
                            JSONObject object = new JSONObject(myResponse);
                            JSONObject result = object.getJSONObject("result");
                            JSONArray array = result.getJSONArray("tramways");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject row = array.getJSONObject(i);
                                String numeroLigne = row.getString("code");
                                list.add(numeroLigne);

                            }
                            countDownLatch.countDown();

                        } catch (JSONException e) {
                            countDownLatch.countDown();
                            e.printStackTrace();
                            countDownLatch.countDown();

                        }

                    }
                }else{

                }

            }
        };

        String url ="https://api-ratp.pierre-grimaud.fr/v4/lines/"+transport + "?_format=json";
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);


        try {
            countDownLatch.await();
            newList = removeDuplicates((ArrayList<String>) list);
            return newList;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return newList;
        }


    }

    public List<String> GetStationsFromLine(String line,String transport){

        OkHttpClient okHttpClient = new OkHttpClient();
        final List<String> list = new ArrayList<>();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        List<String> newList = new ArrayList<String>();

        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                countDownLatch.countDown();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {

                    String myResponse = response.body().string();
                    try {
                        JSONObject object = new JSONObject(myResponse);
                        JSONObject result = object.getJSONObject("result");
                        JSONArray array = result.getJSONArray("stations");
                        for(int i=0;i<array.length();i++){
                            list.add(array.getJSONObject(i).getString("name"));
                        }
                        countDownLatch.countDown();
                    } catch (JSONException e) {
                        countDownLatch.countDown();
                        e.printStackTrace();
                    }


                }
                else{

                    countDownLatch.countDown();
                }

            }
        };


        String url ="https://api-ratp.pierre-grimaud.fr/v4/stations/"+transport + "/"+ line+ "?_format=json";
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);

        try {
            countDownLatch.await();
            newList = removeDuplicates((ArrayList<String>) list);
            return newList;
        } catch (InterruptedException e) {

            e.printStackTrace();
            newList = removeDuplicates((ArrayList<String>) list);
            return newList;
        }

    }

    public String getHorraireFromStation(String line,String transport,String station){

        OkHttpClient okHttpClient = new OkHttpClient();
        final String[] schedule = {""};
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                countDownLatch.countDown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {

                    String myResponse = response.body().string();
                    try {
                        JSONObject object = new JSONObject(myResponse);
                        JSONObject result = object.getJSONObject("result");
                        JSONArray array = result.getJSONArray("schedules");
                        for(int i=0;i<array.length();i++){
                            schedule[0] += "Attente : " + array.getJSONObject(i).getString("message") + "\n" + "Destination : " + array.getJSONObject(i).getString("destination")+"\n"+"\n";
                        }
                        countDownLatch.countDown();
                    } catch (JSONException e) {
                        countDownLatch.countDown();
                        e.printStackTrace();
                    }


                }
                else{
                    countDownLatch.countDown();
                }

            }
        };


        String url ="https://api-ratp.pierre-grimaud.fr/v4/schedules" + "/" + transport + "/" + line +"/" + station +"/A%2BR" +"?_format=json";
        Request request = new Request.Builder().url(url).build();
        countDownLatch.countDown();
        okHttpClient.newCall(request).enqueue(callback);

        try {
            countDownLatch.await();
            return schedule[0];
        } catch (InterruptedException e) {
            e.printStackTrace();
            return schedule[0];
        }
    }

    public ArrayList<String> removeDuplicates(ArrayList<String> list)
    {
        ArrayList<String> newList = new ArrayList<String>();


        for (String element : list) {


            if (!newList.contains(element)) {

                newList.add(element);
            }
        }
        return newList;
    }
}
