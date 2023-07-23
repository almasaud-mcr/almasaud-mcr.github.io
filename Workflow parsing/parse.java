package kNIMEWFs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//import parseKNIME.Operation;



public class parse {
	Map<Integer,ArrayList<String>> dotKnime ;
//	Map<Integer,String> dotKnime ;
	Map<String,ZipEntry> dotKnimeEntry ;
	ZipInputStream zipinputstream ;
	ZipFile zipFile;
	HashMap<String,ArrayList<String>>  taxonomy = new HashMap<String,ArrayList<String>>();

	public void OpsTaxonomy () throws Throwable {
		String l = "";
		String delim = ",";

//		BufferedReader br = new BufferedReader(new FileReader("/home/phd/Desktop/WF_Parsed4.csv"));
		BufferedReader br = new BufferedReader(new FileReader("/home/phd/Desktop/WF_Parsed2021_2.csv"));
			
		while ((l = br.readLine()) != null)
		{
			ArrayList<String> tL = new ArrayList<String>();
			String[] entries = l.split(delim);
//			tL.add(entries[1]); // label
//			tL.add(entries[4]); // Task
			tL.add(entries[5]); // stage
			tL.add(entries[2]);//Wrangling flag
			taxonomy.put(entries[0], tL);
//			System.out.println(tL.toString());
			
		}

	}
	public WF parseF(String fileName) throws Throwable {
		this.OpsTaxonomy();
		zipinputstream = new ZipInputStream(
				new FileInputStream(fileName));


		zipFile = new ZipFile(fileName);

		Enumeration<? extends ZipEntry> entries = zipFile.entries();
//		dotKnime = new TreeMap<String,Integer>();
		dotKnime = new TreeMap<Integer,ArrayList<String>>();
		dotKnimeEntry = new TreeMap<String,ZipEntry>();

		while (entries.hasMoreElements()) {
			ZipEntry entry = entries.nextElement();
			//            String name = entry.getName();
			//            String type = entry.isDirectory() ? "DIR" : "FILE";
			//  
			String entryName = entry.getName();
			System.out.println("entryName: " + entryName);
			if(entryName.endsWith(".knime")) {
				//			System.out.println(entryName);
				Integer te = (int) entryName.codePoints().filter(ch -> ch == '/').count();
				System.out.println("te: " + te);
				ArrayList<String> en;
				if (dotKnime.containsKey(te)) {
					en = dotKnime.get(te);
					en.add(entryName);
				}
				else {
					en = new ArrayList<String>();
					en.add(entryName);
				}

				dotKnime.put(te,en);
				dotKnimeEntry.put(entryName,entry);
			}



		}//End while
		dotKnime.keySet().forEach(args0 -> {System.out.println("dk: "+args0);});
		dotKnimeEntry.keySet().forEach(args0 -> {System.out.println("dke: "+args0);});
//		System.exit(0);
//		System.out.println(dotKnime.size());
//		System.out.println(dotKnime.get(1).size());
//		System.out.println(dotKnime.get(2).size());
//		System.out.println(dotKnime.get(3).size());
//		System.out.println(dotKnime.get(4).size());

		WF nWF = new WF();
		nWF.setFile(fileName);
		Integer l;
		Iterator<Integer> keyit = dotKnime.keySet().iterator();
		while (keyit.hasNext()) {
			l = keyit.next();
			ArrayList<String> pwf = dotKnime.get(l);
			pwf.forEach(args0 -> {System.out.println("pwf: "+args0);});
//			System.exit(0);
			Iterator<String> itlev = pwf.iterator();
			String ename;
			ZipEntry ze;
			while (itlev.hasNext()) {
				ename=itlev.next();
				ze=dotKnimeEntry.get(ename);
				parseKnime(zipFile.getInputStream(ze),ename,l,nWF);
			}
		}				
		//		System.out.println(nWF.getNodes().size());
		//		System.out.println(nWF.getMeta().size());
		return nWF;


	}//End Constructor
	public void close() throws IOException {
		zipinputstream.close();
		zipFile.close();

	}//close

	public void parseKnime(InputStream is,String path,int level,WF wf) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
		String name;
			String base="0";
			String tmpP=path;
			while (tmpP.indexOf("#") > -1) {
				int st = tmpP.indexOf("(#")+2;
				int en = tmpP.substring(st).indexOf(")");

				base = base+":"+tmpP.substring(st, st+en);
				tmpP = tmpP.substring(st+en+1);
				System.out.println("base: "+base);
				System.out.println("tmpP: "+tmpP);
			}
			if (level == 1) {
			name = path.substring(0, path.lastIndexOf('/'));
			wf.setName(name);
			wf.setID(base);
			System.out.println("name1: "+name);
			}
			else {
				String t = path.substring(0, path.lastIndexOf('/'));
				name = t.substring(t.lastIndexOf('/')+1);
				System.out.println("name2: "+name);
				if (!wf.hasName()) {
					wf.setName(name);
					wf.setID(base);
				}
			}
//System.exit(0);

//	    	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	  		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	  		Document doc = dBuilder.parse(is);
	  		doc.getDocumentElement().normalize();
	  		readNodes(doc,base+":",wf);

//		System.out.println(name+level);		
	}//parseKnime

	public void readNodes(Document doc,String baseID,WF wf) throws XPathExpressionException {
		Integer cNodes = 1;
		XPathFactory xpathFactory = XPathFactory.newInstance();
        // Create XPath object
        XPath xpath = xpathFactory.newXPath();

       XPathExpression expr = xpath.compile("descendant-or-self::*[contains(@key,'nodes')]/config/@key");
       
       NodeList nodeList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
//       System.out.println(nodeList.item(0).getNodeValue());
  	 	for (int i=0; i<nodeList.getLength();i++) {
		 String tmp = nodeList.item(i).getNodeValue();
		 String nID = xpath.compile("descendant-or-self::*[contains(@key,'"+tmp+"')]/entry[@key='id']/@value").evaluate(doc);
		 String sFile = xpath.compile("descendant-or-self::*[contains(@key,'"+tmp+"')]/entry[@key='node_settings_file']/@value").evaluate(doc);
		 String nType = xpath.compile("descendant-or-self::*[contains(@key,'"+tmp+"')]/entry[@key='node_is_meta']/@value").evaluate(doc);
		 String tmpN = sFile.substring(0, sFile.indexOf('/'));
		 System.out.println("tmp: "+tmp);
		 System.out.println("nid: "+nID);
		 System.out.println("sFile: "+sFile);
		 System.out.println("nType: "+nType);
		 System.out.println("tmpN: "+tmpN);
		 System.out.println("cNodes: "+cNodes);
		 kNode kn = new kNode(tmpN,baseID+nID);
		 cNodes++;
		 if (tmpN.indexOf(" (#")>0)
			 kn.setLabel(tmpN.substring(0,tmpN.indexOf(" (#")));
		 else 
			 kn.setLabel(tmpN);
		 if (nType.equalsIgnoreCase("true"))
			 kn.setMeta();
		 ArrayList<String> l = taxonomy.get(kn.getLabel());
		 if (l != null) {
//		 System.out.println(l.get(0));
//		 if (l.get(0) != null)
			 kn.setTaxonomy(l.get(0));
			 System.out.println("taxonomy: "+l.get(0));
			 
		 }		 
//		 if (!kn.isMeta())
//			 System.out.println(kn.getTaxonomy());
//		 System.out.println(kn.getFullID()+" "+nID+sFile+nType+tmpN+kn.isMeta());
		 NodeList outList = (NodeList) xpath.compile("//entry[@key='sourceID'][@value='"+nID+"']/../entry[@key='destID']/@value").evaluate(doc, XPathConstants.NODESET);
System.out.println("outList: "+outList.toString());
		 parseOut(baseID,outList,kn);
		 NodeList inList = (NodeList) xpath.compile("//entry[@key='destID'][@value='"+nID+"']/../entry[@key='sourceID']/@value").evaluate(doc, XPathConstants.NODESET);
		 System.out.println("inList: "+inList.toString());
		 parseIn(baseID,inList,kn); 

		 if (kn.isMeta())
			 wf.addMeta(kn.getFullID(), kn);
		 else {
			 wf.addNode(kn.getFullID(), kn);
			 if (taxonomy.get(kn.getLabel()).get(0) != "MetaNode" && !kn.isMeta()) 
					wf.addNonMeta();
			 if (taxonomy.get(kn.getLabel()).get(1).equals("Y"))
				 wf.addWrangling();
		 }
		 
//		 kn.getConn().forEach(ic -> System.out.println(kn.getFullID()+"->"+ic));
//		 Operation tmpO = new Operation(tmp,tmpN,tmpN.substring(0,tmpN.indexOf('(')-1), Integer.decode(tmp1));
//		 if (tmp2.endsWith(".knime")) {
//			 tmpO.setType("WF");
		 }
  	 	System.exit(0);
	}//readNodes

	public void parseOut(String baseID,NodeList connList,kNode kn) {
		 if (connList.getLength() < 1)
			 kn.setLast();
		 else
			 for (int j=0; j<connList.getLength();j++)
				 if (Integer.parseInt(connList.item(j).getNodeValue()) > -1)
				 kn.addOut(baseID+connList.item(j).getNodeValue());

		
	}//parseOut

	public void parseIn(String baseID,NodeList connList,kNode kn) {
		 if (connList.getLength() < 1)
			 kn.setFirst();
		 else
			 for (int j=0; j<connList.getLength();j++)
				 if (Integer.parseInt(connList.item(j).getNodeValue()) > -1)
				 kn.addIn(baseID+connList.item(j).getNodeValue());
		
	}//parseIn
	
}//End Class
