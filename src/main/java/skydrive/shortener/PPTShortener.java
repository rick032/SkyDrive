package skydrive.shortener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import skydrive.IShortener;

/**
 * http://ppt.cc
 * 
 * @author rick
 * 
 */
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
			String prefix = "id=url value=\"";
			String line;
			while ((line = rd.readLine()) != null) {
				int index = line.indexOf(prefix);
				if (index > -1) {
					rd.close();
					connection.disconnect();
					return line.substring(index + prefix.length(), index
							+ prefix.length() + 18);
				}
			}
			rd.close();
			connection.disconnect();
		} catch (Exception e) {
			System.out.println(e.getMessage());			
		}
		return null;
	}

	public static void main(String[] args) {
		PPTShortener s = new PPTShortener();
		System.out.println(s.getShortener("http://tw.yahoo.com"));
	}
}
