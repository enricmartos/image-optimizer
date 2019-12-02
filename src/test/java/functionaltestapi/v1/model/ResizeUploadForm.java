package functionaltestapi.v1.model;

public class ResizeUploadForm {
    private String filename;
    private Integer width;
    private Integer height;

    public ResizeUploadForm(String filename, Integer width, Integer height) {
        this.filename = filename;
        this.width = width;
        this.height = height;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
