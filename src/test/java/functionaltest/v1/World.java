package functionaltest.v1;

import cucumber.runtime.java.guice.ScenarioScoped;
import functionaltest.v1.model.Calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ScenarioScoped
public class World {
    private  String apiKey;
    private final List<Exception> exceptions = new ArrayList<Exception>();
    private final Map<String, Calculator> clients = new HashMap<>();

    public void addClient(String reference, Calculator calculator) {
        clients.put(reference, calculator);
    }

    public Calculator getClient(String reference) {
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
