package heap;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Kenny Yu
 *
 */
public class MergedHeapHisto {
	private Map<String, Long> merged = new TreeMap<String, Long>();
	private int histoCount = 0;
	private long totalHeapBytes = 0;

	public Map<String, Long> getMerged() {
		return merged;
	}

	public int getHistoCount() {
		return histoCount;
	}

	public SimpleHistogram ratio(SimpleHistogram num) {
		if (histoCount < 2) {
			throw new IllegalStateException("ratio is defined in the form x/(1+2...+x)");
		}
		double LittlesRatio = (1.0 * histoCount * num.getTotalHeapBytes() - totalHeapBytes) / totalHeapBytes;
		// merged has all the denominators
		SimpleHistogram ratios = new SimpleHistogram();
		for (Map.Entry<String, Long> denum : merged.entrySet()) {
			int numB = 0;
			if (num.get(denum.getKey()) != null) {
				double ratio = (num.get(denum.getKey()).doubleValue() * histoCount - denum.getValue().longValue())
						/ denum.getValue().longValue();
				if (ratio < -0.01 || ratio > LittlesRatio) {
					numB = (int) (ratio * 1000);
					ratios.addEntry(denum.getKey(), numB);
				}
			}
		}
		ratios.sortByValue();
		return ratios;
	}

	public void add(SimpleHistogram cc) {
		for (Map.Entry<String, Long> entry : cc.getEntries()) {
			Long matched = merged.get(entry.getKey());
			if (matched == null) {// no match
				merged.put(entry.getKey(), entry.getValue());
			} else {
				merged.put(entry.getKey(), entry.getValue().longValue() + matched.longValue());
			}
		}
		histoCount++;
		totalHeapBytes += cc.getTotalHeapBytes();
	}

	@Override
	public String toString() {
		StringBuilder res = new StringBuilder();
		int count = 0;
		for (Map.Entry<String, Long> entry : merged.entrySet()) {
			count++;
			res.append(entry.getKey());
			res.append('=');
			res.append(entry.getValue()).append('\n');
			if (count > 5)
				break;
		}
		res.append(merged.size());
		return res.toString();
	}

}
