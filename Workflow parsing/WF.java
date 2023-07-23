package kNIMEWFs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WF {
	String name="";
	String id;
	HashMap<String,kNode> nodes;
	HashMap<String,kNode> metaNodes;
	String fName="";
	Integer wrangling=0;
	Integer nonMeta=0;
	public void addWrangling() {
		wrangling++;
	}//addWrangling
	public void addNonMeta() {
		nonMeta++;
	}//addNonMeta
	public Integer getWrangling() {
		return wrangling;
	}//getWrangling
	public Integer getNonMeta() {
		return nonMeta;
	}//getNonMeta
	public Integer getTotal() {
		return (nodes.size()+metaNodes.size());
	}//getTotal
	
	public double getPctWrangling() {
		if (this.getNonMeta() > 0)
			return (wrangling/nonMeta);
		else return 0;
	}//getPctWrangling
	public WF() {
		// TODO Auto-generated constructor stub
		nodes = new HashMap<String,kNode>();
		metaNodes = new HashMap<String,kNode>();
	}// End constructor
	public WF(String n) {
		nodes = new HashMap<String,kNode>();
		metaNodes = new HashMap<String,kNode>();
		name = n;
	}// End constructor
	public WF(String n,String i) {
		nodes = new HashMap<String,kNode>();
		metaNodes = new HashMap<String,kNode>();
		name = n;
		id = i;
	}// End constructor
	public void setName(String n) {
		name = n;
	}//setName
	public String getName() {
		return name;
	}//getName
	public boolean hasName() {
		return (name.length()>0);
	}//hasName
	public void setID(String i) {
		id = i;
	}//setName
	public String getID() {
		return id;
	}//getID
	public void addNode(String i,kNode n) {
		nodes.put(i,n);
	}//addNode
	public void addMeta(String i,kNode n) {
		metaNodes.put(i,n);
	}//addNode

	public HashMap<String,kNode> getNodes() {
		return nodes;
	}//addNode
	public HashMap<String,kNode> getMeta() {
		return metaNodes;
	}//addNode
	public void setFile(String f) {
		fName = f;
	}//setFile
	public String getFile() {
		return fName;
	}//getFile
	public void resetIDs() {
		Map<String,Integer> newIDmap = new HashMap<String,Integer>();
		Iterator<kNode> nIter = this.getNodes().values().iterator();
		Iterator<kNode> mIter = this.getMeta().values().iterator();
		Integer tc = 0;
		while (nIter.hasNext()) {
			kNode nn = nIter.next();
			Integer t = newIDmap.get(nn.getFullID());
			if (t != null)
				nn.resetID(t);
			else {
				newIDmap.put(nn.getFullID(), tc);
				nn.resetID(tc);
				tc++;
			}
			
			
			Iterator<String> sC = nn.getConn().iterator();
			while (sC.hasNext()) {
				String cId = sC.next();
				Integer tt = newIDmap.get(cId);
				if (tt != null)
					nn.addNewOut(tt);
				else {
					newIDmap.put(cId, tc);
					nn.addNewOut(tc);
					tc++;
				}
				
			}// while sC
			
		}// while nIter

		while (mIter.hasNext()) {
			kNode nn = mIter.next();
			Integer t = newIDmap.get(nn.getFullID());
			if (t != null)
				nn.resetID(t);
			else {
				newIDmap.put(nn.getFullID(), tc);
				nn.resetID(tc);
				tc++;
			}
			
			
			Iterator<String> sC = nn.getConn().iterator();
			while (sC.hasNext()) {
				String cId = sC.next();
				Integer tt = newIDmap.get(cId);
				if (tt != null)
					nn.addNewOut(tt);
				else {
					newIDmap.put(cId, tc);
					nn.addNewOut(tc);
					tc++;
				}
				
			}// while sC
			
		}// while mIter
		
	}//resetIDs
}//End class
