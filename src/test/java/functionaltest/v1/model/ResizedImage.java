package functionaltest.v1.model;

public class ResizedImage {
    private byte[] resizedImageBytes;
//    private Integer expectedWidth;
//    private Integer expectedHeight;
//
//    public Integer getExpectedWidth() {
//        return expectedWidth;
//    }
//
//    public void setExpectedWidth(Integer expectedWidth) {
//        this.expectedWidth = expectedWidth;
//    }
//
//    public Integer getExpectedHeight() {
//        return expectedHeight;
//    }
//
//    public void setExpectedHeight(Integer expectedHeight) {
//        this.expectedHeight = expectedHeight;
//    }

    public ResizedImage(byte[] resizedImageBytes) {
        this.resizedImageBytes = resizedImageBytes;
    }

    public byte[] getResizedImageBytes() {
        return this.resizedImageBytes;
    }

    public void setResizedImageBytes(byte[] resizedImageBytes) {
        this.resizedImageBytes = resizedImageBytes;
    }
}
