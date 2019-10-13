package service.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PropertiesConfig {

    private static final String API_KEYS_PROPERTY = "apiKeys";
    private static final String DEFAULT_PROPERTIES_PATH = "/opt/conf/server.properties";


    public List<String> getApiKeys() {

        //Know current path for debugging purposes
//        Path currentRelativePath = Paths.get("");
//        String s = currentRelativePath.toAbsolutePath().toString();
//        System.out.println("Current relative path is: "+ s);

        try (InputStream input = new FileInputStream(DEFAULT_PROPERTIES_PATH)) {

            Properties prop = new Properties();
            // load the properties file
            prop.load(input);
            List<String> serverProperties = new ArrayList<>();
            serverProperties.add(prop.getProperty(API_KEYS_PROPERTY));
            return serverProperties;

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }


}
