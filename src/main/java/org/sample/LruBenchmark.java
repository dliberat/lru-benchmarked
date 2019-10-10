/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sample;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode({Mode.Throughput, Mode.AverageTime, Mode.SingleShotTime})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Thread)
public class LruBenchmark {
	
	private interface Worker {
		int work();
	}
	
	private LruCache<Integer, Integer> cache;
	private Worker worker;
	
	@Param({"10", "100", "1000", "10000"})
	int workloadSize;
	
	@Setup
	public void setup(final Blackhole bh) {
		
		final int cacheSize = (int)Math.ceil(workloadSize * 0.5);
		cache = new LruCache<Integer, Integer>(cacheSize);
		
		for (int i = 0; i < workloadSize; i += 2) {
			cache.add(i, 42);
		}

		worker = new Worker() {
			@Override
			public int work() {
				Blackhole.consumeCPU(1000);
				return 0;
			}
		};		
	}
	
    @Benchmark
    public void testBaseline(Blackhole bh) {

    	for (int i = 0; i < workloadSize; i++) {
    		bh.consume(worker.work());
    	}
    }
    
    @Benchmark
    public void testCache(Blackhole bh) {
    	for (int i = 0; i < workloadSize; i++) {
    		Integer val = cache.get(i);
    		if (val == null) {
    			val = worker.work();
    		}
    		
    		bh.consume(val);
    	}
    }
    
    /**
     * build with:
     * 	$ mvn clean install
     * 
     * run with:
     *  $ java -jar target/benchmarks.jar LruBenchmark
     *  
     * @param args
     * @throws RunnerException
     */
    public static void main(String[] args) throws RunnerException {
    	final Options opt = new OptionsBuilder()
    			.include(LruBenchmark.class.getSimpleName())
    			.forks(1)
    			.build();

    	new Runner(opt).run();
    }

}
