import Rate.Structure.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.persistence.*;
import java.beans.Transient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Entity
@Table(name = "Rates")
public class Rate{

       /*static String request = "http://query.yahooapis.com/v1/public/yql?format=json&q=select%20*%20from%20" +
               "yahoo.finance.xchange%20where%20pair%20in%20(\"UAHUSD\",%20\"UAHEUR\",%20\"USDEUR\",%20\"USDUAH\",%20\"EURUSD\",%20\"EURUAH\")&env=store://datatables.org/alltableswithkeys";
*/
       @Id
       @GeneratedValue(strategy = GenerationType.AUTO)
       private long id;

       /*static private final double UAH_TO_USD = getValues()[0];
       static private final double UAH_TO_EUR = getValues()[1];
       static private final double USD_TO_EUR = getValues()[2];
       static private final double USD_TO_UAH = getValues()[3];
       static private final double EUR_TO_USD = getValues()[4];
       static private final double EUR_TO_UAH = getValues()[5];*/
       @Column
       private double UAH_TO_USD = 0.35;
       @Column
       private double UAH_TO_EUR = 0.04;
       @Column
       private double USD_TO_EUR = 1.1094;
       @Column
       private double USD_TO_UAH = 25.662802;
       @Column
       private double EUR_TO_USD = 0.93;
       @Column
       private double EUR_TO_UAH = 27.936526;

       public long getId() {
              return id;
       }

       public void setId(long id) {
              this.id = id;
       }

       public double getUAGToUSD() {
              return UAH_TO_USD;
       }

       public double getUAHToEUR() {
              return UAH_TO_EUR;
       }

       public double getUSDToEUR() {
              return USD_TO_EUR;
       }

       public double getUSDToUAH() {
              return USD_TO_UAH;
       }

       public double getEURToUSD() {
              return EUR_TO_USD;
       }

       public double getEURToUAH() {
              return EUR_TO_UAH;
       }

       public void setUahToUsd(double uahToUsd) {
              UAH_TO_USD = uahToUsd;
       }

       public void setUahToEur(double uahToEur) {
              UAH_TO_EUR = uahToEur;
       }

       public void setUsdToEur(double usdToEur) {
              USD_TO_EUR = usdToEur;
       }

       public void setUsdToUah(double usdToUah) {
              USD_TO_UAH = usdToUah;
       }

       public void setEurToUsd(double eurToUsd) {
              EUR_TO_USD = eurToUsd;
       }

       public void setEurToUah(double eurToUah) {
              EUR_TO_UAH = eurToUah;
       }

/*private static double[] getValues() {     //getting values for all currency exhange rate
              String result = "";
              try {
                     result = performRequest(request);
              } catch (IOException e) {
                     e.printStackTrace();
                     System.out.println("Performing request error");
              }
              Gson gson = new GsonBuilder().create();
              JSON json = (JSON) gson.fromJson(result, JSON.class);

              double[] arr = new double[6];
              for (int i = 0; i < json.query.results.rate.length; i++) {
                     arr[i] = json.query.results.rate[i].Rate;
              }
              return arr;
       }

       private static String performRequest(String urlStr) throws IOException {
              URL url = new URL(urlStr);
              StringBuilder sb = new StringBuilder();

              HttpURLConnection http = (HttpURLConnection) url.openConnection();
              try {
                     BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
                     char[] buf = new char[1000000];

                     int r = 0;
                     do {
                            if ((r = br.read(buf)) > 0)
                                   sb.append(new String(buf, 0, r));
                     } while (r > 0);
              } finally {
                     http.disconnect();
              }

              return sb.toString();
       }*/

}

