package functionaltest.v1.model.request;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import java.io.IOException;

@SuppressWarnings("PMD.UnusedFormalParameter")
public class AutorotateImageRequest extends ImageRequest {

    //Seems that Cucumber needs a dummy argument to parse the examples properly
    public AutorotateImageRequest(String originalImage, String dummy) {
        this.originalImage = originalImage;
    }

    @Override
    public MultipartFormDataOutput getMultipartFormDataOutput() throws IOException {
        return getBaseMultipartFormDataOutput();
    }
}
