package kNIMEWFs;

import java.util.ArrayList;

public class kNode {
	int metaNode=0;
	String name;
	Integer id;
	String fullID;
	ArrayList<Integer> newOut ;

	ArrayList<String> out ;
	ArrayList<String> in ;
	int end=0;
	int start=0;
	String label;
	String taxonomy;
	
//	public kNode() {
//		// TODO Auto-generated constructor stub
//	}//End Constructor
	public kNode(String n,String i) {
		name = n;
		fullID = i;
		newOut = new ArrayList<Integer>();
		out = new ArrayList<String>();
		in = new ArrayList<String>();
	}//End Constructor
	
	public void setMeta() {
		metaNode=1;
		label = "MetaNode";
	}//setMeta
	public boolean isMeta() {
		return (metaNode == 1);
	}//isMeta
	public void setName(String n) {
		name=n;
	}//setName
	public String getName() {
		return name;
	}//getName
	public void setID(String i) {
		fullID=i;
	}//setID
	public void resetID(Integer i) {
		id = i;
	}
	public Integer getID() {
		return id;
	}//getID
	public String getFullID() {
		return fullID;
	}//getFullID
	public void addOut(String c) {
		out.add(c);
	}//End of addOut
	public void addNewOut(Integer i) {
		newOut.add(i);
	}
	public void addIn(String c) {
		in.add(c);
	}//End of addIn
	public ArrayList<String> getConn() {
		return out;
	}//End of getConn
	public ArrayList<Integer> getResetConn() {
		return newOut;
	}//End of getConn
	public void setLast() {
		end = 1;
	}//End of setLast
	public boolean hasNext() {
		return (end ==0);
	}//hasNext

	public void setFirst() {
		start = 1;
	}//End of setFirst
	public boolean hasPre() {
		return (start == 0);
	}//hasPre
	public void setLabel(String ln) {
		label=ln;
	}//setLabel
	public String getLabel() {
		return label;
	}
	
	public void setTaxonomy(String l) {
		taxonomy = l;
	}

	public String getTaxonomy () {
		if (taxonomy != null && !taxonomy.isEmpty())
			return taxonomy;
		else return label;
	}

	

}//End class
