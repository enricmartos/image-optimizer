package web.rest.v1;

import model.FileUploadForm;
import model.ResizeFileUploadForm;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import service.managers.ApiKeyManager;
import web.rest.v1.utils.ServiceControllerValidationHelper;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;

//public class ImageOptimizerServiceController implements ImageOptimizerService {
@Path("/image")
public class ImageOptimizerServiceController {

    @Inject
    private service.ImageOptimizerService imageOptimizerService;

    @Inject
    private ApiKeyManager apiKeyManager;


//    @Override
        @POST
        @Consumes(MediaType.MULTIPART_FORM_DATA)
        @Produces("image/jpeg")
    @Path("/resize")
//    public Response resizeImage(String apiKey, ResizeFileUploadForm resizeFileUploadForm) throws BadRequestException {
      public Response resizeImage(@HeaderParam("apiKey") String apiKey, @MultipartForm ResizeFileUploadForm resizeFileUploadForm) throws BadRequestException {
        apiKeyManager.validateApiKey(apiKey);
        validateResizeFileUploadForm(resizeFileUploadForm);
        Logger.getLogger(ImageOptimizerServiceController.class.getName()).log(Level.INFO, "Init resize controller");
        byte[] file = imageOptimizerService.resizeImage(resizeFileUploadForm.getFileData(),
                resizeFileUploadForm.getWidth(), resizeFileUploadForm.getHeight());
        return file !=null ? Response.ok(file).header("Content-type", "image/jpeg").build() :
                Response.status(INTERNAL_SERVER_ERROR.getStatusCode()).build();
// return the input image
//        byte[] file = resizeFileUploadForm.getFileData();
//        return Response.ok(file).header("Content-type", "image/jpeg").build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("image/jpeg")
    @Path("/docToImage")
//    public Response resizeImage(String apiKey, ResizeFileUploadForm resizeFileUploadForm) throws BadRequestException {
    public Response convertDocToImage(@HeaderParam("apiKey") String apiKey, @MultipartForm FileUploadForm fileUploadForm) throws BadRequestException {
        Logger.getLogger(ImageOptimizerServiceController.class.getName()).log(Level.INFO, "Init convertDocToImage controller");
        apiKeyManager.validateApiKey(apiKey);
//        validateFileUploadForm(fileUploadForm);
        byte[] file = imageOptimizerService.convertDocToImages(fileUploadForm.getFileData());
        return file !=null ? Response.ok(file).header("Content-type", "image/jpeg").build() :
                Response.status(INTERNAL_SERVER_ERROR.getStatusCode()).build();
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/zip")
    @Path("/docToImages")
//    public Response resizeImage(String apiKey, ResizeFileUploadForm resizeFileUploadForm) throws BadRequestException {
    public Response convertDocToImages(@HeaderParam("apiKey") String apiKey, @MultipartForm FileUploadForm fileUploadForm) throws BadRequestException {
        Logger.getLogger(ImageOptimizerServiceController.class.getName()).log(Level.INFO, "Init convertDocToImages controller");
        apiKeyManager.validateApiKey(apiKey);
//        validateFileUploadForm(fileUploadForm);
        byte[] file = imageOptimizerService.convertDocToImages(fileUploadForm.getFileData());
        return file !=null ? Response.ok(file).header("Content-type", "application/zip").build() :
                Response.status(INTERNAL_SERVER_ERROR.getStatusCode()).build();
    }



//    @Override
//    public Response autorotateImage(String apiKey, FileUploadForm fileUploadForm) throws BadRequestException {
//        apiKeyManager.validateApiKey(apiKey);
////        validateFileUploadForm(fileUploadForm);
//        byte[] file = imageOptimizerService.autorotateImage(fileUploadForm.getFileData());
//        return file !=null ? Response.ok(file).header("Content-type", "image/jpeg").build() :
//                Response.status(INTERNAL_SERVER_ERROR.getStatusCode()).build();
//    }

    private void validateResizeFileUploadForm(ResizeFileUploadForm resizeFileUploadForm) {
        new ServiceControllerValidationHelper("ImageOptimizerService")
                .checkValidRange(resizeFileUploadForm.getWidth(), "width")
                .checkValidRange(resizeFileUploadForm.getHeight(), "height")
                .checkValidImage(resizeFileUploadForm.getFileData(), "fileData");
    }

    private void validateFileUploadForm(FileUploadForm fileUploadForm) {
        new ServiceControllerValidationHelper("MediaConverterService")
                .checkValidImage(fileUploadForm.getFileData(), "fileData");
    }
}

