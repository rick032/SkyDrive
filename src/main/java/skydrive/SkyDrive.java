package skydrive;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

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
	final String RESOURCE = "ResourceID";
	final String RESOURCE_SUFFIX = "</ResourceID>";
	final String RELATIONSHIP_NAME = "RelationshipName";
	final String NAME_SUFFIX = "</RelationshipName>";
	private boolean isShort = true;

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
			@SuppressWarnings("rawtypes")
			List<Class> c = Arrays.asList(getClasses("skydrive.shortener"));
			System.out.println(c.size());
			@SuppressWarnings("rawtypes")
			List<Class> classes = new ArrayList<Class>(c);
			System.out.println("classes:" + classes.size());
			URL url = new URL(STORAGE_PREFIX + path);
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(url);
			@SuppressWarnings("unchecked")
			List<Element> list = document
					.selectNodes("//Folder/Items/Document");

			for (Element element : list) {
				String shortUrl = null;
				int retry = 0;
				String fileSN = STORAGE_PREFIX
						+ element.element(RESOURCE).getText();
				if (isShort) {
					while (shortUrl == null && retry < 5) {

						shortUrl = ((IShortener) classes.get(
								count % classes.size()).newInstance())
								.getShortener(fileSN);
						if (shortUrl == null) {
							classes.remove(count % classes.size());
							retry++;
						}
					}
					if (retry == 5) {
						shortUrl = fileSN;
					}
				}
				System.out.println(fileSN + " : " + shortUrl);
				fileSN = isEmpty(shortUrl) || fileSN.equals(shortUrl) ? fileSN
						: shortUrl;
				sb.append(isShort ? "/Max:" + classes.size() + " " : "")
						.append("/Random ").append(fileSN).append('|')
						.append(element.element(RELATIONSHIP_NAME).getText())
						.append("\r\n");
				count++;
			}

			sb.append("total:" + count);

		} catch (MalformedURLException e) {
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
		} catch (DocumentException e) {
			sb.append("File Not Found Exception!!");
			e.printStackTrace();
		} catch (IOException e) {
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
	@SuppressWarnings("rawtypes")
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
	@SuppressWarnings("rawtypes")
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

	public boolean isEmpty(String s) {
		return "".equals(s) || null == s;
	}
}
