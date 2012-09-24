package irys.siri.realtime.model.type;

import irys.siri.realtime.model.type.MessageType;
import uk.org.siri.siri.IDFMessageTypeEnumeration.Enum;

public enum MessageType 
{
	
    /**
     * Short message
     */
	shortMessage("shortMessage"),
    /**
     * Long message
     */
	longMessage("longMessage"),
    /**
     * Encoded message
     */
	codedMessage("codedMessage");
    private String value;

    private MessageType(String value) {
        this.value = value;
    }

    
    public static MessageType fromValue(final String label) {
        for (MessageType c : MessageType.values()) {
            if (c.getValue().equals(label)) {
                return c;
            }
        }
        throw new IllegalArgumentException(label);
    }
	public static MessageType fromSiri(Enum messageType) 
	{
		return valueOf(messageType.toString());
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
