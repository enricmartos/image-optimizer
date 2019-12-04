package service.managers;

import javax.ws.rs.BadRequestException;
import service.config.PropertiesConfig;

import javax.inject.Inject;
import java.util.List;

public class ApiKeyManager {

    @Inject
    private PropertiesConfig propertiesConfig;

    public void validateApiKey(String apiKey) throws BadRequestException {

        List<String> apiKeys = propertiesConfig.getApiKeys();
        if (!apiKeys.contains(apiKey)) {
            throw new BadRequestException("Invalid API key");
//            apiKeys.contains(apiKey);
        }
    }
}
