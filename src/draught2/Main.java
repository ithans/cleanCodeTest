package draught2;

import clean.args.Args;
import clean.args.ArgsException;

public class Main {
    public static void main(String[] args) {
        try {
            Args arg = new Args("l,p#,d*", args);
            boolean logging =arg.getBoolean('l');
            int port= arg.getInt('p');
            String directory =arg.getString('d');
            executeApplication(logging,port,directory);
        } catch (ArgsException e) {
            System.out.printf("Argumenterror:%s\n", e.errorMessage());
        }
    }

    private static void executeApplication(boolean logging,int port,String directory) {
        System.out.println(logging);
        System.out.println(port);
        System.out.println(directory);
    }

}
