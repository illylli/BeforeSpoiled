package com.example.Fanzy.myapplication.backend.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Fanzy on 5/24/16.
 */
public class ShoppingListItemDataStore {

    public static final String SHOPPINGLISTITEM_PARENT_ENTITY_NAME = "ShoppingListItemParentEntity";
    public static final String SHOPPINGLISTITEM_PARENT_KEY_NAME = "ShoppingListItemParentKey";

    public static final String SHOPPINGLISTITEM_ENTITY_NAME = "ShoppingListItem";
    public static final String FIELD_NAME_ID = "id";
    public static final String FIELD_NAME_ITEM_NAME = "itemName";
    public static final String FIELD_NAME_ITEM_NUMBER = "itemNumber";
    public static final String FIELD_NAME_SELECTED = "selected";

    private static final Logger mLogger = Logger.getLogger(ShoppingListItemDataStore.class.getName());
    private static final DatastoreService mDatastore = DatastoreServiceFactory.getDatastoreService();

    private static Key getKey() {
        return KeyFactory.createKey(ShoppingListItemDataStore.SHOPPINGLISTITEM_PARENT_ENTITY_NAME,
                ShoppingListItemDataStore.SHOPPINGLISTITEM_PARENT_KEY_NAME);
    }

    private static void createParentEntity() {
        Entity entity = new Entity(getKey());
        mDatastore.put(entity);
    }

    // add entity to datastore
    public static boolean add(ShoppingListItem item) {
        if (getShoppingListItemById(item.getId(), null) != null) {
            mLogger.log(Level.INFO, "entry exists");
            return false;
        }

        Key parentKey = getKey();

        Entity entity = new Entity(SHOPPINGLISTITEM_ENTITY_NAME, item.getId(), parentKey);
        entity.setProperty(FIELD_NAME_ID, item.getId());
        entity.setProperty(FIELD_NAME_ITEM_NAME, item.getItemName());
        entity.setProperty(FIELD_NAME_ITEM_NUMBER, item.getItemNumber());
        entity.setProperty(FIELD_NAME_SELECTED, item.isSelected());

        mDatastore.put(entity);
        return true;
    }

    // update datastore
    public static boolean update(ShoppingListItem item) {
        try {
            Entity result = mDatastore.get(KeyFactory.createKey(getKey(),
                    SHOPPINGLISTITEM_PARENT_ENTITY_NAME, item.getId()));
            result.setProperty(FIELD_NAME_ITEM_NAME, item.getItemName());
            result.setProperty(FIELD_NAME_ITEM_NUMBER, item.getItemNumber());
            result.setProperty(FIELD_NAME_SELECTED, item.isSelected());
            mDatastore.put(result);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    //delete entity by id in datastore
    public static boolean delete(String id) {
        // query
        Query.Filter filter = new Query.FilterPredicate(FIELD_NAME_ID, Query.FilterOperator.EQUAL, id);
        Query query = new Query(SHOPPINGLISTITEM_ENTITY_NAME);
        query.setFilter(filter);

        // use preparedQuery interface to retrieve results
        PreparedQuery pq = mDatastore.prepare(query);

        Entity result = pq.asSingleEntity();
        boolean ret = false;
        if (result != null) {
            // delete
            mDatastore.delete(result.getKey());
            ret = true;
        }

        return ret;
    }

    public static void deleteAll() {
        Query query = new Query(SHOPPINGLISTITEM_ENTITY_NAME);
        // get every record from datastore
        query.setFilter(null);
        // set query's ancestor to get strong consistency
        query.setAncestor(getKey());

        PreparedQuery pq = mDatastore.prepare(query);

        for (Entity entity : pq.asIterable()) {
            mDatastore.delete(entity.getKey());
        }
    }

    public static ArrayList<ShoppingListItem> query(Long id) {
        ArrayList<ShoppingListItem> resultList = new ArrayList<>();
        if (id != null) {
            ShoppingListItem exerciseEntry = getShoppingListItemById(id, null);
            if (exerciseEntry != null) {
                resultList.add(exerciseEntry);
            }
        } else {
            Query query = new Query(SHOPPINGLISTITEM_ENTITY_NAME);
            // get every record from datastore
            query.setFilter(null);
            // set query's ancestor to get strong consistency
            query.setAncestor(getKey());

            PreparedQuery pq = mDatastore.prepare(query);

            for (Entity entity : pq.asIterable()) {
                ShoppingListItem item = getShoppingListItemFromEntity(entity);
                if (item != null) {
                    resultList.add(item);
                }
            }
        }
        return resultList;
    }

    // get entry by id
    public static ShoppingListItem getShoppingListItemById(Long id, Transaction txn) {
        Entity result = null;
        try {
            result = mDatastore.get(KeyFactory.createKey(getKey(), SHOPPINGLISTITEM_ENTITY_NAME, id));
        } catch (Exception ex) {
        }
        return getShoppingListItemFromEntity(result);
    }

    // get entry from entity
    private static ShoppingListItem getShoppingListItemFromEntity(Entity entity) {
        if (entity == null) return null;
        ShoppingListItem exerciseEntry = new ShoppingListItem();
        exerciseEntry.setId((Long) entity.getProperty(FIELD_NAME_ID));
        exerciseEntry.setItemName((String) entity.getProperty(FIELD_NAME_ITEM_NAME));
        exerciseEntry.setItemNumber((Integer) entity.getProperty(FIELD_NAME_ITEM_NUMBER));
        exerciseEntry.setSelected((boolean) entity.getProperty(FIELD_NAME_SELECTED));
        return exerciseEntry;
    }

}
