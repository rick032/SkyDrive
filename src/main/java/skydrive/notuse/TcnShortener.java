package skydrive.notuse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import skydrive.IShortener;

/**
 * http://www.waqiang.com/index.php/url/shorten
 * 
 * @author rick
 * 
 */
public class TcnShortener implements IShortener {

	@Override
	public String getShortener(String longUrl) {

		try {
			String data = "{\"url\": \"" + URLEncoder.encode(longUrl, "UTF-8")
					+ "\"}";
			URL url = new URL("http://www.waqiang.com/index.php/url/shorten");
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection
					.setRequestProperty(
							"User-agent",
							"Mozilla/5.0 (Windows; U; Windows NT 6.0; zh-TW; rv:1.9.1.2) "
									+ "Gecko/20090729 Firefox/3.5.2 GTB5 (.NET CLR 3.5.30729)");
			connection
					.setRequestProperty("Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			connection.setRequestProperty("Accept-Language",
					"zh-tw,en-us;q=0.7,en;q=0.3");
			connection.setRequestProperty("Accept-Charse",
					"Big5,utf-8;q=0.7,*;q=0.7");
			connection.setRequestProperty("Origin",
					"http://www.waqiang.com");
			connection.setRequestProperty("Host",
					"www.waqiang.com");
			connection.setRequestProperty("Cookie",
					"PHPSESSID=ovjbd8d9ph1f96m4vk6sq0ave1");
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
			rd.close();
			wr.close();
			String prefix = "<input name=\"url\" id=\"url\" type=\"text\" class=\"input\" readonly=\"readonly\" value=\"";
			int index = result.indexOf(prefix);

			return index > -1 ? result.substring(index + prefix.length(), index
					+ prefix.length() + 18) : null;
		} catch (Exception e) {
			return longUrl;
		}
	}

	public static void main(String[] args) {
		TcnShortener s = new TcnShortener();
		System.out
				.println(s
						.getShortener("http://dogbitesme.blogspot.com/2009/01/jquery-ui.html"));
	}
}
