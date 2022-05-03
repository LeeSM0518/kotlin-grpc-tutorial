import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jfree.data.xy.XYSeries;

import java.io.IOException;

public class Main {


  private static void runProfiler(String title, Profiler.Timeable timeable, int startN, int endMillis) throws IOException {
    Profiler profiler = new Profiler(title, timeable);
    XYSeries series = profiler.timingLoop(startN, endMillis);
    profiler.plotResults(series);
  }

  public static void main(String[] args) throws IOException {
    Profiler.Timeable grpc = new Profiler.Timeable() {

      CloseableHttpClient client;
      HttpGet httpGet;

      @Override
      public void setup(final int n) {
        client = HttpClients.createDefault();
        httpGet = new HttpGet("http://localhost:8080/grpc");
      }

      @Override
      public void timeMe(final int n) throws IOException {
        client.execute(httpGet);
      }
    };

    Profiler.Timeable rest = new Profiler.Timeable() {

      CloseableHttpClient client;
      HttpGet httpGet;

      @Override
      public void setup(final int n) {
        client = HttpClients.createDefault();
        httpGet = new HttpGet("http://localhost:8080/rest");
      }

      @Override
      public void timeMe(final int n) throws IOException {
        client.execute(httpGet);
      }
    };

    final int endMillis = 60_000 * 5;
    runProfiler("gRPC", grpc, 1, endMillis);
    runProfiler("REST API", rest, 1, endMillis);
  }

}
