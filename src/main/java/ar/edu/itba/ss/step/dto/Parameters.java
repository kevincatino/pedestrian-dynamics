package ar.edu.itba.ss.step.dto;

import ar.edu.itba.ss.step.utils.IO;
import ar.edu.itba.ss.step.utils.PostProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class Parameters {


    public String getRunner() {
        return runner;
    }

    public void setRunner(String runner) {
        this.runner = runner;
    }

    private String runner;



    public double getStableTime() {
        return stableTime;
    }

    public void setStableTime(double stableTime) {
        this.stableTime = stableTime;
    }

    public double stableTime;

    public void setN(int n) {
        this.n = n;
    }

    private int n;
    private final Map<String, Consumer<Parameters>> runners = new HashMap<>();


    public void setVelocityOutputFile(String velocityOutputFile) {
        this.velocityOutputFile = velocityOutputFile;
    }

    private String velocityOutputFile;

    public boolean isOrder() {
        return order;
    }

    public void setOrder(boolean order) {
        this.order = order;
    }

    private boolean order;


    public Parameters() {

        runners.put("phi",Parameters::phi);

    }

    public void run() {
        runners.get(runner).accept(this);
    }


      private static void phi(Parameters params) {
          ObjectMapper objectMapper = new ObjectMapper();
          try {

              // Write file
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
      }



    public static Parameters readFile(String path) throws IOException {
        String string = IO.readFile(path);
        ObjectMapper objectMapper = new ObjectMapper();

        Parameters params = objectMapper.readValue(string, Parameters.class);


        return params;
    }

}
