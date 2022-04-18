/**
 *
 *  @author Kozłowski Dawid S23112
 *
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.XML;


public class Service {
    Locale locale;

    public Service(String country) {
        for(Locale locale : Locale.getAvailableLocales()){
            if(country.equals(locale.getDisplayCountry(Locale.ENGLISH))) {
                this.locale = locale;
            }
        }
    }

    public String getWeather(String city){
        String weather=null;
        try {
            URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=2cb120f4fc4b139c48654543778f7b72");
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            weather=jsonObject.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return weather;
    }

    public Double getRateFor(String currency){
        Double rate=null;
        try {
            URL url = new URL("https://api.exchangerate.host/latest");
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
            Double selectedRate=Double.parseDouble(((JSONObject) jsonObject.get("rates")).get(currency).toString());
            Double countryRate=Double.parseDouble(((JSONObject) jsonObject.get("rates")).get(String.valueOf(Currency.getInstance(locale))).toString());
            rate = selectedRate/countryRate;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return rate;
    }

    public Double getNBPRate(){
        Double rate=null;
        if(String.valueOf(Currency.getInstance(locale)).equals("PLN")){
            return 1.0;
        }
        try {
            URL url = new URL("https://www.nbp.pl/kursy/xml/a059z220325.xml");
            String s = null;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"))) {
                String line;
                while((line = in.readLine()) != null)
                    s += line;
            }
            org.json.JSONObject jsonObject = XML.toJSONObject(s);
            var PLNrate= ((org.json.JSONArray)((org.json.JSONObject) jsonObject.get("tabela_kursow")).get("pozycja"));

            for(var cur : PLNrate){
                String kurs=cur.toString();
                if(kurs.contains(String.valueOf(Currency.getInstance(locale)))){
                    org.json.JSONObject obj = (org.json.JSONObject) cur;
                    String strRate =obj.get("kurs_sredni").toString();
                    strRate = strRate.replaceAll(",",".");
                    rate=Double.parseDouble(strRate);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rate;
    }
}  
