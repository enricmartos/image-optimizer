package service;

public interface ImageOptimizerService {

    byte[] resizeImage(byte[] image, Integer width, Integer height);

    byte[] autorotateImage(byte[] image);
}
