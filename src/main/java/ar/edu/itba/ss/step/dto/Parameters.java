package ar.edu.itba.ss.step.dto;

import ar.edu.itba.ss.step.models.Pair;
import ar.edu.itba.ss.step.models.Pedestrian;
import ar.edu.itba.ss.step.models.SFMStepProcessor;
import ar.edu.itba.ss.step.models.SimulationEngine;
import ar.edu.itba.ss.step.models.StepProcessor;
import ar.edu.itba.ss.step.models.Vector;
import ar.edu.itba.ss.step.utils.IO;
import ar.edu.itba.ss.step.utils.MathHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

    private final Map<String, Consumer<Parameters>> runners = new HashMap<>();


    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    private double startTime;

    private double endTime;

    private double tau;

    public double getTau() {
        return tau;
    }

    public void setTau(double tau) {
        this.tau = tau;
    }

    public double getInitialVelocity() {
        return initialVelocity;
    }

    public void setInitialVelocity(double initialVelocity) {
        this.initialVelocity = initialVelocity;
    }

    public double getTargetVelocity() {
        return targetVelocity;
    }

    public void setTargetVelocity(double targetVelocity) {
        this.targetVelocity = targetVelocity;
    }

    private double initialVelocity;
    private double targetVelocity;


    public Parameters() {

        runners.put("parseRawFiles",Parameters::parseRawFiles);
        runners.put("experimentVelocity",Parameters::experimentVelocity);
        runners.put("velocityComp",Parameters::velocityComp);

    }

    public void run() {
        runners.get(runner).accept(this);
    }
    private static void parseRawFilesHelper(String file, Map<Double, Set<PedestrianDto>> pedestrians, int initialId) {
         try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.trim().split("\\s+");  // Split by whitespace
                double time = (Double.parseDouble(values[0])-1)*(4.0/30);
                pedestrians.putIfAbsent(time, new TreeSet<>());
                pedestrians.get(time).add(new PedestrianDto(Double.parseDouble(values[2]),
                        -Double.parseDouble(values[1]), initialId + (int)Double.parseDouble(values[3])));
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
        parseRawFilesHelper(filePath2, pedestrians, 14);

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

    public String getExperimentOutput() {
        return experimentOutput;
    }

    public void setExperimentOutput(String experimentOutput) {
        this.experimentOutput = experimentOutput;
    }

    private String experimentOutput;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public String getExperimentVelocityOutput() {
        return experimentVelocityOutput;
    }

    public void setExperimentVelocityOutput(String experimentVelocityOutput) {
        this.experimentVelocityOutput = experimentVelocityOutput;
    }

    private String experimentVelocityOutput;

    public String getExperimentVelocityCompOutput() {
        return experimentVelocityCompOutput;
    }

    public void setExperimentVelocityCompOutput(String experimentVelocityCompOutput) {
        this.experimentVelocityCompOutput = experimentVelocityCompOutput;
    }

    private static String appendValue(String fileName, Object value) {
        String[] splitted = fileName.split("\\.");
        return splitted[0] + value + "." + splitted[1];
    }

    private String experimentVelocityCompOutput;
    private static void velocityComp(Parameters params) {


           ObjectMapper objectMapper = new ObjectMapper();
          List<VelocityDto> velocities = getExperimentVelocity(params);
          Pedestrian p = new Pedestrian(Vector.of(1800,0), params.getTargetVelocity(), 80);
        SimulationEngine sim = new SimulationEngine();
        StepProcessor sfmStepProcessor = new SFMStepProcessor(params.getTau(),0.1);
        double initialTime = -1;
        Iterator<Pair<Double, Pedestrian>> it = sim.simulate(p,sfmStepProcessor);
        for (VelocityDto dto : velocities) {
            if (initialTime == -1) {
                initialTime = dto.getTime();
            }
            double v = 0;
            while(it.hasNext()) {
                Pair<Double, Pedestrian> next = it.next();
                if (next.getOne() + initialTime >= dto.getTime()) {
                    v = next.getOther().getVelocity().getMod();
                    break;
                }
            }
            dto.setvSim(v);
        }

          try {
            VelocityContainerDto c = new VelocityContainerDto(params.getId(), MathHelper.calculateMSE(velocities, v -> Pair.of(v.getvSim(),v.getvExp())),velocities);

            objectMapper.writeValue(new File(appendValue(params.getExperimentVelocityCompOutput(), params.getId())),c);
              // Write file
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
      }

      private static List<VelocityDto> getExperimentVelocity(Parameters params) {

          ObjectMapper objectMapper = new ObjectMapper();
          List<VelocityDto> velocities = new ArrayList<>();
          try {
              List<TimeInstantDto> instants = objectMapper.readValue(new File(params.getExperimentOutput()),new TypeReference<>() {});
              List<Map.Entry<Double,PedestrianDto>> filteredInstants = instants.stream().
                      filter(i -> i.getTime() >= params.getStartTime() && i.getTime() <= params.getEndTime()).
                      map(i -> Map.entry(i.getTime(),i.getPedestrian(params.getId()))).toList();
              Map.Entry<Double,PedestrianDto> prev = null;
              for (Map.Entry<Double,PedestrianDto> i : filteredInstants) {
                  if (prev == null) {
                      prev = i;
                      continue;
                  }

                  double v = Math.sqrt(Math.pow(i.getValue().getX() - prev.getValue().getX(),2) + Math.pow(i.getValue().getY() - prev.getValue().getY(),2)) / (i.getKey() - prev.getKey());
                  velocities.add(new VelocityDto(i.getKey(), v));
                  prev = i;
              }
          } catch(RuntimeException | IOException e) {
              throw new RuntimeException(e);
          }

          return velocities;

      }

       private static void experimentVelocity(Parameters params) {


          ObjectMapper objectMapper = new ObjectMapper();
          List<VelocityDto> velocities = getExperimentVelocity(params);

          try {


              VelocityContainerDto c = new VelocityContainerDto(params.getId(), 0,velocities);
            objectMapper.writeValue(new File(appendValue(params.getExperimentVelocityOutput(), params.getId())), c);

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
