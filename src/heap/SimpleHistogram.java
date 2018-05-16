package heap;

import java.io.IOException;
import java.io.Writer;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
/**
 * @author Kenny Yu
 *
 */
public class SimpleHistogram {
	private Map<String, Long> histo = new TreeMap<String, Long>();
	private long totalHeapBytes;

	static SimpleHistogram create(HeapHistogram hhg) {
		SimpleHistogram hist = new SimpleHistogram();
		for (HeapHistogram.ClassInfo ci : hhg.getHeapHistogram()) {
			addClassInfo(hist, ci);
		}
		for (HeapHistogram.ClassInfo ci : hhg.getPermGenHistogram()) {
			addClassInfo(hist, ci);
		}
		hist.totalHeapBytes = hhg.getTotalHeapBytes();
		return hist;
	}

	private static void addClassInfo(SimpleHistogram hist, HeapHistogram.ClassInfo ci) {
		if (!ci.getName().matches(Config.getExcludes()))
			hist.addEntry(ci.getName(), ci.getInstancesCount());
	}

	public long getTotalHeapBytes() {
		return totalHeapBytes;
	}

	void setTotalHeapBytes(long totalHeapBytes) {
		this.totalHeapBytes = totalHeapBytes;
	}

	public Long get(String cls) {
		return histo.get(cls);
	}

	public void addEntry(String cls, long count) {
		histo.put(cls, count);
	}

	public Set<Map.Entry<String, Long>> getEntries() {
		return histo.entrySet();
	}

	public int size() {
		return histo.size();
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		int count = 0;
		for (Map.Entry<String, Long> entry : histo.entrySet()) {
			count++;
			res.append(entry.getKey());
			res.append('=');
			res.append(entry.getValue()).append('\n');
			if (count > 5)
				break;
		}
		res.append(histo.size());
		return res.toString();
	}

	public void export(Writer wr) throws IOException {
		for (Map.Entry<String, Long> entry : histo.entrySet()) {
			wr.write(entry.getKey());
			wr.write(',');
			wr.write(entry.getValue().toString());
			wr.write('\n');
		}
		wr.flush();
		wr.close();
	}

	public void sortByValue() {
		ValueComparator vc = new ValueComparator(histo);
		TreeMap<String, Long> sortedMap = new TreeMap<String, Long>(vc);
		sortedMap.putAll(histo);
		histo = sortedMap;
	}

	static class ValueComparator implements Comparator<String> {

		Map<String, Long> map;

		public ValueComparator(Map<String, Long> base) {
			this.map = base;
		}

		public int compare(String a, String b) {
			if (map.get(a).longValue() >= map.get(b).longValue()) {
				return -1;
			} else {
				return 1;
			} // returning 0 would merge keys
		}
	}
}
