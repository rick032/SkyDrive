package skydrive.shortener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import skydrive.IShortener;

/**
 * http://is.gd/
 * 
 * @author rick
 * 
 */
public class IsgdShortener implements IShortener {

	@Override
	public String getShortener(String longUrl) {
		try {
			URL url = new URL("http://is.gd/create.php?format=simple&url="
					+ URLEncoder.encode(longUrl, "UTF-8"));
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String result = "";
			String line;
			while ((line = rd.readLine()) != null)
				result += line;
			rd.close();

			return result;
		} catch (Exception e) {
			e.fillInStackTrace();
			return longUrl;
		}
	}

	public static void main(String[] args) {
		IsgdShortener s = new IsgdShortener();
		System.out
				.println(s
						.getShortener("http://dogbitesme.blogspot.com/2009/01/jquery-ui.html"));
	}
}
