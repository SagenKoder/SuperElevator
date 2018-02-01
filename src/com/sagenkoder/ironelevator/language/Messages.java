package com.sagenkoder.ironelevator.language;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.google.common.base.Joiner;
import com.sagenkoder.ironelevator.ElevatorPlugin;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;

public class Messages {

	static Configuration fallbackMessages = null;
	static Configuration messages = null;

	public static void init(String jarLocalePackage, String locale) {
		File langDir = new File(ElevatorPlugin.INSTANCE.getDataFolder(), "language/");
		if(!langDir.exists()) langDir.mkdir();
		JarExtractor ju = new JarExtractor(ElevatorPlugin.INSTANCE);

		try {
			for (String lang : listFilesinJAR(ju.getJarFile(), jarLocalePackage.replace('.', File.pathSeparatorChar), ".yml")) {
				ju.extractResource(lang, langDir);
			}
		} catch (IOException e) {
			System.out.println("can't determine message files to extract!");
			e.printStackTrace();
		}

		try {
			fallbackMessages = loadMessageFile("default");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: can't load fallback messages file!");
		}

		try {
			setMessageLocale(locale);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: can't load messages for " + locale + ": using default");
			messages = fallbackMessages;
		}
	}

	public static void setMessageLocale(String wantedLocale) throws Exception {
		messages = loadMessageFile(wantedLocale);
	}

	private static Configuration loadMessageFile(String wantedLocale) throws Exception {
		// TODO Use Configuration class to load
		return null;
	}

	private static String getString(Configuration conf, String key) {
		String s = null;
		Object o = conf.get(key);
		if (o instanceof String) {
			s = o.toString();
		} else if (o instanceof List<?>) {
			@SuppressWarnings("unchecked")
			List<String> l = (List<String>) o;
			s = Joiner.on("\n").join(l);
		}
		return s;
	}
	
    private static String[] listFilesinJAR(File jarFile, String path, String ext) throws IOException {
        ZipInputStream zip = new ZipInputStream(new FileInputStream(jarFile));
        ZipEntry ze;

        List<String> list = new ArrayList<String>();
        while ((ze = zip.getNextEntry()) != null ) {
            String entryName = ze.getName();
            if (entryName.startsWith(path) && ext != null && entryName.endsWith(ext)) {
                list.add(entryName);
            }
        }
        zip.close();

        return list.toArray(new String[list.size()]);
    }

	public static String getString(String key) {
		if (messages == null) {
			System.out.println("Warning: No messages catalog!?!");
			return "!" + key + "!";
		}
		String s = getString(messages, key);
		if (s == null) {
			System.out.println("Warning: Missing message key '" + key + "'");
			s = getString(fallbackMessages, key);
			if (s == null) {
				s = "!" + key + "!";
			}
		}
		return ChatColor.translateAlternateColorCodes('&', s);
	}

	public static String getString(String key, Object...args) {
		String s = getString(key);
		for(int i = 0; i < args.length; i++) {
			s = s.replaceAll("%" + i, args[i].toString());
		}
		return s;
	}

}
