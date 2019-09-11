import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import SVParser.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Run {
    public static void main(String[] args) throws Exception {
        // create a CharStream thatreads from standard input
        //String inputFile = "/Users/Cloud/Documents/lab/DEF_CON/github/ACE/reasoning/src/observation(1).txt";
        //String inputFile = "/Users/Cloud/Downloads/observation.txt.onos";

        SVParser.Generator.generate();

        String inputFile = "observation.txt";
        InputStream is = System.in;
        File file = null;

        if ( args.length >= 1 && args[0]!= null ) file = new File(args[0]);
        else file = new File(inputFile);

//        if ( args.length >= 1 && args[0]!= null ) is = new FileInputStream(args[0]);
//        else is = new FileInputStream(inputFile);

        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String obsrs = new String(filecontent);

        obsrs = obsrs.replaceAll("\\n\\n", "\n");
        obsrs = obsrs.replaceAll("\\[[^\\{]*\\]", "");
        obsrs = obsrs.replaceAll("\\{ .* \\}", "");
        //obsrs = obsrs.replaceAll("\\{", "");
        //obsrs = obsrs.replaceAll("\\}", "");

        ANTLRInputStream input = new ANTLRInputStream(obsrs);

        DhlLexer lexer = new DhlLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        DhlParser parser = new DhlParser(tokens);
        ParseTree tree = parser.prog(); // parse
        DhlVisitor eval = new EvalVisitor(new String(filecontent));
        eval.visit(tree);
    }
}
