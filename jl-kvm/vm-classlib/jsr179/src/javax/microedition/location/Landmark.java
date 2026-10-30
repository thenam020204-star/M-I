/*
 * jl-kvm guest class library (jsr179 layer, API per tools/api-sigs/db/jsr179-location.json). Origin: original code written for jl-kvm (PLAN D12, T32);
 * behaviour per the javadoc where one exists, otherwise the project's own reading, UNVERIFIED (docs/jsr.md). License: Apache-2.0.
 */

package javax.microedition.location;

public class Landmark {
    private String name, description;
    private QualifiedCoordinates coordinates;
    private AddressInfo address;

    public Landmark(String name, String description, QualifiedCoordinates coordinates, AddressInfo addressInfo) {
        setName(name);
        this.description = description;
        this.coordinates = coordinates;
        this.address = addressInfo;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public QualifiedCoordinates getQualifiedCoordinates() {
        return coordinates;
    }

    public AddressInfo getAddressInfo() {
        return address;
    }

    /** NullPointerException if name is null. */
    public void setName(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQualifiedCoordinates(QualifiedCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.address = addressInfo;
    }
}
