package web.rest.v1;

import model.FileUploadForm;
import model.ResizeFileUploadForm;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import service.managers.ApiKeyManager;
import web.rest.v1.utils.DocType;
import web.rest.v1.utils.ServiceControllerValidationHelper;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

@Path("/image")
public class ImageOptimizerServiceController {

    @Inject
    private service.ImageOptimizerService imageOptimizerService;

    @Inject
    private ApiKeyManager apiKeyManager;

    private final static int THREAD_NUMBER = 6;
    private final static Semaphore SEMAPHORE = new Semaphore(THREAD_NUMBER);

    private static final Logger LOGGER = Logger.getLogger(ImageOptimizerServiceController.class.getName());
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("image/jpeg")
    @Path("/resize")
      public Response resizeImage(@HeaderParam("apiKey") String apiKey, @MultipartForm ResizeFileUploadForm resizeFileUploadForm) throws BadRequestException {
        apiKeyManager.validateApiKey(apiKey);
        validateResizeImage(resizeFileUploadForm);

        try {
            SEMAPHORE.acquire();

            byte[] file = imageOptimizerService.resizeImage(resizeFileUploadForm.getFileData(),
                    resizeFileUploadForm.getWidth(), resizeFileUploadForm.getHeight());
            return file != null ? Response.ok(file).header("Content-type", "image/jpeg").build() :
                    Response.status(INTERNAL_SERVER_ERROR.getStatusCode()).build();
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Interrupted exception", e);
        } finally {
            SEMAPHORE.release();
        }

        return null;
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/zip")
    @Path("/docToImages")
    public Response convertDocToImages(@HeaderParam("apiKey") String apiKey, @MultipartForm FileUploadForm fileUploadForm) throws BadRequestException {
        Logger.getLogger(ImageOptimizerServiceController.class.getName()).log(Level.INFO, "Init convertDocToImages controller");
        apiKeyManager.validateApiKey(apiKey);
        validateDoc(fileUploadForm);
        byte [] inputFile = fileUploadForm.getFileData();
        byte [] outputFile;
        if (DocType.isPDF(inputFile)) {
            outputFile = imageOptimizerService.convertPDFToImages(inputFile);
        } else {
            outputFile = imageOptimizerService.convertDocToImages(inputFile);
        }
        return outputFile !=null ? Response.ok(outputFile).header("Content-type", "application/zip").build() :
                Response.status(INTERNAL_SERVER_ERROR.getStatusCode()).build();
    }

    private void validateResizeImage(ResizeFileUploadForm resizeFileUploadForm) {
        new ServiceControllerValidationHelper("ImageOptimizerService")
                .checkValidRange(resizeFileUploadForm.getWidth(), "width")
                .checkValidRange(resizeFileUploadForm.getHeight(), "height")
                .checkValidImage(resizeFileUploadForm.getFileData(), "fileData");
    }

    private void validateDoc(FileUploadForm fileUploadForm) {
        new ServiceControllerValidationHelper("MediaConverterService")
                .checkValidDoc(fileUploadForm.getFileData(), "fileData");
    }

    @GET
    @Path("/testDocker")
    public String testDocker() {
        return "testDockerChange";
    }
}

