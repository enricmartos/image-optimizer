package functionaltest.v1.model.request;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import java.io.IOException;

public class AutorotateImageRequest extends ImageRequest {

    public AutorotateImageRequest(String originalImage) {
        this.originalImage = originalImage;
    }

    @Override
    public MultipartFormDataOutput getMultipartFormDataOutput() throws IOException {
        return getBaseMultipartFormDataOutput();
    }
}
