package functionaltestapi.v1.model;

public class ResizeImageRequest {
    private String originalImage;
    private Integer width;
    private Integer height;

    public ResizeImageRequest(String filename, Integer width, Integer height) {
        this.originalImage = filename;
        this.width = width;
        this.height = height;
    }

    public String getOriginalImage() {
        return this.originalImage;
    }

    public void setOriginalImage(String originalImage) {
        this.originalImage = originalImage;
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
