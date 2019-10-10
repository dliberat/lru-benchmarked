
# LRU

## About

1. A simple implementation of a thread-safe, generic, least-recently-used cache.
2. A sample benchmarking script using `jhm`.

## Benchmarks

To run the benchmarks, execute the following commands from the repository's base directory.

```
$ mvn clean install
$ java -jar target/benchmarks.jar LruBenchmark
```

Sample benchmarking output is shown below. The __baseline__ test fetches data `workloadSize` times from a resource that simulates expensive CPU computation.
The __cache__ test performs the same work, but half of the data is already present in the cache. 
   
 

|Benchmark                  |(workloadSize) |  Mode | Cnt  |  Score |   Error | Units |
|---------------------------|---------------|-------|------|--------|---------|-------|
|LruBenchmark.testBaseline  |            10 | thrpt |   5  | 52.332 |±  1.185 |ops/ms |
|LruBenchmark.testBaseline  |           100 | thrpt |   5  |  5.269 |±  0.095 |ops/ms |
|LruBenchmark.testBaseline  |          1000 | thrpt |   5  |  0.527 |±  0.022 |ops/ms |
|LruBenchmark.testBaseline  |         10000 | thrpt |   5  |  0.052 |±  0.001 |ops/ms |
|LruBenchmark.testCache     |            10 | thrpt |   5  |103.850 |±  2.110 |ops/ms |
|LruBenchmark.testCache     |           100 | thrpt |   5  | 10.131 |±  0.817 |ops/ms |
|LruBenchmark.testCache     |          1000 | thrpt |   5  |  0.987 |±  0.119 |ops/ms |
|LruBenchmark.testCache     |         10000 | thrpt |   5  |  0.100 |±  0.014 |ops/ms |
|LruBenchmark.testBaseline  |            10 |  avgt |   5  |  0.019 |±  0.001 | ms/op |
|LruBenchmark.testBaseline  |           100 |  avgt |   5  |  0.194 |±  0.009 | ms/op |
|LruBenchmark.testBaseline  |          1000 |  avgt |   5  |  1.941 |±  0.073 | ms/op |
|LruBenchmark.testBaseline  |         10000 |  avgt |   5  | 18.952 |±  0.713 | ms/op |
|LruBenchmark.testCache     |            10 |  avgt |   5  |  0.010 |±  0.001 | ms/op |
|LruBenchmark.testCache     |           100 |  avgt |   5  |  0.099 |±  0.003 | ms/op |
|LruBenchmark.testCache     |          1000 |  avgt |   5  |  0.999 |±  0.017 | ms/op |
|LruBenchmark.testCache     |         10000 |  avgt |   5  |  9.980 |±  0.548 | ms/op |
|LruBenchmark.testBaseline  |            10 |    ss |   5  |  0.163 |±  0.690 | ms/op |
|LruBenchmark.testBaseline  |           100 |    ss |   5  |  0.617 |±  0.641 | ms/op |
|LruBenchmark.testBaseline  |          1000 |    ss |   5  |  2.404 |±  2.273 | ms/op |
|LruBenchmark.testBaseline  |         10000 |    ss |   5  | 23.373 |± 17.612 | ms/op |
|LruBenchmark.testCache     |            10 |    ss |   5  |  0.293 |±  0.136 | ms/op |
|LruBenchmark.testCache     |           100 |    ss |   5  |  0.513 |±  0.465 | ms/op |
|LruBenchmark.testCache     |          1000 |    ss |   5  |  1.224 |±  0.353 | ms/op |
|LruBenchmark.testCache     |         10000 |    ss |   5  | 11.712 |±  5.978 | ms/op |
