package ar.edu.itba.ss.step;


import ar.edu.itba.ss.step.dto.Parameters;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Parameters params = Parameters.readFile("params.json");
        params.run();
    }
}
