package functionaltest.v1.model.request;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import java.io.IOException;

public interface ImageOptimizerRequest {

    MultipartFormDataOutput getMultipartFormDataOutput() throws IOException;
}
