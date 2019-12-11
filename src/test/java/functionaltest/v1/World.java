package functionaltest.v1;

import cucumber.runtime.java.guice.ScenarioScoped;
import functionaltest.v1.model.ImageOptimizerClient;
import functionaltest.v1.model.ResponseImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ScenarioScoped
public class World {
    private final List<Exception> exceptions = new ArrayList<Exception>();
    private final Map<String, ImageOptimizerClient> clients = new HashMap<>();
    private final Map<String, String> apiKeys = new HashMap<>();

    private ResponseImage responseImage;

    private static final String IMAGE_OPTIMIZER_ENDPOINT = "http://172.17.0.1:8080/image-optimizer";

    public void addClient(String reference, ImageOptimizerClient imageOptimizerClient) {
        clients.put(reference, imageOptimizerClient);
    }

    public ImageOptimizerClient getClient(String reference) {
        return clients.get(reference);
    }

    public void addException(Exception exception) {
        exceptions.add(exception);
    }

    public boolean hasException(Class<? extends  Exception> exceptionClass) {
        for (Exception exception : exceptions) {
            if (exceptionClass.isInstance(exception)) {
                return true;
            }
        }
        return false;
    }

    public String getImageOptimizerEndpoint() { return IMAGE_OPTIMIZER_ENDPOINT; }

    public String getApiKey(String clientReference) {
        return apiKeys.get(clientReference  );
    }

    public void setApiKey(String clientReference, String apiKey){
        apiKeys.put(clientReference, apiKey);
    }

    public ResponseImage getResponseImage() {
        return responseImage;
    }

    public void setResponseImage(ResponseImage responseImage) {
        this.responseImage = responseImage;
    }
}

