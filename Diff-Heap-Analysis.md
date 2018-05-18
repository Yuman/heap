
Effective Troubleshooting with Differential Heap Analysis
=========================================================

# Summary (max 750c)

Every memory-allocating operation leaves its footprint in the heap, even if the objects it creates are short-lived and do not cause memory leaks. Undesirable behaviors under load, such as locking, blocking, waiting and cache misses, skew the distribution of object populations. Instead of browsing and querying a single heap, the techniques here  are focused on breadth-wise comparative analysis of multiple heap profiles, usually of a problematic one against a baseline. Heap snapshots are produced with jmap or VisualVM. The relative growths of objects reveal the source of anomalies. Identifying leaks is trivial with the techniques. The techniques are used to exposed stalled external calls, blocked executions and other deviations.

# Detailed Description (max 3950c)

## Background
Heap analysis is not only useful for memory leaks, or OOMEs. Since every memory-allocating operation leaves its footprint in the heap, a heap analysis is akin to a blood test on a human patient. Memory problems are often the symptom of cumulative effects of hidden bugs and unexpected application behaviors. 

Conventional heap analysis tools, such as jhat which is part of JDK 1.7, and techniques are concerned with in-depth accounting and querying of the object population in one heap.It is difficult to discern what is abnormal given the huge number of classes and the complex patterns of instance populations without systematically comparing against the backgroud knowledge on instance distributions.

## Objectives
The techniques here enables the comparative study of multiple jmap histograms across JVMs, across time, releases and load conditions. The differences in object population can reveal deviations in CPU activities and internal or external blockage.

## Object Population Distribution and Variation
In a server-side application, the JVM heap content is a dynamic response to the application load. In steady state under load, the population of live objects is stable for each class and follows Little’s Law (http://en.wikipedia.org/wiki/Little%27s_law): 
P = A * V
where P is the population, or instance count, of a class, A is the rate of allocation, and V is the time of liveness of the objects. On a memory utilization chart, the height of the trough upon a GC run is the live population, P; the slope of ascension, A.
Disruption of the steady state may come from change in A, due to alteration of execution path, or V due to locks, blocks or waits along the execution.
Application-scope objects have infinite Vs and their populations remain constant. Session-scope objects have Vs measured by the minute. Request-scope object have Vs by the second. At moderate load levels until the server is stressed, for both session and request scope objects, Ps are proportional to the load.

## Acquisition of Heap Profiles
A heap profile can be acquired in with ‘jmap -histo:live $pid >server_node.jmp’ in the form of a histogram of object counts or bytes, containing [#instances; #bytes; class name]. Jhat can extract similar data from a heap dump. There are a few points during the lifecycle of an application where a heap profile is of interest.
Base 0, after initial light traffic, and when the application is not receiving traffic. At this point, the application is fully initialized and caches are populated.
Base 1, when the application is receiving moderate traffic of unit 1.
For leak detection, after the traffic is stopped, compared a suspect profile against Base 0.
For detection of functional anomalies, acquire a profile under load, and compare it against Base 1.

## Heap Comparisons for Detection of Anomalies
Leak detection can be achieved by comparing two profiles at different times and similar load conditions, with zero load being a simplifying condition. Leaks usually stand out by orders of magnitude upon the analysis.
Detection of anomalies due to deviation in retention times (locking, blocking, waiting, cache misses) require finer control of the load condition since the live object population is a function of load.

## Pinpointing Abnormal Populations by Relative Growth Rates (RGRs)
The suspect objects can be identified by calculating their relative growth rates, ∂P/∂t/P, the highest of which form a cluster that reveals the root cause.
The methodology consists of three steps: 1. sort the base profile by the class name; 2. in the problematic profile, for each class with an instance count P1, look up for its count in the base profile, P0, and calculate (P1/P0 - 1), which is the relative growth rate; 3. Identify domain objects with high RGRs.
Example problems identified with the techniques: hanging sessions, stalled external calls.

## References:
https://plumbr.eu/blog/memory-leaks/solving-outofmemoryerror-memory-profilers
https://plumbr.eu/blog/memory-leaks/solving-outofmemoryerror-jdk-tools
https://code.google.com/p/javamelody/source/browse/trunk/javamelody-core/src/main/java/net/bull/javamelody/HeapHistogram.java
