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
        final List<String> list = new ArrayList<>();
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("REPONSE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!: " );
                countDownLatch.countDown();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    System.out.println("REPONSE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!: Success" + transport);
                    String myResponse = response.body().string();
                    System.out.println("REPONSE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!: Success" + myResponse);


                    if (transport.equals("metros")) {

                        try {
                            System.out.println("REPONSE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!: " + myResponse);
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
                        countDownLatch.countDown();
                    } else if (transport.equals("rers")) {

                        try {
                            System.out.println("REPONSE !!!!!!!!!!!!!!!!!!!!! : " );

                            JSONObject object = new JSONObject(myResponse.toString());
                            System.out.println("OBJECT !!!!!!!!!!!!!!!!!!!!! : " + object.toString());
                            JSONObject result = object.getJSONObject("result");
                            System.out.println("RESULT !!!!!!!!!!!!!!!!!!!!! : " + result.toString());
                            JSONArray array = result.getJSONArray("rers");
                            System.out.println("RESULT !!!!!!!!!!!!!!!!!!!!! : " + array.toString());
                            for(int i=0;i<array.length();i++){
                                JSONObject row = array.getJSONObject(i);
                                String codeLigne = row.getString("code");
                                System.out.println("CODE !! : " +codeLigne);
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
                        countDownLatch.countDown();


                    }
                }else{
                    System.out.println("REPONSE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!: ");
                    countDownLatch.countDown();
                }

            }
        };

        String url ="https://api-ratp.pierre-grimaud.fr/v3/lines/"+transport + "?_format=json";
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);

        try {
            countDownLatch.await();
            return list;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return list;
        }


    }

    public List<String> GetStationsFromLine(String line,String transport){

        OkHttpClient okHttpClient = new OkHttpClient();
        final List<String> list = new ArrayList<>();
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("REPONSE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!: " );
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


        String url ="https://api-ratp.pierre-grimaud.fr/v3/stations/"+transport + "/"+ line+ "?_format=json";
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);

        try {
            countDownLatch.await();
            return list;
        } catch (InterruptedException e) {

            e.printStackTrace();
            return list;
        }

    }

    public String getHorraireFromStation(String line,String transport,String station){

        OkHttpClient okHttpClient = new OkHttpClient();
        final String[] schedule = {""};
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println("REPONSE !!!!!!!!!!!!!!!!!!!!!!!!!!!!!: " );
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
                            schedule[0] += "Attente : " + array.getJSONObject(i).getString("message") + "Destination : " + array.getJSONObject(i).getString("destination")+"\n";
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


        String url ="https://api-ratp.pierre-grimaud.fr/v3/schedules" + "/" + transport + "/" + line +"/" + station +"/R" +"?_format=json";
        String url2 ="https://api-ratp.pierre-grimaud.fr/v3/schedules" + "/" + transport + "/" + line +"/" + station +"/A" +"?_format=json";
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);

        Request request2 = new Request.Builder().url(url2).build();
        okHttpClient.newCall(request).enqueue(callback);

        try {
            countDownLatch.await();
            return schedule[0];
        } catch (InterruptedException e) {
            e.printStackTrace();
            return schedule[0];
        }

    }
}
