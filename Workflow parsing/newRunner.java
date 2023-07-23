package kNIMEWFs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;





public class newRunner {

	public static void main(String[] args) throws Throwable {
		// TODO Auto-generated method stub
		System.out.println(Calendar.getInstance().getTime());



		String file1="/home/phd/Downloads/WFs/5097_Scaffold_Analysis___Statistical_analysis_of_pre-selected_scaffolds___Scaffold_overlapping_profiles_finding____Common__scaffold_detecting___Substructure_searching-v1.knwf";
		String file2="/home/phd/Downloads/NodePit/nodepit.com/download/workflow/comKnime/com.knime.hub%2FUsers%2Fknime%2FExamples%2F01_Data_Access%2F04_Structured_Data%2F02_XML_processing_(Bush_books)_XPath";
		ArrayList<String> f1 = new ArrayList<String>();
		ArrayList<String> f2 = new ArrayList<String>();
		ArrayList<String> f3 = new ArrayList<String>();
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<String> errorF = new ArrayList<String>();
		String wfDir = "/home/phd/Downloads/WFs/";
		String nPitDir = "/home/phd/Downloads/NodePit/nodepit.com/download/workflow/comNodePit/"; 
		String kHubDir = "/home/phd/Downloads/NodePit/nodepit.com/download/workflow/comKnime/"; 

		String errorDir = "/home/phd/Downloads/NodePit/nodepit.com/download/workflow/Error_parse/"; 

		f1 = getFiles(wfDir,files);
		System.out.println(f1.size());
		f2 = getFiles(nPitDir,files);
		System.out.println(f2.size());
		f3 = getFiles(kHubDir,files);
		System.out.println(f3.size());
		System.out.println(files.size());
//		System.exit(0);
//		files = getFiles(errorDir,files);
//		System.out.println(files.size());
		ArrayList<WF> wfs = new ArrayList<WF>();
		ArrayList<WF> wfs1 = new ArrayList<WF>();
		ArrayList<WF> wfs2 = new ArrayList<WF>();
		ArrayList<WF> wfs3 = new ArrayList<WF>();
		ArrayList<WF> wfs4 = new ArrayList<WF>();
		ArrayList<WF> wfs5 = new ArrayList<WF>();
		parse parser = new parse();
		int nodeTrack = 0;
		int metaTrack = 0;
		for (String file : files) {
			try {
				WF t = parser.parseF(file2);
				t.resetIDs();
				if (t.getWrangling() > 0) {
					double w = t.getWrangling().doubleValue();
					double nm = t.getNonMeta().doubleValue();
					if (w/nm >= 0.8)
						wfs1.add(t);
					if (w/nm >= 0.6)
						wfs2.add(t);
					if (w/nm >= 0.4)
						wfs3.add(t);
					if (w/nm >= 0.2)
						wfs4.add(t);
					wfs5.add(t);
					
//					wfs.add(t);
					nodeTrack += t.getNodes().size();
					metaTrack += t.getMeta().size();
				}
			}
			catch (Exception e) {
				errorF.add(file2);
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}		
		System.out.println(Calendar.getInstance().getTime());

		System.out.println(errorF.size());
		System.out.println("WFs="+wfs.size()+", Nodes="+nodeTrack+", Metas="+metaTrack);
		
		pafiFile2(wfs1,"Group4s_80.txt");
		pafiFile2(wfs2,"Group4s_60.txt");
		pafiFile2(wfs3,"Group4s_40.txt");
		pafiFile2(wfs4,"Group4s_20.txt");
		pafiFile2(wfs5,"Group4s_0.txt");
//		pafiFile(wfs,"oldFull.txt");
		
//		System.exit(0);
		//		FileWriter myW1 = new FileWriter(testOut.csv);
		System.out.println("1: "+wfs1.size());
		System.out.println("2: "+wfs2.size());
		System.out.println("3: "+wfs3.size());
		System.out.println("4: "+wfs4.size());
		System.out.println("5: "+wfs5.size());
		
		FileWriter wfStats;
		try {
			wfStats = new FileWriter("wfStats.csv");
			wfStats.write("file,t,wName,Totals,NonMeta,Wrangling\n");
			for (WF w: wfs) {
				wfStats.write("\""+w.getFile()+"\""+",Node,\""+w.getName()+"\",\""+w.getTotal()+"\",\""+w.getNonMeta()+"\",\""+w.getWrangling()+"\"\n");

			}//end for WF loop
			wfStats.close();
			System.out.println(Calendar.getInstance().getTime());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.exit(0);
		
		FileWriter myW2;
		try {
			myW2 = new FileWriter("testOut4.csv");


			//		ArrayList<String> vert = new ArrayList<String>();
			//		ArrayList<String> edg = new ArrayList<String>();

			//		System.out.println("t # Size="+ reader.getAllWF().size()+" WF"+file);

			//			myW1.write("t #  WF"+w.getName()+"\n");
			myW2.write("file,t,wName,FullID,nLabel\n");
			for (WF w: wfs) {
				Iterator<kNode> in = w.getNodes().values().iterator();
				Iterator<kNode> im = w.getMeta().values().iterator();

				while (in.hasNext()) {

					kNode k = in.next();
					myW2.write("\""+w.getFile()+"\""+",Node,\""+w.getName()+"\",\""+k.getFullID()+"\",\""+k.getTaxonomy()+"\",\""+k.getLabel()+"\"\n");
				}//end while loop
				while (im.hasNext()) {

					kNode k = im.next();
					myW2.write("\""+w.getFile()+"\""+",Meta,\""+w.getName()+"\",\""+k.getFullID()+"\",\""+k.getLabel()+"\"\n");
				}//end while loop
			}//end for WF loop
			myW2.close();
			System.out.println(Calendar.getInstance().getTime());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		for (String f : errorF)
			System.out.println(f);

	}//End main

	public static ArrayList<String> getFiles(String dir,ArrayList<String> filesList){

		File folder = new File(dir);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile())
				filesList.add(dir + listOfFiles[i].getName());
		}
		return filesList;
	}


	public static void pafiFile(ArrayList<WF> wfs,String outTXT) {
//		String outTXT = "newAll_.txt";
		try {
			FileWriter myWriter = new FileWriter(outTXT);

			Integer tId = 0;
			for (WF w: wfs) {
				ArrayList<String> vert = new ArrayList<String>();
				ArrayList<String> edg = new ArrayList<String>();

//				myWriter.write("t # ID="+ tId +" WF"+w.getName()+"\n");
				myWriter.write("t # ID="+tId+"\t"+w.getFile()+"\t WF \n");

				Iterator<kNode> in = w.getNodes().values().iterator();
				Iterator<kNode> im = w.getMeta().values().iterator();

				while (in.hasNext()) {

					kNode k = in.next();
					String id = k.getFullID().replace(":","");
//					System.out.println("v \t "+id+"\t"+k.getTaxonomy()+"\n");
					vert.add("v \t "+id+"\t"+k.getTaxonomy()+"\n");

					List<String> subList = k.getConn().subList(0, k.getConn().size());
					Collections.sort(subList);
					Iterator<String> c = subList.iterator();
					ArrayList<String> ConnList = new ArrayList<String>();
					while (c.hasNext()) {
						String target = c.next().replace(":", "");

						if (!ConnList.contains(target)) {
//							System.out.println("u \t "+id+"\t"+target+"\t E \n");
							if (new BigInteger(id).compareTo( new BigInteger(target)) < 1 )
								edg.add("u \t "+id+"\t"+target+"\t E \n");
							else 
								edg.add("u \t "+target+"\t"+id+"\t E \n");

							ConnList.add(target);
						}
					}
				} //End while in.hasNext()
				while (im.hasNext()) {

					kNode k = im.next();
					String id = k.getFullID().replace(":","");
//					System.out.println("v \t "+id+"\t"+k.getLabel()+"\n");
					vert.add("v \t "+id+"\t"+k.getLabel()+"\n");

					Iterator<String> c = k.getConn().iterator();
					ArrayList<String> ConnList = new ArrayList<String>();
					while (c.hasNext()) {
						String target = c.next().replace(":", "");

						if (!ConnList.contains(target)) {
//							System.out.println("u \t "+id+"\t"+target+"\t E \n");
							if (new BigInteger(id).compareTo( new BigInteger(target)) < 1 )
								edg.add("u \t "+id+"\t"+target+"\t E \n");
							else 
								edg.add("u \t "+target+"\t"+id+"\t E \n");

							ConnList.add(target);
						}
					}
				}// End While im.hasNext

				System.out.println("Added tID "+tId);
				Iterator<String> vl = vert.iterator();
				while (vl.hasNext())
					myWriter.write(vl.next());
				//					System.out.print(vl.next());
				Iterator<String> el = edg.iterator();
				if (vert.size()>0)
				while (el.hasNext())
					myWriter.write(el.next());

				tId++;
			}

			myWriter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}// End pafiFile

	public static void pafiFile2(ArrayList<WF> wfs,String outTXT) {
//		String outTXT = "newAll_.txt";
		try {
			FileWriter myWriter = new FileWriter(outTXT);

			Integer tId = 0;
			for (WF w: wfs) {
				ArrayList<String> vert = new ArrayList<String>();
				ArrayList<String> edg = new ArrayList<String>();

//				myWriter.write("t # ID="+ tId +" WF"+w.getName()+"\n");
				myWriter.write("t # ID="+tId+"\t"+w.getFile()+"\t WF \n");

				Iterator<kNode> in = w.getNodes().values().iterator();
				Iterator<kNode> im = w.getMeta().values().iterator();

				while (in.hasNext()) {

					kNode k = in.next();
//					String id = k.getFullID().replace(":","");
					Integer id = k.getID();
					//					System.out.println("v \t "+id+"\t"+k.getTaxonomy()+"\n");
					vert.add("v \t "+id+"\t"+k.getTaxonomy()+"\n");

//					List<String> subList = k.getConn().subList(0, k.getConn().size());
					List<Integer> subList = k.getResetConn().subList(0, k.getResetConn().size());
					Collections.sort(subList);
					Iterator<Integer> c = subList.iterator();
					ArrayList<Integer> ConnList = new ArrayList<Integer>();
					while (c.hasNext()) {
//						String target = c.next().replace(":", "");
						Integer target = c.next();
						if (!ConnList.contains(target)) {
//							System.out.println("u \t "+id+"\t"+target+"\t E \n");
//							if (new BigInteger(id).compareTo( new BigInteger(target)) < 1 )
							if (id < target)
								edg.add("u \t "+id+"\t"+target+"\t E \n");
							else 
								edg.add("u \t "+target+"\t"+id+"\t E \n");

							ConnList.add(target);
						}
					}
				} //End while in.hasNext()
				while (im.hasNext()) {

					kNode k = im.next();
					Integer id = k.getID();
//					String id = k.getFullID().replace(":","");
//					System.out.println("v \t "+id+"\t"+k.getLabel()+"\n");
					vert.add("v \t "+id+"\t"+k.getLabel()+"\n");

//					Iterator<String> c = k.getConn().iterator();
					Iterator<Integer> c = k.getResetConn().iterator();
					ArrayList<Integer> ConnList = new ArrayList<Integer>();
					while (c.hasNext()) {
//						String target = c.next().replace(":", "");
						Integer target = c.next();
						if (!ConnList.contains(target)) {
//							System.out.println("u \t "+id+"\t"+target+"\t E \n");
//							if (new BigInteger(id).compareTo( new BigInteger(target)) < 1 )
							if (id < target)
								edg.add("u \t "+id+"\t"+target+"\t E \n");
							else 
								edg.add("u \t "+target+"\t"+id+"\t E \n");

							ConnList.add(target);
						}
					}
				}// End While im.hasNext

				System.out.println("Added tID "+tId);
				Iterator<String> vl = vert.iterator();
				while (vl.hasNext())
					myWriter.write(vl.next());
				//					System.out.print(vl.next());
				Iterator<String> el = edg.iterator();
				if (vert.size()>0)
				while (el.hasNext())
					myWriter.write(el.next());

				tId++;
			}

			myWriter.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}// End pafiFile2


}//End class
