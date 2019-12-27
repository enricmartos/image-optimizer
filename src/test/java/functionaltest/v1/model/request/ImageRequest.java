package functionaltest.v1.model.request;

import functionaltest.v1.random.ImageRandomizer;
import org.apache.commons.lang3.ArrayUtils;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

abstract class ImageRequest implements ImageOptimizerRequest {

    private static final String RANDOM_ORIGINAL_IMAGE = "randomImage";
    private static final String INPUT_IMG_PATH = "src/test/resources/images/input/";
    private static final String FILE_REQUEST_FIELD_KEY = "selectedFile";

    String originalImage;

    MultipartFormDataOutput getBaseMultipartFormDataOutput() throws IOException {
        byte[] fileData = new byte[0];
        MultipartFormDataOutput mdo = new MultipartFormDataOutput();
        if (originalImage.equals(RANDOM_ORIGINAL_IMAGE)) {
            fileData = ArrayUtils.toPrimitive(new ImageRandomizer().getRandomValue());
        } else if (!originalImage.isEmpty()) {
            fileData = Files.readAllBytes(new File(INPUT_IMG_PATH + originalImage).toPath());
        }

        mdo.addFormData(FILE_REQUEST_FIELD_KEY, fileData, MediaType.APPLICATION_OCTET_STREAM_TYPE);
        return mdo;
    }
}
