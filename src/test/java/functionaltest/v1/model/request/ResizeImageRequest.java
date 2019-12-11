package functionaltest.v1.model.request;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import javax.ws.rs.core.MediaType;
import java.io.IOException;

public class ResizeImageRequest extends ImageRequest  {

    private static final String WIDTH_REQUEST_FIELD_KEY = "width";
    private static final String HEIGHT_REQUEST_FIELD_KEY = "height";

    private final Integer width;
    private final Integer height;

    public ResizeImageRequest(String originalImage, Integer width, Integer height) {
        this.originalImage = originalImage;
        this.width = width;
        this.height = height;
    }

    @Override
    public MultipartFormDataOutput getMultipartFormDataOutput() throws IOException {
        MultipartFormDataOutput mdo = getBaseMultipartFormDataOutput();
        mdo.addFormData(WIDTH_REQUEST_FIELD_KEY, width, MediaType.TEXT_PLAIN_TYPE);
        mdo.addFormData(HEIGHT_REQUEST_FIELD_KEY, height, MediaType.TEXT_PLAIN_TYPE);
        return mdo;
    }
}
