Effective Troubleshootin with Differential Heap Analysis
========================================================

Beyond detecting memory leaks, differential heap analysis 
pinpoints trouble spots (locks, blocks, bottlenecks and other 
anomalies) by comparing heap dumps.

The sizes of population of objects are compared. The relative changes in population size, when sorted, form a sequence, reflecting the object creation and consumption. The analysis points to the front of the object queue-up in the heap. This complements CPU profiling and is particularly useful where bottlenecks does not involve high CPU burn.

For more details, refer to the presentation.

