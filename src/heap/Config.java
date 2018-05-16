package heap;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class Config {

	static Configurations configs = new Configurations();
	static Configuration config;
	static String configFile = "heapdiff.props";
	 
	static void load() {
		try {

			config = configs.properties(new File(configFile));
			/*
			 * Parameters params = new Parameters();
			 * FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
			 * new FileBasedConfigurationBuilder<FileBasedConfiguration>(
			 * PropertiesConfiguration.class)
			 * .configure(params.properties().setFileName("heapdiff.props")
			 * .setListDelimiterHandler(new DefaultListDelimiterHandler(',')));
			 * config = builder.getConfiguration();
			 */

		} catch (ConfigurationException cex) {
			System.err.println(cex);
			System.exit(0);
		}
	}

	static List<String> getBaselineHistos() {
		return config.getList(String.class, "BaselineHistos");
	}

	static String getSubjectHisto() {
		return config.getString("SubjectHisto");
	}

	static String getExcludes() {
		String regex = config.getString("Excludes");
		// Pattern pat = Pattern.compile(regex);
		return regex;
	}

	static double getLittlesRatio() {
		return config.getDouble("LittlesRatio");
	}

	static String getRatioExportPath() {
		String path = config.getString("RatioOutput");
		return path;
	}

	public static void main(String[] args) {
		int c = 0;
		for (String s : getBaselineHistos()) {
			System.out.println(++c + ": " + s);
		}
		Pattern pat = Pattern.compile(getExcludes());
		System.out.println(pat);
		String cls = "com.sun.j2ee.descriptor.LoginConfigBean";
		System.out.println(cls.matches(getExcludes()));
		System.out.println("LittlesRatio: " + getLittlesRatio());

	}
}