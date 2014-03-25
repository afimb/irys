package irys.siri.realtime.model.type;

import irys.siri.realtime.model.type.InfoChannel;
import irys.uk.org.siri.siri.InfoChannelRefStructure;

public enum InfoChannel {

    Perturbation("Perturbation"), 
    Information("Information"),
    Commercial("Commercial");
    private String value;

    private InfoChannel(String value) {
        this.value = value;
    }

    /**
     * @param value
     * @return
     */
    public static InfoChannel fromValue(final String label) {
        for (InfoChannel c : InfoChannel.values()) {
            if (c.getValue().equals(label)) {
                return c;
            }
        }
        throw new IllegalArgumentException(label);
    }
    // Perturbation,Information,Commercial;

    public static InfoChannel fromSiri(InfoChannelRefStructure infoChannelRef) {
        return valueOf(infoChannelRef.getStringValue());
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    public String toString() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
}
