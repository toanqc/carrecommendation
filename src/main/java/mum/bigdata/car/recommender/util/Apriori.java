package mum.bigdata.car.recommender.util;

import java.util.*;

/*
 * Apriori algorithm implementation (https://en.wikipedia.org/wiki/Apriori_algorithm)
 * Returns frequent item set {item, frequency} given a minimum support
 * @author Mark Pit
 */
public class Apriori {

    public static void main(String[] args) throws Exception {
    	List<int[]> data = new ArrayList<int[]>();
    	
    	data.add(new int[]{3,2,1,3,4});  	
    	data.add(new int[]{1,9,3,4,7,3,2,5});  
    	data.add(new int[]{7,3,4,5,6,1,2});  
    	
        Apriori ap = new Apriori(data, 1);
        ArrayList<Integer> resultSet = ap.getItemSet();
        System.out.println(Arrays.deepToString(resultSet.toArray()));
        System.out.println(QueryHelper.formatToLikeQuery(resultSet, "cartrace"));
    }

    private List<int[]> dataSet;
    private double minSup; 
 
    public  Apriori(List<int[]> data, int s){
    	dataSet = data;  
    	minSup = s;  	
    }

    public ArrayList<Integer> getItemSet(){
        ArrayList<SetItem> resultSet = calculateFrequentItemsets(createList());
        return getSortedSet(resultSet);
    }    
    
	public ArrayList<Integer> createList() {
    	ArrayList<Integer> items = new ArrayList<Integer>();
    
    	for(int i = 0; i < dataSet.size(); i++){
    		int[] row = dataSet.get(i);
    		
    		for(int j = 0; j < row.length; j++){
				items.add(row[j]);
    		}
    	}  

    	return items;
	}	
		
	public ArrayList<SetItem> calculateFrequentItemsets(ArrayList<Integer> set){
		ArrayList<SetItem> resultSet = new ArrayList<SetItem>();
		int count, i, j;
		int size = set.size();
		
		Collections.sort(set);
		
		//Debug
		//System.out.println(Arrays.deepToString(set.toArray()));
		
		for(i = 0; i < size; i++){
			count = 0;	
			
			int item = set.get(i);
			count++;

			for(j = i+1; j < size; j++){
				if( set.get(j) != item ){
					break;
				}
				
				count++;				
			}
			
			i = j-1;

			if( count >= minSup ){
				resultSet.add( new SetItem(item, count) );
			}
		}
		
		//Debug
//        for(SetItem t: resultSet){
//        	System.out.print(t.getItem() + "=" + t.getCount() + ",");
//        }		
//        System.out.println();
		
		return resultSet;
	}
	
	public ArrayList<Integer> getSortedSet(ArrayList<SetItem> set){
		Collections.sort(set, new MyComparator());   
        ArrayList<Integer> result = new ArrayList<Integer>();
        
        for(SetItem t: set){
        	result.add(t.getItem());
        }
        
        return result;
	}
	
}

class MyComparator implements Comparator<SetItem>{
	
    @Override
    public int compare(SetItem n1, SetItem n2) {
    	
        if(n1.getCount() < n2.getCount()){
            return 1;
        } else {
            return -1;
        }
    }	
    
}