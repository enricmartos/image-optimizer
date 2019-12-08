package functionaltest.v1.model;

public class ResizedImage {
    private byte[] resizedImageBytes;

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
