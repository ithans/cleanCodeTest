package com.objectMentor;

import com.objectMentor.utils.args.Args;
import com.objectMentor.utils.args.ArgsException;

public class Main {
    public static void main(String[] args) {
        try {
            Args arg = new Args("1,p#,d*", args);
            boolean logging= arg.getBoolean('1');
            int port =arg.getInt('p');
            String directory = arg.getString('d');
            executeApplication(logging,port,directory);
        } catch (ArgsException e) {
            System.out.printf("Argumenterror:%s\n", e.errorMessage());
        }
    }

    private static void executeApplication(boolean logging, int port, String directory) {
        System.out.println(logging);
        System.out.println(port);
        System.out.println(directory);
    }

}
