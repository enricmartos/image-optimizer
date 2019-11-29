package v1;

import model.FileUploadForm;
import model.ResizeFileUploadForm;
import org.jboss.resteasy.spi.BadRequestException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import service.managers.ApiKeyManager;
import web.rest.v1.ImageOptimizerServiceController;
import service.ImageOptimizerService;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static javax.ws.rs.core.Response.Status.OK;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class ImageOptimizerServiceControllerTest {

    private static final String TEST_IMG_PATH = "src/test/resources/testimg.jpg";

    //Resolution of a HD image (horizontal)
    private static final Integer VALID_IMG_WIDTH = 1280;

    private static final Integer VALID_IMG_HEIGHT = 720;

    //Resolution of a 8K image (horizontal)
    private static final Integer EXCESSIVE_IMG_WIDTH= 7680;

    private static final Integer EXCESSIVE_IMG_HEIGHT = 4320;

    //Invalid image resolution
    private static final Integer NEGATIVE_IMG_RESOLUTION= -1;


    @InjectMocks
    ImageOptimizerServiceController imageOptimizerServiceController;
    @Mock
    ImageOptimizerService imageOptimizerService;
    @Mock
    private ApiKeyManager apiKeyManager;

    @Rule
    private final ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        doNothing().when(apiKeyManager).validateApiKey(anyString());
    }


    @Test
    public void testValidImageWithEmptyFile() throws BadRequestException {
        when(imageOptimizerService.resizeImage(Mockito.anyObject(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(null);

        Response response = imageOptimizerServiceController.resizeImage("apiKey",
                getStubFileUploadForm(getStubValidImage(), VALID_IMG_WIDTH, VALID_IMG_HEIGHT));

        Assert.assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test
    public void testValidImageWithPresentFile() throws BadRequestException, IOException {
        when(imageOptimizerService.resizeImage(Mockito.anyObject(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(getStubFile());

        Response response = imageOptimizerServiceController.resizeImage("apiKey",
                getStubFileUploadForm(getStubValidImage(), VALID_IMG_WIDTH, VALID_IMG_HEIGHT));

        Assert.assertEquals(OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testInvalidImageWithInvalidFileData() throws BadRequestException {
        thrown.expect(BadRequestException.class);
        imageOptimizerServiceController.resizeImage("apiKey",
                getStubFileUploadForm(getStubInvalidFile(), VALID_IMG_WIDTH, VALID_IMG_HEIGHT));
    }

    @Test
    public void testValidImageWithExcessiveResolution() throws BadRequestException {
        thrown.expect(BadRequestException.class);
        imageOptimizerServiceController.resizeImage("apiKey",
                getStubFileUploadForm(getStubValidImage(), EXCESSIVE_IMG_WIDTH, EXCESSIVE_IMG_HEIGHT));
    }

    @Test
    public void testValidImageWithNegativeResolution() throws BadRequestException {
        thrown.expect(BadRequestException.class);
        imageOptimizerServiceController.resizeImage("apiKey",
                getStubFileUploadForm(getStubValidImage(), NEGATIVE_IMG_RESOLUTION, NEGATIVE_IMG_RESOLUTION));
    }

    @Test
    public void testValidPDFWithEmptyOutputFile() throws BadRequestException {
        when(imageOptimizerService.convertPDFToImages(Mockito.anyObject())).thenReturn(null);

        Response response = imageOptimizerServiceController.convertDocToImages("apiKey",
                getStubFileUploadForm(getStubValidPDF()));

        Assert.assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test
    public void testValidPDFWithEmptyPresentFile() throws BadRequestException, IOException {
        when(imageOptimizerService.convertPDFToImages(Mockito.anyObject())).thenReturn(getStubFile());

        Response response = imageOptimizerServiceController.convertDocToImages("apiKey",
                getStubFileUploadForm(getStubValidPDF()));

        Assert.assertEquals(OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testInvalidDocWithInvalidFileData() throws BadRequestException {
        thrown.expect(BadRequestException.class);
        imageOptimizerServiceController.convertDocToImages("apiKey",
                getStubFileUploadForm(getStubInvalidFile()));
    }

    @Test
    public void testValidDocWithEmptyOutputFile() throws BadRequestException {
        when(imageOptimizerService.convertDocToImages(Mockito.anyObject())).thenReturn(null);

        Response response = imageOptimizerServiceController.convertDocToImages("apiKey",
                getStubFileUploadForm(getStubValidDoc()));

        Assert.assertEquals(INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test
    public void testValidDocWithEmptyPresentFile() throws BadRequestException, IOException {
        when(imageOptimizerService.convertDocToImages(Mockito.anyObject())).thenReturn(getStubFile());

        Response response = imageOptimizerServiceController.convertDocToImages("apiKey",
                getStubFileUploadForm(getStubValidDoc()));

        Assert.assertEquals(OK.getStatusCode(), response.getStatus());
    }

    private FileUploadForm getStubFileUploadForm(byte[] fileData) {
        return new FileUploadForm(fileData);
    }

    private ResizeFileUploadForm getStubFileUploadForm(byte[] fileData, Integer width, Integer height) {
        return new ResizeFileUploadForm(fileData, width, height);
    }

    private byte[] getStubFile() throws IOException {
        return Files.readAllBytes(new File(TEST_IMG_PATH).toPath());
    }

    //Valid Image (JPG header)
    private byte[] getStubValidImage() {
        return new byte[]{-1, -40, -1, -32};
    }

    //Invalid Image (GIF header)
    private byte[] getStubInvalidFile() {
        return new byte[]{47, 49, 46, 38};
    }

    //Valid Image (DOC, PPT, XLS header)
    private byte[] getStubValidPDF() {
        return new byte[]{37, 80, 68, 70};
    }

    //Valid Image (DOC, PPT, XLS header)
    private byte[] getStubValidDoc() {
        return new byte[]{-48, -49, 17, -32, -95, -79, 26, -31};
    }
}