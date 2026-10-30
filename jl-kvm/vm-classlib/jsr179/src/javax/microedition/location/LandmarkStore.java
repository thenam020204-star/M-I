/*
 * jl-kvm guest class library (jsr179 layer, API per tools/api-sigs/db/jsr179-location.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.location;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Landmark stores in memory, for the run only (no persistence: a store created by a game does not survive the next start, UNVERIFIED point of the
 * project). The default store (name null) always exists and starts empty.
 */
public class LandmarkStore {
    private static final Hashtable stores = new Hashtable();
    private static LandmarkStore defaultStore;

    private final Vector categories = new Vector();
    private final Vector landmarks = new Vector();
    private final Vector landmarkCategory = new Vector();   // per landmark: Vector of category names

    private LandmarkStore() {
    }

    public static LandmarkStore getInstance(String storeName) {
        synchronized (stores) {
            if (storeName == null) {
                if (defaultStore == null) {
                    defaultStore = new LandmarkStore();
                }
                return defaultStore;
            }
            return (LandmarkStore) stores.get(storeName);
        }
    }

    public static void createLandmarkStore(String storeName) throws IOException, LandmarkException {
        if (storeName == null) {
            throw new NullPointerException("storeName");
        }
        if (storeName.length() == 0) {
            throw new IllegalArgumentException("storeName is empty");
        }
        synchronized (stores) {
            if (stores.containsKey(storeName)) {
                throw new LandmarkException("a landmark store named " + storeName + " already exists");
            }
            stores.put(storeName, new LandmarkStore());
        }
    }

    public static void deleteLandmarkStore(String storeName) throws IOException, LandmarkException {
        if (storeName == null) {
            throw new NullPointerException("storeName");
        }
        synchronized (stores) {
            if (stores.remove(storeName) == null) {
                throw new LandmarkException("no landmark store named " + storeName);
            }
        }
    }

    /** The names of the created stores; the default store is the entry null, which is first (the javadoc: "null for the default store"). */
    public static String[] listLandmarkStores() throws IOException {
        synchronized (stores) {
            String[] r = new String[stores.size() + 1];
            int i = 1;
            for (Enumeration e = stores.keys(); e.hasMoreElements();) {
                r[i++] = (String) e.nextElement();
            }
            return r;
        }
    }

    public void addCategory(String categoryName) throws LandmarkException, IOException {
        if (categoryName == null) {
            throw new NullPointerException("categoryName");
        }
        synchronized (this) {
            if (!categories.contains(categoryName)) {
                categories.addElement(categoryName);
            }
        }
    }

    public void deleteCategory(String categoryName) throws LandmarkException, IOException {
        if (categoryName == null) {
            throw new NullPointerException("categoryName");
        }
        synchronized (this) {
            if (!categories.removeElement(categoryName)) {
                throw new IllegalArgumentException("no category " + categoryName);
            }
            for (int i = 0; i < landmarkCategory.size(); i++) {
                ((Vector) landmarkCategory.elementAt(i)).removeElement(categoryName);
            }
        }
    }

    public Enumeration getCategories() {
        synchronized (this) {
            Vector c = new Vector();
            for (int i = 0; i < categories.size(); i++) {
                c.addElement(categories.elementAt(i));
            }
            return c.elements();
        }
    }

    /** category == null: the landmark belongs to no category. */
    public void addLandmark(Landmark landmark, String category) throws IOException {
        if (landmark == null) {
            throw new NullPointerException("landmark");
        }
        synchronized (this) {
            if (category != null && !categories.contains(category)) {
                throw new IllegalArgumentException("no category " + category);
            }
            int i = landmarks.indexOf(landmark);
            if (i < 0) {
                landmarks.addElement(landmark);
                landmarkCategory.addElement(new Vector());
                i = landmarks.size() - 1;
            }
            Vector cats = (Vector) landmarkCategory.elementAt(i);
            if (category != null && !cats.contains(category)) {
                cats.addElement(category);
            }
        }
    }

    public void deleteLandmark(Landmark landmark) throws LandmarkException, IOException {
        if (landmark == null) {
            throw new NullPointerException("landmark");
        }
        synchronized (this) {
            int i = landmarks.indexOf(landmark);
            if (i < 0) {
                throw new LandmarkException("the landmark is not in this store");
            }
            landmarks.removeElementAt(i);
            landmarkCategory.removeElementAt(i);
        }
    }

    public void removeLandmarkFromCategory(Landmark lm, String category) throws IOException {
        if (lm == null || category == null) {
            throw new NullPointerException("lm or category");
        }
        synchronized (this) {
            if (!categories.contains(category)) {
                throw new IllegalArgumentException("no category " + category);
            }
            int i = landmarks.indexOf(lm);
            if (i >= 0) {
                ((Vector) landmarkCategory.elementAt(i)).removeElement(category);
            }
        }
    }

    /** Objects are stored by reference, so a changed landmark is already changed; the call only checks that it is in the store. */
    public void updateLandmark(Landmark landmark) throws LandmarkException, IOException {
        if (landmark == null) {
            throw new NullPointerException("landmark");
        }
        synchronized (this) {
            if (!landmarks.contains(landmark)) {
                throw new LandmarkException("the landmark is not in this store");
            }
        }
    }

    public Enumeration getLandmarks() throws IOException {
        synchronized (this) {
            Vector r = new Vector();
            for (int i = 0; i < landmarks.size(); i++) {
                r.addElement(landmarks.elementAt(i));
            }
            return r.size() == 0 ? null : r.elements();
        }
    }

    /** name == null: any name; category == null: any category. Null if nothing matches ("null if there are no matching landmarks"). */
    public Enumeration getLandmarks(String category, String name) throws IOException {
        synchronized (this) {
            if (category != null && !categories.contains(category)) {
                throw new IllegalArgumentException("no category " + category);
            }
            Vector r = new Vector();
            for (int i = 0; i < landmarks.size(); i++) {
                Landmark l = (Landmark) landmarks.elementAt(i);
                if ((category == null || ((Vector) landmarkCategory.elementAt(i)).contains(category)) && (name == null || name.equals(l.getName()))) {
                    r.addElement(l);
                }
            }
            return r.size() == 0 ? null : r.elements();
        }
    }

    public Enumeration getLandmarks(String category, double minLatitude, double maxLatitude, double minLongitude, double maxLongitude) throws IOException {
        if (minLatitude < -90.0 || maxLatitude > 90.0 || minLatitude > maxLatitude || minLongitude < -180.0 || minLongitude >= 180.0
                || maxLongitude < -180.0 || maxLongitude >= 180.0 || !(minLatitude == minLatitude) || !(maxLatitude == maxLatitude)) {
            throw new IllegalArgumentException("bad bounds");
        }
        synchronized (this) {
            if (category != null && !categories.contains(category)) {
                throw new IllegalArgumentException("no category " + category);
            }
            Vector r = new Vector();
            for (int i = 0; i < landmarks.size(); i++) {
                Landmark l = (Landmark) landmarks.elementAt(i);
                QualifiedCoordinates c = l.getQualifiedCoordinates();
                if (c == null || (category != null && !((Vector) landmarkCategory.elementAt(i)).contains(category))) {
                    continue;
                }
                boolean lon = minLongitude <= maxLongitude ? c.getLongitude() >= minLongitude && c.getLongitude() <= maxLongitude
                        : c.getLongitude() >= minLongitude || c.getLongitude() <= maxLongitude;   // min > max = the range crosses 180
                if (c.getLatitude() >= minLatitude && c.getLatitude() <= maxLatitude && lon) {
                    r.addElement(l);
                }
            }
            return r.size() == 0 ? null : r.elements();
        }
    }
}
