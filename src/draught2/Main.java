package draught2;

import clean.args.Args;
import clean.args.ArgsException;

public class Main {
    public static void main(String[] args) {
        try {
            Args arg = new Args("l,d*", args);
            boolean logging =arg.getBoolean('l');
            String directory =arg.getString('d');
            executeApplication(logging,directory);
        } catch (ArgsException e) {
            System.out.printf("Argumenterror:%s\n", e.errorMessage());
        }
    }

    private static void executeApplication(boolean logging,String directory) {
        System.out.println(logging+"**");
        System.out.println(directory+"**");
    }

}
