package skydrive;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 
 * @author rick032
 * 
 */
public class SkyDrive {
	public SkyDrive() {
	};

	final String FOLDER_PREFIX = "https://skydrive.live.com/?cid=";
	final String STORAGE_PREFIX = "http://storage.live.com/items/";
	final String RESID_PREFIX = "https://skydrive.live.com/redir?resid=";
	final String RESID = "resid=";
	final String ID = "&id=";
	final String CID = "cid=";
	final String RESOURCE_PREFIX = "<ResourceID>";
	final String RESOURCE_SUFFIX = "</ResourceID>";
	final String NAME_PREFIX = "<RelationshipName>";
	final String NAME_SUFFIX = "</RelationshipName>";

	public String getURLList(String input) {
		int count = 0;
		String path = null;
		if (input.startsWith(RESID_PREFIX)) {
			// ex:https://skydrive.live.com/redir?resid=C4C325ECA88F0B10!152
			path = input.substring(input.indexOf(RESID) + RESID.length());
			path = path.indexOf("&") > 0 ? path.substring(0, path.indexOf("&"))
					: path;
		} else if (input.indexOf(CID) > 0 && input.indexOf(ID) > 0) {
			path = input.substring(input.indexOf(ID) + ID.length());
			path = path.replace("%21", "!");
			path = path.indexOf("&") > 0 ? path.substring(0, path.indexOf("&"))
					: path;
		}
		StringBuffer sb = new StringBuffer();
		if (path == null) {
			return path;
		}
		try {
			URL url = new URL(STORAGE_PREFIX + path);

			URLConnection con = url.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String fileSN = null;
			String line;
			int i = 0;
			while ((line = in.readLine()) != null) {
				if (i == 0 && line.indexOf("xml") == -1) {
					System.out.println(line);
					return null;
				}
				i++;
				int start = line.indexOf(RESOURCE_PREFIX);
				int end = line.indexOf(RESOURCE_SUFFIX);
				int nameStart = line.indexOf(NAME_PREFIX);
				int nameEnd = line.indexOf(NAME_SUFFIX);
				if (end > start && start > -1) {
					fileSN = line.substring(start + RESOURCE_PREFIX.length(),
							end);

				} else if (nameEnd > nameStart && nameStart > -1
						&& fileSN != null) {
					sb.append(STORAGE_PREFIX)
							.append(fileSN)
							.append('|')
							.append(line.substring(
									nameStart + NAME_PREFIX.length(), nameEnd))
							.append("\r\n");
					fileSN = null;
					count++;
				}
			}
			sb.append("total:" + count);

			in.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			sb.append("URL Not Found Exception!!");
			e.printStackTrace();
		}
		return sb.toString();
	}
}
