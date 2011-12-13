package net.dryade.siri.sequencer.model;

import uk.org.siri.siri.IDFMessageStructure;
import net.dryade.siri.sequencer.model.type.MessageType;

public class Message {
	private MessageType type;
	private String text;
	private String lang;

	/**
	 * @param type
	 * @param text
	 */
	public Message(MessageType type, String text) {
		this.type = type;
		this.text = text;
                this.lang = "FR";
	}
	public Message(MessageType type, String text, String lang) {
		this.type = type;
		this.text = text;
                this.lang = lang;
	}
	public Message() {}

	public Message(IDFMessageStructure messageIDF) {
		this.type = MessageType.fromSiri(messageIDF.getMessageType());
		this.text = messageIDF.getMessageText().getStringValue();
                this.lang = "FR";
	}

	/**
	 * @return the type
	 */
	public MessageType getType() {
		return type;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

    /**
     * @return the lang
     */
    public String getLang() {
        return lang;
    }

    /**
     * @param lang the lang to set
     */
    public void setLang(String lang) {
        this.lang = lang;
    }
	@Override
	public boolean equals(Object obj) {
		Message message = (Message) obj;
		return (type.equals(message.getType()) && text.equals(message.getText()));
	}

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @param type the type to set
     */
    public void setType(MessageType type) {
        this.type = type;
    }
}
