import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import com.company.*;

public class Command_parse extends ABSCommand {
    public Command_parse(Data _dir) {
        super("parse", 0, _dir);
    }

    public void StartExecute(ArrayList<String> commands) {}

    public void ABSExecCommand() throws FileNotFound {
        String res = "";
        if (Commands.size() == 1) {
            for (ABSCommand com : WorkDir.exec_commands) {
                res += " " + com.GetName();
            }
            WorkDir.ShellOutput.SetOutput(res);
        }
        else if (Commands.size() == 2) {
            Pattern p = Pattern.compile("^"+Commands.get(1));
            //Matcher m;
            for (ABSCommand com : WorkDir.exec_commands) {
                if (p.matcher(com.GetName()).find()) {
                    res += " " + com.GetName();
                }
            }
            WorkDir.ShellOutput.SetOutput(res);
        }
    }

    public String ForMan() {
        return "parse need 1 argument, return all comands to match true of argument";
    }
}
