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
    private  String apiKey;
    private final List<Exception> exceptions = new ArrayList<Exception>();
    private final Map<String, ImageOptimizerClient> clients = new HashMap<>();

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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKeyValue){
        apiKey = apiKeyValue;
    }


}

