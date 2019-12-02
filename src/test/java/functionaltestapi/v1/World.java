package functionaltestapi.v1;

import cucumber.runtime.java.guice.ScenarioScoped;
import functionaltest.v1.model.Calculator;
import functionaltestapi.v1.model.ImageOptimizerClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ScenarioScoped
public class World {
    private final List<Exception> exceptions = new ArrayList<Exception>();
    private final Map<String, ImageOptimizerClient> clients = new HashMap<>();

    private static final String IMAGE_OPTIMIZER_ENDPOINT = "http://172.17.0.1:8080/image-optimizer";
    private static final String IMAGE_OPTIMIZER_API_KEY = "d08da773-bae1-4589-bed8-828075c54f5c";
    private static final String RESIZE_IMAGE_ENDPOINT = "/api/v1/image/resize";

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

    public String getResizeImageEndpoint() { return RESIZE_IMAGE_ENDPOINT; }

    public String getApiKey() {
        return IMAGE_OPTIMIZER_API_KEY;
    }


}

