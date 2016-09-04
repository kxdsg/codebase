package com.argus.util;

import java.util.*;

/**
 * User: xingding
 * Date: Nov 16, 2011
 * Time: 11:09:51 AM
 */
public class CollectionUtil {	

    public static void printList(List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String s = (String) it.next();
            System.out.format("[%s]%n", s);
        }
    }

    public static void printMap(Map map) {
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            System.out.format("[%s]:[%s]%n", entry.getKey(), entry.getValue());
        }
    }

    public static <T> List getDuplicate(Collection<T> list) {

        final List<T> duplicatedObjects = new ArrayList<T>();
        Set<T> set = new HashSet<T>() {
            public boolean add(T e) {
                if (contains(e)) {
                    duplicatedObjects.add(e);
                }
                return super.add(e);
            }
        };
        for (T t : list) {
            set.add(t);
        }
        return duplicatedObjects;
    }


    public static <T> boolean hasDuplicate(Collection<T> list) {
        return getDuplicate(list).isEmpty();
    }

    public static boolean compareList(List list1, List list2) {
        Collections.sort(list1);
        Collections.sort(list2);
        return list1.equals(list2);
    }

    public static Collection union(Collection col1, Collection col2){
		Set union = new HashSet(col1);
		union.addAll(col2);
		return new ArrayList(union);
	}

	public static Collection intersect(Collection col1, Collection col2){
		Set intersect = new HashSet(col1);
		intersect.retainAll(col2);
		return intersect;
	} 
}
