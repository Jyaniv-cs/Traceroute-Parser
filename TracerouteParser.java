import java.io.*;
import java.util.*;

/**
 * Parses network trace dump
 */ 
 public class TracerouteParser {

	public static void main(String[] args) {
		String tracerouteFile = "./sampletcpdump.txt";
		String pFile = "./output.txt";
		String inputLine;
		int curPos = 0;
		int ttlNumCount = 0;
		String id;
		String TTL;
		String messageTime;
		String responseTime;
		double differenceTime;
		

		try {
			BufferedReader bufferReader = new BufferedReader(new FileReader(tracerouteFile));
			FileWriter fileWriter = new FileWriter(pFile);

			// String list to store the tracerouteFile
			ArrayList<String> traceList = new ArrayList<String>();
			
			// populates string list with the traceroutFile
			while((inputLine =bufferReader.readLine()) != null) {
				traceList.add(inputLine);
			} // while

			for(String s: traceList) {
				String cLine = traceList.get(curPos);
				
				// check if current line has an id value
				if(cLine.indexOf("id") >= 0) {
					
					// get message id
					id = cLine.substring(cLine.indexOf("id"), cLine.indexOf(", offset"));
					
					// get message TTL
					TTL = cLine.substring(cLine.indexOf("ttl") + 3, cLine.indexOf(", id"));
					
					// get message time
					messageTime = cLine.substring(0, cLine.indexOf("IP"));

				// search the remainder of traceList to find response with corsponding id
				for(int i = curPos + 1; i < traceList.size(); i++) {
					String response = traceList.get(i);

					//check if the line has an id value
					if(response.indexOf("id") >= 0) {
						String responseID = response.substring(response.indexOf("id"), response.indexOf(", offset"));

						// find responseID that is equal to the message id
						if (responseID.equals(id) && !responseID.equals("id 0")) {
							
							// get response IP
							String responseIP = traceList.get(i-1);
							responseIP = responseIP.substring(4, responseIP.indexOf(">"));

							// get response time
							responseTime = traceList.get(i-2);
							responseTime = responseTime.substring(0, responseTime.indexOf("IP"));

							// get differenceTime
							differenceTime = (Double.parseDouble(responseTime) - Double.parseDouble(messageTime)) * 1000;

							differenceTime = Math.round(differenceTime * 1000d) / 1000d;
							
							// write to output.txt
							if(ttlNumCount % 3 == 0) {
								fileWriter.write("TTL" + TTL + "\n");
								fileWriter.write(responseIP+ "\n");
								fileWriter.write(differenceTime + " ms"+ "\n");
								ttlNumCount++;
							} else {
								fileWriter.write(differenceTime + " ms"+ "\n");
								ttlNumCount++;
							}

						} // if responseID = id
					} // if responseID is present
				} // for
			} // if cLine.indexOf("id")
				
				curPos++;
			} // for
			
			// close FileWriter object
			fileWriter.close();
		
		} catch(FileNotFoundException e) {
			System.out.println("Could not open file " + tracerouteFile);
		} catch(IOException e) {
			System.out.println("Error occured while writing to the output file");
		} //catch exceptions
	} // main
} // class