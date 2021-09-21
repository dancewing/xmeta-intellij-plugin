package io.xmeta.platform;

public class ShouldNeverHappenException extends RuntimeException {

    public ShouldNeverHappenException(String developer, String reason) {
        super(developer + " claims that " + reason + "should never happen");
    }

}
