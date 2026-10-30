/*
 * jl-kvm guest class library (MIDP 2.0 layer, API per the MIDP 2.0 stub signature DB). Origin: original code written for jl-kvm (PLAN D12).
 * License: Apache-2.0.
 */
package javax.microedition.lcdui;

import java.util.Vector;

public class Form extends Screen {
    private final Vector items = new Vector();
    private ItemStateListener stateListener;
    /** Item the host should show (Display.setCurrentItem). */
    Item focus;

    public Form(String title) {
        setTitle(title);
    }

    public Form(String title, Item[] items) {
        setTitle(title);
        if (items != null) {
            for (int i = 0; i < items.length; i++) if (items[i] == null) throw new NullPointerException();
            for (int i = 0; i < items.length; i++) {
                if (items[i].owner != null) throw new IllegalStateException("item already owned");
            }
            for (int i = 0; i < items.length; i++) {
                if (items[i].owner != null) throw new IllegalStateException("item appears twice");
                add(this.items.size(), items[i]);
            }
        }
    }

    private void add(int at, Item it) {
        synchronized (items) {
            if (it.owner != null) throw new IllegalStateException("item already owned");
            it.owner = this;
            items.insertElementAt(it, at);
        }
    }

    public int append(Item item) {
        if (item == null) throw new NullPointerException();
        int at;
        synchronized (items) {
            at = items.size();
            add(at, item);
        }
        touch();
        return at;
    }

    public int append(String str) {
        if (str == null) throw new NullPointerException();
        return append(new StringItem(null, str));
    }

    public int append(Image img) {
        if (img == null) throw new NullPointerException();
        return append(new ImageItem(null, img, Item.LAYOUT_DEFAULT, null));
    }

    public void insert(int itemNum, Item item) {
        synchronized (items) {
            if (itemNum < 0 || itemNum > items.size()) throw new IndexOutOfBoundsException("item " + itemNum);
            if (item == null) throw new NullPointerException();
            add(itemNum, item);
        }
        touch();
    }

    public void delete(int itemNum) {
        synchronized (items) {
            if (itemNum < 0 || itemNum >= items.size()) throw new IndexOutOfBoundsException("item " + itemNum);
            Item old = (Item) items.elementAt(itemNum);
            items.removeElementAt(itemNum);
            old.owner = null;
            if (focus == old) focus = null;
        }
        touch();
    }

    public void deleteAll() {
        synchronized (items) {
            for (int i = 0; i < items.size(); i++) ((Item) items.elementAt(i)).owner = null;
            items.removeAllElements();
            focus = null;
        }
        touch();
    }

    public void set(int itemNum, Item item) {
        synchronized (items) {
            if (itemNum < 0 || itemNum >= items.size()) throw new IndexOutOfBoundsException("item " + itemNum);
            if (item == null) throw new NullPointerException();
            if (item.owner != null) throw new IllegalStateException("item already owned");
            Item old = (Item) items.elementAt(itemNum);
            old.owner = null;
            if (focus == old) focus = null;
            item.owner = this;
            items.setElementAt(item, itemNum);
        }
        touch();
    }

    public Item get(int itemNum) {
        synchronized (items) {
            if (itemNum < 0 || itemNum >= items.size()) throw new IndexOutOfBoundsException("item " + itemNum);
            return (Item) items.elementAt(itemNum);
        }
    }

    public int size() {
        return items.size();
    }

    public void setItemStateListener(ItemStateListener iListener) {
        stateListener = iListener;
    }

    public int getWidth() {
        return super.getWidth();
    }

    public int getHeight() {
        return super.getHeight();
    }

    int uiKind() {
        return ScreenCodec.K_FORM;
    }

    void encodeBody() {
        ScreenCodec.formBody(this);
    }

    Vector items0() {
        return items;
    }

    Item findItem(int iid) {
        synchronized (items) {
            for (int i = 0; i < items.size(); i++) {
                Item it = (Item) items.elementAt(i);
                if (it.iid == iid) return it;
            }
        }
        return null;
    }

    void itemChanged(Item item) {
        ItemStateListener l = stateListener;
        if (l == null) return;
        try {
            l.itemStateChanged(item);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    void onShow() {
        super.onShow();
        synchronized (items) {
            for (int i = 0; i < items.size(); i++) if (items.elementAt(i) instanceof CustomItem) ((CustomItem) items.elementAt(i)).onShow();
        }
    }

    void onHide() {
        synchronized (items) {
            for (int i = 0; i < items.size(); i++) if (items.elementAt(i) instanceof CustomItem) ((CustomItem) items.elementAt(i)).onHide();
        }
        super.onHide();
    }
}
