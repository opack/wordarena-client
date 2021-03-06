package com.slamdunk.toolkit.lang;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Cette classe permet de gérer un tableau à double entrée dont chaque entrée
 * est de type EntryType, et dont la valeur est de type ValueType.
 */
public class DoubleEntryArray<Entry1Type, Entry2Type, ValueType> {
    private Map<Entry1Type, Map<Entry2Type, ValueType>> data;

    public DoubleEntryArray() {
        data = new HashMap<Entry1Type, Map<Entry2Type, ValueType>>();
    }

    public ValueType get(Entry1Type entry1, Entry2Type entry2) {
        Map<Entry2Type, ValueType> values = data.get(entry1);
        if (values == null) {
            return null;
        }
        return values.get(entry2);
    }

    public void put(Entry1Type entry1, Entry2Type entry2, ValueType value) {
        Map<Entry2Type, ValueType> values = data.get(entry1);
        if (values == null) {
            values = new HashMap<Entry2Type, ValueType>();
            data.put(entry1, values);
        }
        values.put(entry2, value);
    }
    
    public void remove(Entry1Type entry1, Entry2Type entry2, ValueType value) {
        Map<Entry2Type, ValueType> values = data.get(entry1);
        if (values == null) {
            return;
        }
        values.remove(entry2);
    }

	public void clear() {
		for (Map<Entry2Type, ValueType> map : data.values()) {
			map.clear();
		}
		data.clear();
	}

	/**
	 * Retourne l'ensemble des clés de type Entry1Type
	 * @return 
	 */
	public Set<Entry1Type> getEntries1() {
		return data.keySet();
	}
	
	/**
	 * Retourne l'ensemble des clés de type Entry2Type
	 * @return 
	 */
	public Set<Entry2Type> getEntries2(Entry1Type entry1) {
		Map<Entry2Type, ValueType> values = data.get(entry1);
        if (values == null) {
            return null;
        }
		return values.keySet();
	}
	
	public Collection<ValueType> getValues(Entry1Type entry1) {
		Map<Entry2Type, ValueType> values = data.get(entry1);
        if (values == null) {
            return null;
        }
        return values.values();
	}

	public boolean isEmpty() {
		return data.isEmpty();
	}
}