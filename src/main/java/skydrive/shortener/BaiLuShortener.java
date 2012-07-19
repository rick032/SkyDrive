package skydrive.shortener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONObject;

import skydrive.IShortener;

/**
 * http://bai.lu/api_help
 * 
 * @author rick
 * 
 */
public class BaiLuShortener implements IShortener {

	@Override
	public String getShortener(String longUrl) {
		try {
			URL url = new URL("http://bai.lu/api?url="
					+ URLEncoder.encode(longUrl, "UTF-8"));
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String result = "";
			String line;
			while ((line = rd.readLine()) != null)
				result += line;
			rd.close();
			JSONObject json = new JSONObject(result);
			return json.getString("url");
		} catch (Exception e) {
			e.printStackTrace();
			return longUrl;
		}
	}

	public static void main(String[] args) {
		BaiLuShortener s = new BaiLuShortener();
		System.out
				.println(s
						.getShortener("http://dogbitesme.blogspot.com/2009/01/jquery-ui.html"));
	}
}
