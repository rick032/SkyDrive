package skydrive;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class OrztwShortener implements IShortener {

	@Override
	public String getShortener(String longUrl) {
		try {
			URL url = new URL("http://0rz.tw/createget?redirect=1&url="
					+ URLEncoder.encode(longUrl, "UTF-8"));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String result = "";
			String line;
			while ((line = rd.readLine()) != null)
				result += line;
			rd.close();
			int index = result.indexOf("id=\"link\" value=\"");

			return index > -1 ? result.substring(index + 17, index + 36) : null;
		} catch (Exception e) {
			return longUrl;
		}
	}

	public static void main(String[] args) {
		OrztwShortener s = new OrztwShortener();
		System.out
				.println(s
						.getShortener("http://dogbitesme.blogspot.com/2009/01/jquery-ui.html"));
	}
}
