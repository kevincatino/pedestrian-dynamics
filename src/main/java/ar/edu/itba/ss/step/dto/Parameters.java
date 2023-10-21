package ar.edu.itba.ss.step.dto;

import ar.edu.itba.ss.step.utils.IO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
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



    public boolean isOrder() {
        return order;
    }

    public void setOrder(boolean order) {
        this.order = order;
    }

    private boolean order;


    public Parameters() {

        runners.put("parseRawFiles",Parameters::parseRawFiles);

    }

    public void run() {
        runners.get(runner).accept(this);
    }
    private static void parseRawFilesHelper(String file, Map<Double, Set<PedestrianDto>> pedestrians, int initialId) {
         try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.trim().split("\\s+");  // Split by whitespace
                double time = Double.parseDouble(values[0])*(4.0/30);
                pedestrians.putIfAbsent(time, new TreeSet<>());
                pedestrians.get(time).add(new PedestrianDto(Double.parseDouble(values[2]),
                        Double.parseDouble(values[1]), initialId + (int)Double.parseDouble(values[3])));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

      private static void parseRawFiles(Parameters params) {
        Map<Double, Set<PedestrianDto>> pedestrians = new TreeMap<>();
        String filePath = "Trayectorias_0_To_13_frames_1_1000_m.txt";
          String filePath2 = "Trayectorias_14_To_25_frames_1_1000_m.txt";
        parseRawFilesHelper(filePath, pedestrians, 0);
        parseRawFilesHelper(filePath2, pedestrians, 13);

          ObjectMapper objectMapper = new ObjectMapper();
          List<TimeInstantDto> timeInstantDtoList = new ArrayList<>();
          int count =-1;
          for (Map.Entry<Double, Set<PedestrianDto>> entry : pedestrians.entrySet()) {
              if (count == -1)
                  count = entry.getValue().size();
              if (entry.getValue().size() == count)
                timeInstantDtoList.add(new TimeInstantDto(entry.getKey(),entry.getValue()));
          }
          try {
            objectMapper.writeValue(new File("pedestrians.json"), timeInstantDtoList);
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
