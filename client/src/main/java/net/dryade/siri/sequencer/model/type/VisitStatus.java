package net.dryade.siri.sequencer.model.type;

import uk.org.siri.siri.ProgressStatusEnumeration.Enum;

public enum VisitStatus {

    onTime("onTime"),
    early("early"),
    delayed("delayed"),
    cancelled("cancelled"),
    arrived("arrived"),
    noReport("noReport");
    private String value;

    public static VisitStatus fromSiri(Enum progressStatus) {
        return valueOf(progressStatus.toString());
    }

    private VisitStatus(String value) {
        this.value = value;
    }

    /**
     * 
     * @param value
     * @return
     */
    public static VisitStatus fromValue(final String label) {
        for (VisitStatus c : VisitStatus.values()) {
            if (c.getValue().equals(label)) {
                return c;
            }
        }
        throw new IllegalArgumentException(label);
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
