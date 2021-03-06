package tv.notube.commons.storage.kvs;

import tv.notube.commons.storage.model.Query;
import tv.notube.commons.storage.model.fields.Field;
import tv.notube.commons.storage.model.fields.StringField;

import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface KVStore {

    public List<String> search(String table, Query query, int limit, int offset)
        throws KVStoreException;

    public List<String> search(String table, Query query)
        throws KVStoreException;

    public List<String> search(String table)
        throws KVStoreException;

    public Object getValue(String table, String key)
            throws KVStoreException;

    public StringField[] getFields(String table, String key)
        throws KVStoreException;

    public void setValue(
            String table,
            String key,
            Object object,
            boolean autoCommit,
            StringField... fields
    ) throws KVStoreException;

    public void setValue(
            String table,
            String key,
            Object object,
            StringField... fields
    ) throws KVStoreException;

    void deleteValue(String table, String key) throws KVStoreException;

    public void commit() throws KVStoreException;

}
