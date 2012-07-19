package skydrive;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

/**
 * 
 * @author rick
 *
 */
public class GoogleShortener implements IShortener {
	@Override
	public String getShortener(String longUrl) {
		String data = "{\"longUrl\": \"" + longUrl + "\"}";

		try {
			URL url = new URL(
					"https://www.googleapis.com/urlshortener/v1/url?key=AIzaSyB8xe9FS1-PxTu8dijn2dxrUiC6JNo5ClI");
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "application/json");

			OutputStreamWriter wr = new OutputStreamWriter(
					connection.getOutputStream());
			wr.write(data);
			wr.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String result = "";
			String line;
			while ((line = rd.readLine()) != null)
				result += line;
			JSONObject json = new JSONObject(result);
			wr.close();
			rd.close();
			return (String) json.get("id");
		} catch (Exception e) {
			return longUrl;
		}
	}
}