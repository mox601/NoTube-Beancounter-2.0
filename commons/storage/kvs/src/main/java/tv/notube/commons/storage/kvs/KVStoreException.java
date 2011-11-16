package tv.notube.commons.storage.kvs;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class KVStoreException extends Exception {

    public KVStoreException(String message, Exception e) {
        super(message, e);
    }
}
