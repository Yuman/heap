Effective Troubleshooting with Differential Heap Analysis
========================================================

Beyond detecting memory leaks, differential heap analysis 
pinpoints trouble spots (locks, blocks, bottlenecks and other 
anomalies) by comparing heap dumps.

A heap dump is like a blood sample for health diagnosis purpose. Just like an illness leaves markers in the blood, behaviors of applications leave traces in the heap, causing variations in object population.

The sizes of population of objects are compared across load conditions. The relative changes in population size, when sorted, form a sequence, reflecting the object creation and consumption. The analysis points to the front of the object queue-up in the heap. This complements CPU profiling and is particularly useful where bottlenecks do not involve high CPU burn.

For more details, refer to the presentation.

