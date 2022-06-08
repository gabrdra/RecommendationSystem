package application;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.openjdk.jcstress.infra.runners.ForkedTestConfig;
import org.openjdk.jcstress.infra.collectors.TestResult;
import org.openjdk.jcstress.infra.runners.Runner;
import org.openjdk.jcstress.infra.runners.WorkerSync;
import org.openjdk.jcstress.util.Counter;
import org.openjdk.jcstress.os.AffinitySupport;
import org.openjdk.jcstress.vm.AllocProfileSupport;
import org.openjdk.jcstress.infra.runners.FootprintEstimator;
import org.openjdk.jcstress.infra.runners.VoidThread;
import org.openjdk.jcstress.infra.runners.LongThread;
import org.openjdk.jcstress.infra.runners.CounterThread;
import application.JCSTest.StressTest;
import org.openjdk.jcstress.infra.results.IIIIII_Result;

public final class JCSTest_StressTest_jcstress extends Runner<IIIIII_Result> {

    volatile WorkerSync workerSync;
    StressTest[] gs;
    IIIIII_Result[] gr;

    public JCSTest_StressTest_jcstress(ForkedTestConfig config) {
        super(config);
    }

    @Override
    public void sanityCheck(Counter<IIIIII_Result> counter) throws Throwable {
        sanityCheck_API(counter);
        sanityCheck_Footprints(counter);
    }

    private void sanityCheck_API(Counter<IIIIII_Result> counter) throws Throwable {
        final StressTest s = new StressTest();
        final IIIIII_Result r = new IIIIII_Result();
        VoidThread a0 = new VoidThread() { protected void internalRun() {
            s.test(r);
        }};
        a0.start();
        a0.join();
        if (a0.throwable() != null) {
            throw a0.throwable();
        }
        counter.record(r);
    }

    private void sanityCheck_Footprints(Counter<IIIIII_Result> counter) throws Throwable {
        config.adjustStrideCount(new FootprintEstimator() {
          public void runWith(int size, long[] cnts) {
            long time1 = System.nanoTime();
            long alloc1 = AllocProfileSupport.getAllocatedBytes();
            StressTest[] ls = new StressTest[size];
            IIIIII_Result[] lr = new IIIIII_Result[size];
            for (int c = 0; c < size; c++) {
                StressTest s = new StressTest();
                IIIIII_Result r = new IIIIII_Result();
                lr[c] = r;
                ls[c] = s;
            }
            LongThread a0 = new LongThread() { public long internalRun() {
                long a1 = AllocProfileSupport.getAllocatedBytes();
                for (int c = 0; c < size; c++) {
                    ls[c].test(lr[c]);
                }
                long a2 = AllocProfileSupport.getAllocatedBytes();
                return a2 - a1;
            }};
            a0.start();
            try {
                a0.join();
                cnts[0] += a0.result();
            } catch (InterruptedException e) {
            }
            for (int c = 0; c < size; c++) {
                counter.record(lr[c]);
            }
            long time2 = System.nanoTime();
            long alloc2 = AllocProfileSupport.getAllocatedBytes();
            cnts[0] += alloc2 - alloc1;
            cnts[1] += time2 - time1;
        }});
    }

    @Override
    public ArrayList<CounterThread<IIIIII_Result>> internalRun() {
        int len = config.strideSize * config.strideCount;
        gs = new StressTest[len];
        gr = new IIIIII_Result[len];
        for (int c = 0; c < len; c++) {
            gs[c] = new StressTest();
            gr[c] = new IIIIII_Result();
        }
        workerSync = new WorkerSync(false, 1, config.spinLoopStyle);

        control.isStopped = false;

        if (config.localAffinity) {
            try {
                AffinitySupport.tryBind();
            } catch (Exception e) {
                // Do not care
            }
        }

        ArrayList<CounterThread<IIIIII_Result>> threads = new ArrayList<>(1);
        threads.add(new CounterThread<IIIIII_Result>() { public Counter<IIIIII_Result> internalRun() {
            return task_test();
        }});

        for (CounterThread<IIIIII_Result> t : threads) {
            t.start();
        }

        if (config.time > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(config.time);
            } catch (InterruptedException e) {
            }
        }

        control.isStopped = true;

        return threads;
    }

    private void jcstress_consume(Counter<IIIIII_Result> cnt, int a) {
        StressTest[] ls = gs;
        IIIIII_Result[] lr = gr;
        int len = config.strideSize * config.strideCount;
        int left = a * len / 1;
        int right = (a + 1) * len / 1;
        for (int c = left; c < right; c++) {
            IIIIII_Result r = lr[c];
            StressTest s = ls[c];
            cnt.record(r);
            r.r1 = 0;
            r.r2 = 0;
            r.r3 = 0;
            r.r4 = 0;
            r.r5 = 0;
            r.r6 = 0;
        }
    }

    private void jcstress_sink(int v) {};
    private void jcstress_sink(short v) {};
    private void jcstress_sink(byte v) {};
    private void jcstress_sink(char v) {};
    private void jcstress_sink(long v) {};
    private void jcstress_sink(float v) {};
    private void jcstress_sink(double v) {};
    private void jcstress_sink(Object v) {};

    private Counter<IIIIII_Result> task_test() {
        int len = config.strideSize * config.strideCount;
        int stride = config.strideSize;
        Counter<IIIIII_Result> counter = new Counter<>();
        if (config.localAffinity) AffinitySupport.bind(config.localAffinityMap[0]);
        while (true) {
            WorkerSync sync = workerSync;
            if (sync.stopped) {
                return counter;
            }
            int check = 0;
            for (int start = 0; start < len; start += stride) {
                run_test(gs, gr, start, start + stride);
                check += 1;
                sync.awaitCheckpoint(check);
            }
            jcstress_consume(counter, 0);
            if (sync.tryStartUpdate()) {
                workerSync = new WorkerSync(control.isStopped, 1, config.spinLoopStyle);
            }
            sync.postUpdate();
        }
    }

    private void run_test(StressTest[] gs, IIIIII_Result[] gr, int start, int end) {
        StressTest[] ls = gs;
        IIIIII_Result[] lr = gr;
        for (int c = start; c < end; c++) {
            StressTest s = ls[c];
            IIIIII_Result r = lr[c];
            jcstress_sink(r.jcstress_trap);
            s.test(r);
        }
    }

}
