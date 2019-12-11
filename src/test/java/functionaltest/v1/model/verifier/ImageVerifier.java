package functionaltest.v1.model.verifier;

import functionaltest.v1.model.ResponseImage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.testng.Assert.assertEquals;

abstract class ImageVerifier {

    private static final String GROUND_TRUTH_IMG_PATH = "src/test/resources/images/groundtruth/";

    private String expectedResponseImage;
    private ResponseImage responseImage;

    public String getExpectedResponseImage() {
        return expectedResponseImage;
    }

    public void setExpectedResponseImage(String expectedResponseImage) {
        this.expectedResponseImage = expectedResponseImage;
    }

    public ResponseImage getResponseImage() {
        return responseImage;
    }

    public void setResponseImage(ResponseImage responseImage) {
        this.responseImage = responseImage;
    }

    public void verifyImage(ResponseImage responseImage) throws IOException {
        if (expectedResponseImage != null) {
            byte[] expectedImage = Files.readAllBytes(new File(GROUND_TRUTH_IMG_PATH
                    + expectedResponseImage).toPath());
            assertEquals(responseImage.getResponseImageBytes(), expectedImage);
        }
    }


}
