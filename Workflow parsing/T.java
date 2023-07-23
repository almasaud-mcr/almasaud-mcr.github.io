package kNIMEWFs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class T {

	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
String l = "";
String delim = ",";
HashMap<String,String>  taxonomy = new HashMap<String,String>();

BufferedReader br = new BufferedReader(new FileReader("/home/phd/Desktop/WF_Parsed.csv"));
	
while ((l = br.readLine()) != null)
{
	String[] entries = l.split(delim);
	taxonomy.put(entries[0], entries[1]);
	
	
}

System.out.println(taxonomy.size());
	
	}
}
