/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.microedition.rms.RecordStore
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.microedition.rms.RecordStore;

public final class r {
    public int[] a = new int[7];
    public int b;
    public byte[] c = null;
    public boolean d = false;

    public final byte[] a(byte[] byArray) {
        block3: {
            ByteArrayInputStream byteArrayInputStream = null;
            DataInputStream dataInputStream = null;
            try {
                RecordStore recordStore = RecordStore.openRecordStore((String)"ML.sav", (boolean)true);
                if (recordStore.getNumRecords() > 0) {
                    byteArrayInputStream = new ByteArrayInputStream(recordStore.getRecord(1));
                    dataInputStream = new DataInputStream(byteArrayInputStream);
                    dataInputStream.read(byArray, 0, dataInputStream.available());
                    dataInputStream.close();
                    byteArrayInputStream.close();
                    dataInputStream.close();
                    recordStore.closeRecordStore();
                    break block3;
                }
                recordStore.closeRecordStore();
                byArray = null;
                return null;
            }
            catch (Exception exception) {}
        }
        return byArray;
    }

    public final void b(byte[] byArray) {
        try {
            RecordStore recordStore = RecordStore.openRecordStore((String)"ML.sav", (boolean)true);
            if (recordStore.getNumRecords() > 0) {
                recordStore.setRecord(1, byArray, 0, byArray.length);
            } else {
                System.out.println("WriteFile::rs.getNumRecords() <=0");
                recordStore.addRecord(byArray, 0, byArray.length);
            }
            recordStore.closeRecordStore();
        }
        catch (Exception exception) {
            Exception exception2 = exception;
            exception.printStackTrace();
        }
        System.gc();
    }

    public final boolean a() {
        DataInputStream dataInputStream = null;
        try {
            RecordStore recordStore = RecordStore.openRecordStore((String)"MLOP.sav", (boolean)true);
            if (recordStore.getNumRecords() > 0) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(recordStore.getRecord(1));
                dataInputStream = new DataInputStream(byteArrayInputStream);
                this.b = 0;
                while (this.b < this.a.length) {
                    this.a[this.b] = dataInputStream.readInt();
                    ++this.b;
                }
                dataInputStream.close();
                byteArrayInputStream.close();
                recordStore.closeRecordStore();
                this.d = true;
            } else {
                this.d = false;
                recordStore.closeRecordStore();
            }
        }
        catch (Exception exception) {}
        System.gc();
        return this.d;
    }

    public final void b() {
        try {
            RecordStore recordStore = RecordStore.openRecordStore((String)"MLOP.sav", (boolean)true);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            if (recordStore.getNumRecords() == 0) {
                this.b = 0;
                while (this.b < this.a.length) {
                    dataOutputStream.writeInt(this.a[this.b]);
                    ++this.b;
                }
                this.c = byteArrayOutputStream.toByteArray();
                recordStore.addRecord(this.c, 0, this.c.length);
            } else {
                this.b = 0;
                while (this.b < this.a.length) {
                    dataOutputStream.writeInt(this.a[this.b]);
                    ++this.b;
                }
                this.c = byteArrayOutputStream.toByteArray();
                recordStore.setRecord(1, this.c, 0, this.c.length);
            }
            dataOutputStream.close();
            byteArrayOutputStream.close();
            this.c = null;
            recordStore.closeRecordStore();
        }
        catch (Exception exception) {}
        System.gc();
    }
}

