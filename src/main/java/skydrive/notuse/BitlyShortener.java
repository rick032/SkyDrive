package skydrive.notuse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import skydrive.IShortener;

public class BitlyShortener implements IShortener {
	String BITLY = "https://api-ssl.bitly.com/";

	@Override
	public String getShortener(String longUrl) {
		try {
			URL auth = new URL("https://bitly.com/oauth/authorize?client_id="
					+ "wertyuix" + "&redirect_uri="
					+ URLEncoder.encode(longUrl, "UTF-8"));
			HttpsURLConnection connection = (HttpsURLConnection) auth
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String result = "";
			String line;
			while ((line = rd.readLine()) != null)
				result += line;

			JSONObject json = new JSONObject(result);

			rd.close();
			return json.getJSONObject("data").getString("url");
		} catch (Exception e) {
			return longUrl;
		}
	}

	public static void main(String[] args) {
		BitlyShortener s = new BitlyShortener();
		System.out
				.println(s
						.getShortener("http://dogbitesme.blogspot.com/2009/01/jquery-ui.html"));
	}
}
