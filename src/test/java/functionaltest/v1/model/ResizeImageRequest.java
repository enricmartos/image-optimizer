package functionaltest.v1.model;

public class ResizeImageRequest {
    private String originalImage;
    private Integer width;
    private Integer height;

    public ResizeImageRequest(String filename, Integer width, Integer expectedHeight) {
        this.originalImage = filename;
        this.width = width;
        this.height = expectedHeight;
    }

    public String getOriginalImage() {
        return this.originalImage;
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

}
