package com.demo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

public class Main {
		public static void main(String ar[]){


			ReportGenerator reportGenerator =new ReportGenerator();
			reportGenerator.addFiles("fil1",100,List.of());
			reportGenerator.addFiles("fil2",200,List.of("Collection1"));
			reportGenerator.addFiles("fil5",300,List.of("collection2"));
			reportGenerator.addFiles("fil3",200,List.of("Collection1"));
			reportGenerator.addFiles("fil5",400,List.of("collection3"));
			reportGenerator.addFiles("fil6",10,List.of());


			Report report= reportGenerator.getReport(6);
			System.out.println(report.totalSize);
			System.out.println(report.collections);

		}
}
//test-1 100
//test-1 200

class Collection{


	String name;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Collection that = (Collection) o;
		return totalSize == that.totalSize && Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, totalSize);
	}

	int totalSize;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}

}

class ReportGenerator {

Map<String,Collection> mapCollection =new HashMap<>();
TreeSet<Collection> treeSet=new TreeSet<>((a, b)->{
	if(a.totalSize==b.totalSize){
		return a.name.compareToIgnoreCase(b.name);
	}
	return b.totalSize-a.totalSize;
});
int totalSize;


void addIntoCollection(String collection,String fileName,int size){

	if(collection!=null && !collection.equals("")){
		if(!mapCollection.containsKey(collection)){

			Collection newCollection=new Collection();
			newCollection.setTotalSize(size);
			newCollection.setName(collection);
			mapCollection.put(collection,newCollection);
			treeSet.add(newCollection);
			return;
		}

		Collection current= mapCollection.get(collection);
		current.setTotalSize(current.getTotalSize()+size);
		treeSet.remove(current);
		treeSet.add(current);
	}
}

void addFiles(String fileName,int size,List<String> collections){

	totalSize+=size;
	for(String collection:collections){
		addIntoCollection(collection,fileName,size);
	}
}
	Report getReport(int limit){
		Iterator<Collection> itr=treeSet.iterator();
		List<String> list=new LinkedList<>();
		while(itr.hasNext() && limit>0){
			list.add(itr.next().getName());
			limit--;
		}
		Report report=new Report(totalSize,list);
		return report;
	}
}

class Report{
	int totalSize;

	public Report(int totalSize, List<String> collections) {
		this.totalSize = totalSize;
		this.collections = collections;
	}

	List<String> collections;

}
