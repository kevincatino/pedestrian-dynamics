package ar.edu.itba.ss.step.dto;

import ar.edu.itba.ss.step.models.CustomSFMHeuristic;
import ar.edu.itba.ss.step.models.Pair;
import ar.edu.itba.ss.step.models.PathTargetProvider;
import ar.edu.itba.ss.step.models.Pedestrian;
import ar.edu.itba.ss.step.models.SFMStepProcessor;
import ar.edu.itba.ss.step.models.SimpleTargetProvider;
import ar.edu.itba.ss.step.models.SimulationEngine;
import ar.edu.itba.ss.step.models.SimulationHeuristic;
import ar.edu.itba.ss.step.models.StepProcessor;
import ar.edu.itba.ss.step.models.TargetHelper;
import ar.edu.itba.ss.step.models.TargetProvider;
import ar.edu.itba.ss.step.models.Vector;
import ar.edu.itba.ss.step.utils.Constants;
import ar.edu.itba.ss.step.utils.IO;
import ar.edu.itba.ss.step.utils.MathHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.Map.entry;

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
        runners.put("tauErrors",Parameters::tauErrors);
        runners.put("pedestrianSimulation",Parameters::pedestrianSimulation);
        runners.put("minDistance",Parameters::minDistance);

    }

    public void run() {
        runners.get(runner).accept(this);
    }
    private static void parseRawFilesHelper(String file, Map<Double, Map<Integer,PedestrianDto>> pedestrians, int initialId) {
         try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.trim().split("\\s+");  // Split by whitespace
                double time = (Double.parseDouble(values[0])-1)*(4.0/30);
                pedestrians.putIfAbsent(time, new TreeMap<>());

                pedestrians.get(time).put(initialId + (int)Double.parseDouble(values[3]),new PedestrianDto(Double.parseDouble(values[2]),
                        -Double.parseDouble(values[1]), initialId + (int)Double.parseDouble(values[3])));
            }
            for (Integer id : pedestrians.get(0.0).keySet()) {
                double prevTime = -1;
                double prevX = -1;
                double prevY = -1;
                 for (Map.Entry<Double,Map<Integer,PedestrianDto>> entry : pedestrians.entrySet()) {
                     PedestrianDto p = entry.getValue().get(id);
                      if (p == null) {
                         continue;
                     }
                     Vector target = getTarget(p,entry.getKey());
                      p.setTargetX(target.getX());
                      p.setTargetY(target.getY());
                 if (prevTime < 0) {
                     p.setVx(0);
                     p.setVy(0);
                     prevTime = 0;
                     prevX = p.getX();
                     prevY = p.getY();
                 } else {
                     double time = entry.getKey();

                     p.setVx((p.getX() - prevX)/(time - prevTime));
                     p.setVy((p.getY() - prevY)/(time - prevTime));
                     prevX = p.getX();
                     prevY = p.getY();
                     prevTime = time;
                 }
             }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<TimeInstantDto> getPedestrians(Parameters params) {
         Map<Double, Map<Integer,PedestrianDto>> pedestrians = new TreeMap<>();
        String filePath = "Trayectorias_0_To_13_frames_1_1000_m.txt";
          String filePath2 = "Trayectorias_14_To_25_frames_1_1000_m.txt";
        parseRawFilesHelper(filePath, pedestrians, 0);
        parseRawFilesHelper(filePath2, pedestrians, 14);

          List<TimeInstantDto> timeInstantDtoList = new ArrayList<>();
          int count =-1;
          for (Map.Entry<Double, Map<Integer,PedestrianDto>> entry : pedestrians.entrySet()) {
              if (count == -1)
                  count = entry.getValue().size();
              if (entry.getValue().size() == count)
                  timeInstantDtoList.add(new TimeInstantDto(entry.getKey(),new HashSet<>(entry.getValue().values())));
          }
          return timeInstantDtoList;
    }

    private static final Map<Integer,Vector> targets = Map.ofEntries(
                entry(1, Vector.of(-12.8,-6.5)),
                entry(2, Vector.of(-9.6,-6.5)),
                entry(3, Vector.of(-12.8,0)),
                entry(4, Vector.of(-9.6,0)),
                entry(5, Vector.of(-3.3,-6.5)),
                entry(6, Vector.of(-3.3, 0)),
                entry(7, Vector.of(-9.6, 6.5)),
                entry(8, Vector.of(-12.8, 6.5)),
                entry(9, Vector.of(-3.3, 6.5)),
                entry(10, Vector.of(3.15, 6.5)),
                entry(11, Vector.of(10, 6.5)),
                entry(12, Vector.of(12.5, 6.5)),
                entry(13, Vector.of(3.15, -6.5)),
                entry(14, Vector.of(3.15, 0)),
                entry(15, Vector.of(12.5, 0)),
                entry(16, Vector.of(10, 0)),
                entry(17, Vector.of(10, -6.5)),
                entry(18, Vector.of(12.5, -6.5)),
                entry(19, Vector.of(0.0, -3.5)),
                entry(20, Vector.of(0.0, 0.0)),
                entry(21, Vector.of(0.0, 3.5))
        );

    private static Vector getTarget(PedestrianDto dto, double time) {

        //
        //targets = [(-12.8, -6.5), (-9.6, -6.5), (-12.8, 0), (-9.6, 0), (-3.3, -6.5), (-3.3, 0), (-9.6, 6.5),
        //(-12.8, 6.5), (-3.3, 6.5), (3.15, 6.5), (10, 6.5), (12.5, 6.5), (3.15, -6.5), (3.15, 0),
        //(12.5, 0), (10, 0), (10, -6.5), (12.5, -6.5)]

        Map<Integer, TargetHelper> helpers = Map.ofEntries(
                entry(0, new TargetHelper(
                        Pair.of(1.9,targets.get(7)),
                        Pair.of(7.7, targets.get(9)),
                        Pair.of(15.7, targets.get(14)),
                        Pair.of(23.7, targets.get(17)),
                        Pair.of(29.2, targets.get(16)),
                        Pair.of(Double.MAX_VALUE, targets.get(10)))
                ),
                entry(1, new TargetHelper(
                        Pair.of(4.7, targets.get(7)),
                        Pair.of(7.7, targets.get(8)),
                        Pair.of(13.1, targets.get(3)),
                        Pair.of(16.4, targets.get(4)),
                        Pair.of(22.2, targets.get(6)),
                        Pair.of(26.7, targets.get(19)),
                        Pair.of(30.7, targets.get(13)),
                        Pair.of(Double.MAX_VALUE, targets.get(17)))
                ),
                entry(2, new TargetHelper(
                        Pair.of(6.1, targets.get(11)),
                        Pair.of(21.6, targets.get(3)),
                        Pair.of(Double.MAX_VALUE, targets.get(13)))
                ),
                entry(3, new TargetHelper(
                        Pair.of(8.0, targets.get(4)),
                        Pair.of(14.1, targets.get(1)),
                        Pair.of(23.9, targets.get(20)),
                        Pair.of(30.0, targets.get(10)),
                        Pair.of(Double.MAX_VALUE, targets.get(4)))
                ),
                entry(4, new TargetHelper(
                        Pair.of(2.7, targets.get(14)),
                        Pair.of(8.4, targets.get(10)),
                        Pair.of(18.1, targets.get(4)),
                        Pair.of(24.7, targets.get(5)),
                        Pair.of(30.3, targets.get(2)),
                        Pair.of(Double.MAX_VALUE, targets.get(6)))
                ),
                 entry(5, new TargetHelper(
                        Pair.of(1.1, targets.get(6)),
                        Pair.of(8.1, targets.get(2)),
                        Pair.of(18.7, targets.get(13)),
                        Pair.of(23.7, targets.get(14)),
                        Pair.of(30.7, targets.get(16)),
                        Pair.of(Double.MAX_VALUE, targets.get(15)))
                ),
                entry(6, new TargetHelper(
                        Pair.of(2.4, targets.get(21)),
                        Pair.of(5.7, targets.get(9)),
                        Pair.of(16.8, targets.get(2)),
                        Pair.of(19.2, targets.get(1)),
                        Pair.of(28.7, targets.get(8)),
                        Pair.of(Double.MAX_VALUE, targets.get(6)))
                ),
                entry(7, new TargetHelper(
                        Pair.of(5.7, targets.get(14)),
                        Pair.of(5.7, targets.get(9)),
                        Pair.of(11.9, targets.get(7)),
                        Pair.of(18.5, targets.get(2)),
                        Pair.of(25.9, targets.get(3)),
                        Pair.of(Double.MAX_VALUE, targets.get(5)))
                ),
                entry(8, new TargetHelper(
                        Pair.of(0.8, targets.get(9)),
                        Pair.of(5.3, targets.get(10)),
                        Pair.of(12.5, targets.get(11)),
                        Pair.of(18.9, targets.get(16)),
                        Pair.of(26.3, targets.get(13)),
                        Pair.of(Double.MAX_VALUE, targets.get(4)))
                ),
                entry(9, new TargetHelper(
                        Pair.of(6.7, targets.get(3)),
                        Pair.of(13.1, targets.get(2)),
                        Pair.of(21.9, targets.get(19)),
                        Pair.of(27.9, targets.get(13)),
                        Pair.of(Double.MAX_VALUE, targets.get(13)))
                ),
                entry(10, new TargetHelper(
                        Pair.of(1.6, targets.get(20)),
                        Pair.of(4.0, targets.get(14)),
                        Pair.of(10.0, targets.get(11)),
                        Pair.of(21.3, targets.get(7)),
                        Pair.of(27.5, targets.get(21)),
                        Pair.of(Double.MAX_VALUE, targets.get(11)))
                ),
                entry(11, new TargetHelper(
                        Pair.of(4.8, targets.get(6)),
                        Pair.of(12.1, targets.get(10)),
                        Pair.of(21.2, targets.get(16)),
                        Pair.of(24.0, targets.get(15)),
                        Pair.of(29.6, targets.get(17)),
                        Pair.of(Double.MAX_VALUE, targets.get(13)))
                ),
                entry(12, new TargetHelper(
                        Pair.of(5.2, targets.get(8)),
                        Pair.of(16.1, targets.get(20)),
                        Pair.of(27.5, targets.get(12)),
                        Pair.of(Double.MAX_VALUE, targets.get(21)))
                ),
                entry(13, new TargetHelper(
                        Pair.of(1.1, targets.get(4)),
                        Pair.of(6.1, targets.get(2)),
                        Pair.of(9.2, targets.get(1)),
                        Pair.of(20.4, targets.get(20)),
                        Pair.of(27.5, targets.get(10)),
                        Pair.of(Double.MAX_VALUE, targets.get(11)))
                ),
                entry(14, new TargetHelper(
                        Pair.of(4.7, targets.get(1)),
                        Pair.of(10.8, targets.get(4)),
                        Pair.of(15.7, targets.get(6)),
                        Pair.of(25.2, targets.get(10)),
                        Pair.of(Double.MAX_VALUE, targets.get(16)))
                ),
                entry(15, new TargetHelper(
                        Pair.of(2.7, targets.get(6)),
                        Pair.of(11.6, targets.get(17)),
                        Pair.of(20.5, targets.get(10)),
                        Pair.of(27.7, targets.get(15)),
                        Pair.of(Double.MAX_VALUE, targets.get(9)))
                ),
                entry(16, new TargetHelper(
                        Pair.of(7.1, targets.get(13)),
                        Pair.of(12.3, targets.get(14)),
                        Pair.of(16.8, targets.get(21)),
                        Pair.of(21.7, targets.get(10)),
                        Pair.of(27.7, targets.get(11)),
                        Pair.of(30.5, targets.get(12)),
                        Pair.of(Double.MAX_VALUE, targets.get(15)))
                ),
                entry(17, new TargetHelper(
                        Pair.of(7.5, targets.get(17)),
                        Pair.of(13.1, targets.get(15)),
                        Pair.of(18.4, targets.get(11)),
                        Pair.of(21.2, targets.get(12)),
                        Pair.of(23.8, targets.get(11)),
                        Pair.of(31.2, targets.get(14)),
                        Pair.of(Double.MAX_VALUE, targets.get(19)))
                ),
                entry(18, new TargetHelper(
                        Pair.of(1.6, targets.get(19)),
                        Pair.of(9.1, targets.get(9)),
                        Pair.of(13.9, targets.get(7)),
                        Pair.of(18.4, targets.get(3)),
                        Pair.of(23.4, targets.get(2)),
                        Pair.of(30.7, targets.get(20)),
                        Pair.of(Double.MAX_VALUE, targets.get(16)))
                ),
                 entry(19, new TargetHelper(
                        Pair.of(9.3, targets.get(18)),
                        Pair.of(15.7, targets.get(16)),
                        Pair.of(23.2, targets.get(10)),
                        Pair.of(28.8, targets.get(9)),
                        Pair.of(Double.MAX_VALUE, targets.get(4)))
                ),
                entry(20, new TargetHelper(
                        Pair.of(2.0, targets.get(13)),
                        Pair.of(6.4, targets.get(19)),
                        Pair.of(17.7, targets.get(7)),
                        Pair.of(29.5, targets.get(19)),
                        Pair.of(Double.MAX_VALUE, targets.get(5)))
                ),
                entry(21, new TargetHelper(
                        Pair.of(3.2, targets.get(17)),
                        Pair.of(12.7, targets.get(12)),
                        Pair.of(19.3, targets.get(10)),
                        Pair.of(23.9, targets.get(9)),
                        Pair.of(28.7, targets.get(7)),
                        Pair.of(Double.MAX_VALUE, targets.get(4)))
                ),
                entry(22, new TargetHelper(
                        Pair.of(3.6, targets.get(16)),
                        Pair.of(9.5, targets.get(12)),
                        Pair.of(17.2, targets.get(16)),
                        Pair.of(23.5, targets.get(18)),
                        Pair.of(Double.MAX_VALUE, targets.get(20)))
                ),
                entry(23, new TargetHelper(
                        Pair.of(1.6, targets.get(16)),
                        Pair.of(7.0, targets.get(18)),
                        Pair.of(22.7, targets.get(4)),
                        Pair.of(30.4, targets.get(9)),
                        Pair.of(Double.MAX_VALUE, targets.get(21)))
                ),
                entry(24, new TargetHelper(
                        Pair.of(2.0, targets.get(15)),
                        Pair.of(12.2, targets.get(18)),
                        Pair.of(15.7, targets.get(17)),
                        Pair.of(24.0, targets.get(16)),
                        Pair.of(29.2, targets.get(14)),
                        Pair.of(Double.MAX_VALUE, targets.get(19)))
                )
        );

        return helpers.get(dto.getId()).getTarget(time);
    }


      private static void parseRawFiles(Parameters params) {

          ObjectMapper objectMapper = new ObjectMapper();
          List<TimeInstantDto> timeInstantDtoList =getPedestrians(params);

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

    @Override
    public String toString() {
        return String.format("-%d-%.2f-%.2f", id, startTime, endTime);
    }

    private String experimentVelocityCompOutput;

    private static VelocityContainerDto getVelocityComp(Parameters params) {
          List<VelocityDto> velocities = getExperimentVelocity(params);
          Pedestrian p = new Pedestrian(new SimpleTargetProvider(Vector.of(1800,0)), Vector.of(0,0), params.getTargetVelocity(), params.getInitialVelocity(),80);
        SimulationEngine sim = new SimulationEngine();
        double da = -1;
        StepProcessor sfmStepProcessor = new SFMStepProcessor(params.getTau());
        double initialTime = -1;
        Iterator<Pair<Double, Pedestrian>> it = sim.simulate(p,sfmStepProcessor, 0.1, params.getEndTime());
        for (VelocityDto dto : velocities) {
            if (da < 0) {
                da = dto.getDa();
            }
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
        List<Double> weights = new ArrayList<>();
        for (VelocityDto d : velocities) {
            weights.add(1.0);
        }
        weights.set(0, 10.0);
        weights.set(weights.size()-1, 10.0);


        VelocityContainerDto c = new VelocityContainerDto(params.getId(), MathHelper.calculateWeightedMSE(velocities, v -> Pair.of(v.getvSim(),v.getvExp()), weights),velocities,da);
        return c;


    }

    public double getTauMax() {
        return tauMax;
    }

    public void setTauMax(double tauMax) {
        this.tauMax = tauMax;
    }

    public double getDeltaTau() {
        return deltaTau;
    }

    public void setDeltaTau(double deltaTau) {
        this.deltaTau = deltaTau;
    }

    private double tauMax;
    private double deltaTau;
    private static void tauErrors(Parameters params) {
           ObjectMapper objectMapper = new ObjectMapper();
           List<TauErrorDto> errors = new ArrayList<>();
           double startTime = params.getStartTime();
           double endTime = params.getEndTime();
           int id = params.getId();
        for (double tau = params.getTau() ; tau <= params.getTauMax() ; tau+= params.getDeltaTau()) {
            params.setTau(tau);
            VelocityContainerDto c = getVelocityComp(params);
            double error = c.getError();
            errors.add(new TauErrorDto(tau, error));
        }


          try {
            objectMapper.writeValue(new File(appendValue(params.getTauErrorOutput(), params)),new TauErrorContainerDto(id, errors, startTime, endTime));
              // Write file
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
      }

       private static void velocityComp(Parameters params) {

           ObjectMapper objectMapper = new ObjectMapper();

           VelocityContainerDto c =getVelocityComp(params);

          try {

            objectMapper.writeValue(new File(appendValue(params.getExperimentVelocityCompOutput(), params)),c);
              // Write file
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
      }

    public String getTauErrorOutput() {
        return tauErrorOutput;
    }

    public void setTauErrorOutput(String tauErrorOutput) {
        this.tauErrorOutput = tauErrorOutput;
    }

    private String tauErrorOutput;

      private static List<VelocityDto> getExperimentVelocity(Parameters params) {

          ObjectMapper objectMapper = new ObjectMapper();
          List<VelocityDto> velocities = new ArrayList<>();
          try {
              List<TimeInstantDto> instants = objectMapper.readValue(new File(params.getExperimentOutput()),new TypeReference<>() {});
              List<Map.Entry<Double,PedestrianDto>> filteredInstants = instants.stream().
                      filter(i -> i.getTime() >= params.getStartTime() && i.getTime() <= params.getEndTime()).
                      map(i -> entry(i.getTime(),i.getPedestrian(params.getId()))).toList();
              Map.Entry<Double,PedestrianDto> prev = null;
              for (Map.Entry<Double,PedestrianDto> i : filteredInstants) {
                  if (prev == null) {
                      prev = i;
                      continue;
                  }
                  PedestrianDto p = i.getValue();

                  double v = Math.sqrt(Math.pow(i.getValue().getX() - prev.getValue().getX(),2) + Math.pow(i.getValue().getY() - prev.getValue().getY(),2)) / (i.getKey() - prev.getKey());
                  velocities.add(new VelocityDto(i.getKey(), v, Vector.of(p.getTargetX(), p.getTargetY()).substract(Vector.of(p.getX(), p.getY())).getMod()));
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


              VelocityContainerDto c = new VelocityContainerDto(params.getId(), 0,velocities, velocities.get(0).getDa());
            objectMapper.writeValue(new File(appendValue(params.getExperimentVelocityOutput(), params)), c);

              // Write file
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
      }

    public double getVd() {
        return vd;
    }

    public void setVd(double vd) {
        this.vd = vd;
    }

    public String getSimulationOutput() {
        return simulationOutput;
    }

    public void setSimulationOutput(String simulationOutput) {
        this.simulationOutput = simulationOutput;
    }

    private String simulationOutput;

    private double vd;

    private static Pair<Integer,Double> findMinDistance(Collection<PedestrianDto> pedestrians) {
        PedestrianDto p = null;
        for (PedestrianDto o : pedestrians) {
            if (o.getId() == -1) {
                p = o;
                break;
            }
        }
        double minDistance = Double.MAX_VALUE;
        int id = 0;
        for (PedestrianDto other : pedestrians) {
            double d = Vector.of(other.getX(), other.getY()).substract(Vector.of(p.getX(), p.getY())).getMod();
            if (d < minDistance && other.getId() != p.getId()) {
                minDistance = d;
                id = other.getId();
            }
        }
        return Pair.of(id, minDistance);
    }

    public String getMinDistanceOutput() {
        return minDistanceOutput;
    }

    public void setMinDistanceOutput(String minDistanceOutput) {
        this.minDistanceOutput = minDistanceOutput;
    }

    private String minDistanceOutput;

     private static void minDistance(Parameters params) {


          try {
          ObjectMapper objectMapper = new ObjectMapper();
          List<TimeInstantDto> instants = objectMapper.readValue(new File(params.getSimulationOutput()),new TypeReference<>() {});
          List<MinDistanceDto> minDistances = new ArrayList<>();
        for (TimeInstantDto dto : instants) {
            Set<PedestrianDto> pedestrians = new HashSet<>(dto.getPedestrians());
            Pair<Integer, Double> minDistance = findMinDistance(pedestrians);
            minDistances.add(new MinDistanceDto(dto.getTime(),minDistance.getOther(), minDistance.getOne()));
        }

            objectMapper.writeValue(new File(params.getMinDistanceOutput()), minDistances);

              // Write file
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
      }

      private static void pedestrianSimulation(Parameters params) {


          try {
          ObjectMapper objectMapper = new ObjectMapper();
          // 7, 5, 13, 11
          Pedestrian pedestrian = new Pedestrian(new PathTargetProvider(targets.get(7), targets.get(5), targets.get(13), targets.get(11)), targets.get(17), params.getVd(), 0, Constants.MASS);
          List<TimeInstantDto> instants = objectMapper.readValue(new File(params.getExperimentOutput()),new TypeReference<>() {});
              List<Pair<Double, Set<Pedestrian>>> others = instants.stream().map(i -> Pair.of(i.getTime(), i.getPedestrians().stream().map(Pedestrian::fromDto).collect(Collectors.toSet()))).collect(Collectors.toList());
              SimulationEngine sim = new SimulationEngine();
        double da = 1;
        StepProcessor sfmStepProcessor = new SFMStepProcessor(params.getTau(), da);
        Iterator<Pair<Double, Pedestrian>> it = sim.simulate(pedestrian,sfmStepProcessor, others, new CustomSFMHeuristic());
        instants.remove(instants.get(instants.size()-1));
        for (TimeInstantDto dto : instants) {
            Set<PedestrianDto> pedestrians = new HashSet<>(dto.getPedestrians());
            pedestrians.add(PedestrianDto.from(it.next().getOther(), -1));
            dto.setPedestrians(pedestrians);
        }

            objectMapper.writeValue(new File(params.getSimulationOutput()), instants);

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
