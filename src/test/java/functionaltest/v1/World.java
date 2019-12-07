package functionaltest.v1;

import cucumber.runtime.java.guice.ScenarioScoped;
import functionaltest.v1.model.ImageOptimizerClient;
import functionaltest.v1.model.ResizedImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ScenarioScoped
public class World {
    private final List<Exception> exceptions = new ArrayList<Exception>();
    private final Map<String, ImageOptimizerClient> clients = new HashMap<>();
    private final Map<String, String> apiKeys = new HashMap<>();
    private ResizedImage resizedImage;

    //    private ResizeImageRequest resizeImageRequest;

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
//
//    public ResizeImageRequest getResizeImageRequest() {
//        return resizeImageRequest;
//    }
//
//    public void setResizeImageRequest(ResizeImageRequest resizeImageRequest) {
//        this.resizeImageRequest = resizeImageRequest;
//    }

    public String getImageOptimizerEndpoint() { return IMAGE_OPTIMIZER_ENDPOINT; }

    public String getApiKey(String clientReference) {
        return apiKeys.get(clientReference  );
    }

    public void setApiKey(String clientReference, String apiKey){
        apiKeys.put(clientReference, apiKey);
    }

    public ResizedImage getResizedImage() {
        return resizedImage;
    }

    public void setResizedImage(ResizedImage resizedImage) {
        this.resizedImage = resizedImage;
    }
}

