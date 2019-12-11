package functionaltest.v1.model.request;

import functionaltest.v1.random.ImageRandomizer;
import org.apache.commons.lang3.ArrayUtils;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

abstract class ImageRequest implements ImageOptimizerRequest {

    private static final String INPUT_IMG_PATH = "src/test/resources/images/input/";
    private static final String FILE_REQUEST_FIELD_KEY = "selectedFile";

    String originalImage;

    MultipartFormDataOutput getBaseMultipartFormDataOutput() throws IOException {
        byte[] fileData;
        if (!originalImage.isEmpty()) {
            fileData = Files.readAllBytes(new File(INPUT_IMG_PATH + originalImage).toPath());
        } else {
            fileData = ArrayUtils.toPrimitive(new ImageRandomizer().getRandomValue());
        }
        MultipartFormDataOutput mdo = new MultipartFormDataOutput();
        mdo.addFormData(FILE_REQUEST_FIELD_KEY, fileData, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        return mdo;
    }
}
