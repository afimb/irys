/**
 * 
 */
package irys.siri.client.message;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;

/**
 * @author michel
 *
 */
public class BasicMessageTrace implements MessageTraceInterface,Runnable
{
	private static final Logger logger = Logger.getLogger(BasicMessageTrace.class);

	private String directoryName; 

	private String baseFileName ;

	private long maxFileLength ;

	private long actualFileLength; 

	private int maxFile ;

	private Queue<MessageData> messageFifo = new LinkedList<MessageData>();

	private long flushPeriod; 

	private File directory;

	private PrintWriter output;

	private boolean active = false;

	private String dateFormat = "dd/MM/yy HH:mm:ss,SSS";
	
	private String separator = "";

	private DateFormat formater;

	private int lineSeparatorLength;
	
	private Thread innerThread = null;

	public BasicMessageTrace()
	{

	}

	public void init()
	{
		directory = new File(directoryName);
		if (!directory.exists())
		{
			if (!directory.mkdirs())
			{
				logger.error("Cannot create Message directory "+directoryName+", no trace available ");
				return;
			}
		}
		if (!directory.isDirectory())
		{
			logger.error("Cannot access Message directory "+directoryName+" : not a directory, no trace available ");
			return;
		}
		File messageFile = new File(directory,baseFileName);
		actualFileLength = messageFile.length();

		try 
		{
			output = new PrintWriter(new FileOutputStream(messageFile,true));
		} 
		catch (FileNotFoundException e) 
		{
			logger.error("Cannot access Message file "+directoryName+"/"+baseFileName+", no trace available ");
			return;
		}

		formater = new SimpleDateFormat(dateFormat);

		lineSeparatorLength = System.getProperty("line.separator").length();

		active = true;
		// start fifo check
		innerThread = new Thread(this);
		innerThread.start();
		logger.info("Message trace launched");

	}


	/* (non-Javadoc)
	 * @see net.dryade.siri.message.MessageTraceInterface#addMessage(org.apache.xmlbeans.XmlObject)
	 */
	@Override
	public synchronized void addMessage(XmlObject message) 
	{
		if (!active) return;
		MessageData data = new MessageData(Calendar.getInstance(), message);
		messageFifo.add(data);

	}

	private synchronized void flushMessages()
	{
		if (messageFifo.size() == 0) return;

		while (messageFifo.size() > 0)
		{
			MessageData data = messageFifo.poll();
			String s = formater.format(data.getDate());
			long length = s.length()+data.getMessage().length()+lineSeparatorLength*2;
			if (separator.length() > 0) length += separator.length() + lineSeparatorLength;
			if (actualFileLength+length > maxFileLength)
			{
				swapFiles();
				if (output == null) return;
			}
			output.println(s);
			output.println(data.getMessage());
			if (separator.length() > 0) output.println(separator);
			actualFileLength += length;
            logger.debug("taille fichier message = "+actualFileLength);
			
		}
		output.flush();
	}


	private void swapFiles() 
	{
		int maxFileIndice = maxFile - 1;
		output.close();
		output = null;
		//logger.debug("start message trace files rotate");
		if (maxFile >= 1)
		{
			File file = new File(directory,baseFileName+"."+maxFileIndice);
			if (file.exists())
			{
				//logger.debug("file "+file.getName()+" deleted");
				file.delete();
			}
			int lastSuffix = maxFileIndice - 1;
			while (lastSuffix > 0)
			{
				file = new File(directory,baseFileName+"."+lastSuffix);
				int newSuffix = lastSuffix+1;
				File dest = new File(directory,baseFileName+"."+newSuffix);
				if (file.exists())
				{
					//logger.debug("file "+file.getName()+" renamed to "+dest.getName());
					file.renameTo(dest);
				}
				lastSuffix --;
			}
		}

		File firstfile = new File(directory,baseFileName);
		if (maxFile >= 1)
		{
			File dest = new File(directory,baseFileName+".1");
			//logger.debug("file "+firstfile.getName()+" renamed to "+dest.getName());
			firstfile.renameTo(dest);
		}
		else
		{
			firstfile.delete();
		}
		firstfile = new File(directory,baseFileName);
		try 
		{
			//logger.debug("new file "+firstfile.getName()+" opened ");
			output = new PrintWriter(firstfile);
			actualFileLength = 0;
		} 
		catch (FileNotFoundException e) 
		{
			logger.error("Cannot create Message file "+directoryName+"/"+baseFileName+", no trace available ");
			active = false;
		}

	}

	@Override
	public void run() 
	{
		logger.info("Message trace active");
		while (active) 
		{
			try 
			{
				Thread.sleep(flushPeriod);
			} 
			catch (InterruptedException e) 
			{
				// logger.debug("Message trace interrupted");
				active = false;
			}
			flushMessages();
			if (!active) output.close();
		}

	}

	@Override
	public void close() 
	{
	   logger.debug("Message trace stopped");
	   active=false;
	   if (innerThread != null) innerThread.interrupt();
	   innerThread = null;
	}
	/**
	 * @return the directoryName
	 */
	public String getDirectoryName() {
		return directoryName;
	}

	/**
	 * @param directoryName the directoryName to set
	 */
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}

	/**
	 * @return the baseFileName
	 */
	public String getBaseFileName() {
		return baseFileName;
	}

	/**
	 * @param baseFileName the baseFileName to set
	 */
	public void setBaseFileName(String baseFileName) {
		this.baseFileName = baseFileName;
	}

	/**
	 * @return the maxFileLength
	 */
	public long getMaxFileLength() {
		return maxFileLength;
	}

	/**
	 * @param maxFileLength the maxFileLength to set
	 */
	public void setMaxFileLength(long maxFileLength) {
		this.maxFileLength = maxFileLength;
	}

	/**
	 * @return the maxFile
	 */
	public int getMaxFile() {
		return maxFile;
	}

	/**
	 * @param maxFile the maxFile to set
	 */
	public void setMaxFile(int maxFile) {
		this.maxFile = maxFile;
	}

	/**
	 * @return the flushPeriod
	 */
	public long getFlushPeriod() {
		return flushPeriod;
	}

	/**
	 * @param flushPeriod the flushPeriod to set
	 */
	public void setFlushPeriod(long flushPeriod) {
		this.flushPeriod = flushPeriod;
	}
	/**
	 * @return the dateFormat
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	/**
	 * @return the separator
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * @param separator the separator to set
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}
	private class MessageData
	{
		private Date date;

		private String message;

		/**
		 * @param date
		 * @param message
		 */
		public MessageData(Calendar date,XmlObject message)
		{
			this.date = date.getTime();
			this.message = message.toString();
		}

		/**
		 * @return the date
		 */
		public Date getDate() {
			return date;
		}

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

	}


}
