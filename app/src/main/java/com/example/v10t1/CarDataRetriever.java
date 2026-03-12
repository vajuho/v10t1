package com.example.v10t1;

import android.content.Context;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class CarDataRetriever {
    public ArrayList<CarData> getData(Context context, String area, String year) {
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode areas = null;

        try {
            areas = objectMapper.readTree(new URL("https://statfin.stat.fi/PxWeb/api/v1/en/StatFin/synt/statfin_synt_pxt_12dy.px"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> keys = new ArrayList<>();
        ArrayList<String> values = new ArrayList<>();

        for (JsonNode node : areas.get("variables").get(1).get("values")) {
            values.add(node.asText());
        }
        for (JsonNode node : areas.get("variables").get(1).get("valueTexts")) {
            keys.add(node.asText());
        }

        HashMap<String, String> municipalityCodes = new HashMap<>();

        for(int i = 0; i < keys.size(); i++) {
            municipalityCodes.put(keys.get(i), values.get(i));
        }
        String code = null;
        code = null;
        code = municipalityCodes.get(area);

            try {
                URL url = new URL("https://pxdata.stat.fi:443/PxWeb/api/v1/fi/StatFin/mkan/statfin_mkan_pxt_11ic.px");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-Type", "application/json; utf-8");
                con.setRequestProperty("Accept", "application/json");
                con.setDoOutput(true);

                JsonNode jsonInputString = objectMapper.readTree(context.getResources().openRawResource(R.raw.esimerkkihaku));
                ((ObjectNode) jsonInputString.get("query").get(0).get("selection")).putArray("values").add(code);
                ((ObjectNode) jsonInputString.get("query").get(3).get("selection")).putArray("values").add(year);

                byte[] input = objectMapper.writeValueAsBytes(jsonInputString);
                OutputStream os = con.getOutputStream();
                os.write(input, 0, input.length);

                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                StringBuilder response = new StringBuilder();
                String line = null;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
                JsonNode carData = objectMapper.readTree(response.toString());

                ArrayList<String> carType = new ArrayList<>();
                ArrayList<String> amount = new ArrayList<>();
                ArrayList<String> num = new ArrayList<>();

                carType.add("Henkilöautot");
                carType.add("Pakettiautot");
                carType.add("Kuorma-autot");
                carType.add("Linja-autot");
                carType.add("Erikoisautot");
                carType.add("Yhteensä");
                num.add("01");
                num.add("02");
                num.add("03");
                num.add("04");
                num.add("05");

                for (JsonNode node : carData.get("value")) {
                    amount.add(node.asText());
                }
                ArrayList<CarData> carDataList = new ArrayList<>();

                for(int i = 0; i < carType.size()-1; i++) {
                    int currentAmount = Integer.parseInt(amount.get(i));
                    carDataList.add(new CarData(carType.get(i), currentAmount));
                }
                return carDataList;

        } catch (MalformedURLException e) {
                e.printStackTrace();
        } catch (ProtocolException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }

        return null;
    }
}
