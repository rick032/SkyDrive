package skydrive;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class PPTShortener implements IShortener {

	@Override
	public String getShortener(String longUrl) {
		try {
			URL url = new URL("http://ppt.cc/gen.php?r=1&t=1&s="
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
			String prefix = "id=url value=\"";
			int index = result.indexOf(prefix);

			return index > -1 ? result.substring(index + prefix.length(), index
					+ prefix.length() + 18) : null;
		} catch (Exception e) {
			return longUrl;
		}
	}

	public static void main(String[] args) {
		PPTShortener s = new PPTShortener();
		System.out
				.println(s
						.getShortener("http://tw.yahoo.com"));
	}
}
