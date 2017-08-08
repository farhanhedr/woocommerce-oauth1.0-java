package authentication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Executer {

    public static String execute(String BASE_URL, String COSTUMER_KEY, String COSTUMER_SECRET){
        String METHORD="GET";//change API method eg POST,PUT, DELETE etc (ONLY FOR THIS EXAMPLE FOR LIB LIKE RETROFIT,OKHTTP, The Are Dynamic Way)

        final String nonce=new TimestampServiceImpl().getNonce();
        final String timestamp=new TimestampServiceImpl().getTimestampInSeconds();

        String firstEncodedString =METHORD+"&"+encodeUrl(BASE_URL);
        String parameterString="oauth_consumer_key="+COSTUMER_KEY+"&oauth_nonce="+nonce+"&oauth_signature_method=HMAC-SHA1&oauth_timestamp="+timestamp+"&oauth_version=1.0";
        String secoundEncodedString="&"+encodeUrl(parameterString);
        String baseString=firstEncodedString+secoundEncodedString;
        String signature=new HMACSha1SignatureService().getSignature(baseString,COSTUMER_SECRET,"");

        signature=encodeUrl(signature);

        final String finalSignature = signature;

        return run(BASE_URL, COSTUMER_KEY, timestamp, nonce, finalSignature);
    }

    public static String run(String BASE_URL, String COSTUMER_KEY, String timestamp, String nonce, String finalSignature) {
        String filterid="filter[categories]=gedgets";
        String parseUrl=BASE_URL+"?&oauth_signature_method=HMAC-SHA1&oauth_consumer_key="+COSTUMER_KEY+"&oauth_version=1.0&oauth_timestamp="+timestamp+"&oauth_nonce="+nonce+"&oauth_signature="+ finalSignature;

        return getJSON(parseUrl);
    }

    public static String encodeUrl(String url)
    {
        String encodedurl="";
        try {
            encodedurl = URLEncoder.encode(url,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encodedurl;
    }

    public static String getJSON(String url) {
        HttpURLConnection c = null;
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.connect();

            int status = c.getResponseCode();

            System.out.println("status: "+status);
            switch (status) {
                case 200:
                case 401:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    //System.out.println("Response: "+sb.toString());

                    return sb.toString();
            }

        } catch (MalformedURLException ex) {
            System.out.println("MalformedURLException:"+ex);
        } catch (IOException ex) {
            System.out.println("IOException: "+ex);
        } finally {
            if (c != null) {
                try {
                    c.disconnect();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }
        }
        return "failed";
    }

}