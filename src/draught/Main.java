package draught;

import clean.args.ArgsException;

import java.text.ParseException;

public class Main {
    public static void main(String[] args) {
        try {
            Args arg = new Args("l,p#,d*", args);
            boolean logging =arg.getBoolean('l');
            int port= arg.getInt('p');
            String directory =arg.getString('d');
            executeApplication(logging,port,directory);
        } catch (ParseException e) {
            System.out.print("Argumenterror");
        }
    }

    private static void executeApplication(boolean logging,int port,String directory) {
        System.out.println(logging+"**");
        System.out.println(port+"**");
        System.out.println(directory+"**");
    }

}
