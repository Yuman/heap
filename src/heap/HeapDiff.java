package heap;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Kenny.Yu@att.com
 *
 */
public class HeapDiff {
	private MergedHeapHisto merged = new MergedHeapHisto();
	SimpleHistogram ratio;

	public static void main(String[] args) throws IOException {
		if (args.length == 0 || args[0].length() < 3) {
			System.out.println("Please provide a configuration file as the argument.");
			System.out.println("For an example of configuration file, look inside the jar.");
		}
		System.out.println("Using " + args[0] + " as the configuration");
		Config.configFile = args[0];
		Config.load();
		HeapDiff diff = new HeapDiff();
		diff.analyze();
		diff.outputRatio();
	}

	void analyze() throws IOException {
		List<String> base = Config.getBaselineHistos();
		String subj = Config.getSubjectHisto();
		if (!base.contains(subj)) {
			base.add(subj);
		}
		merge(base);
		ratio(subj);
	}

	void merge(List<String> base) throws IOException {
		for (String b : base) {
			InputStream is = new FileInputStream(b);
			HeapHistogram hhg = new HeapHistogram(is, false);
			SimpleHistogram histg = SimpleHistogram.create(hhg);
			System.out.println("adding " + b + " total bytes: " + histg.getTotalHeapBytes());
			merged.add(histg);
		}
	}

	void ratio(String sub) throws IOException {
		InputStream is = new FileInputStream(sub);
		HeapHistogram hhb = new HeapHistogram(is, false);
		SimpleHistogram histb = SimpleHistogram.create(hhb);
		System.out.println("ratioing " + sub + " total bytes: " + histb.getTotalHeapBytes());
		ratio = merged.ratio(histb);
	}

	void outputRatio() throws IOException, IOException {
		ratio.export(new PrintWriter(Config.getRatioExportPath()));
	}

}
