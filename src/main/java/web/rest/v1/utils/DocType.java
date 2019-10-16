package web.rest.v1.utils;

public enum DocType {

    //Java is in Complement a 2 numeric range (-127,128)
    //IN Hexa
    //25 50 44 46
    PDF(new byte[]{37, 80, 68, 70}),
    //DOC, PPT, XLS -> D0 CF 11 E0 A1 B1 1A E1
    DOC(new byte[]{-48, -49, 17, -32, -95, -79, 26, -31}),;

    private byte[] header;

    DocType(byte[] header) {
        this.header = header;
    }

    public static boolean isValidDoc(byte [] content) {
        for(DocType docType : DocType.values()) {
            if(sameFirstOctects(content, docType.header)){
                return true;
            }
        }
        return false;
    }

    public static boolean isPDF(byte [] content) {
        return (sameFirstOctects(content, PDF.header));
    }

    public static boolean isDoc(byte [] content) {
        return (sameFirstOctects(content, DOC.header));
    }





    private static boolean sameFirstOctects(byte[] arrayOriginal, byte[] arrayTarget) {
        for (int i = 0; i < arrayTarget.length; i++) {
            if (arrayOriginal[i] != arrayTarget[i]) {
                return false;
            }
        }
        return true;
    }

}
