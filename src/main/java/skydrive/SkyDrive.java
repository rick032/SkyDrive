package skydrive;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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
	private boolean isShort = true;

	// private String[] shorteners = new String[] { "BaiLuShortener",
	// "IsgdShortener", "OrztwShortener", "PPTShortener",
	// "TinyurlShortener" };

	public boolean isShort() {
		return isShort;
	}

	public void setShort(boolean isShort) {
		this.isShort = isShort;
	}

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
			Class[] classes = getClasses("skydrive.shortener");
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
					fileSN = STORAGE_PREFIX
							+ line.substring(start + RESOURCE_PREFIX.length(),
									end);
					String shortUrl = null;
					if (isShort) {
						shortUrl = ((IShortener) classes[count % 5]
								.newInstance()).getShortener(fileSN);
					}
					System.out.println(fileSN + " : " + shortUrl);
					fileSN = fileSN.equals(shortUrl) ? fileSN : shortUrl;

				} else if (nameEnd > nameStart && nameStart > -1
						&& fileSN != null) {
					sb.append(isShort ? "/Max:5 " : "")
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
			sb.append("File Not Found Exception!!");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * Scans all classes accessible from the context class loader which belong
	 * to the given package and subpackages.
	 * 
	 * @param packageName
	 *            The base package
	 * @return The classes
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private static Class[] getClasses(String packageName)
			throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * Recursive method used to find all classes in a given directory and
	 * subdirs.
	 * 
	 * @param directory
	 *            The base directory
	 * @param packageName
	 *            The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class> findClasses(File directory, String packageName)
			throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file,
						packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName
						+ '.'
						+ file.getName().substring(0,
								file.getName().length() - 6)));
			}
		}
		return classes;
	}
}
