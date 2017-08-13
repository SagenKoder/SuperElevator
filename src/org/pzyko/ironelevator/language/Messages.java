package org.pzyko.ironelevator.language;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.pzyko.ironelevator.ElevatorPlugin;
import org.pzyko.ironelevator.utils.JarUtils;
import org.pzyko.ironelevator.utils.MiscUtil;

public class Messages {

	static Configuration fallbackMessages = null;
	static Configuration messages = null;

	public static void init(String locale) {
		File langDir = new File(ElevatorPlugin.INSTANCE.getDataFolder(), "language/");
		if(!langDir.exists()) langDir.mkdir();
		JarUtils ju = new JarUtils(ElevatorPlugin.INSTANCE);

		try {
			for (String lang : MiscUtil.listFilesinJAR(ju.getJarFile(), "org/pzyko/ironelevator/locales", ".yml")) {
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
		File langDir = new File(ElevatorPlugin.INSTANCE.getDataFolder(), "language/");
		if(!langDir.exists()) langDir.mkdir();
		File wanted = new File(langDir, wantedLocale + ".yml");
		File located = locateMessageFile(wanted);
		if (located == null) {
			throw new Exception("Unknown locale '" + wantedLocale + "'");
		}
		YamlConfiguration conf;
		try {
			conf = MiscUtil.loadYamlUTF8(located);
		} catch (Exception e) {
			throw new Exception("Can't load message file [" + located + "]: " + e.getMessage());
		}

		// ensure that the config we're loading has all of the messages that the fallback has
		// make a note of any missing translations
		if (fallbackMessages != null && conf.getKeys(true).size() != fallbackMessages.getKeys(true).size()) {
			Map<String,String> missingKeys = new HashMap<String, String>();
			for (String key : fallbackMessages.getKeys(true)) {
				if (!conf.contains(key) && !fallbackMessages.isConfigurationSection(key)) {
					conf.set(key, fallbackMessages.get(key));
					missingKeys.put(key, fallbackMessages.get(key).toString());
				}
			}
			conf.set("NEEDS_TRANSLATION", missingKeys);
			try {
				conf.save(located);
			} catch (IOException e) {
				System.out.println("Error: Can't write " + located + ": " + e.getMessage());
			}
		}

		return conf;
	}

	private static File locateMessageFile(File wanted) {
		if (wanted == null) {
			return null;
		}
		if (wanted.isFile() && wanted.canRead()) {
			return wanted;
		} else {
			String basename = wanted.getName().replaceAll("\\.yml$", "");
			if (basename.contains("_")) {
				basename = basename.replaceAll("_.+$", "");
			}
			File actual = new File(wanted.getParent(), basename + ".yml");
			if (actual.isFile() && actual.canRead()) {
				return actual;
			} else {
				return null;
			}
		}
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
