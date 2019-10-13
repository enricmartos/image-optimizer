package model;

import javax.ws.rs.FormParam;


public class ResizeFileUploadForm extends FileUploadForm {

    private Integer width;
    private Integer height;


    public ResizeFileUploadForm() {
        // This constructor is intentionally empty. Nothing special is needed here.
    }

    public ResizeFileUploadForm(byte[] fileData, Integer width, Integer height) {
        super(fileData);
        this.width = width;
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    @FormParam("width")
    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    @FormParam("height")
    public void setHeight(Integer height) {
        this.height = height;
    }
}
