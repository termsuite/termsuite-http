package fr.univnantes.termsuite.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.univnantes.termsuite.model.Lang;

public class TermSuiteHttpConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(TermSuiteHttpConfig.class);
	
	private static final String JVM_ARG_PROPERTY_FILE="TermSuiteHttpConfig";
	
	private static Properties _properties = null;
	public static Properties properties() {
		if(_properties == null) {
			String propertyFile = System.getProperty(JVM_ARG_PROPERTY_FILE, "termsuite-http.properties");
			Properties prop = new Properties();
			if(new File(propertyFile).isFile()) {
				try (InputStream in = new FileInputStream(propertyFile)) {
					prop.load(in);
					LOGGER.info("TermSuiteHttp config: {}", prop);					
					_properties = prop;
				} catch (IOException e) {
					LOGGER.error("Could not read property file {}", propertyFile, e);
				}
			} else {
				throw new RuntimeException("Could not find any termsuite-http.properties file.");
			}				
			_properties = prop;
		}
		return _properties;
	}

	private static final String P_TAGGER_PATH="tagger.path";
	private static final String P_MAX_REQUEST_PER_SERVICE="termsuite.preprocessor.max_requests";
	private static final String P_LANG="termsuite.lang";
	private static final String P_HTTP_PORT="http.port";

	
	public static Path getTaggerPath() {
		return Paths.get(getProperty(P_TAGGER_PATH));
	}

	public static String getProperty(String p, String defaultValue) {
		return properties().getProperty(p, defaultValue);
	}
	
	public static String getProperty(String p) {
		String property = properties().getProperty(p);
		if(property == null)
			throw new RuntimeException("No such property in onfiguration file: " + p);
		return property;
	}

	public static Lang getLang() {
		return Lang.forName(getProperty(P_LANG, Lang.EN.getCode()));
	}

	public static int getMaxRequests() {
		return Integer.parseInt(getProperty(P_MAX_REQUEST_PER_SERVICE, "1000"));
	}

	public static int getHttpPort() {
		return Integer.parseInt(getProperty(P_HTTP_PORT, "4567"));
	}
}
